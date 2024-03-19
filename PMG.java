public class PMG {
    private int[][] luminosities; 
    private int maxLuminosity;

    /**
     * Constructor for the QuadtreePrefab class.
     *
     * @param T      The array from which the Quadtree will be constructed.
     * @param maxL   The maximum luminosity value.
     */
    public PMG(int[][] T, int maxL) {
        this.luminosities = T;
        this.maxLuminosity = maxL;
    }

    /**
     * Default constructor for the QuadtreePrefab class.
     */
    public PMG() {
        this.luminosities = null;
        this.maxLuminosity = -1;
    }


    /**
     * Gets the array from which the Quadtree will be constructed.
     *
     * @return The array from which the Quadtree will be constructed.
     */
    public int[][] getLuminosities() {
        return this.luminosities;
    }

    /**
     * Sets the array from which the Quadtree will be constructed.
     *
     * @param T The new array to be assigned.
     */
    public void setLuminosities(int[][] T) {
        this.luminosities = T;
    }

    /**
     * Gets the maximum luminosity value.
     *
     * @return The maximum luminosity value.
     */
    public int getMaxLuminosity() {
        return this.maxLuminosity;
    }

    /**
     * Sets the maximum luminosity value.
     *
     * @param maxL The new maximum luminosity value to be assigned.
     */
    public void setMaxLuminosity(int maxL) {
        this.maxLuminosity = maxL;
    }
}