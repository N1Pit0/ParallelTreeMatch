package com.bachelor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class Coordinator {
    private static final Logger logger = LoggerFactory.getLogger(Coordinator.class);
    private static final Phaser phaser = new Phaser();

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

    public static void main(String[] args) throws InterruptedException, TimeoutException {

        int inputPatternLength = 10;
        try (ExecutorService executor = Executors.newFixedThreadPool(inputPatternLength)) {

            TreeNode[] T = initializeTArray();
            InputArray inputArray = new InputArray(T);

            phaser.bulkRegister(inputPatternLength);
            for (int i = 0; i < inputPatternLength; i++) {
                final int index = i;

                executor.submit(() -> {
                    logger.info("Inside the thread");
                    new InitializeNodeInfo(index, inputArray).run();
                    phaser.arriveAndDeregister();
                    logger.debug("Called the arriveAndDeregister");
                });
            }

//            phaser.awaitAdvance(phaser.getPhase());

            phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 5, TimeUnit.SECONDS);

            phaser.bulkRegister(inputPatternLength);
            for (int i = 0; i < inputPatternLength; i++) {
                final int index = i;

                executor.submit(() -> {
                    new InitializeTourInfo(index, inputArray).run();
                    phaser.arriveAndDeregister();
                });
            }

//            phaser.awaitAdvance(phaser.getPhase());

            phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 5, TimeUnit.SECONDS);

        }
    }

}
