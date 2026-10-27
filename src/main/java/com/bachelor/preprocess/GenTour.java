package com.bachelor.preprocess;

import com.bachelor.preprocess.initializer.*;
import com.bachelor.utils.ExecutorBarrierUtils;
import java.util.concurrent.*;

public class GenTour {

    public static EulerChain buildAndPreprocess(ExecutorService executor, InputArray inputArray){
        EulerChain eulerChain = new EulerChain(inputArray);
        preprocessTree(executor, inputArray, eulerChain, inputArray.getT().length);
        return eulerChain;
    }
    private static void preprocessTree(ExecutorService executor, InputArray inputArray,
                                       EulerChain eulerChain, int length) {
        ExecutorBarrierUtils.forEachIndex(executor, length,
                i -> new InitializeNodeInfo(i, inputArray).run(),
                i -> new InitializeTourInfo(i, inputArray).run(),
                i -> new InitializeType(i, inputArray).run());

        InitializeCost.createAndRunNewInstance(executor, eulerChain);

        ExecutorBarrierUtils.forEachIndex(executor, length,
                i -> new InitializeSubTree(i, eulerChain).run(),
                i -> new InitializeEulerChain(i, eulerChain).run());
    }
}
