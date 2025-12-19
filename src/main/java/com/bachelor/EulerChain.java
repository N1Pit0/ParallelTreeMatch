package com.bachelor;

class EulerChain {

    volatile SubNode[] chain;
    InputArray inputArray;

    public EulerChain(InputArray inputArray) {
        this.inputArray = inputArray;
        this.chain = new SubNode[inputArray.T.length];
    }

}
