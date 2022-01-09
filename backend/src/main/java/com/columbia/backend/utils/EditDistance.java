package com.columbia.backend.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EditDistance {
    final boolean allowTranspose;

    protected double[][] score = null;

    public EditDistance() {
        allowTranspose = true;
    }

    public EditDistance(boolean allowTranspose) {
        this.allowTranspose = allowTranspose;
    }

    protected void clear(int sourceLength, int targetLength) {
        if (score == null || score.length < sourceLength + 1 || score[0].length < targetLength + 1) {
            score = new double[sourceLength + 1][targetLength + 1];
        }
        for (double[] aScore : score) {
            Arrays.fill(aScore, worst());
        }
    }

    // CONSTRAINT SEMIRING START

    protected double best() {
        return 0.0;
    }

    protected double worst() {
        return Double.POSITIVE_INFINITY;
    }

    protected double unit() {
        return 1.0;
    }

    protected double better(double x, double y) {
        if (x < y) {
            return x;
        }
        return y;
    }

    protected double combine(double x, double y) {
        return x + y;
    }

    // CONSTRAINT SEMIRING END

    // COST FUNCTION BEGIN

    protected double insertCost(Object o) {
        return unit();
    }

    protected double deleteCost(Object o) {
        return unit();
    }

    protected double substituteCost(Object source, Object target) {
        if (source.equals(target)) {
            return best();
        }
        return unit();
    }

    double transposeCost(Object s1, Object s2, Object t1, Object t2) {
        if (s1.equals(t2) && s2.equals(t1)) {
            if (allowTranspose) {
                return unit();
            } else {
                return 2*unit();
            }
        }
        return worst();
    }

    // COST FUNCTION END

    double score(Object[] source, int sPos, Object[] target, int tPos) {
        for (int i = 0; i <= sPos; ++i) {
            for (int j = 0; j <= tPos; ++j) {
                double bscore = score[i][j];
                if (bscore != worst())
                    continue;
                if (i == 0 && j == 0) {
                    bscore = best();
                } else {
                    if (i > 0) {
                        bscore = better(bscore,
                                (combine(score[i - 1][j],
                                        deleteCost(source[i - 1]))));
                    }
                    if (j > 0) {
                        bscore = better(bscore,
                                (combine(score[i][j - 1],
                                        insertCost(target[j - 1]))));
                    }
                    if (i > 0 && j > 0) {
                        bscore = better(bscore,
                                (combine(score[i - 1][j - 1],
                                        substituteCost(source[i - 1],
                                                target[j - 1]))));
                    }
                    if (i > 1 && j > 1) {
                        bscore = better(bscore,
                                (combine(score[i - 2][j - 2],
                                        transposeCost(source[i - 2], source[i - 1],
                                                target[j - 2], target[j - 1]))));
                    }
                }
                score[i][j] = bscore;
            }
        }
        return score[sPos][tPos];
    }

    public double score(Object[] source, Object[] target) {
        clear(source.length, target.length);
        return score(source, source.length, target, target.length);
    }

    public double score(String sourceStr, String targetStr) {
        if(sourceStr.equals(targetStr))
            return 0;
        Object[] source = Utils.asCharacterArray(sourceStr);
        Object[] target = Utils.asCharacterArray(targetStr);
        clear(source.length, target.length);
        return score(source, source.length, target, target.length);
    }
}
