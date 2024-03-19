/**
 * Class that implements the test cases for our compression
 * 
 */
public class QuadTreeTest {

    public static void main(String[] args) {
        testImageCompression();
        //testImageConstruction();
    }

    private static void printQuadTree(QuadTreeNode node, int depth) {
        if (node != null) {
            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
            System.out.println("Value: " + node.getValue());

            if (!node.isLeaf()) {
                printQuadTree(node.getChild(0), depth + 1);
                printQuadTree(node.getChild(1), depth + 1);
                printQuadTree(node.getChild(2), depth + 1);
                printQuadTree(node.getChild(3), depth + 1);
            }
        }
    }

    private static void testImageConstruction() {

        long startTime = System.currentTimeMillis();
        QuadTree tree = new QuadTree("JDD3.pgm");
        long endTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (endTime - startTime) / 1000.0;

        System.out.println("Le programme a mis " + elapsedTimeInSeconds + " secondes à s'exécuter.");
        printQuadTree(tree.getRoot(), 0);
        System.out.println("-------------------------------");
    }
    
    private static void testImageCompression() {

        QuadTree lowCompressionQuadTree = new QuadTree("train.pgm");
        int initialNodesLow = lowCompressionQuadTree.getNbNodes();
        long startTime = System.currentTimeMillis();
        lowCompressionQuadTree.rhoCompressTree(1);
        long endTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (endTime - startTime) / 1000.0;

        System.out.println("Le programme a mis " + elapsedTimeInSeconds + " secondes à s'exécuter.");
        System.out.println("Custom Compression Result:");
        System.out.println("Initial Nodes: " + initialNodesLow);
        System.out.println("Final Nodes: " + lowCompressionQuadTree.getNbNodes());
        System.out.println("Tree structure after compression:");
        printQuadTree(lowCompressionQuadTree.getRoot(), 0);
        System.out.println("-------------------------------");
    }
    
}
