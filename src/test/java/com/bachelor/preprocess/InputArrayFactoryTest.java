package com.bachelor.preprocess;

import com.bachelor.parser.InputParserImpl;
import com.bachelor.parser.Term;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputArrayFactoryTest {

    private static Term parse(String s) throws Exception {
        return new InputParserImpl().parse(s);
    }

    @Test
    void producesBreadthFirstStructure() throws Exception {
        TreeNode[] tree = InputArrayFactory.create(parse("f(f(a,b), f(f(a,a),a))")).getT();

        String[] names   = {"f", "f", "f", "a", "b", "f", "a", "a", "a"};
        int[] fathers    = {-1,   0,   0,   1,   1,   2,   2,   5,   5};
        int[] edges      = { 0,   0,   1,   0,   1,   0,   1,   0,   1};
        int[] outDegrees = { 2,   2,   2,   0,   0,   2,   0,   0,   0};

        assertEquals(names.length, tree.length);
        for (int i = 0; i < tree.length; i++) {
            assertEquals(names[i],      tree[i].getLabel(),      "label at " + i);
            assertEquals(fathers[i],    tree[i].getFather(),    "father at " + i);
            assertEquals(edges[i],      tree[i].getEdgeLabel(), "edgeLabel at " + i);
            assertEquals(outDegrees[i], tree[i].arity(), "outDegree at " + i);
        }
    }

    @Test
    void variablesAreInInputOrderNotBfsOrder() throws Exception {
        // BFS visits B before A, but the variable list must be [A, B]
        InputArray array = InputArrayFactory.create(parse("f(f(A), B)"));

        assertEquals("A", array.getVariableWithIndex(0).getName());
        assertEquals("B", array.getVariableWithIndex(1).getName());
    }
}