public class Quadtree {
   private Node root;
   private PPMImage originalImage;

   public Quadtree(PPMImage var1, double var2) {
      this.originalImage = var1;
      this.root = new Node(this, 0, 0, var1.getWidth(), var2);
   }

   private int[] calculateMeanColor(int var1, int var2, int var3) {
      long var4 = 0L;
      long var6 = 0L;
      long var8 = 0L;
      int var10 = 0;

      for(int var11 = var2; var11 < var2 + var3; ++var11) {
         for(int var12 = var1; var12 < var1 + var3; ++var12) {
            if (var11 < this.originalImage.getHeight() && var12 < this.originalImage.getWidth()) {
               int[] var13 = this.originalImage.getPixel(var11, var12);
               var4 += (long)var13[0];
               var6 += (long)var13[1];
               var8 += (long)var13[2];
               ++var10;
            }
         }
      }

      if (var10 == 0) {
         return new int[]{0, 0, 0};
      } else {
         return new int[]{(int)(var4 / (long)var10), (int)(var6 / (long)var10), (int)(var8 / (long)var10)};
      }
   }

   private double calculateMeanSquaredError(int var1, int var2, int var3, int[] var4) {
      double var5 = 0.0;
      int var7 = 0;

      for(int var8 = var2; var8 < var2 + var3; ++var8) {
         for(int var9 = var1; var9 < var1 + var3; ++var9) {
            if (var8 < this.originalImage.getHeight() && var9 < this.originalImage.getWidth()) {
               int[] var10 = this.originalImage.getPixel(var8, var9);
               double var11 = Math.pow((double)(var10[0] - var4[0]), 2.0) + Math.pow((double)(var10[1] - var4[1]), 2.0) + Math.pow((double)(var10[2] - var4[2]), 2.0);
               var5 += var11;
               ++var7;
            }
         }
      }

      return var7 == 0 ? 0.0 : var5 / (double)var7;
   }

   public int getLeafCount() {
      return this.countLeaves(this.root);
   }

   private int countLeaves(Node var1) {
      return var1.isLeaf ? 1 : this.countLeaves(var1.nw) + this.countLeaves(var1.ne) + this.countLeaves(var1.sw) + this.countLeaves(var1.se);
   }

   public PPMImage decompress(boolean var1) {
      PPMImage var2 = new PPMImage(this.originalImage.getWidth(), this.originalImage.getHeight(), 255);
      this.fillImageFromNode(this.root, var2, var1);
      return var2;
   }

   private void fillImageFromNode(Node var1, PPMImage var2, boolean var3) {
      if (var1.isLeaf) {
         for(int var4 = var1.y; var4 < var1.y + var1.size; ++var4) {
            for(int var5 = var1.x; var5 < var1.x + var1.size; ++var5) {
               if (var4 < var2.getHeight() && var5 < var2.getWidth()) {
                  if (!var3 || var4 != var1.y && var5 != var1.x && var4 != var1.y + var1.size - 1 && var5 != var1.x + var1.size - 1) {
                     var2.getPixel(var4, var5)[0] = var1.color[0];
                     var2.getPixel(var4, var5)[1] = var1.color[1];
                     var2.getPixel(var4, var5)[2] = var1.color[2];
                  } else {
                     var2.getPixel(var4, var5)[0] = 0;
                     var2.getPixel(var4, var5)[1] = 0;
                     var2.getPixel(var4, var5)[2] = 255;
                  }
               }
            }
         }
      } else {
         this.fillImageFromNode(var1.nw, var2, var3);
         this.fillImageFromNode(var1.ne, var2, var3);
         this.fillImageFromNode(var1.sw, var2, var3);
         this.fillImageFromNode(var1.se, var2, var3);
      }

   }

   public PPMImage edgeDetect(boolean var1) {
      PPMImage var2 = new PPMImage(this.originalImage.getWidth(), this.originalImage.getHeight(), 255);
      this.applyEdgeDetectionRecursive(this.root, var2, var1);
      return var2;
   }

   private void applyEdgeDetectionRecursive(Node var1, PPMImage var2, boolean var3) {
      if (var1.isLeaf) {
         int var4;
         int var5;
         if (var1.size > 2) {
            for(var4 = var1.y; var4 < var1.y + var1.size; ++var4) {
               for(var5 = var1.x; var5 < var1.x + var1.size; ++var5) {
                  if (var4 < var2.getHeight() && var5 < var2.getWidth()) {
                     if (var3 && (var4 == var1.y || var5 == var1.x || var4 == var1.y + var1.size - 1 || var5 == var1.x + var1.size - 1)) {
                        var2.getPixel(var4, var5)[0] = 0;
                        var2.getPixel(var4, var5)[1] = 0;
                        var2.getPixel(var4, var5)[2] = 255;
                     } else {
                        var2.getPixel(var4, var5)[0] = 0;
                        var2.getPixel(var4, var5)[1] = 0;
                        var2.getPixel(var4, var5)[2] = 0;
                     }
                  }
               }
            }
         } else {
            for(var4 = var1.y; var4 < var1.y + var1.size; ++var4) {
               for(var5 = var1.x; var5 < var1.x + var1.size; ++var5) {
                  if (var4 < var2.getHeight() && var5 < var2.getWidth()) {
                     int[] var6 = this.applyKernel(var5, var4);
                     var2.getPixel(var4, var5)[0] = var6[0];
                     var2.getPixel(var4, var5)[1] = var6[1];
                     var2.getPixel(var4, var5)[2] = var6[2];
                  }
               }
            }
         }
      } else {
         this.applyEdgeDetectionRecursive(var1.nw, var2, var3);
         this.applyEdgeDetectionRecursive(var1.ne, var2, var3);
         this.applyEdgeDetectionRecursive(var1.sw, var2, var3);
         this.applyEdgeDetectionRecursive(var1.se, var2, var3);
      }

   }

   private int[] applyKernel(int var1, int var2) {
      int[][] var3 = new int[][]{{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;

      for(int var7 = -1; var7 <= 1; ++var7) {
         for(int var8 = -1; var8 <= 1; ++var8) {
            int var9 = var1 + var8;
            int var10 = var2 + var7;
            if (var9 < 0) {
               var9 = 0;
            }

            if (var9 >= this.originalImage.getWidth()) {
               var9 = this.originalImage.getWidth() - 1;
            }

            if (var10 < 0) {
               var10 = 0;
            }

            if (var10 >= this.originalImage.getHeight()) {
               var10 = this.originalImage.getHeight() - 1;
            }

            int[] var11 = this.originalImage.getPixel(var10, var9);
            int var12 = var3[var7 + 1][var8 + 1];
            var4 += var11[0] * var12;
            var5 += var11[1] * var12;
            var6 += var11[2] * var12;
         }
      }

      return new int[]{this.clamp(var4), this.clamp(var5), this.clamp(var6)};
   }

   private int clamp(int var1) {
      if (var1 < 0) {
         return 0;
      } else {
         return var1 > 255 ? 255 : var1;
      }
   }

   private class Node {
      int x;
      int y;
      int size;
      int[] color;
      Node nw;
      Node ne;
      Node sw;
      Node se;
      boolean isLeaf;

      Node(final Quadtree var1, int var2, int var3, int var4, double var5) {
         this.x = var2;
         this.y = var3;
         this.size = var4;
         this.isLeaf = false;
         this.color = var1.calculateMeanColor(var2, var3, var4);
         if (var4 != 1 && !(var1.calculateMeanSquaredError(var2, var3, var4, this.color) <= var5)) {
            int var7 = var4 / 2;
            this.nw = var1.new Node(var1, var2, var3, var7, var5);
            this.ne = var1.new Node(var1, var2 + var7, var3, var7, var5);
            this.sw = var1.new Node(var1, var2, var3 + var7, var7, var5);
            this.se = var1.new Node(var1, var2 + var7, var3 + var7, var7, var5);
         } else {
            this.isLeaf = true;
         }
      }
   }
}
