package com.bachelor.data;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import jdk.jshell.spi.ExecutionControl;

public interface DataLoader {
    InputArray loadSubjectTree() throws InvalidInputException;
    InputArray loadPatternTree() throws InvalidInputException;
    InputArray acceptInput(String prompt) throws ExecutionControl.NotImplementedException;
}
