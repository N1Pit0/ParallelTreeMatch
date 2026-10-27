package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import com.bachelor.utils.ExecutorBarrierUtils;
import java.util.concurrent.ExecutorService;

/**
 * List ranking by pointer jumping (Wyllie's algorithm).
 *
 * Before: every SubNode has cost 1 (the last one 0) and next = its successor in the chain.
 * After:  every SubNode's cost is the number of entries that follow it in the chain,
 *         so its position is (chainSize - 1 - cost).
 *
 * Each round reads one pair of fields and writes the other (double buffering),
 * so only one barrier per round is needed.
 */
public final class InitializeCost {

    private final ExecutorService executor;
    private final SubNode[] entries;   // all SubNodes of the chain, in arbitrary order

    public static void createAndRunNewInstance(ExecutorService executor,
                                               EulerChain eulerChain) {
        new InitializeCost(executor, eulerChain).initializeCosts();
    }

    private InitializeCost(ExecutorService executor, EulerChain eulerChain) {
        this.executor = executor;
        this.entries = collectEntries(eulerChain.getT(), eulerChain.getChainSize());
    }

    /** Every SubNode belongs to exactly one T[i].tour, so no list traversal is needed. */
    private static SubNode[] collectEntries(TreeNode[] T, int chainSize) {
        SubNode[] result = new SubNode[chainSize];
        int count = 0;
        for (TreeNode node : T) {
            for (SubNode entry : node.tour) {
                result[count++] = entry;
            }
        }
        if (count != chainSize) {
            throw new IllegalStateException("Expected " + chainSize + " chain entries but found " + count);
        }
        return result;
    }

    private void initializeCosts() {
        int rounds = roundsNeeded(entries.length);

        // After an even number of rounds the results are back in cost/next.
        // An extra round after the last necessary one changes nothing, because every next is null by then.
        if (rounds % 2 == 1) {
            rounds++;
        }

        for (int round = 0; round < rounds; round++) {
            boolean readPrimary = (round % 2 == 0);
            ExecutorBarrierUtils.forEachIndex(executor, entries.length, i -> jump(entries[i], readPrimary));
        }
    }

    /** ceil(log2(size)): after this many rounds every pointer has reached the end of the list. */
    private static int roundsNeeded(int size) {
        return size <= 1 ? 0 : 32 - Integer.numberOfLeadingZeros(size - 1);
    }

    /** One pointer-jumping step for a single entry. Reads one field pair, writes the other. */
    private static void jump(SubNode current, boolean readPrimary) {
        if (readPrimary) {
            SubNode next = current.getNext();
            if (next != null) {
                current.setCostTmp(current.getCost() + next.getCost());
                current.setNextTmp(next.getNext());
            } else {
                current.setCostTmp(current.getCost());
                current.setNextTmp(null);
            }
        } else {
            SubNode next = current.getNextTmp();
            if (next != null) {
                current.setCost(current.getCostTmp() + next.getCostTmp());
                current.setNext(next.getNextTmp());
            } else {
                current.setCost(current.getCostTmp());
                current.setNext(null);
            }
        }
    }
}