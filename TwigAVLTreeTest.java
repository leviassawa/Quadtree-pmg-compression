public class TwigAVLTreeTest {
    public static void main(String[] args) {
        TwigAVLTree avlTree = new TwigAVLTree();

        QuadTreeNode quadTreeNode1 = createQuadTreeNode(1, 2, 3, 4);
        QuadTreeNode quadTreeNode2 = createQuadTreeNode(5, 6, 7, 8);
        QuadTreeNode quadTreeNode3 = createQuadTreeNode(9, 10, 11, 12);
        QuadTreeNode quadTreeNode4 = createQuadTreeNode(13, 14, 15, 16);
        QuadTreeNode quadTreeNode5 = createQuadTreeNode(17, 18, 19, 20);
        QuadTreeNode quadTreeNode6 = createQuadTreeNode(21, 22, 23, 24);

        avlTree.insert(0.2, quadTreeNode1);
        avlTree.insert(0.5, quadTreeNode2);
        avlTree.insert(0.1, quadTreeNode3);
        avlTree.insert(0.8, quadTreeNode4);
        avlTree.insert(0.3, quadTreeNode5);
        avlTree.insert(0.7, quadTreeNode6);
        avlTree.insert(0.1, quadTreeNode6);


        // Display AVL Tree
        System.out.println("AVL Tree:");
        avlTree.print();

        double searchEpsilon = 0.3;
        System.out.println("Search for " + searchEpsilon + ": " + avlTree.search(searchEpsilon));

        double deleteEpsilon = 0.1;
        System.out.println("Deletion of " + deleteEpsilon);
        avlTree.delete(deleteEpsilon);

        System.out.println("AVL Tree after deletion:");
        avlTree.print();
    }

    private static QuadTreeNode createQuadTreeNode(int value1, int value2, int value3, int value4) {
        QuadTreeNode quadTreeNode = new QuadTreeNode();
        quadTreeNode.createChildren();
        quadTreeNode.setChildValue(0, value1);
        quadTreeNode.setChildValue(1, value2);
        quadTreeNode.setChildValue(2, value3);
        quadTreeNode.setChildValue(3, value4);

        return quadTreeNode;
    }
}
