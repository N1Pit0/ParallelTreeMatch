package com.bachelor;

import com.bachelor.algorithm.ComputeCosts;
import com.bachelor.algorithm.Splices;
import static com.bachelor.TestExamples.initializePatternTArray;
import static com.bachelor.TestExamples.initializeSubjectTArray;
import com.bachelor.preprocess.*;
import static com.bachelor.preprocess.GenTour.buildAndPreprocess;
import static com.bachelor.preprocess.TreePrintUtils.printTreeTours;

import java.util.Arrays;
import java.util.concurrent.*;

//This class need huge refactoring. It is just for testing now.
public class Coordinator {

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
            Object[] subjectT = buildAndPreprocess(executor2, phaser2, () -> initializeSubjectTArray(subjectLength), 2, 10);

//             print pattern tours
            printTreeTours((TreeNode[]) patternT[0], "Pattern tours:");

            EulerChain patterChain = (EulerChain) patternT[1];
            for (var elem : patterChain.getChain()) {
                if (elem == null) continue;
                System.out.println(elem.getNodeInfo());
            }

            // print subject tours
            printTreeTours((TreeNode[]) subjectT[0], "Subject tours:");

            EulerChain subjectChain = (EulerChain) subjectT[1];
            for (var elem : subjectChain.getChain()) {
                if (elem == null) continue;
                System.out.println(elem.getNodeInfo());
            }

            System.out.println("parallel prefix for pattern: ");
            var splices = new Splices(patterChain);
            var a = new ComputeCosts(patterChain, splices, executor1, phaser1);
            a.createSplices();

            System.out.println(Arrays.deepToString(splices.getSplices()));
        }
    }


}
