package com.bachelor.utils;

import com.bachelor.algorithm.MTablesContainer;
import com.bachelor.preprocess.TreeNode;

@SuppressWarnings("unused")
public class TreePrintUtils {
    /**
     * Prints tours for each TreeNode in the array. Safe to call with null arrays or null tours.
     */
    public static void printTreeTours(TreeNode[] treeNodes, String title) {
        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }
        if (treeNodes == null) return;
        for (int i = 0; i < treeNodes.length; i++) {
            TreeNode node = treeNodes[i];
            if (node == null || node.tour == null) continue;
            for (int j = 0; j < node.tour.length; j++) {
                System.out.println(" T[" + i + "].tour[" + j + "]" + node.tour[j]
                        + " SubNode@" + Integer.toHexString(System.identityHashCode(node.tour[j])));
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static void printMTablesState(String label, MTablesContainer container) {
        System.out.println("\n" + label + ":");
        System.out.println(container);
    }
}
