package com.bachelor.preprocess;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.Term;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.bachelor.utils.TreeProcessingUtils.createVariable;

/**
 * Converts a parsed {@link Term} into the {@link InputArray} layout required by the algorithm:
 *   - nodes in breadth-first order
 *   - father    = index of the parent node (-1 for the root)
 *   - edgeLabel = argument position within the parent (0-based, 0 for the root)
 *   - variables listed in the order they appear in the input (left to right)
 */
public final class InputArrayFactory {

    private InputArrayFactory() {}

    public static InputArray create(Term root) throws InvalidInputException {
        // Variables are created in input order (pre-order), NOT in BFS order.
        // VariableMatch looks them up by index, so this order matters.
        List<Variable> variablesInOrder = new ArrayList<>();
        Map<Term, Variable> variableByTerm = new IdentityHashMap<>();
        collectVariables(root, variablesInOrder, variableByTerm);

        TreeNode[] nodes = toBreadthFirstArray(root, variableByTerm);
        return new InputArray(nodes, variablesInOrder);
    }

    /** Pre-order walk: creates one Variable per variable occurrence, left to right. */
    private static void collectVariables(Term term, List<Variable> inOrder, Map<Term, Variable> byTerm) throws InvalidInputException {
        if (term.isVariable()) {
            Variable variable = createVariable(String.valueOf(term.symbol()));
            inOrder.add(variable);
            byTerm.put(term, variable);
        }
        for (Term argument : term.arguments()) {
            collectVariables(argument, inOrder, byTerm);
        }
    }

    private static TreeNode[] toBreadthFirstArray(Term root, Map<Term, Variable> variableByTerm) {
        record Pending(Term term, int fatherIndex, int edgeLabel) {}

        List<TreeNode> result = new ArrayList<>();
        Queue<Pending> queue = new ArrayDeque<>();
        queue.add(new Pending(root, -1, 0));

        while (!queue.isEmpty()) {
            Pending current = queue.poll();
            int currentIndex = result.size();

            result.add(toTreeNode(current.term(), current.fatherIndex(), current.edgeLabel(),
                    variableByTerm.get(current.term())));

            List<Term> arguments = current.term().arguments();
            for (int i = 0; i < arguments.size(); i++) {
                queue.add(new Pending(arguments.get(i), currentIndex, i));
            }
        }
        return result.toArray(new TreeNode[0]);
    }

    private static TreeNode toTreeNode(Term term, int fatherIndex, int edgeLabel, Variable variable) {
        SubNode[] tour = new SubNode[term.arity() + 1];
        Arrays.setAll(tour, i -> new SubNode());

        return new TreeNode.Builder(String.valueOf(term.symbol()))
                .variable(variable)
                .father(fatherIndex)
                .edgeLabel(edgeLabel)
                .outDegree(term.arity())
                .tour(tour)
                .build();
    }
}