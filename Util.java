/**
 * Utility class providing methods for QuadTree operations and numerical calculations.
 * 
 */
public class Util {

    /**
     * Constant representing an undefined log luminosity value.
     * 
     */
    public static final double UNDEFINED_LOG_LUMINOSITY = Double.MIN_VALUE;

    /**
     * Calculates and returns the average log luminosity for a QuadTreeNode.
     * If the node is a Twig root, it computes the average log luminosity of its four children.
     * Uses a logarithmic transformation and exponential scaling for the calculation.
     *
     * @param node The QuadTreeNode for which to calculate the average log luminosity.
     * @return The average log luminosity value.
     * 
     */
    public static double calculateAvgLogLuminosity(QuadTreeNode node) {
        if (node.isTwigRoot()) {
            double res = 0;

            for (int i = 0; i < 4; i++) {
                res += Math.log(0.1 + node.getChildValue(i));
            }

            res = Math.exp(0.25 * res);
            return res;
        }

        return UNDEFINED_LOG_LUMINOSITY;
    }

    /**
     * Calculates and returns the maximum epsilon value for a QuadTreeNode.
     * Compares the average log luminosity of the node with its leaf children
     * and determines the maximum absolute difference (epsilon).
     *
     * @param node The QuadTreeNode for which to calculate the epsilon.
     * @return The maximum epsilon value.
     * 
     */
    public static double calculateEpsilon(QuadTreeNode node) {
        double maxEpsilon = Double.MIN_VALUE;
        double avgLogLuminosity = calculateAvgLogLuminosity(node);

        for (int i = 0; i < 4; i++) {
            QuadTreeNode child = node.getChild(i);

            if (child != null && child.isLeaf()) {
                double epsilon = Math.abs(avgLogLuminosity - child.getValue());
                maxEpsilon = Math.max(maxEpsilon, epsilon);
            }
        }

        return maxEpsilon;
    }

    /**
     * Checks if a given number is a power of two.
     *
     * @param number The number to check.
     * @return true if the number is a power of two, otherwise false.
     * 
     */
    public static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            return false;
        }

        return (number & (number - 1)) == 0;
    }

    /**
     * Finds and returns the maximum value from an array of integers.
     *
     * @param arr The array of integers.
     * @return The maximum value in the array.
     * 
     */
    public static int getMax(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < 4; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        return max;
    }
}
