import java.io.IOException;
import java.util.Scanner;

public class CompressionMenu {

    // QuadTree instance to hold the image
    private QuadTree newImage;

    /**
     * Constructor to initialize the CompressionMenu with a QuadTree.
     *
     * @param Q The QuadTree representing the image.
     */
    public CompressionMenu(QuadTree Q) {
        this.newImage = Q;
    }

    /**
     * Method to start the compression menu.
     */
    public void start() {
        boolean applyCompression = true;

        while(applyCompression){
            displayCompMenu();
            applyCompression();
            applyCompression = endCompression();
        }
        
    }

    /**
     * Method to display the compression menu options.
     */
    public void displayCompMenu() {
        System.out.println("Choose compression by entering the number before (1 or 2):");
        System.out.println("1. Lambda compression");
        System.out.println("2. RHO compression");
    }

    /**
     * Method to apply compression based on user input.
     */
    public void applyCompression() {
        int initialNodesLow = newImage.getNbNodes();
        long startTime ;
        long endTime ;
        double elapsedTimeInSeconds;
        if (chooseCompression()) {
            System.out.println("PROCESSING LAMBDA COMPRESSION: ");
            startTime = System.currentTimeMillis();
            newImage.lambdaCompressTree();
            endTime = System.currentTimeMillis();
            elapsedTimeInSeconds = (endTime - startTime) / 1000.0;
            try {
                FileManager.saveQuatree(( newImage.getImageName().substring(0,newImage.getImageName().length()- 3)) + "txt", newImage.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            newImage.toPgm("Lambda"+newImage.getImageName());
        } else {
            System.out.println("PROCESSING RHO COMPRESSION: ");
            startTime = System.currentTimeMillis();
            newImage.rhoCompressTree(readRho());
            endTime = System.currentTimeMillis();
            elapsedTimeInSeconds = (endTime - startTime) / 1000.0;
            
             try {
                FileManager.saveQuatree(( newImage.getImageName().substring(0,newImage.getImageName().length()- 3)) + "txt", newImage.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            newImage.toPgm("RHO"+newImage.getImageName());
        }
        System.out.println("COMPRESSION COMPLETED!");
        System.out.println("Le programme a mis " + elapsedTimeInSeconds + " secondes à s'exécuter.");
        System.out.println("Custom Compression Result:");
        System.out.println("Initial Nodes: " + initialNodesLow);
        System.out.println("Final Nodes: " + newImage.getNbNodes());  
        
    }

    /**
     * Method to prompt the user to choose compression type.
     *
     * @return true if Lambda compression is chosen, false if RHO compression is chosen.
     */
    public boolean chooseCompression() {
        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();

        while (answer != 1 && answer != 2) {
            System.out.println("ERROR: INVALID INPUT! Enter 1 OR 2: ");
            answer = scanner.nextInt();
            scanner.nextLine();
        }
        return answer == 1;
    }

    /**
     * Method to read and validate the ρ factor for RHO compression.
     *
     * @return The validated ρ factor for RHO compression.
     */
    public int readRho() {
        System.out.println("Please Enter the factor ρ for the RHO compression: ");

        Scanner scanner = new Scanner(System.in);
        int rho = scanner.nextInt();
        scanner.nextLine();

        while (rho < 1 || rho > 100) {
            System.out.println("ERROR: INVALID INPUT! ρ value must be between 1 and 100: ");
            rho = scanner.nextInt();
            scanner.nextLine();
        }
        
        return rho;
    }

    public boolean endCompression(){
       
        
        System.out.println("do you want to compress this image one more time (1 or 2):");
        System.out.println("1. yes");
        System.out.println("2. no");
        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();
        
        

        while (answer != 1 && answer != 2) {
            System.out.println("ERROR: INVALID INPUT! Enter 1 OR 2: ");
            answer = scanner.nextInt();
            scanner.nextLine();
        }
        
        return answer == 1;
    }
}