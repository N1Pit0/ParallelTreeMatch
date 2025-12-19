package com.bachelor;

class TreeNode{

    //contains either a function symbol or a variable associated with the
    //node
    String label;

    //contains a pointer to the parent of the node.
    int parent;

    //  contains an integer specifying the node’s ordering relative to
//  its sibling, i.e., which argument of its parent the current node is.
    int edge_label;

    //  contains the outdegree (the number of outgoing edges) of the
//  node.
    int outDegree;

    //  an array containing n + 1 elements, where n is the outdegree of the
//  current node in the tree.
    SubNode[] tour;

    TreeNode(int outDegree){
        this.outDegree = outDegree;
        tour = new SubNode[outDegree + 1];
    }

    int arity(){
        return outDegree;
    }

}
