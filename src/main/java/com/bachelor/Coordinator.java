package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.preprocess.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

import java.util.concurrent.*;

//This class need huge refactoring. It is just for testing now.
public class Coordinator {

    public static void main(String[] args) throws InterruptedException, TimeoutException {

        try (ExecutorService executor1 = Executors.newCachedThreadPool();
             ExecutorService executor2 = Executors.newCachedThreadPool()) {
            Phaser phaser1 = new Phaser();
            Phaser phaser2 = new Phaser();

            EulerChain pattern = buildAndPreprocess(executor1, phaser1, TestExamples::initializePatternTArrayOne, 4, 10);
            EulerChain subject = buildAndPreprocess(executor2, phaser2, TestExamples::initializeSubjectTArrayOne, 0, 10);
            TreeMatch treeMatch = new TreeMatch(executor1, phaser1, subject, pattern);
            treeMatch.runPhaseTwo();
        }
    }
}
