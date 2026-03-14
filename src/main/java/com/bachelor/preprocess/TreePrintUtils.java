package com.bachelor.preprocess;

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
}
