package com.bachelor;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.*;
import com.bachelor.preprocess.*;

import java.util.List;

public interface InputParser {
    InputArray parseToArray(String inputValue) throws InvalidInputException;

    void validateInput(String input) throws InvalidInputException;

    void validateParentheses(String input) throws InvalidInputException;

    void validateExpressionStructure(String input) throws InvalidInputException;

    boolean isValidExpression(String expr);

    boolean validateParameterListStructure(String paramList);

    List<String> splitParameters(String paramList);

    ParseResult parseExpression(String input, int startIndex, List<NodeDto> nodeList,
                                int parentIndex, int edgeLabel) throws InvalidInputException;

    TreeNode[] convertToBfs(List<NodeDto> nodeList);
}
