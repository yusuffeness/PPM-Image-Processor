Name: Yusuf Enes Kütük
# PPM Image Processor

This project is a Java-based image processing application developed for academic purposes.
It operates on PPM (P3 format) images and implements image compression and edge detection
using a quadtree-based approach.

The compression module recursively divides the image into regions and represents them
using a quadtree structure. Different compression levels are achieved by automatically
optimizing a threshold value through binary search.

The edge detection module highlights image edges by analyzing quadtree leaf nodes.
Small regions are processed to preserve details, while larger regions are suppressed
to improve edge visibility.

The project is designed as a command-line application and focuses on understanding
core concepts of image processing, recursion, and tree-based data structures.


   <img width="263" height="527" alt="image" src="https://github.com/user-attachments/assets/29b9f9ad-4c12-4296-bf6d-d6b008971608" />



COMPILATION
-------------------------------------------------------
To compile the source files, navigate to the directory
containing the source code and run the following command:

javac *.java

This command compiles Main.java, PPMImage.java, and
Quadtree.java.



RUNNING
-------------------------------------------------------
The program is executed using command line arguments (flags).
Example usages are shown below:

1. COMPRESSION MODE:
Compresses the input image into 8 different levels and
generates output files.

Command:
java Main -i kira.ppm -o result -c

If you also want to draw quadtree boundaries, add the -t flag:
java Main -i kira.ppm -o result -c -t


2. EDGE DETECTION MODE:
Detects edges in the input image.

Command:
java Main -i kira.ppm -o edge -e

To visualize the quadtree structure during edge detection:
java Main -i kira.ppm -o edge -e -t


Arguments:
-i <file>  : Input file (e.g., kira.ppm)
-o <name>  : Output file name
-c         : Enables compression mode (produces 8 files)
-e         : Enables edge detection mode
-t         : (Optional) Draws quadtree boundaries with blue lines



FILE LIST (FILE DIRECTORY)
-------------------------------------------------------
1. Main.java:
   Entry point of the program. Handles command line arguments
   (-i, -o, -c, etc.).
   Uses Binary Search to automatically find the optimal
   threshold value to achieve the desired compression ratios.

2. Quadtree.java:
   Stores the tree structure that recursively divides the
   image into 4 regions.
   - decompress(): Converts compressed data back into an image.
   - edgeDetect(): Applies edge filtering to small leaf nodes
     and fills large blocks with black.

3. PPMImage.java:
   Helper class used for reading and writing .ppm (P3 format)
   image files.



NOTES AND LIMITATIONS (BUGS & LIMITATIONS)
-------------------------------------------------------
- The program only works with square (N x N) images. If a
  non-square image is provided, the program terminates with
  an error.
- To achieve the target compression ratios (e.g., 0.002,
  0.004, etc.), the program dynamically searches for an
  appropriate threshold value. Therefore, the resulting
  ratios may not be exact (e.g., 0.2001 instead of 0.2),
  but they are very close.
- In edge detection mode, the threshold value is automatically
  optimized to preserve detail as much as possible.
