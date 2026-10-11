package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

import java.util.Arrays;
import java.util.function.Supplier;

public enum AlgorithmType {

    TERM(TermVariableMatch.Builder::new),
    SEQUENTIAL(SequentialVariableMatch.Builder::new);

    private final Supplier<VariableMatch.Builder<?>> builderSupplier;

    AlgorithmType(Supplier<VariableMatch.Builder<?>> builderSupplier) {
        this.builderSupplier = builderSupplier;
    }

    public VariableMatch create(MTablesContainer mTablesContainer,
                                EulerChain subject,
                                EulerChain pattern,
                                int tableIdx,
                                int nextTableIdx,
                                int subjPos) {
        return builderSupplier.get()
                .mTablesContainer(mTablesContainer)
                .subject(subject)
                .pattern(pattern)
                .tableIdx(tableIdx)
                .nextTableIndex(nextTableIdx)
                .subjPos(subjPos)
                .build();
    }

    public static AlgorithmType from(String value) {
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown com.bachelor.application.matchingalgorithm.type '"
                    + value + "'. Valid values: " + Arrays.toString(values()));
        }
    }
}