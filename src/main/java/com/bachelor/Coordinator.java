package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.config.Config;
import com.bachelor.data.DataLoader;
import com.bachelor.data.DataLoaderFactory;
import com.bachelor.data.json.InputPair;
import com.bachelor.data.json.LoadResult;
import com.bachelor.exceptions.DataLoadException;
import com.bachelor.parser.InputParserImpl;
import com.bachelor.preprocess.EulerChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

public class Coordinator {

    private static final Logger log = LoggerFactory.getLogger(Coordinator.class);

    public static void main(@SuppressWarnings("unused") String[] args) {
        // 1. Load input data
        InputParser parser = new InputParserImpl();
        DataLoader dataLoader = DataLoaderFactory.create(parser);

        LoadResult result;
        try {
            result = dataLoader.load();
        } catch (DataLoadException e) {
            log.error("Cannot load input data: {}", e.getMessage());
            return;
        }

        result.errors().forEach(error -> log.warn("{}", error));
        log.info("Loaded {} pairs, skipped {} entries",
                result.pairs().size(), result.skippedEntryCount());

        if (result.pairs().isEmpty()) {
            log.warn("No valid input pairs, nothing to match");
            return;
        }

        int threadTimeout = Config.getInt("com.bachelor.coordinator.thread.timeout");

        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            for (InputPair pair : result.pairs()) {
                if (!processPair(executor, pair, threadTimeout)) {
                    break;
                }
            }
        }
    }

    /**
     * Preprocesses and matches one subject/pattern pair.
     * A failure is logged and does not stop the remaining pairs.
     *
     * @return false if the thread was interrupted and processing should stop
     */
    private static boolean processPair(ExecutorService executor, InputPair pair, int threadTimeout) {
        log.info("Entry {}: matching pattern '{}' against subject '{}'",
                pair.entryNumber(), pair.patternText(), pair.subjectText());
        try {
            EulerChain pattern = buildAndPreprocess(executor, pair.pattern(), threadTimeout);
            EulerChain subject = buildAndPreprocess(executor, pair.subject(), threadTimeout);

            TreeMatch.performTreeMatch(executor, subject, pattern);
            return true;

        } catch (Exception e) {
            log.error("Entry {} failed: {}", pair.entryNumber(), e.getMessage(), e);
            return false;
        }
    }
}