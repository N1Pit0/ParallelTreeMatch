package com.bachelor;

import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

public class TestExamples {
    static TreeNode[] initializeSubjectTArray(int length) {
        TreeNode[] treeNodes = new TreeNode[length];

        int outdegree = 2;
        SubNode[] tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode0 = new TreeNode.Builder("f")
                .father(-1)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 2;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode1 = new TreeNode.Builder("f")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 2;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("f")
                .father(0)
                .isVariable(false)
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
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode4 = new TreeNode.Builder("b")
                .father(1)
                .isVariable(false)
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
                .isVariable(false)
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
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(1)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode7 = new TreeNode.Builder("a")
                .father(5)
                .isVariable(false)
                .outDegree(outdegree)
                .edgeLabel(0)
                .tour(tour)
                .build();

        outdegree = 0;
        tour = new SubNode[outdegree + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode8 = new TreeNode.Builder("a")
                .father(5)
                .isVariable(false)
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

        return treeNodes;
    }

    static TreeNode[] initializePatternTArray(int length) {
        TreeNode[] treeNodes = new TreeNode[length];

        int outdegree0 = 2;
        SubNode[] tour = new SubNode[outdegree0 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode0 = new TreeNode.Builder("f")
                .father(-1)
                .isVariable(false)
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
                .isVariable(false)
                .outDegree(outdegree1)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree2 = 0;
        tour = new SubNode[outdegree2 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("Y")
                .father(0)
                .isVariable(true)
                .outDegree(outdegree2)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree3 = 0;
        tour = new SubNode[outdegree3 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode3 = new TreeNode.Builder("a")
                .father(1)
                .isVariable(false)
                .outDegree(outdegree3)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree4 = 0;
        tour = new SubNode[outdegree4 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode4 = new TreeNode.Builder("X")
                .father(1)
                .isVariable(true)
                .outDegree(outdegree4)
                .edgeLabel(1)
                .tour(tour)
                .build();

        treeNodes[0] = treeNode0;
        treeNodes[1] = treeNode1;
        treeNodes[2] = treeNode2;
        treeNodes[3] = treeNode3;
        treeNodes[4] = treeNode4;

        return treeNodes;
    }
}
