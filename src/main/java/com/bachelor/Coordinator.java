package com.bachelor;

import com.bachelor.algorithm.ComputeCosts;
import com.bachelor.algorithm.MTabes;
import com.bachelor.algorithm.Splice;

import com.bachelor.preprocess.*;

import static com.bachelor.TestExamples.*;
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

            Splice splice = createAndComputeSplices(pattern, executor1, phaser1);

            MTabes mTabes = new MTabes(splice, subject, pattern);
            mTabes.createMTables();

            System.out.println(mTabes);
        }
    }

    private static Splice createAndComputeSplices(EulerChain pattern, ExecutorService executor, Phaser phaser)
            throws InterruptedException, TimeoutException {
        Splice splice = new Splice(pattern);
        ComputeCosts computeCosts = new ComputeCosts(pattern, splice, executor, phaser);
        computeCosts.createSplices();
        return splice;
    }
}
