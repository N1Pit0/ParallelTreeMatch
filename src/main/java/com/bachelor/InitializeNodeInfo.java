package com.bachelor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitializeNodeInfo implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(InitializeNodeInfo.class);
    private final int index;
    private final TreeNode[] T;

    public InitializeNodeInfo(int index, InputArray inputArray){
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void run() {
        logger.info("Inside run");
        int iFather = T[index].getFather();
        logger.debug("got the father {}", iFather);
        if (iFather <= 0) {
            logger.debug("Returned With father <=0");
            return;
        }

        int edgeLabel = T[index].getEdge_label();
        T[iFather].tour[edgeLabel].setNodeInfo(iFather);
    }
}
