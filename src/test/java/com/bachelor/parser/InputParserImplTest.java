package com.bachelor.parser;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.TreeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InputParserImplTest {

    private final InputParser parser = new InputParserImpl();

    @ParameterizedTest
    @ValueSource(strings = {
            "a", "A",
            "f(a)", "f(A)",
            "f(a,b)", "f( a , b )", " f (a) ",
            "f(f(a,b), f(f(a,a),a))",
            "f(f(a,A), B)",
            "f(a,f(a,b,c),f(a,f(a,b,c),e),f)",
            "f(A,f(a,B,C),D)"
    })
    void acceptsValidInput(String input) {
        assertDoesNotThrow(() -> parser.parseToArray(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", "   ",
            "f()", "f( )",          // empty argument list
            "f(a,,b)",              // consecutive commas
            "f(,a)", "f(a,)",       // leading / trailing comma
            "A(b)", "A (b)",        // variable with arguments
            "f(a", "f(a))", ")(",   // unbalanced parentheses
            "f(a b)", "ab",         // missing comma / multi-letter
            "f(1)", "f(a)#",        // invalid characters
            "a,b", "f(a)g(b)"       // more than one top-level term
    })
    void rejectsInvalidInput(String input) {
        assertThrows(InvalidInputException.class, () -> parser.parseToArray(input));
    }

    @Test
    void producesBreadthFirstStructure() throws InvalidInputException {
        InputArray result = parser.parseToArray("f(f(a,b), f(f(a,a),a))");
        TreeNode[] tree = result.getT(); // adjust accessor name

        String[] labels   = {"f", "f", "f", "a", "b", "f", "a", "a", "a"};
        int[] fathers    = {-1,   0,   0,   1,   1,   2,   2,   5,   5};
        int[] edges      = { 0,   0,   1,   0,   1,   0,   1,   0,   1};
        int[] outDegrees = { 2,   2,   2,   0,   0,   2,   0,   0,   0};

        assertEquals(labels.length, tree.length);
        for (int i = 0; i < tree.length; i++) {
            assertEquals(labels[i],      tree[i].getLabel(),      "label at " + i);
            assertEquals(fathers[i],    tree[i].getFather(), "father at " + i);
            assertEquals(edges[i],      tree[i].getEdgeLabel(), "edgeLabel at " + i);
            assertEquals(outDegrees[i], tree[i].arity(), "outDegree at " + i);
        }
    }
}