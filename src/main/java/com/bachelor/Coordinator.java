package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.preprocess.EulerChain;

import java.util.concurrent.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

public class Coordinator {

    public static void main(@SuppressWarnings("unused") String[] args){

        try (ExecutorService executor = Executors.newCachedThreadPool()) {

            EulerChain pattern = buildAndPreprocess(executor, TestExamples::initializePatternTArray, 2, 5);
            EulerChain subject = buildAndPreprocess(executor, TestExamples::initializeSubjectTArray, 5);
            TreeMatch.performTreeMatch(executor, subject, pattern);
        }
    }
}
