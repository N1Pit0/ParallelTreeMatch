package com.bachelor;

import com.bachelor.preprocess.*;
import com.bachelor.preprocess.initializer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;


//This class need huge refactoring. It is just for testing now.
public class Coordinator {
    private static final Logger logger = LoggerFactory.getLogger(Coordinator.class);

    private static TreeNode[] initializeSubjectTArray(int length) {
        TreeNode[] treeNodes = new TreeNode[length];

        int outdegree = 2;
        SubNode[] tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode0 = new TreeNode.Builder("f(f(a,b), f(f(a,a),a))")
                .father(-1)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 2;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode1 = new TreeNode.Builder("f(a,b)")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 2;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("f(f(a,a),a)")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode3 = new TreeNode.Builder("a")
                .father(1)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode4 = new TreeNode.Builder("b")
                .father(1)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        outdegree = 2;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode5 = new TreeNode.Builder("f(a,a)")
                .father(2)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode6 = new TreeNode.Builder("a")
                .father(2)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode7 = new TreeNode.Builder("a")
                .father(5)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode8 = new TreeNode.Builder("a")
                .father(5)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        treeNodes[0] = treeNode0;
        treeNodes[1] = treeNode1;
        treeNodes[2] = treeNode2;
        treeNodes[3] = treeNode3;
        treeNodes[4] = treeNode4;
        treeNodes[5] = treeNode5;
        treeNodes[6] = treeNode6;
        treeNodes[7] = treeNode7;
        treeNodes[8] = treeNode8;

        return treeNodes;
    }

    private static TreeNode[] initializePatternTArray(int length) {
        TreeNode[] treeNodes = new TreeNode[length];

        int outdegree0 = 2;
        SubNode[] tour = new SubNode[outdegree0 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode0 = new TreeNode.Builder("f(f(a,X),Y)")
                .father(-1)
                .isVariable(false)
                .outDegree(outdegree0)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree1 = 2;
        tour = new SubNode[outdegree1 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode1 = new TreeNode.Builder("f(a,X)")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree1)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree2 = 0;
        tour = new SubNode[outdegree2 + 1]; //Do we need here outdegree + 1?
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("Y")
                .father(0)
                .isVariable(true)
                .outDegree(outdegree2)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree3 = 0;
        tour = new SubNode[outdegree3 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode3 = new TreeNode.Builder("a")
                .father(1)
                .isVariable(false)
                .outDegree(outdegree3)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree4 = 0;
        tour = new SubNode[outdegree4 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
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
        final int patternLength = 5;
        final int subjectLength = 9;

        try (ExecutorService executor1 = Executors.newCachedThreadPool();
             ExecutorService executor2 = Executors.newCachedThreadPool()) { // Problems with number of nodes and available processors {
            Phaser phaser1 = new Phaser();
            Phaser phaser2 = new Phaser();

//             Preprocess pattern
            Object[] patternT = buildAndPreprocess(executor1, phaser1, () -> initializePatternTArray(patternLength), 2, 10);

            // Preprocess subject
            Object[] subjectT = buildAndPreprocess(executor2, phaser2, () -> initializeSubjectTArray(subjectLength), 0, 10);

//             print pattern tours
            printTreeTours((TreeNode[]) patternT[0], "Pattern tours:");

            EulerChain chain = (EulerChain) patternT[1];
            for (var elem : chain.getChain()) {
                if (elem == null) continue;
                System.out.println(elem.getNodeInfo());
            }

            // print subject tours
            printTreeTours((TreeNode[]) subjectT[0], "Subject tours:");

            chain = (EulerChain) subjectT[1];
            for (var elem : chain.getChain()) {
                if (elem == null) continue;
                System.out.println(elem.getNodeInfo());
            }

            int a = 2;
        }
    }

    private static Object[] buildAndPreprocess(ExecutorService executor, Phaser phaser, Supplier<TreeNode[]> treeSupplier, int variableCount, int timeoutSeconds) throws InterruptedException, TimeoutException {
        TreeNode[] treeNodes = treeSupplier.get();
        InputArray inputArray = new InputArray(treeNodes, variableCount);
        EulerChain eulerChain = new EulerChain(inputArray);
        preprocessTree(executor, phaser, inputArray, eulerChain, treeNodes.length, timeoutSeconds);
        return new Object[]{treeNodes, eulerChain};
    }

    /**
     * Prints tours for each TreeNode in the array. Safe to call with null arrays or null tours.
     */
    private static void printTreeTours(TreeNode[] treeNodes, String title) {
        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }
        if (treeNodes == null) return;
        for (int i = 0; i < treeNodes.length; i++) {
            TreeNode node = treeNodes[i];
            if (node == null || node.tour == null) continue;
            for (int j = 0; j < node.tour.length; j++) {
                System.out.println(" T[" + i + "].tour[" + j + "]" + node.tour[j]
                        + " SubNode@" + Integer.toHexString(System.identityHashCode(node.tour[j])));
            }
        }
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
                .mapToObj(i -> new InitializeSubTree(i, inputArray))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);

        // Phase 5: EulerChain
        initializers = IntStream.range(0, length)
                .mapToObj(i -> new InitializeEulerChain(i, eulerChain))
                .toArray(Initializer[]::new);
        runPhase(executor, phaser, initializers, timeoutSeconds);
    }
}
