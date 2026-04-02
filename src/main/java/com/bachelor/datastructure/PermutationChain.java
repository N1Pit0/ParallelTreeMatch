package com.bachelor.datastructure;

import java.util.Iterator;

public class PermutationChain implements Iterable<Permutation> {
    private PermutationNode head;
    private PermutationNode tail;

    public PermutationChain() {
        this.head = null;
        this.tail = null;
    }

    // O(1) append operation
    public void appendNode(PermutationNode node) {
        if (head == null) {
            head = tail = node;
        } else {
            tail.setNext(node);
            tail = node;
        }
    }

    // O(1) concatenate another chain
    public void concatenate(PermutationChain other) {
        if (other.head == null) return;

        if (this.head == null) {
            this.head = other.head;
        } else {
            this.tail.setNext(other.head);
        }
        this.tail = other.tail;
    }

    @Override
    public Iterator<Permutation> iterator() {
        return new PermutationNode.PermutationIterator(head);
    }

    // Utility methods
    public boolean isEmpty() {
        return head == null;
    }

    public PermutationNode getHead() {
        return head;
    }
}
