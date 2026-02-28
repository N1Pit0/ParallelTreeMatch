package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

import java.util.Objects;

/**
 * An implementation of the Knuth-Morris-Pratt algorithm
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 * @author NikoloziMatsaberidze
 */

public class Kmp {

    // Given a pattern and a text kmp finds all the places that the pattern
    // is found in the text (even overlapping pattern matches)
    public static int[][] kmp(EulerChain subject, EulerChain pattern, int patStart, int patEnd) {
        int subjectChainSize = subject.getChainSize();
        SubNode[] subjectChain = subject.getChain();
        SubNode[] patternChain = pattern.getChain();
        TreeNode[] patternT = pattern.getT();
        TreeNode[] subjectT = subject.getT();

        int[][] matches = new int[subjectChainSize][2];
        for (int i = 0; i < subjectChainSize; i++) {
            matches[i][0] = -1;
            matches[i][1] = -1;
        }

        // Validate slice bounds
        if (patEnd < patStart || patStart < 0 || patEnd >= pattern.getChainSize()) {
            return matches;
        }

        // Pattern slice length (inclusive range)
        int patternLength = patEnd - patStart + 1;
        int i = 0;
        int j = 0;  // j tracks position within the pattern slice, starting from 0

        // Build failure function for the pattern slice
        int[] failureFunction = kmpFailureHelper(pattern, patStart, patEnd);

        while (i < subjectChainSize) {
            if (j >= 0 && Objects.equals(patternT[patternChain[patStart + j].getNodeInfo()].getLabel(), subjectT[subjectChain[i].getNodeInfo()].getLabel())) {
                j++;
                i++;
            }

            if (j == patternLength) {
                // Match found: record start and end indices
                int matchStart = i - patternLength;
                int matchEnd = i - 1;
                if (matchStart >= 0 && matchStart < subjectChainSize) {
                    matches[matchStart][0] = matchStart;
                    matches[matchStart][1] = matchEnd;
                }
                // Continue searching for overlapping matches
                j = failureFunction[j - 1];
            } else if (i < subjectChainSize && !Objects.equals(patternT[patternChain[patStart + j].getNodeInfo()].getLabel(), subjectT[subjectChain[i].getNodeInfo()].getLabel())) {
                if (j > 0) {
                    j = failureFunction[j - 1];
                } else {
                    i++;
                }
            }
        }

        return matches;
    }

    // For each index i compute the longest match between the proper
    // prefix starting at 0 and the proper suffix starting at i
    // Builds the failure function for a slice of the pattern from patStart to patEnd (inclusive)
    private static int[] kmpFailureHelper(EulerChain pattern, int patStart, int patEnd) {
        int patternLength = patEnd - patStart + 1;
        int[] failureFunction = new int[patternLength];
        SubNode[] patternChain = pattern.getChain();
        TreeNode[] patternT = pattern.getT();

        for (int i = 1, len = 0; i < patternLength; ) {
            if (Objects.equals(patternT[patternChain[patStart + i].getNodeInfo()].getLabel(), patternT[patternChain[patStart + len].getNodeInfo()].getLabel())) {
                failureFunction[i++] = ++len;
            } else {
                if (len > 0) {
                    len = failureFunction[len - 1];
                } else {
                    i++;
                }
            }
        }
        return failureFunction;
    }

}
