package com.bachelor.utils;

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

        return IntStream.rangeClosed(start, end)
                .mapToObj(i -> T[chain[i].getNodeInfo()].getLabel())
                .collect(Collectors.joining());
    }
}
