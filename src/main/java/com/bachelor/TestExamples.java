package com.bachelor;

import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

public class TestExamples {
    static TreeNode[] initializeSubjectTArray() {
        TreeNode[] treeNodes = new TreeNode[9];

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

    static TreeNode[] initializePatternTArray() {
        TreeNode[] treeNodes = new TreeNode[5];

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

    static TreeNode[] initializeSubjectTArrayOne() {
        TreeNode[] treeNodes = new TreeNode[14];

        int outdegree0 = 4;
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

        int outdegree1 = 0;
        tour = new SubNode[outdegree1 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode1 = new TreeNode.Builder("a")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree1)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree2 = 3;
        tour = new SubNode[outdegree2 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("f")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree2)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree3 = 3;
        tour = new SubNode[outdegree3 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode3 = new TreeNode.Builder("f")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree3)
                .edgeLabel(2)
                .tour(tour)
                .build();

        int outdegree4 = 0;
        tour = new SubNode[outdegree4 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode4 = new TreeNode.Builder("f")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree4)
                .edgeLabel(3)
                .tour(tour)
                .build();

        int outdegree5 = 0;
        tour = new SubNode[outdegree5 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode5 = new TreeNode.Builder("a")
                .father(2)
                .isVariable(false)
                .outDegree(outdegree5)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree6 = 0;
        tour = new SubNode[outdegree6 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode6 = new TreeNode.Builder("b")
                .father(2)
                .isVariable(false)
                .outDegree(outdegree6)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree7 = 0;
        tour = new SubNode[outdegree7 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode7 = new TreeNode.Builder("c")
                .father(2)
                .isVariable(false)
                .outDegree(outdegree7)
                .edgeLabel(2)
                .tour(tour)
                .build();

        int outdegree8 = 0;
        tour = new SubNode[outdegree8 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode8 = new TreeNode.Builder("a")
                .father(3)
                .isVariable(false)
                .outDegree(outdegree8)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree9 = 3;
        tour = new SubNode[outdegree9 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode9 = new TreeNode.Builder("f")
                .father(3)
                .isVariable(false)
                .outDegree(outdegree9)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree10 = 0;
        tour = new SubNode[outdegree10 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode10 = new TreeNode.Builder("e")
                .father(3)
                .isVariable(false)
                .outDegree(outdegree10)
                .edgeLabel(2)
                .tour(tour)
                .build();

        int outdegree11 = 0;
        tour = new SubNode[outdegree11 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode11 = new TreeNode.Builder("a")
                .father(9)
                .isVariable(false)
                .outDegree(outdegree11)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree12 = 0;
        tour = new SubNode[outdegree12 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode12 = new TreeNode.Builder("b")
                .father(9)
                .isVariable(false)
                .outDegree(outdegree12)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree13 = 0;
        tour = new SubNode[outdegree13 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode13 = new TreeNode.Builder("d")
                .father(9)
                .isVariable(false)
                .outDegree(outdegree13)
                .edgeLabel(2)
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
        treeNodes[9] = treeNode9;
        treeNodes[10] = treeNode10;
        treeNodes[11] = treeNode11;
        treeNodes[12] = treeNode12;
        treeNodes[13] = treeNode13;

        return treeNodes;
    }

    static TreeNode[] initializePatternTArrayOne() {
        TreeNode[] treeNodes = new TreeNode[7];

        int outdegree0 = 3;
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

        int outdegree1 = 0;
        tour = new SubNode[outdegree1 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode1 = new TreeNode.Builder("X")
                .father(0)
                .isVariable(true)
                .outDegree(outdegree1)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree2 = 3;
        tour = new SubNode[outdegree2 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode2 = new TreeNode.Builder("f")
                .father(0)
                .isVariable(false)
                .outDegree(outdegree2)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree3 = 0;
        tour = new SubNode[outdegree3 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode3 = new TreeNode.Builder("Z")
                .father(0)
                .isVariable(true)
                .outDegree(outdegree3)
                .edgeLabel(2)
                .tour(tour)
                .build();

        int outdegree4 = 0;
        tour = new SubNode[outdegree4 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode4 = new TreeNode.Builder("a")
                .father(2)
                .isVariable(false)
                .outDegree(outdegree4)
                .edgeLabel(0)
                .tour(tour)
                .build();

        int outdegree5 = 0;
        tour = new SubNode[outdegree5 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode5 = new TreeNode.Builder("X")
                .father(2)
                .isVariable(true)
                .outDegree(outdegree5)
                .edgeLabel(1)
                .tour(tour)
                .build();

        int outdegree6 = 0;
        tour = new SubNode[outdegree6 + 1];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = new SubNode();
        }
        TreeNode treeNode6 = new TreeNode.Builder("Y")
                .father(2)
                .isVariable(true)
                .outDegree(outdegree6)
                .edgeLabel(2)
                .tour(tour)
                .build();

        treeNodes[0] = treeNode0;
        treeNodes[1] = treeNode1;
        treeNodes[2] = treeNode2;
        treeNodes[3] = treeNode3;
        treeNodes[4] = treeNode4;
        treeNodes[5] = treeNode5;
        treeNodes[6] = treeNode6;

        return treeNodes;
    }
}
