package com.bachelor.data;

import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import com.bachelor.preprocess.Variable;
import jdk.jshell.spi.ExecutionControl;

import java.util.LinkedList;
import java.util.List;

import static com.bachelor.utils.TreeProcessingUtils.createVariable;


//NOT WORKING. I forgot how to populate data manually
public class SampleDataLoader implements DataLoader {

    //String subjectString = "f(f(a,b), f(f(a,a),a))";
// String patternString = "f(f(a,A), B)";
    @Override
    public InputArray loadSubjectTree() {
        TreeNode[] treeNodes = new TreeNode[9];

        int outdegree = 2;
        SubNode[] tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode0 = new TreeNode.Builder("f")
                .father(-1)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode1 = new TreeNode.Builder("f")
                .father(0)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("f")
                .father(0)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode3 = new TreeNode.Builder("a")
                .father(1)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode4 = new TreeNode.Builder("b")
                .father(1)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        outdegree = 2;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode5 = new TreeNode.Builder("f")
                .father(2)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode6 = new TreeNode.Builder("a")
                .father(2)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode7 = new TreeNode.Builder("a")
                .father(5)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode8 = new TreeNode.Builder("a")
                .father(5)
                .variable(null)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        treeNodes[0] = treeNode0;
        treeNodes[1] = treeNode1;
        treeNodes[2] = treeNode2;
        treeNodes[3] = treeNode3;
        treeNodes[4] = treeNode4;
        treeNodes[5] = treeNode5;
        treeNodes[6] = treeNode6;
        treeNodes[7] = treeNode7;
        treeNodes[8] = treeNode8;

        return new InputArray(treeNodes, new LinkedList<>());
    }

    @Override
    public InputArray loadPatternTree() {
        try {
            TreeNode[] treeNodes = new TreeNode[5];
            List<Variable> variableList = new LinkedList<>();

            int outdegree0 = 2;
            SubNode[] tour = new SubNode[outdegree0 + 1];
            for (int i = 0; i < tour.length; i++) {
                tour[i] = new SubNode();
            }
            TreeNode treeNode0 = new TreeNode.Builder("f")
                    .father(-1)
                    .variable(null)
                    .outDegree(outdegree0)
                    .edgeLabel(0)
                    .tour(tour)
                    .build();

            int outdegree1 = 2;
            tour = new SubNode[outdegree1 + 1];
            for (int i = 0; i < tour.length; i++) {
                tour[i] = new SubNode();
            }
            TreeNode treeNode1 = new TreeNode.Builder("f")
                    .father(0)
                    .variable(null)
                    .outDegree(outdegree1)
                    .edgeLabel(0)
                    .tour(tour)
                    .build();

            int outdegree2 = 0;
            tour = new SubNode[outdegree2 + 1];
            for (int i = 0; i < tour.length; i++) {
                tour[i] = new SubNode();
            }

            Variable variableOne = createVariable("Y");
            TreeNode treeNode2 = new TreeNode.Builder("Y")
                    .father(0)
                    .variable(variableOne)
                    .outDegree(outdegree2)
                    .edgeLabel(1)
                    .tour(tour)
                    .build();
            variableList.add(variableOne);

            int outdegree3 = 0;
            tour = new SubNode[outdegree3 + 1];
            for (int i = 0; i < tour.length; i++) {
                tour[i] = new SubNode();
            }
            TreeNode treeNode3 = new TreeNode.Builder("a")
                    .father(1)
                    .variable(null)
                    .outDegree(outdegree3)
                    .edgeLabel(0)
                    .tour(tour)
                    .build();

            int outdegree4 = 0;
            tour = new SubNode[outdegree4 + 1];
            for (int i = 0; i < tour.length; i++) {
                tour[i] = new SubNode();
            }

            Variable variableTwo = createVariable("X");
            TreeNode treeNode4 = new TreeNode.Builder("X")
                    .father(1)
                    .variable(variableTwo)
                    .outDegree(outdegree4)
                    .edgeLabel(1)
                    .tour(tour)
                    .build();

            treeNodes[0] = treeNode0;
            treeNodes[1] = treeNode1;
            treeNodes[2] = treeNode2;
            treeNodes[3] = treeNode3;
            treeNodes[4] = treeNode4;

            return new InputArray(treeNodes, variableList);
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputArray acceptInput(String prompt) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("method not available for term variable");
    }
}
