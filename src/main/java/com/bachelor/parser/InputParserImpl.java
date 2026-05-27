package com.bachelor.parser;

import com.bachelor.InputParser;
import com.bachelor.parser.dto.*;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


public class InputParserImpl implements InputParser {

    @Override
    public InputArray parseToArray(String inputValue) throws InvalidInputException {
        // Validate input first
        validateInput(inputValue);

        // Remove whitespaces
        inputValue = inputValue.replaceAll("\\s+", "");

        // Parse the tree structure
        List<NodeDto> nodeList = new ArrayList<>();
        List<Variable> variableList = new ArrayList<>();
        parseExpression(inputValue, 0, nodeList, variableList,-1, 0);

        // Convert to BFS order
        TreeNode[] tree = convertToBfs(nodeList);
        InputArray inputArray = new InputArray(tree, variableList);

        return inputArray;
    }

    public static Variable createVariable(String variable) throws InvalidInputException {
        if (variable.length() != 1) throw new InvalidInputException("Variable name length should be only 1");
        return (variable.charAt(0) <= 'M') ? new TermVariable(variable) : new SequentialVariable(variable);
    }

    @Override
    public void validateInput(String input) throws InvalidInputException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException("Input cannot be null or empty");
        }

        // Remove all whitespaces for validation
        String cleanInput = input.replaceAll("\\s+", "");

        // Check for invalid characters
        if (!cleanInput.matches("[a-zA-Z(),]*")) {
            throw new InvalidInputException("Input contains invalid characters. Only letters, parentheses, and commas are allowed.");
        }

        // Check for uppercase letters followed by parentheses (invalid)
        Pattern upperCaseWithParens = Pattern.compile("[A-Z]\\s*\\(");
        if (upperCaseWithParens.matcher(input).find()) {
            throw new InvalidInputException("Variables ( uppercase letters) cannot have parameters. Found uppercase letter followed by parentheses.");
        }

        // Check for mismatched parentheses
        validateParentheses(cleanInput);

        // Check for empty parameter lists
        Pattern emptyParams = Pattern.compile("\\(\\s*\\)");
        if (emptyParams.matcher(input).find()) {
            throw new InvalidInputException("Functions cannot have empty parameter lists.");
        }

        // Check for consecutive commas
        Pattern consecutiveCommas = Pattern.compile(",\\s*,");
        if (consecutiveCommas.matcher(input).find()) {
            throw new InvalidInputException("Consecutive commas are not allowed.");
        }

        // Check for leading or trailing commas in parameter lists
        Pattern invalidCommas = Pattern.compile("\\(\\s*,|,\\s*\\)");
        if (invalidCommas.matcher(input).find()) {
            throw new InvalidInputException("Parameter lists cannot start or end with commas.");
        }

        // Check for invalid function/constant patterns
        validateExpressionStructure(cleanInput);
    }

    @Override
    public void validateParentheses(String input) throws InvalidInputException {
        int count = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count < 0) {
                    throw new InvalidInputException("Mismatched parentheses: closing parenthesis without opening.");
                }
            }
        }
        if (count != 0) {
            throw new InvalidInputException("Mismatched parentheses: " + count + " unclosed opening parentheses.");
        }
    }

    @Override
    public void validateExpressionStructure(String input) throws InvalidInputException {
        // For nested expressions, we need to validate recursively
        if (!isValidExpression(input)) {
            throw new InvalidInputException("Invalid expression structure: " + input);
        }
    }

    @Override
    public boolean isValidExpression(String expr) {
        if (expr.isEmpty()) return false;

        // Single character (variable or constant)
        if (expr.length() == 1) {
            return Character.isLetter(expr.charAt(0));
        }

        // Must start with a letter
        if (!Character.isLetter(expr.charAt(0))) {
            return false;
        }

        // Must be a function with parameters
        if (expr.charAt(1) != '(') {
            return false;
        }

        if (!expr.endsWith(")")) {
            return false;
        }

        // Extract parameter list and validate
        String paramList = expr.substring(2, expr.length() - 1);
        return validateParameterListStructure(paramList);
    }

    public boolean validateParameterListStructure(String paramList) {
        if (paramList.isEmpty()) return false;

        List<String> params = splitParameters(paramList);
        for (String param : params) {
            if (!isValidExpression(param.trim())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> splitParameters(String paramList) {
        List<String> params = new ArrayList<>();
        int level = 0;
        StringBuilder current = new StringBuilder();

        for (char c : paramList.toCharArray()) {
            if (c == '(') {
                level++;
                current.append(c);
            } else if (c == ')') {
                level--;
                current.append(c);
            } else if (c == ',' && level == 0) {
                params.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            params.add(current.toString());
        }

        return params;
    }


    @Override
    public ParseResult parseExpression(String input, int startIndex, List<NodeDto> nodeList,
                                       List<Variable> variableList, int parentIndex, int edgeLabel) throws InvalidInputException {
        if (startIndex >= input.length()) {
            throw new InvalidInputException("Unexpected end of input");
        }

        char currentChar = input.charAt(startIndex);

        // Check if it's a variable (uppercase)
        if (Character.isUpperCase(currentChar)) {
            String currentCharString = String.valueOf(currentChar);
            Variable variable = createVariable(currentCharString);
            NodeDto node = new NodeDto(currentCharString, variable, edgeLabel, parentIndex, 0);
            nodeList.add(node);
            variableList.add(variable);
            return new ParseResult(node, startIndex + 1);
        }

        // Check if it's a function or constant (lowercase)
        if (Character.isLowerCase(currentChar)) {
            String functionName = String.valueOf(currentChar);
            int nextIndex = startIndex + 1;

            // Check if it has parameters
            if (nextIndex < input.length() && input.charAt(nextIndex) == '(') {
                // It's a function with parameters
                nextIndex++; // Skip '('

                // First, create and add the function node to get its correct index
                NodeDto functionNode = new NodeDto(functionName, null, edgeLabel, parentIndex, 0);
                int functionNodeIndex = nodeList.size();
                nodeList.add(functionNode);

                int parameterIndex = 0;
                int childCount = 0;

                // Parse parameters with correct parent index
                while (nextIndex < input.length() && input.charAt(nextIndex) != ')') {
                    if (input.charAt(nextIndex) == ',') {
                        nextIndex++; // Skip comma
                        continue;
                    }

                    ParseResult childResult = parseExpression(input, nextIndex, nodeList, variableList, functionNodeIndex, parameterIndex);
                    nextIndex = childResult.nextIndex();
                    parameterIndex++;
                    childCount++;
                }

                if (nextIndex >= input.length() || input.charAt(nextIndex) != ')') {
                    throw new InvalidInputException("Missing closing parenthesis");
                }
                nextIndex++; // Skip ')'

                // Update the function node with correct outDegree
                NodeDto updatedFunctionNode = new NodeDto(functionName, null, edgeLabel, parentIndex, childCount);
                nodeList.set(functionNodeIndex, updatedFunctionNode);

                return new ParseResult(updatedFunctionNode, nextIndex);
            } else {
                // It's a constant (no parameters)
                NodeDto constantNode = new NodeDto(functionName, null, edgeLabel, parentIndex, 0);
                nodeList.add(constantNode);
                return new ParseResult(constantNode, nextIndex);
            }
        }

        throw new InvalidInputException("Invalid character: " + currentChar);
    }

    @Override
    public TreeNode[] convertToBfs(List<NodeDto> nodeList) {
        if (nodeList.isEmpty()) {
            return new TreeNode[0];
        }

        // Find the root
        int rootIndex = -1;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).father() == -1) {
                rootIndex = i;
                break;
            }
        }

        if (rootIndex == -1) {
            throw new IllegalArgumentException("No root node found");
        }

        // BFS traversal with immediate parent index correction
        List<TreeNode> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> indexMapping = new HashMap<>();

        // Add root
        queue.offer(rootIndex);

        while (!queue.isEmpty()) {
            int currentOldIndex = queue.poll();
            NodeDto currentNode = nodeList.get(currentOldIndex);

            // Determine new parent index
            int newParentIndex = -1;
            if (currentNode.father() != -1) {
                newParentIndex = indexMapping.get(currentNode.father());
            }

            SubNode[] tour = IntStream.rangeClosed(0, currentNode.outdegree()).mapToObj(i -> new SubNode()).toArray(SubNode[]::new);

            // Create new node with correct parent index
            TreeNode newNode = new TreeNode.Builder(currentNode.name())
                    .variable(currentNode.variable())
                    .father(newParentIndex)
                    .edgeLabel(currentNode.edgeLabel())
                    .outDegree(currentNode.outdegree())
                    .tour(tour)
                    .build();

            // Record index mapping and add to result
            indexMapping.put(currentOldIndex, result.size());
            result.add(newNode);

            // Find and sort children
            List<Integer> children = new ArrayList<>();
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).father() == currentOldIndex) {
                    children.add(i);
                }
            }

            queue.addAll(children);
        }

        return result.toArray(new TreeNode[0]);
    }


}
