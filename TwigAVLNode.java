import java.util.ArrayList;

/**
 * Represents a node in the AVL tree used for storing Twigs.
 * 
 */
class TwigAVLNode {

    /**
     * The epsilon value associated with the QuadTreeNode.
     * 
     */
    private double epsilon;

    /**
     * Reference to the left child node.
     * 
     */
    private TwigAVLNode left;

    /**
     * Reference to the right child node.
     * 
     */
    private TwigAVLNode right;

    /**
     * Height of the node in the AVL tree.
     * 
     */
    private int height;

    /**
     * Balance factor of the node in the AVL tree.
     * 
     */
    private int bal;

    /**
     * List of references to the quadtree nodes associated with this AVL node.
     * In other terms, this list has all the twig roots of the quadtree that has the same epsilon
     * 
     */
    private ArrayList<QuadTreeNode> quadNodes;

    /**
     * Constructs a new TwigAVLNode with the specified epsilon and QuadTreeNode.
     *
     * @param epsilon  The epsilon value associated with the QuadTreeNode.
     * @param quadNode The QuadTreeNode associated with this AVL node.
     * 
     */
    public TwigAVLNode(double epsilon, QuadTreeNode quadNode) {
        this.epsilon = epsilon;
        this.height = 1;
        this.bal = 0;
        this.left = null;
        this.right = null;
        this.quadNodes = new ArrayList<>();
        this.quadNodes.add(quadNode);    
    }

    /**
     * Get the epsilon value associated with the QuadTreeNode.
     * 
     * @return The epsilon value.
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * Set the epsilon value associated with the QuadTreeNode.
     * 
     * @param epsilon The new epsilon value.
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Get the left child node.
     * 
     * @return The left child node.
     */
    public TwigAVLNode getLeft() {
        return left;
    }

    /**
     * Set the left child node.
     * 
     * @param left The new left child node.
     */
    public void setLeft(TwigAVLNode left) {
        this.left = left;
    }

    /**
     * Get the right child node.
     * 
     * @return The right child node.
     */
    public TwigAVLNode getRight() {
        return right;
    }

    /**
     * Set the right child node.
     * 
     * @param right The new right child node.
     */
    public void setRight(TwigAVLNode right) {
        this.right = right;
    }

    /**
     * Get the height of the node in the AVL tree.
     * 
     * @return The height of the node.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the height of the node in the AVL tree.
     * 
     * @param height The new height of the node.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the balance factor of the node in the AVL tree.
     * 
     * @return The balance factor.
     */
    public int getBal() {
        return bal;
    }

    /**
     * Set the balance factor of the node in the AVL tree.
     * 
     * @param bal The new balance factor.
     */
    public void setBal(int bal) {
        this.bal = bal;
    }

    /**
     * Get the list of references to the quadtree nodes associated with this AVL node.
     * 
     * @return The list of quadtree nodes.
     */
    public ArrayList<QuadTreeNode> getQuadNodes() {
        return quadNodes;
    }

    /**
     * Set the list of references to the quadtree nodes associated with this AVL node.
     * 
     * @param quadNodes The new list of quadtree nodes.
     */
    public void setQuadNodes(ArrayList<QuadTreeNode> quadNodes) {
        this.quadNodes = quadNodes;
    }

    /**
     * Get the last QuadTreeNode in the list of quadNodes.
     *
     * @return The last QuadTreeNode or null if the list is empty.
     * 
     */
    public QuadTreeNode getLastQuadNode() {
        return quadNodes.isEmpty() ? null : quadNodes.get(quadNodes.size() - 1);
    }

    /**
     * Add a QuadTreeNode to the end of the list of quadNodes.
     *
     * @param quadNode The QuadTreeNode to add.
     * 
     */
    public void addToQuadNodes(QuadTreeNode quadNode) {
        quadNodes.add(quadNodes.size(), quadNode);
    }

    /**
     * Deletes the last QuadTreeNode from the list of quadNodes.
     * If the list is empty, nothing happens.
     * 
     */
    public void deleteLastQuadNode() {
        if (!quadNodes.isEmpty()) {
            quadNodes.remove(quadNodes.size() - 1);
        }
    }  

    /**
     * Check if the list of quadNodes is empty.
     *
     * @return True if the list is empty, false otherwise.
     * 
     */
    public boolean isQuadNodesEmpty() {
        return quadNodes.isEmpty();
    }
}
