package com.bachelor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bachelor.InputArray.T;

public class InitializeTourInfo implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(InitializeTourInfo.class);
    private final int index;

    public InitializeTourInfo(int index){
        this.index = index;
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
        T[iFather].tour[edgeLabel].setTourInfo(T[index].tour[0]);
        // + 1 might be problem here since the paper has 1-indexing not 0
        SubNode subNode = T[iFather].tour[T[index].getEdge_label() + 1]; // Change the name of the variable to something else
        T[index].tour[T[index].arity()].setTourInfo(subNode);
    }
}
