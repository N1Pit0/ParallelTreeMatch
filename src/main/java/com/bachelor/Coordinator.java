package com.bachelor;

import com.bachelor.algorithm.ComputeCosts;
import com.bachelor.algorithm.Splices;
import static com.bachelor.TestExamples.initializePatternTArray;
import static com.bachelor.TestExamples.initializeSubjectTArray;
import com.bachelor.preprocess.*;
import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

import java.util.Arrays;
import java.util.concurrent.*;

//This class need huge refactoring. It is just for testing now.
public class Coordinator {

    static void main(String[] args) throws InterruptedException, TimeoutException {
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

            EulerChain patterChain = (EulerChain) patternT[1];

            System.out.println("parallel prefix for pattern: ");
            var splices = new Splices(patterChain);
            var a = new ComputeCosts(patterChain, splices, executor1, phaser1);
            a.createSplices();

            System.out.println(Arrays.deepToString(splices.getSplices()));
        }
    }
}
