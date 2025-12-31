import java.io.*;
import java.util.Scanner;

public class PPMImage {
    private int width;
    private int height;
    private int maxColorVal;
    private int[][][] pixels; 

    public PPMImage(int width, int height, int maxColorVal) {
        this.width = width;
        this.height = height;
        this.maxColorVal = maxColorVal;
        this.pixels = new int[height][width][3];
    }

    
    public static PPMImage read(String filename) throws IOException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);

       
        if (!sc.hasNext() || !sc.next().equals("P3")) {
            throw new IOException("Invalid PPM file");
        }

        
        
        int w = 0, h = 0, maxVal = 0;
        
        
        while (sc.hasNext() && !sc.hasNextInt()) { sc.next(); } 
        w = sc.nextInt();
        while (sc.hasNext() && !sc.hasNextInt()) { sc.next(); }
        h = sc.nextInt();
        while (sc.hasNext() && !sc.hasNextInt()) { sc.next(); }
        maxVal = sc.nextInt();

        PPMImage img = new PPMImage(w, h, maxVal);

       
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                img.pixels[i][j][0] = sc.nextInt(); // R
                img.pixels[i][j][1] = sc.nextInt(); // G
                img.pixels[i][j][2] = sc.nextInt(); // B
            }
        }
        sc.close();
        return img;
    }

  
    public void write(String filename) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filename));
        pw.println("P3");
        pw.println(width + " " + height);
        pw.println(maxColorVal);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pw.print(pixels[i][j][0] + " " + pixels[i][j][1] + " " + pixels[i][j][2] + " ");
            }
            pw.println();
        }
        pw.close();
    }

  
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[] getPixel(int row, int col) { return pixels[row][col]; }
}
