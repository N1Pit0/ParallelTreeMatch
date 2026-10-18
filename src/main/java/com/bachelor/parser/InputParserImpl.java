package com.bachelor.parser;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import com.bachelor.preprocess.Variable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static com.bachelor.utils.TreeProcessingUtils.createVariable;

/**
 * Parses a term such as "f(a, f(B, c))" into an {@link InputArray}.
 *
 * <pre>
 * term := VARIABLE                              A-Z, never has arguments
 *       | CONSTANT                              a-z without arguments
 *       | FUNCTION "(" term ("," term)* ")"     a-z with at least one argument
 * </pre>
 *
 * Each symbol is exactly one letter. Whitespace between symbols is ignored.
 * The resulting nodes are in breadth-first order:
 *   father    = index of the parent node (-1 for the root)
 *   edgeLabel = argument position within the parent (0-based, 0 for the root)
 * Variables are listed in the order they appear in the input.
 */
public class InputParserImpl implements InputParser {

    @Override
    public InputArray parseToArray(String input) throws InvalidInputException {
        if (input == null || input.isBlank()) {
            throw new InvalidInputException("Input cannot be null or empty");
        }

        // Step 1: string -> tree (also validates the input)
        TermReader reader = new TermReader(input);
        ParsedNode root = reader.parseTerm();
        reader.expectEnd();

        // Step 2: tree -> breadth-first array
        return new InputArray(toBreadthFirstArray(root), reader.variables);
    }
    /** Intermediate tree, only used inside this class. variable is null for functions and constants. */
    private record ParsedNode(char symbol, Variable variable, List<ParsedNode> children) {}

    /**
     * Holds the state of a single parse run (input, current position, collected variables).
     */
    private static final class TermReader {

        private static final int END = -1;

        private final String input;
        private final List<Variable> variables = new ArrayList<>();
        private int pos = 0;

        TermReader(String input) {
            this.input = input;
        }

        /** term := VARIABLE | CONSTANT | FUNCTION "(" term ("," term)* ")" */
        ParsedNode parseTerm() throws InvalidInputException {
            int c = peek();

            if (c == END) {
                throw error("Unexpected end of input, expected a letter");
            }

            char symbol = (char) c;

            if (isUpper(symbol)) {
                pos++;
                return parseVariable(symbol);
            } else if (isLower(symbol)) {
                pos++;
                return (peek() == '(') ? parseFunction(symbol) : constant(symbol);
            }

            throw error("Expected a letter but found '" + symbol + "'");
        }

        /** VARIABLE: an uppercase letter that must not be followed by "(". */
        private ParsedNode parseVariable(char symbol) throws InvalidInputException {
            if (peek() == '(') {
                throw error("Variable '" + symbol + "' cannot have arguments");
            }
            Variable variable = createVariable(String.valueOf(symbol));
            variables.add(variable);
            return new ParsedNode(symbol, variable, List.of());
        }

        /** CONSTANT: a lowercase letter without arguments. */
        private ParsedNode constant(char symbol) {
            return new ParsedNode(symbol, null, List.of());
        }

        /** FUNCTION "(" term ("," term)* ")" — called when the next character is "(". */
        private ParsedNode parseFunction(char symbol) throws InvalidInputException {
            pos++; // consume '('

            if (peek() == ')') {
                throw error("Function '" + symbol + "' cannot have an empty argument list");
            }

            List<ParsedNode> arguments = new ArrayList<>();
            arguments.add(parseTerm());

            while (peek() == ',') {
                pos++; // consume ','
                arguments.add(parseTerm());
            }

            int c = peek();
            if (c == END) {
                throw error("Missing closing parenthesis for '" + symbol + "('");
            }
            if (c != ')') {
                throw error("Expected ',' or ')' but found '" + (char) c + "'");
            }
            pos++; // consume ')'

            return new ParsedNode(symbol, null, arguments);
        }

        /** Ensures nothing but whitespace follows the parsed term. */
        void expectEnd() throws InvalidInputException {
            int c = peek();
            if (c != END) {
                throw error("Unexpected '" + (char) c + "' after the end of the expression");
            }
        }

        /** Skips whitespace and returns the next character without consuming it, or END. */
        private int peek() {
            while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }
            return pos < input.length() ? input.charAt(pos) : END;
        }

        /** Builds an error message that points at the current position. */
        private InvalidInputException error(String message) {
            return new InvalidInputException(message + " at position " + (pos + 1) + ":\n"
                    + "  " + input + "\n"
                    + "  " + " ".repeat(pos) + "^");
        }

        private static boolean isUpper(char c) { return c >= 'A' && c <= 'Z'; }
        private static boolean isLower(char c) { return c >= 'a' && c <= 'z'; }
    }

    private static TreeNode[] toBreadthFirstArray(ParsedNode root) {
        record Pending(ParsedNode node, int fatherIndex, int edgeLabel) {}

        List<TreeNode> result = new ArrayList<>();
        Queue<Pending> queue = new ArrayDeque<>();
        queue.add(new Pending(root, -1, 0));

        while (!queue.isEmpty()) {
            Pending current = queue.poll();
            int currentIndex = result.size();
            result.add(toTreeNode(current.node(), current.fatherIndex(), current.edgeLabel()));

            List<ParsedNode> children = current.node().children();
            for (int i = 0; i < children.size(); i++) {
                queue.add(new Pending(children.get(i), currentIndex, i));
            }
        }
        return result.toArray(new TreeNode[0]);
    }

    private static TreeNode toTreeNode(ParsedNode node, int fatherIndex, int edgeLabel) {
        int outDegree = node.children().size();

        SubNode[] tour = new SubNode[outDegree + 1];
        Arrays.setAll(tour, _ -> new SubNode());

        return new TreeNode.Builder(String.valueOf(node.symbol()))
                .variable(node.variable())
                .father(fatherIndex)
                .edgeLabel(edgeLabel)
                .outDegree(outDegree)
                .tour(tour)
                .build();
    }
}