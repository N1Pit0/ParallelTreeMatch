package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;


@SuppressWarnings("ClassCanBeRecord")
public class TreeMatch {
    private final ExecutorService executor;
    private final EulerChain subject;
    private final EulerChain pattern;

    private TreeMatch(ExecutorService executor, EulerChain subject, EulerChain pattern) {
        this.executor = executor;
        this.subject = subject;
        this.pattern = pattern;
    }

    public static void performTreeMatch(ExecutorService executor, EulerChain subject, EulerChain pattern) throws InterruptedException, TimeoutException {
        TreeMatch treeMatch = new TreeMatch(executor, subject, pattern);
        treeMatch.runPhaseTwo();
    }

    private static Splice createAndComputeSplices(EulerChain pattern, ExecutorService executor)
            throws InterruptedException, TimeoutException {
        Splice splice = new Splice(pattern);
        PopulateSplice populateSplice = new PopulateSplice(pattern, splice, executor);
        populateSplice.createSplices();
        return splice;
    }

    private static MTables createAndComputeMTables(EulerChain subject, EulerChain pattern, Splice splice) {
        MTables mTables = new MTables(splice, subject, pattern);
        mTables.createMTables();

        return mTables;
    }

    private MTables runPhaseOne() throws InterruptedException, TimeoutException {
        Splice splice = createAndComputeSplices(pattern, executor);

        return createAndComputeMTables(subject, pattern, splice);
    }

    private void runPhaseTwo() throws InterruptedException, TimeoutException {
        MTables mTables = runPhaseOne();
        MTableMerger tableMerger = new MTableMerger(mTables, executor);
        tableMerger.computeAndMergeMTables();
    }
}
