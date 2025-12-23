package com.bachelor;

import java.util.concurrent.Phaser;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Coordinator {
    public static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Phaser phaser = new Phaser();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
    }

}
