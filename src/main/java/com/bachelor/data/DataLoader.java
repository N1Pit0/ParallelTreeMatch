package com.bachelor.data;

import com.bachelor.preprocess.InputArray;
import jdk.jshell.spi.ExecutionControl;

public interface DataLoader {
    InputArray loadSubjectTree();
    InputArray loadPatternTree();
    InputArray acceptInput(String prompt) throws ExecutionControl.NotImplementedException;
}
