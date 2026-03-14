package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.preprocess.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

import java.util.concurrent.*;

public class Coordinator {

    @SuppressWarnings("UnnecessaryModifier")
    public static void main(@SuppressWarnings("unused") String[] args) throws InterruptedException, TimeoutException {

        try (ExecutorService executor1 = Executors.newCachedThreadPool();
             ExecutorService executor2 = Executors.newCachedThreadPool()) {
            Phaser phaser1 = new Phaser();
            Phaser phaser2 = new Phaser();

            EulerChain pattern = buildAndPreprocess(executor1, phaser1, TestExamples::initializePatternTArray, 2, 5);
            EulerChain subject = buildAndPreprocess(executor2, phaser2, TestExamples::initializeSubjectTArray,5);
            TreeMatch.performTreeMatch(executor2, phaser2, subject, pattern);
        }
    }
}
