package com.bachelor.utils;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TreeProcessingUtils {

    public static int getMatchStartNodeIndex(EulerChain eulerChain, int startIndexInEulerChian){
        return eulerChain.getChain()[startIndexInEulerChian].getNodeInfo();
    }

    public static String getReplacementForVar(EulerChain eulerChain, int start, int end){
        SubNode[] chain = eulerChain.getChain();
        TreeNode[] T = eulerChain.getT();

        return IntStream.range(start, end)
                .mapToObj(i -> T[chain[i].getNodeInfo()].getLabel())
                .collect(Collectors.joining());
    }

    /*
    TODO: Change behavior based on the algorihtn type
     Means we should not reserve only half the alphabet for TermVariable if the algorightm is term
     */
    public static Variable createVariable(String variable) throws InvalidInputException {
        if (variable.length() != 1) throw new InvalidInputException("Variable name length should only be 1");
        return (variable.charAt(0) <= 'M') ? new TermVariable(variable) : new SequentialVariable(variable);
    }
}
