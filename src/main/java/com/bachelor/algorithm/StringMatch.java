package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

import java.util.Map;

public interface StringMatch <T>{
    Map<Integer, T> matchString(EulerChain subject, EulerChain pattern, int patStart, int patEnd);
}
