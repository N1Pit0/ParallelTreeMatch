package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.config.Config;
import com.bachelor.data.*;
import com.bachelor.preprocess.*;

import java.util.concurrent.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

public class Coordinator {

    public static void main(@SuppressWarnings("unused") String[] args) {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            System.out.println(Config.getActiveProfile());
            System.out.println(Config.getMatchingAlgorithm());

            DataLoader dataLoader = DataLoaderFactory.create();

            InputArray subjectTree = dataLoader.loadSubjectTree();
            InputArray patternTree = dataLoader.loadPatternTree();

            int threadTimeout = Config.getInt("com.bachelor.coordinator.thread.timeout");
            EulerChain pattern = buildAndPreprocess(executor, patternTree, threadTimeout);
            EulerChain subject = buildAndPreprocess(executor, subjectTree, threadTimeout);

            TreeMatch.performTreeMatch(executor, subject, pattern);
        }
    }
}

