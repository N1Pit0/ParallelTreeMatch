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
            DataLoader dataLoader = DataLoaderFactory.create();

            InputArray subjectTree = dataLoader.loadSubjectTree();
            InputArray patternTree = dataLoader.loadPatterTree();

            EulerChain pattern = buildAndPreprocess(executor, patternTree, Config.getInt("com.bachelor.coordinator.thread.timeout"));
            EulerChain subject = buildAndPreprocess(executor, subjectTree, Config.getInt("com.bachelor.coordinator.thread.timeout"));

            TreeMatch.performTreeMatch(executor, subject, pattern);
        }
    }
}

