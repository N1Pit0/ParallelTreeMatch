package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class InitializeCost {
    private final ExecutorService executor;
    private final EulerChain eulerChain;
    private SubNode head;
    private CountDownLatch latch;

    public InitializeCost( ExecutorService executor, EulerChain eulerChain){
        this.executor = executor;
        this.eulerChain = eulerChain;
        this.head = eulerChain.getT()[0].tour[0];
        this.latch = new CountDownLatch(eulerChain.getChainSize());
    }

    private void initialize(){
        for(var current : head){
            executor.submit(() -> {
                try{
                    if (current.getTourInfo() != null) {
                        current.setCost(1);
                    } else {
                        current.setCost(0);
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void doWork(){
        initialize();
        final int eulerChainSize = eulerChain.getChainSize();
        for (int i = 0; i < (int) Math.ceil(Math.log(eulerChainSize)); i ++){
            for (var current : head){
                executor.submit(() -> {
                    if (current.getNext() != null){
                        current.setCost((current.getCost() + current.getNext().getCost()));
                        current.setNext(current.getNext().getNext());
                    }
                });
            }
        }
    }
}
