package com.bachelor;

import com.bachelor.algorithm.ComputeCosts;
import com.bachelor.algorithm.MTabes;
import com.bachelor.algorithm.Splices;
import static com.bachelor.TestExamples.initializePatternTArray;
import static com.bachelor.TestExamples.initializeSubjectTArray;
import com.bachelor.preprocess.*;
import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

import java.util.concurrent.*;

//This class need huge refactoring. It is just for testing now.
public class Coordinator {

    public static void main(String[] args) throws InterruptedException, TimeoutException {
        final int patternLength = 5;
        final int subjectLength = 9;

        try (ExecutorService executor1 = Executors.newCachedThreadPool();
             ExecutorService executor2 = Executors.newCachedThreadPool()) {
            Phaser phaser1 = new Phaser();
            Phaser phaser2 = new Phaser();

            EulerChain pattern = buildAndPreprocess(executor1, phaser1, () -> initializePatternTArray(patternLength), 2, 10);
            EulerChain subject = buildAndPreprocess(executor2, phaser2, () -> initializeSubjectTArray(subjectLength), 0, 10);

            Splices splices = createAndComputeSplices(pattern, executor1, phaser1);

            MTabes mTabes = new MTabes(splices, subject, pattern);
            mTabes.createMTables();

            System.out.println(mTabes);
        }
    }

    private static Splices createAndComputeSplices(EulerChain pattern, ExecutorService executor, Phaser phaser)
            throws InterruptedException, TimeoutException {
        Splices splices = new Splices(pattern);
        ComputeCosts computeCosts = new ComputeCosts(pattern, splices, executor, phaser);
        computeCosts.createSplices();
        return splices;
    }
}
