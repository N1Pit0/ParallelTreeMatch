package com.bachelor.parser;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InputParserImplTest {

    private final InputParser parser = new InputParserImpl();

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "a                            | a",
            "A                            | A",
            "' f ( a , B ) '              | f(a,B)",
            "f(f(a,b), f(f(a,a),a))       | f(f(a,b),f(f(a,a),a))",
            "f(A,f(a,B,C),D)              | f(A,f(a,B,C),D)"
    })
    void parsesValidInput(String input, String expected) throws InvalidInputException {
        assertEquals(expected, parser.parse(input).toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", "   ", "f()", "f( )", "f(a,,b)", "f(,a)", "f(a,)",
            "A(b)", "A (b)", "f(a", "f(a))", ")(", "f(a b)", "ab",
            "f(1)", "f(a)#", "a,b", "f(a)g(b)"
    })
    void rejectsInvalidInput(String input) {
        assertThrows(InvalidInputException.class, () -> parser.parse(input));
    }

    @Test
    void classifiesSymbols() throws InvalidInputException {
        Term term = parser.parse("f(a,B)");
        assertTrue(term.isFunction());
        assertTrue(term.arguments().get(0).isConstant());
        assertTrue(term.arguments().get(1).isVariable());
    }
}