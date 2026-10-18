package com.bachelor;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.*;

public interface InputParser {
    InputArray parseToArray(String input) throws InvalidInputException;
}
