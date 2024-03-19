/**
 * Class that represents a Node of a QuadTree
 * 
 */
public class QuadTreeNode {

    /**
     * The value attribute of the Node.
     * 
     * It could be set to -1 if this Node is not a leaf
     * 
     */
    private int value;
    
    /**
     * The parent of the node, used in dynamic compression
     * 
     */
    private QuadTreeNode parent;

    /**
     * The children children of a Node
     * 
     * It contains the 4 children nodes of this Node
     * Could be null if this Node is a leaf
     * 
     */
    private QuadTreeNode[] children;

    /**
     * The constructor of this node 
     *
     */
    public QuadTreeNode()
    {
        this.parent = null;
        this.value = -1;
        this.children = null;
    }

    /**
     * The constructor of this node 
     *
     * @param value the value of the node
     * 
     */
    public QuadTreeNode(int value)
    {
        this.parent = null;
        this.value = value;
        this.children = null;
    }

    /**
     * The getter of the parent of this node
     * 
     * @return the parent of this node
     */
    public QuadTreeNode getParent()
    {
        return this.parent;
    }

    /**
     * The getter of the value attribute
     * 
     * @return the value attribute of this node
     * 
     */
    public int getValue()
    {
        return this.value;
    }

    /**
     * The setter of the value attribute
     * 
     * @param value the value that we want to store in the value attribute of the Node
     * 
     */
    public void setValue(int value)
    {
        this.value = value;
    }

    public boolean areChildrenEqual()
    {
        if (this.isTwigRoot()) {
            int val = this.getChildValue(0);
            boolean res = true;

            for (int i = 1; i < 4; i++) {
                res = res && this.getChildValue(i) == val;
            }

            return res;
        }
        return false;
    }

    /**
     * Get the value of the nth child of this node
     * 
     * @param i the index of the child 
     * 
     * @return the nth child if this node has children, null if it doesn't have children or the index is invalid
     * 
     */
    public QuadTreeNode getChild(int i)
    {
        if (this.children != null && i < 4 && i >= 0) 
        {
            return this.children[i];
        }
        
        return null;
    }

    /**
     * Get the value of the nth child of this node
     * 
     * @param i the index of the child 
     * 
     * @return the value of the nth child if this node has children, -1 if it doesn't have children or the value of the children is still not set or the index is not valid
     * 
     */
    public int getChildValue(int i)
    {
        if (this.children != null && i < 4 && i >= 0) 
        {
            return this.children[i].value;
        }
        
        return -1;
    }

    /**
     * Set the value of the nth child
     * NB: It creates the children if not done yet
     * 
     * @param i the index of the child
     * @param value the value to be set
     * 
     */
    public void setChildValue(int i, int value)
    {
        if (this.children == null) {
            createChildren();
        }

        this.children[i].value = value;
    }

    /**
     * Create 4 children for this node 
     * 
     */
    public void createChildren() 
    {
        this.children = new QuadTreeNode[4];

        for (int i = 0; i < 4; i++) 
        {
            this.children[i] = new QuadTreeNode();
            this.children[i].parent = this;
        }
    }

    /**
     * Print the values of the children of this node
     * 
     */
    public void printChildren() {
        if (this.children != null) {
            for (int i = 0; i < 4; i++) {
                System.out.println("Child " + i + ": " + this.children[i].getValue());
            }
        } else {
            System.out.println("No children for this node.");
        }
    }

    /**
     * Destroy the children of this node
     * 
     */
    public void destroyChildren()
    {
        this.children = null;
    }

    /**
     * Verify that the node is a leaf
     * 
     * @return true if this node is a leaf, false otherwise
     */
    public boolean isLeaf() {
        return this.value != -1;
    }

    /**
     * Verify that the node is a twig root
     * 
     * @return true if this node is a twig root, false otherwise
     */
    public boolean isTwigRoot() {
        if (this.children != null) {
            return this.getChild(0).isLeaf()
                && this.getChild(1).isLeaf()
                && this.getChild(2).isLeaf()
                && this.getChild(3).isLeaf();
        }
        return false;
    }
}