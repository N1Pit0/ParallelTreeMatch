package com.bachelor.data.json;

import com.bachelor.preprocess.InputArray;

/** A valid entry from the input file. entryNumber is 1-based. */
public record InputPair(int entryNumber,
                        String subjectText,
                        String patternText,
                        InputArray subject,
                        InputArray pattern) {}