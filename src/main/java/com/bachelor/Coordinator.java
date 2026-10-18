package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.config.Config;
import com.bachelor.data.*;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.InputParserImpl;
import com.bachelor.preprocess.*;

import java.util.concurrent.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

public class Coordinator {

    public static void main(@SuppressWarnings("unused") String[] args) throws InvalidInputException {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            InputParser parser = new InputParserImpl();
            DataLoader dataLoader = DataLoaderFactory.create(parser);

            InputArray subjectTree = dataLoader.loadSubjectTree();
            InputArray patternTree = dataLoader.loadPatternTree();

            int threadTimeout = Config.getInt("com.bachelor.coordinator.thread.timeout");
            EulerChain pattern = buildAndPreprocess(executor, patternTree, threadTimeout);
            EulerChain subject = buildAndPreprocess(executor, subjectTree, threadTimeout);

            TreeMatch.performTreeMatch(executor, subject, pattern);
        }
    }
}

