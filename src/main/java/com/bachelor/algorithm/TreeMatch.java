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
        MTablesContainer mTablesContainer = treeMatch.runPhaseOne();
        treeMatch.runPhaseTwo(mTablesContainer);
        int a = 0;
    }

    private static Splice createAndComputeSplices(EulerChain pattern, ExecutorService executor) {
        Splice splice = new Splice(pattern);
        PopulateSpliceStep populateSpliceStep = new PopulateSpliceStep(pattern, splice, executor);
        populateSpliceStep.createSplices();
        return splice;
    }

    private static MTablesContainer createAndComputeMTables(EulerChain subject, EulerChain pattern, Splice splice) {
        MTablesContainer mTablesContainer = new MTablesContainer(splice, subject, pattern);
        mTablesContainer.createMTables();

        return mTablesContainer;
    }

    private MTablesContainer runPhaseOne(){
        Splice splice = createAndComputeSplices(pattern, executor);

        return createAndComputeMTables(subject, pattern, splice);
    }

    private void runPhaseTwo(MTablesContainer mTablesContainer){
        MTableMerger tableMerger = new MTableMerger(mTablesContainer, executor);
        tableMerger.computeAndMergeMTables();
    }
}
