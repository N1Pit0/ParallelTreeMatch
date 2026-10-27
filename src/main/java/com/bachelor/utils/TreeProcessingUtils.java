package com.bachelor.utils;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.*;

public class TreeProcessingUtils {

    public static String getReplacementForVar(EulerChain eulerChain, int start, int end){
        SubNode[] E = eulerChain.getChain();
        TreeNode[] T = eulerChain.getT();
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < end; i++) {
            switch (E[i].getType()) {
                case FIRST -> sb.append(T[E[i].getNodeInfo()].getLabel()).append('(');
                case LEAF  -> sb.append(T[E[i].getNodeInfo()].getLabel());
                case DUMMY -> sb.append(',');
                case LAST  -> sb.append(')');
            }
        }
        return sb.toString();
    }

    /*
    TODO: Change behavior based on the algorihtn type
     Means we should not reserve only half the alphabet for TermVariable if the algorightm is term
     */
    public static Variable createVariable(String variable) throws InvalidInputException {
        if (variable.length() != 1) throw new InvalidInputException("Variable name length should only be 1");
//        return (variable.charAt(0) <= 'M') ? new TermVariable(variable) : new SequentialVariable(variable);
        return new TermVariable(variable);
    }
}
