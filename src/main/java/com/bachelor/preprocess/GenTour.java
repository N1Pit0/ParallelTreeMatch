package com.bachelor.preprocess;

import com.bachelor.preprocess.initializer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GenTour {

    private static void runPhase(ExecutorService executor, Phaser phaser, Initializer[] initializers, int timeoutSeconds) throws InterruptedException, TimeoutException {
        int phaseSize = initializers.length;
        phaser.bulkRegister(phaseSize);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < phaseSize; i++) {
            final int index = i;
           futures.add( executor.submit(() -> {
                try {
                    initializers[index].initialize();
                } catch (Exception _) {
                }
            }));
        }

        for (Future<?> f : futures) {
            try {
                f.get(timeoutSeconds, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                // Handle exceptions from the initializer tasks if needed
                e.printStackTrace();
            }
        }
//        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), timeoutSeconds, TimeUnit.SECONDS);
    }

    public static EulerChain buildAndPreprocess(ExecutorService executor, Phaser phaser, Supplier<TreeNode[]> treeSupplier, int variableCount, int timeoutSeconds) throws InterruptedException, TimeoutException {
        TreeNode[] treeNodes = treeSupplier.get();
        InputArray inputArray = new InputArray(treeNodes, variableCount);
        EulerChain eulerChain = new EulerChain(inputArray);
        preprocessTree(executor, phaser, inputArray, eulerChain, treeNodes.length, timeoutSeconds);
        return eulerChain;
    }

    public static EulerChain buildAndPreprocess(ExecutorService executor, Phaser phaser, Supplier<TreeNode[]> treeSupplier, int timeoutSeconds) throws InterruptedException, TimeoutException {
        return buildAndPreprocess(executor, phaser, treeSupplier, 0, timeoutSeconds);
    }


    private static void preprocessTree(ExecutorService executor, Phaser phaser, InputArray inputArray, EulerChain eulerChain, int length, int timeoutSeconds) throws InterruptedException, TimeoutException {
        Initializer[] initializers;

        // Phase 1: NodeInfo
        initializers = IntStream.range(0, length)
                .mapToObj(i -> new InitializeNodeInfo(i, inputArray))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);

        // Phase 2: TourInfo
        initializers = IntStream.range(0, length)
                .mapToObj(i -> new InitializeTourInfo(i, inputArray))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);

        // Phase 3: Leaf/Type
        initializers = IntStream.range(0, length)
                .mapToObj(i -> new InitializeType(i, inputArray))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);

        InitializeCost initializeCost = new InitializeCost(executor, eulerChain);
        initializeCost.doWork();

        // Phase 4: SubTree
        initializers = IntStream.range(0, length)
                .mapToObj(i -> new InitializeSubTree(i, eulerChain))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);

        // Phase 5: EulerChain
        initializers = IntStream.range(0, length)
                .mapToObj(i -> new InitializeEulerChain(i, eulerChain))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);
    }
}
