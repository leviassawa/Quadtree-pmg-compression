import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {
    private ArrayList<String> images;  // List of image file names
    private QuadTree compressedQuadtree;  // Compressed QuadTree representing the selected image
    private CompressionMenu compMenu;  // Compression menu for the QuadTree

    /**
     * Constructor for the MainMenu class.
     *
     * @param images List of image file names.
     */
    public MainMenu(ArrayList<String> images) {
        this.images = images;
    }

    /**
     * Starts the main menu for image compression.
     */
    public void start() {
        displayImages();
        compressedQuadtree = new QuadTree(images.get(readAnswer()));  
        compMenu = new CompressionMenu(compressedQuadtree);
        compMenu.start();
    }

    /**
     * Reads the user's answer for selecting an image.
     *
     * @return The index of the selected image in the list.
     */
    public int readAnswer() {
        Scanner scanner = new Scanner(System.in);
        int answer;

        answer = scanner.nextInt();
        scanner.nextLine();
        while (answer < 1 || answer > images.size()) {
            System.out.println("ERROR: INVALID INPUT! Enter the number before the file you want to compress: ");
            answer = scanner.nextInt();
            scanner.nextLine();
        }

        return answer - 1;
    }

    /**
     * Displays the list of available images to the user.
     */
    public void displayImages() {
        for (int i = 0; i < images.size(); i++) {
            System.out.println(i + 1 + ". " + images.get(i));
        }
        System.out.println("Enter the number before the file you want to compress: ");
    }
}
    