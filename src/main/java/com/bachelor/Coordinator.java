package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.preprocess.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

import java.util.concurrent.*;

public class Coordinator {

    public static void main(@SuppressWarnings("unused") String[] args) throws InterruptedException, TimeoutException {

        try (ExecutorService executor1 = Executors.newCachedThreadPool()) {

            EulerChain pattern = buildAndPreprocess(executor1, TestExamples::initializePatternTArray, 2, 5);
            EulerChain subject = buildAndPreprocess(executor1, TestExamples::initializeSubjectTArray,5);
            TreeMatch.performTreeMatch(executor1, subject, pattern);
        }
    }
}
