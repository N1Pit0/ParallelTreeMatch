package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bachelor.preprocess.NodeType.LEAF;

public class InitializeSubTree implements Initializer {
    private final int index;
    private final TreeNode[] T;
    private final Logger logger = LoggerFactory.getLogger(InitializeSubTree.class);

    public InitializeSubTree(int index, InputArray inputArray){
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {
        logger.debug("Inside run of InitializeSubTree");
        int iFather = T[index].getFather();
        logger.debug("got the father {}", iFather);

        if (iFather < 0) {
            logger.debug("Returned With father <0");
            return;
        }

        int edgeLabel = T[index].getEdge_label();
        int subTree = T[iFather].tour[T[iFather].arity()].getCost();
        T[iFather].tour[edgeLabel].setSubtree(subTree);
        T[iFather].tour[T[iFather].arity()].setSubtree(subTree);

        SubNode firstSubNode = T[index].tour[0];
        if(firstSubNode.getType().equals(LEAF)){
            firstSubNode.setSubtree(firstSubNode.getCost());
        }
        logger.debug("Completed the task of InitializeSubTree");
    }
}
