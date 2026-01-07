package com.bachelor;

import com.bachelor.preprocess.*;
import com.bachelor.preprocess.initializer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Coordinator {
    private static final Logger logger = LoggerFactory.getLogger(Coordinator.class);

    private static TreeNode[] initializeTArray(int length){
        TreeNode[] treeNodes = new TreeNode[length];

        int outdegree0 = 2;
        SubNode[] tour = new SubNode[outdegree0+1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode(5);
        }
        TreeNode treeNode0 = new TreeNode.Builder("f(f(a,X),Y)")
                .father(-1)
                .isVariable(false)
                .outDegree(outdegree0)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree1 = 2;
        tour = new SubNode[outdegree1+1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode(5);
        }
        TreeNode treeNode1 = new TreeNode.Builder("f(a,X)")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree1)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree2 = 0;
        tour = new SubNode[outdegree2+1]; //Do we need here outdegree + 1?
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode(5);
        }
        TreeNode treeNode2 = new TreeNode.Builder("Y")
                .father(0)
                .isVariable(true)
                .outDegree(outdegree2)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree3 = 0;
        tour = new SubNode[outdegree3+1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode(5);
        }
        TreeNode treeNode3 = new TreeNode.Builder("a")
                .father(1)
                .isVariable(false)
                .outDegree(outdegree3)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree4 = 0;
        tour = new SubNode[outdegree4+1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode(5);
        }
        TreeNode treeNode4 = new TreeNode.Builder("X")
                .father(1)
                .isVariable(true)
                .outDegree(outdegree4)
                .edgeLabel(1)
                .tour(tour)
                .build();

        treeNodes[0] = treeNode0;
        treeNodes[1] = treeNode1;
        treeNodes[2] = treeNode2;
        treeNodes[3] = treeNode3;
        treeNodes[4] = treeNode4;

        return treeNodes;
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
        int inputPatternLength = 5;

        try (ExecutorService executor = Executors.newFixedThreadPool(inputPatternLength)) {

            TreeNode[] T = initializeTArray(inputPatternLength);
            InputArray inputArray = new InputArray(T,2);
            EulerChain eulerChain = new EulerChain(inputArray);

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
                    .mapToObj(i -> new InitializeType(i, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            // Phase 4: SubTree
            initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeSubTree(i, inputArray))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            // Phase 5: EulerChain
            initializers = IntStream.range(0, inputPatternLength)
                    .mapToObj(i -> new InitializeEulerChain(i, eulerChain))
                    .toArray(Initializer[]::new);
            runPhase(executor, phaser, initializers, 10);

            for (int i = 0; i < T.length; i++) {
                for (var subNode : T[i].tour){
                    System.out.println("T[" + i +"]" + subNode + " SubNode@" + Integer.toHexString(System.identityHashCode(subNode)));
                }
            }
        }
    }

}
