package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

import java.util.Arrays;

public enum AlgorithmType {

    TERM((container, subject, pattern, tableIdx, nextTableIdx, subjPos) ->
            new TermVariableMatch.Builder()
                    .mTablesContainer(container)
                    .subject(subject)
                    .pattern(pattern)
                    .tableIdx(tableIdx)
                    .nextTableIndex(nextTableIdx)
                    .subjPos(subjPos)
                    .build()),

    SEQUENTIAL((container, subject, pattern, tableIdx, nextTableIdx, subjPos) ->
            new SequentialVariableMatch.Builder()
                    .mTablesContainer(container)
                    .subject(subject)
                    .pattern(pattern)
                    .tableIdx(tableIdx)
                    .nextTableIndex(nextTableIdx)
                    .subjPos(subjPos)
                    .build());

    private final AlgorithmSupplier<VariableMatch> algorithm;

    AlgorithmType(AlgorithmSupplier<VariableMatch> algorithm) {
        this.algorithm = algorithm;
    }

    public VariableMatch create(MTablesContainer mTablesContainer,
                                EulerChain subject,
                                EulerChain pattern,
                                int tableIdx,
                                int nextTableIdx,
                                int subjPos) {
        return algorithm.get(mTablesContainer, subject, pattern, tableIdx, nextTableIdx, subjPos);
    }

    public static AlgorithmType from(String value){
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown com.bachelor.application.matchingalgorithm.type '" + value
                    + "'. Valid values: " + Arrays.toString(values()));
        }
    }
}
