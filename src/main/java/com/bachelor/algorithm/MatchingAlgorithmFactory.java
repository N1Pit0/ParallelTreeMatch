package com.bachelor.algorithm;

import com.bachelor.config.Config;
import com.bachelor.preprocess.EulerChain;

public final class MatchingAlgorithmFactory {
    private static final MatchingAlgorithmType TYPE = MatchingAlgorithmType.from(Config.getMatchingAlgorithm());

    private MatchingAlgorithmFactory() {}

    public static MatchingAlgorithmType getType() {
        return TYPE;
    }

    public static VariableMatch create(MTablesContainer mTablesContainer,
                                       EulerChain subject,
                                       EulerChain pattern,
                                       int tableIdx,
                                       int nextTableIdx,
                                       int subjPos) {
        return TYPE.create(mTablesContainer, subject, pattern, tableIdx, nextTableIdx, subjPos);
    }
}
