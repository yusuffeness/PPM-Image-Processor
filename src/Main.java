import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        
        String inputFilename = null;
        String outputFilename = null;
        boolean modeCompress = false;
        boolean modeEdge = false;
        boolean showOutline = false;

       
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    if (i + 1 < args.length) inputFilename = args[++i];
                    break;
                case "-o":
                    if (i + 1 < args.length) outputFilename = args[++i];
                    break;
                case "-c":
                    modeCompress = true;
                    break;
                case "-e":
                    modeEdge = true;
                    break;
                case "-t":
                    showOutline = true;
                    break;
            }
        }

        
        if (inputFilename == null) {
            System.err.println("Hata: Girdi dosyasi belirtilmedi (-i <dosya>).");
            return;
        }
        if (outputFilename == null) {
            System.err.println("Hata: Cikti dosyasi ismi belirtilmedi (-o <isim>).");
            return;
        }
        if (!modeCompress && !modeEdge) {
            System.err.println("Hata: Mod secilmedi (-c veya -e kullanin).");
            return;
        }

        try {
            
            PPMImage image = PPMImage.read(inputFilename);

           
            if (image.getWidth() != image.getHeight()) {
                System.err.println("Error: Input image is not square.");
                System.exit(1);
            }

            int totalPixels = image.getWidth() * image.getHeight();

            
            if (modeCompress) {
                
                double[] targetRatios = {0.002, 0.004, 0.01, 0.033, 0.077, 0.2, 0.5, 0.65};

                System.out.println("Mod: Sikistirma (-c)");
                
                for (int i = 0; i < targetRatios.length; i++) {
                    double target = targetRatios[i];
                    
                   
                    double bestThreshold = findThresholdForRatio(image, target, totalPixels);
                    
                   
                    Quadtree qt = new Quadtree(image, bestThreshold);
                    int leaves = qt.getLeafCount();
                    double actualRatio = (double) leaves / totalPixels;

                   
                    String currentOutName = outputFilename + "-" + (i + 1) + ".ppm";
                    
                    
                    qt.decompress(showOutline).write(currentOutName);

                    
                    System.out.println(String.format("Output: %s | Leaves: %d | Pixels: %d | Ratio: %.5f (Target: %.3f)", 
                        currentOutName, leaves, totalPixels, actualRatio, target));
                }
            } 
            
           
            else if (modeEdge) {
                System.out.println("Mod: Edge Detection (-e)");
               
                double target = 0.5; 
                double threshold = findThresholdForRatio(image, target, totalPixels);

                Quadtree qt = new Quadtree(image, threshold);
                
                
                String currentOutName = outputFilename + ".ppm";
                
                
                qt.edgeDetect(showOutline).write(currentOutName);

                System.out.println("Output generated: " + currentOutName);
                System.out.println("Used Threshold: " + String.format("%.2f", threshold));
            }

        } catch (IOException e) {
            System.err.println("IO Hatasi: " + e.getMessage());
        }
    }

    
    private static double findThresholdForRatio(PPMImage img, double targetRatio, int totalPixels) {
        double low = 0.0;
        double high = 300000.0;
        double bestThresh = high;
        
        for (int i = 0; i < 40; i++) {
            double mid = (low + high) / 2;
            Quadtree qt = new Quadtree(img, mid);
            double currentRatio = (double) qt.getLeafCount() / totalPixels;

            if (currentRatio > targetRatio) {
                low = mid; 
            } else {
                high = mid;
                bestThresh = mid;
            }
        }
        return bestThresh;
    }
}
