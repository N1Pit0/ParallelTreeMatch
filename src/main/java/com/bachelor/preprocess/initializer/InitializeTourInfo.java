package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitializeTourInfo implements Initializer {
    private static final Logger logger = LoggerFactory.getLogger(InitializeTourInfo.class);
    private final int index;
    private final TreeNode[] T;

    public InitializeTourInfo(int index, InputArray inputArray){
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {
        logger.info("Inside run of InitializeTourInfo");
        int iFather = T[index].getFather();
        logger.debug("got the father {}", iFather);
        if (iFather <= 0) {
            logger.debug("Returned With father <=0");
            return;
        }

        int edgeLabel = T[index].getEdge_label();
        T[iFather].tour[edgeLabel].setTourInfo(T[index].tour[0]);
        // + 1 might be problem here since the paper has 1-indexing not 0
        SubNode subNode = T[iFather].tour[T[index].getEdge_label() + 1]; // Change the name of the variable to something else
        T[index].tour[T[index].arity()].setTourInfo(subNode);
        logger.debug("Completed the task");
    }
}
