package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.Step;
import com.bachelor.preprocess.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bachelor.preprocess.NodeType.*;

public class InitializeType implements Initializer {
    private final int index;
    private final TreeNode[] T;
    private final Logger logger = LoggerFactory.getLogger(InitializeType.class);

    public InitializeType(int index, InputArray inputArray) {
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {
        logger.info("Inside run of InitializeType");

        try {
            T[index].getLatches()[Step.INITIALIZE_TYPE.ordinal()].await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int iFather = T[index].getFather();
        logger.debug("got the father {}", iFather);

        if (iFather >= 0) {
//            logger.debug("Returned With father <0");
            int edgeLabel = T[index].getEdge_label();
            T[iFather].tour[edgeLabel].setType(DUMMY);
        }

        int currentOutDegree = T[index].arity();
        if (currentOutDegree == 0) {
            T[index].tour[0].setType(LEAF);
        } else {
            T[index].tour[0].setType(FIRST);
            T[index].tour[currentOutDegree].setType(LAST); // I removed + 1 from here inside tour array
        }

        if(iFather >= 0){
            T[iFather].getLatches()[Step.INITIALIZE_TYPE.ordinal()].countDown();
        }

        logger.debug("Completed the task of InitializeType");
    }
}
