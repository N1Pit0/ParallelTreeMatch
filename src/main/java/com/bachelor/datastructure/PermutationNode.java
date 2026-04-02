package com.bachelor.datastructure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class PermutationNode implements Iterable<Permutation> {
    private int matchStartIndex;
    private List<Permutation> permutations;
    private PermutationNode next;

    public PermutationNode(int matchStartIndex) {
        this.matchStartIndex = matchStartIndex;
        this.permutations = new LinkedList<>();
        this.next = null;
    }

    // Getters and setters
    public int getMatchStartIndex() {
        return matchStartIndex;
    }

    public List<Permutation> getPermutations() {
        return permutations;
    }

    public PermutationNode getNext() {
        return next;
    }

    public void setNext(PermutationNode next) {
        this.next = next;
    }

    public void setPermutations(List<Permutation> permutations) {
        this.permutations = permutations;
    }

    // Add permutation to current node
    public void addPermutation(Permutation permutation) {
        this.permutations.add(permutation);
    }

    @Override
    public Iterator<Permutation> iterator() {
        return new PermutationIterator(this);
    }

    static class PermutationIterator implements Iterator<Permutation> {
        private PermutationNode currentNode;
        private Iterator<Permutation> currentListIterator;

        PermutationIterator(PermutationNode startNode) {
            this.currentNode = startNode;
            if (currentNode != null && !currentNode.getPermutations().isEmpty()) {
                this.currentListIterator = currentNode.getPermutations().iterator();
            } else {
                moveToNextNonEmptyNode();
            }
        }

        @Override
        public boolean hasNext() {
            return currentListIterator != null && currentListIterator.hasNext();
        }

        @Override
        public Permutation next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Permutation result = currentListIterator.next();

            // If current list is exhausted, move to next node
            if (!currentListIterator.hasNext()) {
                moveToNextNonEmptyNode();
            }

            return result;
        }

        private void moveToNextNonEmptyNode() {
            currentNode = (currentNode != null) ? currentNode.getNext() : null;
            currentListIterator = null;

            // Find next node with non-empty permutations list
            while (currentNode != null) {
                if (!currentNode.getPermutations().isEmpty()) {
                    currentListIterator = currentNode.getPermutations().iterator();
                    break;
                }
                currentNode = currentNode.getNext();
            }
        }
    }
}
