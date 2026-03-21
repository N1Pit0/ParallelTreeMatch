package com.bachelor.preprocess;

import com.bachelor.preprocess.initializer.*;
import com.bachelor.utils.ExecutorBarrierUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GenTour {

    public static EulerChain buildAndPreprocess(ExecutorService executor, Supplier<TreeNode[]> treeSupplier, int variableCount, int timeoutSeconds){
        TreeNode[] treeNodes = treeSupplier.get();
        InputArray inputArray = new InputArray(treeNodes, variableCount);
        EulerChain eulerChain = new EulerChain(inputArray);
        preprocessTree(executor, inputArray, eulerChain, treeNodes.length, timeoutSeconds);
        return eulerChain;
    }

    public static EulerChain buildAndPreprocess(ExecutorService executor, Supplier<TreeNode[]> treeSupplier, int timeoutSeconds){
        return buildAndPreprocess(executor, treeSupplier, 0, timeoutSeconds);
    }

    private static void preprocessTree(ExecutorService executor, InputArray inputArray, EulerChain eulerChain, int length, int timeoutSeconds){
        List<Runnable> initializers;

        // Phase 1: NodeInfo
        initializers = IntStream.range(0, length)
                .parallel()
                .mapToObj(i -> (Runnable) new InitializeNodeInfo(i, inputArray))
                .toList();
        runPhase(executor, initializers, timeoutSeconds);

        // Phase 2: TourInfo
        initializers = IntStream.range(0, length)
                .parallel()
                .mapToObj(i -> (Runnable) new InitializeTourInfo(i, inputArray))
                .toList();
        runPhase(executor, initializers, timeoutSeconds);

        // Phase 3: Leaf/Type
        initializers = IntStream.range(0, length)
                .parallel()
                .mapToObj(i -> (Runnable) new InitializeType(i, inputArray))
                .toList();
        runPhase(executor, initializers, timeoutSeconds);

        InitializeCost.createAndRunNewInstance(executor, eulerChain, timeoutSeconds);

        // Phase 4: SubTree
        initializers = IntStream.range(0, length)
                .parallel()
                .mapToObj(i -> (Runnable) new InitializeSubTree(i, eulerChain))
                        .toList();
        runPhase(executor, initializers, timeoutSeconds);

        // Phase 5: EulerChain
        initializers = IntStream.range(0, length)
                .parallel()
                .mapToObj(i -> (Runnable) new InitializeEulerChain(i, eulerChain))
                        .toList();
        runPhase(executor, initializers, timeoutSeconds);
    }

    private static void runPhase(ExecutorService executor, List<Runnable> initializers, int timeoutSeconds){
        ExecutorBarrierUtils.invokeAll(executor, initializers, timeoutSeconds);
    }
}
