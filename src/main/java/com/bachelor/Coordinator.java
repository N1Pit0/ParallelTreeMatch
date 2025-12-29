package com.bachelor;


import com.bachelor.initializer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Coordinator {
    private static final Logger logger = LoggerFactory.getLogger(Coordinator.class);

    private static TreeNode[] initializeTArray(){
        return new TreeNode[]{
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
                new TreeNode(5),
        };
    }

    private static void runPhase(ExecutorService executor, Phaser phaser, Initializer[] initializers, int timeoutSeconds) throws InterruptedException, TimeoutException {
        int phaseSize = initializers.length;
        phaser.bulkRegister(phaseSize);
        for (int i = 0; i < phaseSize; i++) {
            final int index = i;
            executor.submit(() -> {
                Coordinator.logger.info("Phase {}: Task {} started", phaser.getPhase(), index);
                try {
                    initializers[index].initialize();
                } catch (Exception e) {
                    Coordinator.logger.error("Phase {}: Task {} exception", phaser.getPhase(), index, e);
                } finally {
                    phaser.arriveAndDeregister();
                    Coordinator.logger.debug("Phase {}: Task {} finished and deregistered", phaser.getPhase(), index);
                }
            });
        }
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), timeoutSeconds, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException, TimeoutException {
        Phaser phaser = new Phaser();
        int inputPatternLength = 10;

        try (ExecutorService executor = Executors.newFixedThreadPool(inputPatternLength)) {

            TreeNode[] T = initializeTArray();
            InputArray inputArray = new InputArray(T);
            EulerChain eulerChain = new EulerChain();

            // Phase 1: NodeInfo
            Initializer[] initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeNodeInfo(i, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            // Phase 2: TourInfo
            initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeTourInfo(i, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            // Phase 3: Leaf
            initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeLeaf(i, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            // Phase 4: SubTree
            initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeSubTree(i, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            // Phase 5: EulerChain
            initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeEulerChain(i, eulerChain, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);
        }
    }

}
