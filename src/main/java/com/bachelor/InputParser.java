package com.bachelor;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.Term;

public interface InputParser {
    Term parse(String input) throws InvalidInputException;
}
