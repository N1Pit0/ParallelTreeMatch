package com.bachelor.data;

import com.bachelor.preprocess.InputArray;

public interface DataLoader {
    InputArray loadSubjectTree();
    InputArray loadPatterTree();
}
