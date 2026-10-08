package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

@FunctionalInterface
public interface AlgorithmSupplier<T> {

    T get(MTablesContainer mTablesContainer,
          EulerChain subject,
          EulerChain pattern,
          int tableIdx,
          int nextTableIdx,
          int subjPos);
}
