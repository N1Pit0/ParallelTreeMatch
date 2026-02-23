package com.bachelor.algorithm;

import com.bachelor.preprocess.SubNode;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelPrefixSum {

    public static void parallelPrefixSum(SubNode[] arr) {
        try(var pool = ForkJoinPool.commonPool()) {
            pool.invoke(new PrefixSumTask(arr, 0, arr.length));
        }
    }

    private static class PrefixSumTask extends RecursiveAction{
        private static final int THRESHOLD = 10;
        SubNode[] arr;
        int lo,hi;

        PrefixSumTask(SubNode[] arr, int lo, int hi){
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if(hi - lo <= THRESHOLD){
                for(int i = lo + 1; i < hi; i++){
                    arr[i].setCost(arr[i].getCost() + arr[i-1].getCost());
                }
            }else{
                int mid = (lo + hi) / 2;
                PrefixSumTask left = new PrefixSumTask(arr, lo, mid);
                PrefixSumTask right = new PrefixSumTask(arr, mid, hi);

                left.fork();
                right.compute();
                left.join();

                int add = arr[mid - 1].getCost();
                for(int i = mid; i < hi; i++){
                    arr[i].setCost(arr[i].getCost() + add);
                }
            }
        }
    }
}
