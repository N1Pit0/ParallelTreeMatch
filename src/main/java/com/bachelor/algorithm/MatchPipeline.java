package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.InputArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

/**
 * Everything that happens after the input strings have been parsed:
 * preprocessing of pattern and subject, followed by the matching.
 */
public final class MatchPipeline {
    private static final Logger log = LoggerFactory.getLogger(MatchPipeline.class);

    private MatchPipeline() {}

    public static void run(ExecutorService executor,
                           InputArray subjectInput,
                           InputArray patternInput) throws Exception {
        EulerChain pattern = buildAndPreprocess(executor, patternInput);
        EulerChain subject = buildAndPreprocess(executor, subjectInput);

        log.info("Euler Chain for pattern is: {}, Euler Chain for subject is: {}", pattern, subject);
        TreeMatch.performTreeMatch(executor, subject, pattern);
    }
}