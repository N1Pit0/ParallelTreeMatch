package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeoutException;


@SuppressWarnings("ClassCanBeRecord")
public class TreeMatch {
    private final ExecutorService executor;
    private final Phaser phaser;
    private final EulerChain subject;
    private final EulerChain pattern;

    public TreeMatch(ExecutorService executor, Phaser phaser, EulerChain subject, EulerChain pattern) {
        this.executor = executor;
        this.phaser = phaser;
        this.subject = subject;
        this.pattern = pattern;
    }

    private MTables runPhaseOne() throws InterruptedException, TimeoutException {
        Splice splice = createAndComputeSplices(pattern, executor, phaser);

        return createAndComputeMTables(subject, pattern, splice);
    }

    public void runPhaseTwo() throws InterruptedException, TimeoutException {
        MTables mTables = runPhaseOne();
        MTableMerger tableMerger = new MTableMerger(mTables, executor);
        tableMerger.computeAndMergeMTables();
    }

    private static Splice createAndComputeSplices(EulerChain pattern, ExecutorService executor, Phaser phaser)
            throws InterruptedException, TimeoutException {
        Splice splice = new Splice(pattern);
        PopulateSplice populateSplice = new PopulateSplice(pattern, splice, executor, phaser);
        populateSplice.createSplices();
        return splice;
    }

    private static MTables createAndComputeMTables(EulerChain subject, EulerChain pattern, Splice splice){
        MTables mTables = new MTables(splice, subject, pattern);
        mTables.createMTables();

        return mTables;
    }
}
