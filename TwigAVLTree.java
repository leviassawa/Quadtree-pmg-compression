/**
 * AVL Tree implementation for storing Twig nodes based on their epsilon values.
 * 
 */
public class TwigAVLTree {

    private TwigAVLNode root;

    /**
     * Inserts a new TwigAVLNode with the given epsilon and associated QuadTreeNode into the AVL tree.
     *
     * @param epsilon   The epsilon value of the TwigAVLNode.
     * @param quadNode  The QuadTreeNode associated with the TwigAVLNode.
     * 
     */
    public void insert(double epsilon, QuadTreeNode quadNode) {
        root = insert(root, epsilon, quadNode);
    }

    private TwigAVLNode insert(TwigAVLNode node, double epsilon, QuadTreeNode quadNode) {
        if (node == null) {
            return new TwigAVLNode(epsilon, quadNode);
        }

        if (epsilon < node.getEpsilon()) {
            node.setLeft(insert(node.getLeft(), epsilon, quadNode));
        } else if (epsilon > node.getEpsilon()) {
            node.setRight(insert(node.getRight(), epsilon, quadNode));
        } else {
            node.addToQuadNodes(quadNode);
            return node;
        }

        updateHeight(node);

        return balance(node);
    }

    /**
     * Prints the AVL tree for debugging purposes.
     * 
     */
    public void print() {
        print(root, 0);
    }

    private void print(TwigAVLNode node, int level) {
        if (node != null) {
            print(node.getRight(), level + 1);
            for (int i = 0; i < level; i++) {
                System.out.print("   ");
            }
            System.out.println(node.getEpsilon() + " (Balance: " + node.getBal() + ")");
            print(node.getLeft(), level + 1);
        }
    }

    /**
     * Deletes the TwigAVLNode with the given epsilon from the AVL tree.
     *
     * @param epsilon   The epsilon value of the TwigAVLNode to be deleted.
     * 
     */
    public void delete(double epsilon) {
        root = delete(root, epsilon);
    }

    private TwigAVLNode delete(TwigAVLNode node, double epsilon) {
        if (node == null) {
            return null;
        }

        if (epsilon < node.getEpsilon()) {
            node.setLeft(delete(node.getLeft(), epsilon));
        } else if (epsilon > node.getEpsilon()) {
            node.setRight(delete(node.getRight(), epsilon));
        } else {
            if (node.getQuadNodes().size() > 1) {
                // If there are multiple QuadTreeNode references, just remove one
                node.deleteLastQuadNode();;
            } else {
                // If there is only one QuadTreeNode reference or none, perform deletion logic
                if (node.getLeft() == null || node.getRight() == null) {
                    TwigAVLNode temp = (node.getLeft() != null) ? node.getLeft() : node.getRight();
                    if (temp == null) {
                        temp = node;
                        node = null;
                    } else {
                        node = temp;
                    }
                } else {
                    TwigAVLNode temp = findMin(node.getRight());
                    node.setEpsilon(temp.getEpsilon());
                    node.setRight(delete(node.getRight(), temp.getEpsilon()));
                }
            }
        }

        if (node == null) {
            return null;
        }

        updateHeight(node);

        return balance(node);
    }

    /**
     * Retrieves a QuadTreeNode associated with the given epsilon value.
     * Returns null if no QuadTreeNode is associated.
     *
     * @param epsilon   The epsilon value to search for in the AVL tree.
     * @return A QuadTreeNode associated with the specified epsilon, or null if not found.
     * 
     */
    public QuadTreeNode getQuadTreeNodeByEpsilon(double epsilon) {
        return getQuadTreeNodeByEpsilon(root, epsilon);
    }

    private QuadTreeNode getQuadTreeNodeByEpsilon(TwigAVLNode node, double epsilon) {
        if (node == null) {
            return null;
        }

        if (epsilon < node.getEpsilon()) {
            return getQuadTreeNodeByEpsilon(node.getLeft(), epsilon);
        } else if (epsilon > node.getEpsilon()) {
            return getQuadTreeNodeByEpsilon(node.getRight(), epsilon);
        } else {
            // Return the first QuadTreeNode in the list (if any)
            return (node.isQuadNodesEmpty()) ? null : node.getLastQuadNode();
        }
    }

    /**
     * Finds and returns the TwigAVLNode with the minimum epsilon value in the AVL tree.
     *
     * @param node  The root node to start the search.
     * @return The TwigAVLNode with the minimum epsilon value.
     * 
     */
    public TwigAVLNode findMin(TwigAVLNode node) {
        
        if (node == null) {
            return null;
        }

        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        
        return node;
    }

    /**
     * Checks if the AVL tree contains a TwigAVLNode with the given epsilon value.
     *
     * @param epsilon   The epsilon value to search for in the AVL tree.
     * @return True if the AVL tree contains the specified epsilon, false otherwise.
     * 
     */
    public boolean search(double epsilon) {
        return search(root, epsilon);
    }

    private boolean search(TwigAVLNode node, double epsilon) {
        if (node == null) {
            return false;
        }

        if (epsilon == node.getEpsilon()) {
            return true;
        } else if (epsilon < node.getEpsilon()) {
            return search(node.getLeft(), epsilon);
        } else {
            return search(node.getRight(), epsilon);
        }
    }

    /**
     * Gets the height of the specified AVL node.
     *
     * @param node The AVL node for which to determine the height.
     * @return The height of the AVL node or 0 if the node is null.
     * 
     */
    private int height(TwigAVLNode node) {
        return (node != null) ? node.getHeight() : 0;
    }

    /**
     * Updates the height and balance factor of the specified AVL node based on its children's heights.
     *
     * @param node The AVL node to update.
     * 
     */
    private void updateHeight(TwigAVLNode node) {
        if (node != null) {
            node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
            node.setBal(balanceFactor(node));
        }
    }

    /**
     * Calculates the balance factor of the specified AVL node.
     *
     * @param node The AVL node for which to calculate the balance factor.
     * @return The balance factor (difference in heights between right and left subtrees).
     * 
     */
    private int balanceFactor(TwigAVLNode node) {
        return (node != null) ? height(node.getRight()) - height(node.getLeft()) : 0;
    }

    /**
     * Performs a left rotation on the AVL node and its right child.
     *
     * @param x The AVL node to rotate.
     * @return The new root of the rotated subtree.
     * 
     */
    private TwigAVLNode rotateLeft(TwigAVLNode x) {
        TwigAVLNode y = x.getRight();
        x.setRight(y.getLeft());
        y.setLeft(x);

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Performs a right rotation on the AVL node and its left child.
     *
     * @param y The AVL node to rotate.
     * @return The new root of the rotated subtree.
     * 
     */
    private TwigAVLNode rotateRight(TwigAVLNode y) {
        TwigAVLNode x = y.getLeft();
        y.setLeft(x.getRight());
        x.setRight(y);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Performs a double left-right rotation on the AVL node and its right child's left child.
     *
     * @param x The AVL node to rotate.
     * @return The new root of the double-rotated subtree.
     * 
     */
    private TwigAVLNode doubleRotateLeft(TwigAVLNode x) {
        x.setRight(rotateRight(x.getRight()));
        return rotateLeft(x);
    }

    /**
     * Performs a double right-left rotation on the AVL node and its left child's right child.
     *
     * @param y The AVL node to rotate.
     * @return The new root of the double-rotated subtree.
     * 
     */
    private TwigAVLNode doubleRotateRight(TwigAVLNode y) {
        y.setLeft(rotateLeft(y.getLeft()));
        return rotateRight(y);
    }

    /**
     * Balances the AVL node by performing necessary rotations based on its balance factor.
     *
     * @param node The AVL node to balance.
     * @return The new root of the balanced subtree.
     * 
     */
    private TwigAVLNode balance(TwigAVLNode node) {
        int bal = balanceFactor(node);

        if (bal > 1) {
            if (balanceFactor(node.getRight()) < 0) {
                return doubleRotateLeft(node);
            } else {
                return rotateLeft(node);
            }
        } else if (bal < -1) {
            if (balanceFactor(node.getLeft()) > 0) {
                return doubleRotateRight(node);
            } else {
                return rotateRight(node);
            }
        }

        return node;
    }
    
    /**
     * Gets the root node of the AVL tree.
     *
     * @return The root node of the AVL tree.
     * 
     */
    public TwigAVLNode getRoot() {
        return this.root;
    }
}