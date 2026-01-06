package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InitializeEulerChain implements Initializer {
    private final int index;
    private final EulerChain eulerChain;
    private final TreeNode[] T;
    private final static Logger logger = LoggerFactory.getLogger(InitializeEulerChain.class);

    public InitializeEulerChain(int index, EulerChain eulerChain){
        this.index = index;
        this.eulerChain = eulerChain;
        this.T = eulerChain.getT();
    }

    @Override
    public void initialize() {
        logger.debug("Inside run of InitializeEulerChain");
        int iFather = T[index].getFather();
        logger.debug("got the father {}", iFather);
        if (iFather < 0) {
            logger.debug("Returned With father <0");
            return;
        }

        int edgeLabel = T[index].getEdge_label();
        eulerChain.setAtIndex(T[iFather].tour[edgeLabel + 1].getCost(), T[iFather].tour[edgeLabel + 1]);
        eulerChain.setAtIndex(T[index].tour[0].getCost(), T[index].tour[0]);
        logger.debug("Completed the task of InitializeEulerChain");
    }
}
