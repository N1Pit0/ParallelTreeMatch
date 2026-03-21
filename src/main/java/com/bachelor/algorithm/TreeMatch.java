package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import java.util.concurrent.ExecutorService;

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

    public static void performTreeMatch(ExecutorService executor, EulerChain subject, EulerChain pattern){
        TreeMatch treeMatch = new TreeMatch(executor, subject, pattern);
        treeMatch.runPhaseTwo();
    }

    private static Splice createAndComputeSplices(EulerChain pattern, ExecutorService executor) {
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

    private MTables runPhaseOne(){
        Splice splice = createAndComputeSplices(pattern, executor);

        return createAndComputeMTables(subject, pattern, splice);
    }

    private void runPhaseTwo(){
        MTables mTables = runPhaseOne();
        MTableMerger tableMerger = new MTableMerger(mTables, executor);
        tableMerger.computeAndMergeMTables();
    }
}
