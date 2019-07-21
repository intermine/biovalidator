package org.intermine.biovalidator.validator.csv;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * Calculate approximate text similarity between two string
 * @author deepak kumar
 */
public class SimilarityScore
{
    private enum CharacterType
    {
        LETTER, DIGIT, SPACE, SYMBOL
    }

    /**
     * <p>find and return similarity score between two string.</p>
     * <p>
     *    It uses edit-distance to calculate dissimilarity between both the string
     *    and then it normalizes the edit-distance to calculate similarity score.
     *    score = 1 - (calculated_edit_distance/max_edit_distance)
     * </p>
     * <pre>
     * findSimilarityScore("abc", "xyz")             = 1.0
     * findSimilarityScore("abc", "123")             = 0.0
     * findSimilarityScore("abc-p-123", "xyz-b-456") = 1.0
     * </pre>
     * @param left first string
     * @param right second string
     * @return similarity score between 0 and 1
     */
    public double findSimilarityScore(String left, String right) {
        if (left == null && right == null) {
            return 1.0;
        }
        if (left == null || right == null) {
            return 0.0;
        }
        if ("-".equals(left) || "-".equals(right)) {
            return 1.0;
        }
        double max = Math.max(left.length(), right.length());
        double distance = simpleEditDistance(left, right, left.length(), right.length());

        /*// decreasing the length difference between two string
        int lengthDiff = Math.abs(left.length() - right.length());
        distance -= lengthDiff / 2;*/
        return 1.0 - ((double) distance / max);
    }

    /**
     * <p>Currently using 'edit-distance' as a metrics to calculate dissimilarity
     * between given two string. This gives good approximation when both string
     * have similar length but, doesn't work when the length of the string is
     * completely different.</p>
     * <p>
     * It implementation uses dynamic-programming to implement edit-distance,
     * the implementation measures dissimilarity based on type of character(rather than actual char)
     * at each index in both given string</p>
     * <p>
     * Runtime complexity: O(m*rightSize), where m and rightSize is length of given string
     * Space complexity  : O(m*rightSize), where m and N is length of given string
     * </p>
     * @see <a href="https://en.wikipedia.org/wiki/Edit_distance">Edit distance</a>
     * @param left first string
     * @param right second string
     * @param leftSize length of first string
     * @param rightSize length of second string
     * @return edit-distance between two both string
     */
    private int simpleEditDistance(String left, String right, int leftSize, int rightSize) {
        int[][] dp = new int[leftSize + 1][rightSize + 1];

        for (int i = 0; i <= leftSize; i++) {
            for (int j = 0; j <= rightSize; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }

                else if ( j == 0) {
                    dp[i][j] = i;
                }

                else if (getCharacterType(left.charAt(i - 1))
                        == getCharacterType(right.charAt(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1];
                }

                else {
                    dp[i][j] = 1 + minimumOfThree(dp[i][j - 1],  // Insert
                            dp[i - 1][j],  // Remove
                            dp[i - 1][j - 1]); // Substitution
                }
            }
        }

        return dp[leftSize][rightSize];
    }

    /**
     * Identifies type of character(LETTER, DIGIT, SPACE, SYMBOL).
     * <pre>
     *     getCharacterType('a') = CharacterType.LETTER
     *     getCharacterType('z') = CharacterType.LETTER
     *     getCharacterType('0') = CharacterType.DIGIT
     *     getCharacterType('8') = CharacterType.DIGIT
     *     getCharacterType(' ') = CharacterType.SPACE
     *     getCharacterType('\n') = CharacterType.SPACE
     *     getCharacterType('\t') = CharacterType.SPACE
     *     getCharacterType('!') = CharacterType.SYMBOL
     *     getCharacterType('@') = CharacterType.SYMBOL
     * </pre>
     * @param c character
     * @return type of character
     */
    private CharacterType getCharacterType(char c) {
        if (Character.isLetter(c)) {
            return CharacterType.LETTER;
        }
        else if (Character.isLetter(c)) {
            return CharacterType.DIGIT;
        }
        else if (Character.isWhitespace(c)) {
            return CharacterType.SPACE;
        }
        else {
            return CharacterType.SYMBOL;
        }
    }

    /**
     * find minimum between tree numbers
     * @param x int
     * @param y int
     * @param z int
     * @return max of three integers
     */
    private int minimumOfThree(int x, int y, int z) {
        if (x <= y && x <= z) {
            return x;
        }
        if (y <= x && y <= z) {
            return y;
        }
        else {
            return z;
        }
    }

}
