/**
 * Class that represents a quadtree of a PMG file
 * 
 */
public class QuadTree {

    /**
     * The twigs avl, it's gonna stay empty except when doing the dynamic
     * compression
     * It's gonna be used by the dynamic compression to store the twigs with their
     * epsilon
     * to be able to determine in a efficient way with a logarithmic complexity
     * which twig
     * has the less luminosity difference to give it priority for future compressions
     * 
     */
    private TwigAVLTree twigs;

    /**
     * The root attribute of this quadtree
     * 
     */
    private QuadTreeNode root;

    /**
     * The length of the original image
     * 
     */
    private int length;

    /**
     * Number of nodes in the tree
     * 
     */
    private int nbNodes;

    /**
     * The maximum luminosity of the image represented by this tree
     * 
     */
    private int maxLuminosity;

    /**
     * The name of the image
     * 
     */
    private String imageName;

    /**
     * Construct quadtree from 2d array representation of the image
     * 
     * @param path The local path of the image
     * 
     */
    public QuadTree(String path) {
        PMG image = FileManager.loadImage(path);
        this.root = new QuadTreeNode();
        this.length = image.getLuminosities().length;
        nbNodes = 1;
        this.imageName =  "compressed-"+ path;

        constructQuadtree(root, image.getLuminosities(), 0, 0, image.getLuminosities().length - 1, image.getLuminosities().length - 1);
    }

    /**
     * Construct quadtree from the 2D array representation of the image
     * 
     * @param node      The root node of the quadtree
     * @param arr       The 2D array that represents the image file body
     * @param startLine The starting line index of the image in the array
     * @param startCol  The starting column index of the image in the array
     * @param endLine   The ending line index of the image in the array
     * @param endCol    The ending column index of the image in the array
     * 
     */
    public void constructQuadtree(QuadTreeNode node, int[][] arr, int startLine, int startCol, int endLine,
            int endCol) {

        if ((endLine - startLine) == 1 && (endCol - startCol) == 1) {
            // The values of the twig's leaves are identical
            if (arr[startLine][startCol] == arr[startLine][endCol]
                    && arr[startLine][startCol] == arr[endLine][endCol]
                    && arr[startLine][startCol] == arr[endLine][startCol]) {
                
                node.setValue(arr[startLine][startCol]);
            }

            else {
                node.createChildren();
                node.setChildValue(0, arr[startLine][startCol]);
                node.setChildValue(1, arr[startLine][endCol]);
                node.setChildValue(2, arr[endLine][endCol]);
                node.setChildValue(3, arr[endLine][startCol]);
                this.nbNodes += 4;
            }

        }

        else {
            node.createChildren();

            constructQuadtree(node.getChild(0), arr, startLine, startCol, startLine + (endLine - startLine) / 2,
                    startCol + (endCol - startCol) / 2);
            constructQuadtree(node.getChild(1), arr, startLine, startCol + (endCol - startCol) / 2 + 1,
                    startLine + (endLine - startLine) / 2,
                    endCol);
            constructQuadtree(node.getChild(2), arr, startLine + (endLine - startLine) / 2 + 1,
                    startCol + (endCol - startCol) / 2 + 1,
                    endLine, endCol);
            constructQuadtree(node.getChild(3), arr, startLine + (endLine - startLine) / 2 + 1, startCol, endLine,
                    startCol + (endCol - startCol) / 2);
                    
            if(node.areChildrenEqual()){
                node.setValue(node.getChildValue(0));
                node.destroyChildren();
            }else{
                this.nbNodes+=4;
            }
            
        }
    }

    /**
     * Convert the quadtree to its parenthesized string representation.
     * 
     * @return The parenthesized string representation of the quadtree.
     */
    public String toString() {
        return toString(root);
    }

    /**
     *  Save the New compressed image in a PGM file
     * 
     * @param paht the where to save the image
     * 
     */
    public void toPgm(String path){
        FileManager.SaveImage(this, path);
    }

    /**
     * Helper method for recursively building the parenthesized string representation.
     * 
     * @param node The current node in the traversal.
     * @return The parenthesized string representation.
     */
    private String toString(QuadTreeNode node) {
        if (node != null) {
            if (node.isLeaf()) {
                return node.getValue() + " ";
            } else if(node.isTwigRoot()){
                String result = "(";
                for (int i = 0; i < 4; i++) {
                    result += toString(node.getChild(i));
                    if (i < 3) {
                        result += " ";
                    }
                }
                result += ")";
                return result;
            }
        }
        return "";
    }



    /**
     * Compress the quadtree with lambda method
     * 
     * @param tree the tree to compress
     * 
     */
    public void lambdaCompressTree() {
        lambdaCompressTree(this.root);
    }

    /**
     * Compress the quadtree with lambda method
     * 
     * @param node the node of the tree to compress, gonna change recursively to
     *             iterate through all the nodes of the tree
     * 
     */
    private void lambdaCompressTree(QuadTreeNode node) {
        if (!node.isLeaf()) {
            if (node.isTwigRoot()) {
                lambdaCompressTwig(node);
                this.nbNodes -= 4;
            } else {
                lambdaCompressTree(node.getChild(0));
                lambdaCompressTree(node.getChild(1));
                lambdaCompressTree(node.getChild(2));
                lambdaCompressTree(node.getChild(3));
            }
        }
    }

    /**
     * Compress a twig by using the lambda compression algorithm
     * 
     * @param twig the twig that we want to compress
     * 
     */
    public static void lambdaCompressTwig(QuadTreeNode twigRoot) {
        twigRoot.setValue((int)Math.round(Util.calculateAvgLogLuminosity(twigRoot)));
        twigRoot.destroyChildren();
    }

    /**
     * Méthode pour compresser le quadtree avec l'algorithme Rho.
     * 
     * @param rho La valeur de ρ pour la compression Rho.
     * 
     */
    public void rhoCompressTree(int rho) {
        detectCompressableTwigs();
        rhoCompressTree_(rho);
        this.twigs = null;
    }

    /**
     * Compresses the quadtree with the Rho compression algorithm.
     *
     * @param rho The value of ρ for the Rho compression.
     * 
     */
    public void rhoCompressTree_(int rho) {
        int initial_nodes_number = this.nbNodes;
        TwigAVLNode minTwig = this.twigs.findMin(twigs.getRoot());
        double ratio = 1.0;
        while (minTwig != null && ratio*100 > rho) {
            lambdaCompressTwig(minTwig.getLastQuadNode());

            QuadTreeNode parentNode = minTwig.getLastQuadNode().getParent();

            this.twigs.delete(minTwig.getEpsilon());
            this.nbNodes -= 4;

            while (parentNode != null && parentNode.areChildrenEqual()) {
                parentNode.setValue(parentNode.getChildValue(0));
                parentNode.destroyChildren();
                this.nbNodes -= 4;
                parentNode = parentNode.getParent();
            }
            
            if (parentNode != null && parentNode.isTwigRoot()) {
                double epsilon = Util.calculateEpsilon(parentNode);
                twigs.insert(epsilon, parentNode);
            }

            minTwig = this.twigs.findMin(this.twigs.getRoot());
            ratio = (double)this.nbNodes / (double)initial_nodes_number;
        }

    }

    /**
     * Detects compressible twigs and add them to twigs AVL for future compressions.
     *
     */
    private void detectCompressableTwigs() {
        this.twigs = new TwigAVLTree();
        detectCompressableTwigs(this.root);
    }

    /**
     *  Insert all twigs in the AVL twigs tree 
     * 
     * @param node the current node of the recursive traversal
     * 
     */
    private void detectCompressableTwigs(QuadTreeNode node) {
        if (node != null && !node.isLeaf()) {

            if (node.isTwigRoot()) {
                double epsilon = Util.calculateEpsilon(node);
                this.twigs.insert(epsilon, node);
            } else {
                for (int i = 0; i < 4; i++) {
                    detectCompressableTwigs(node.getChild(i));
                }
            }

        }
    }

    /**
     * Get the root of this quadtree
     * 
     * @return the root node of this quadtree
     * 
     */
    public QuadTreeNode getRoot() {
        return this.root;
    }

    /**
     * Get the length of the original image
     * 
     * @return the length of the original image
     * 
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Gets the number of nodes in the tree.
     *
     * @return The number of nodes in the tree.
     * 
     */
    public int getNbNodes()
    {
        return this.nbNodes;
    }

    /**
     * Gets the name of thr image.
     *
     * @return The name of the image.
     * 
     */
    public String getImageName()
    {
        return this.imageName;
    }
}
