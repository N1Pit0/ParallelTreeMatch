package com.bachelor;

import com.bachelor.algorithm.MatchPipeline;
import com.bachelor.data.DataLoader;
import com.bachelor.data.DataLoaderFactory;
import com.bachelor.data.json.InputPair;
import com.bachelor.data.json.LoadResult;
import com.bachelor.exceptions.DataLoadException;
import com.bachelor.parser.InputParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Coordinator {

    private static final Logger log = LoggerFactory.getLogger(Coordinator.class);

    public static void main(@SuppressWarnings("unused") String[] args) {
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

        int succeeded = 0;
        int failed = 0;

        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            for (InputPair pair : result.pairs()) {
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("Interrupted, stopping before entry {}", pair.entryNumber());
                    break;
                }
                if (processPair(executor, pair)) {
                    succeeded++;
                } else {
                    failed++;
                }
            }
        }

        log.info("Finished: {} succeeded, {} failed", succeeded, failed);
    }

    private static boolean processPair(ExecutorService executor, InputPair pair) {
        log.info("Entry {}: matching pattern '{}' against subject '{}'",
                pair.entryNumber(), pair.patternText(), pair.subjectText());
        try {
            MatchPipeline.run(executor, pair.subject(), pair.pattern());
            return true;
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Entry {} failed: {}", pair.entryNumber(), e.getMessage(), e);
            return false;
        }
    }
}