import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Utility class for reading and writing images in PGM (Portable Gray Map) format.
 *
 */
public class FileManager{

    /**
     * Checks if the image format is correct.
     *
     * @param magicNumber The magic number of the image format.
     * @param width       The width of the image.
     * @param height      The height of the image.
     * @return true if the format is correct, otherwise false.
     */
    private static boolean isGoodFormat(String magicNumber, int width, int height) {
        boolean isGoodFormat;

        if (magicNumber.startsWith("P2")) {
            isGoodFormat = true;
            
            if (width == height && Util.isPowerOfTwo(width)) {
                isGoodFormat = true;
            } else {
                System.out.println("Incorrect image size!");
                isGoodFormat = false;
            }
        } else {
            isGoodFormat = false;
            System.out.println("Incorrect file format!");
        }
        
        return isGoodFormat;
    }

    /*
     * 
     */
    public static void saveQuatree(String file,String quadtree) throws IOException{
         try ( FileWriter writer = new FileWriter(new File(file))){
             writer.write(quadtree);
            // Quadtree file header
         } catch (FileNotFoundException e) {
            // Handling error in case of file writing failure
            System.err.println("Error writing to the file: : " + e.getMessage());}

    }

    /**
     * Reads the content of a file and returns a 2D array of integers.
     *
     * @param path The path of the file to read.
     * @return A 2D array of integers, or null in case of a file reading error.
     * @throws FileNotFoundException If the specified file is not found.
     */
    public static PMG loadImage(String path) {
        try (Scanner scanner = new Scanner(new File(path))) {
            int width;
            int height;
            int maxLuminosity = -1 ;
            PMG newImage;
            int[][] tQuadtree = null;
            String magicNumber = scanner.nextLine();
            String comment = scanner.nextLine(); // Reading the comment

            // Check if the file starts with the correct magic number
            if (scanner.hasNextInt()) {
                width = scanner.nextInt();
                height = scanner.nextInt();
                maxLuminosity = scanner.nextInt();

                // Check if the file format is correct
                if (isGoodFormat(magicNumber, width, height)) {

                    // Initialize the 2D array based on the size read from the file
                    tQuadtree = new int[width][height];
                    boolean isGoodValue = true;
                    int i = 0;
                    int j = 0;
                    int nbElements = 0;
                    int value;

                    // Read values from the file and populate the 2D array
                    while (scanner.hasNextInt() && isGoodValue && i < width && (nbElements < (width * height))) {
                        while (j < height && isGoodValue) {
                            value = scanner.nextInt();
                            isGoodValue = (value >= 0) && (value <= maxLuminosity);
                            if (isGoodValue) {
                                tQuadtree[i][j] = value;
                                j++;
                                nbElements++;
                            } else {
                                System.out.println("A value in the file exceeds the maximum luminosity.");
                            }
                        }

                        j = 0;
                        i++;
                    }

                    // Check if the number of elements matches the expected size
                    if (nbElements < (width * height)) {
                        System.out.println("Value missing in your file!");
                    }
                } 
            }
            // Create a QuadtreePrefab object with the loaded data and return it
            newImage = new PMG(tQuadtree,maxLuminosity);
            return newImage;
        } catch (FileNotFoundException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves an image represented by a QuadTree to a PGM file.
     *
     * @param image The QuadTree representing the image.
     * @param file  The path to the file where the image will be saved.
     */
    public static void SaveImage(QuadTree image ,String file ){
        String mot;

        int length = image.getLength();

        
        int[][] grilleTemp = new int[length][length];

        createGrilleTemp(image.getRoot(),grilleTemp, 0, 0, length - 1, length - 1);

        try ( FileWriter writer = new FileWriter(new File(file))){
            // PGM file header
            writer.write("P2");
            writer.write(System.lineSeparator()); 
            writer.write("# version comprésser!");
            writer.write(System.lineSeparator()); 
            mot = String.valueOf(length);
            writer.write(mot + " "+ mot);
            writer.write(System.lineSeparator()); 
            mot = String.valueOf(255);
            writer.write(mot);
            writer.write(System.lineSeparator()); 
            for (int i = 0; i < grilleTemp.length; i++) {
                for (int j = 0; j < grilleTemp[i].length; j++) {
                    mot = String.valueOf(grilleTemp[i][j]); 
                    writer.write(mot+ " ");
                }

                writer.write(System.lineSeparator()); 
               
            }
        }catch (IOException e) {
            // Handling error in case of file writing failure
            System.err.println("Error writing to the file: : " + e.getMessage());
        }
    }


    /**
     * Recursively fills a 2D array with values from a QuadTree starting from a specified root node.
     *
     * @param root      The root node of the QuadTree.
     * @param grid      The 2D array to be filled with QuadTree values.
     * @param startLine The starting index of the row in the array.
     * @param startCol  The starting index of the column in the array.
     * @param endLine   The ending index of the row in the array.
     * @param endCol    The ending index of the column in the array.
     */
    private static void createGrilleTemp(QuadTreeNode root, int[][] grille, int startLine, int startCol, int endLine, int endCol){
        
        if (root.isLeaf()){
            // Filling the area with values from the leaf
            for(int i = startLine ; i <= endLine;i++){
                for (int j = startCol; j <= endCol;j++){
                    grille[i][j] = root.getValue();
                }
            }
        }else{
            if(root.isTwigRoot()){
                // Special case for Twig nodes
                if((endLine - startLine) == 1 && (endCol - startCol) == 1 ){
                    grille[startLine][startCol] = root.getChildValue(0);
                    grille[startLine][endCol] = root.getChildValue(1);
                    grille[endLine][endCol] = root.getChildValue(2);
                    grille[endLine][startCol] = root.getChildValue(3);
                }else{
                    // Recursive call for the four children of the Twig node
                    createGrilleTemp(root.getChild(0), grille, startLine, startCol, startLine + (endLine - startLine) / 2,
                    startCol + (endCol - startCol) / 2);
                    createGrilleTemp(root.getChild(1), grille,startLine, startCol + (endCol - startCol) / 2 + 1, startLine + (endLine - startLine) / 2,
                    endCol);
                    createGrilleTemp(root.getChild(2), grille, startLine + (endLine - startLine) / 2 + 1, startCol + (endCol - startCol) / 2 + 1,
                    endLine, endCol);
                    createGrilleTemp(root.getChild(3), grille,startLine + (endLine - startLine) / 2 + 1, startCol, endLine,
                    startCol + (endCol - startCol) / 2);
                }
            }else{
                // Recursive call for the four children of the node
                    createGrilleTemp(root.getChild(0), grille, startLine, startCol, startLine + (endLine - startLine) / 2,
                    startCol + (endCol - startCol) / 2);
                    createGrilleTemp(root.getChild(1), grille,startLine, startCol + (endCol - startCol) / 2 + 1, startLine + (endLine - startLine) / 2,
                    endCol);
                    createGrilleTemp(root.getChild(2), grille, startLine + (endLine - startLine) / 2 + 1, startCol + (endCol - startCol) / 2 + 1,
                    endLine, endCol);
                    createGrilleTemp(root.getChild(3), grille,startLine + (endLine - startLine) / 2 + 1, startCol, endLine,
                    startCol + (endCol - startCol) / 2);
            }
        }
    }
} 
