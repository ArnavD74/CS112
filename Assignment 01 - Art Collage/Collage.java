package art;

import java.awt.Color;

/*
 * This class contains methods to create and perform operations on a collage of images.
 * 
 * @author Ana Paula Centeno
 */

public class Collage {

    // The orginal picture
    private Picture originalPicture;

    // The collage picture is made up of tiles.
    // Each tile consists of tileDimension X tileDimension pixels
    // The collage picture has collageDimension X collageDimension tiles
    private Picture collagePicture;

    // The collagePicture is made up of collageDimension X collageDimension tiles
    // Imagine a collagePicture as a 2D array of tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    // Imagine a tile as a 2D array of pixels
    // A pixel has three components (red, green, and blue) that define the color 
    // of the pixel on the screen.
    private int tileDimension;

    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 150
     * 2. initializes originalPicture with the filename image
     * 3. initializes collagePicture as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see constructors for the Picture class).
     * 4. update collagePicture to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public Collage(String filename) {
        collageDimension = 4;
        tileDimension = 150;
        originalPicture = new Picture(filename);
        collagePicture = new Picture(600, 600);
        scale(originalPicture, collagePicture);
    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes originalPicture with the filename image
     * 3. initializes collagePicture as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collagePicture to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public Collage(String filename, int td, int cd) {
        collageDimension = cd;
        tileDimension = td;
        originalPicture = new Picture(filename);
        collagePicture = new Picture(tileDimension * collageDimension, tileDimension * collageDimension);
        scale(originalPicture, collagePicture);
    }


    /*
     * Scales the Picture @source into Picture @target size.
     * In another words it changes the size of @source to make it fit into
     * @target. Do not update @source. 
     *  
     * @param source is the image to be scaled.
     * @param target is the 
     */
    public static void scale(Picture source, Picture target) {
        int width = target.width();
        int height = target.height();
        for (int targetCol = 0; targetCol < target.width(); targetCol++) {
            for (int targetRow = 0; targetRow < target.height(); targetRow++) {
                int sourceCol = targetCol * source.width() / width;
                int sourceRow = targetRow * source.height() / height;
                Color color = source.get(sourceCol, sourceRow);
                target.set(targetCol, targetRow, color);
            }
        }
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {
        return collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {
        return tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */

    public Picture getOriginalPicture() {
        return originalPicture;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */

    public Picture getCollagePicture() {
        return collagePicture;
    }

    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {
        originalPicture.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {
        collagePicture.show();
    }

    /*
     * Updates collagePicture to be a collage of tiles from original Picture.
     * collagePicture will have collageDimension x collageDimension tiles, 
     * where each tile has tileDimension X tileDimension pixels.
     */
    // public void makeCollage() {

    //     collagePicture = new Picture(tileDimension * collageDimension, tileDimension * collageDimension);

    //     for (int i = 0; i < (tileDimension * collageDimension-1); i++) {
    //         for (int j = 0; j < (tileDimension * collageDimension-1); j++) {
    //             int col = i * originalPicture.width() / (tileDimension * collageDimension);
    //             int row = j * originalPicture.height() / (tileDimension * collageDimension);
    //             Color color = originalPicture.get(col, row);
    //             originalPicture.set(i, j, color);
    //         }
    //     }
    // }

    public void makeCollage() {

        Picture newCollage = new Picture(tileDimension, tileDimension);
        scale(originalPicture, newCollage);

        int width = 0;
        int height = 0;
        int c = 0;

        while (height < collageDimension * tileDimension) {
            while (width < collageDimension * tileDimension) {
                for (int i = 0; i < tileDimension; i++) {
                    for (int j = 0; j < tileDimension; j++) {
                        collagePicture.set(width, height, newCollage.get(i, j));
                        height++;
                    }
                    height = c * tileDimension;
                    width++;
                }
            }
            width = 0;
            c++;
            height = c * tileDimension;
        }
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile(String component, int collageCol, int collageRow) {

        int r = 0;
        int g = 0;
        int b = 0;

        int ncol = collageCol * tileDimension;
        int nrow = collageRow * tileDimension;

        for (int i = ncol; i < (ncol + tileDimension); i++) {
            for (int j = nrow; j < (nrow + tileDimension); j++) {
                Color color = collagePicture.get(i, j);
                if (component == "red")
                    r = color.getRed();
                else if (component == "green")
                    g = color.getGreen();
                else if (component == "blue")
                    b = color.getBlue();

                collagePicture.set(i, j, new Color(r, g, b));
            }
        }
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile(String filename, int collageCol, int collageRow) {

        int ncol = collageCol * tileDimension;
        int nrow = collageRow * tileDimension;

        Picture temp = new Picture(filename);
        Picture scaled = new Picture(tileDimension, tileDimension);

        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {

                int col = i * temp.width() / tileDimension;
                int row = j * temp.height() / tileDimension;
                Color color = temp.get(col, row);
                scaled.set(i, j, color);
            }
        }

        for (int i = 0; i < tileDimension; i++) {
            nrow = collageRow * tileDimension;
            for (int j = 0; j < tileDimension; j++) {
                Color color = scaled.get(i, j);
                collagePicture.set(ncol, nrow, color);
                nrow++;
            }
            ncol++;
        }

    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void grayscaleTile(int collageCol, int collageRow) {

        // int r = 0;
        // int g = 0;
        // int b = 0;
        // int average = 0;

        // int ncol = (collageCol - 1) * tileDimension;
        // int nrow = (collageRow - 1) * tileDimension;

        // for (int i = ncol; i < (ncol + tileDimension); i++) {
        //     for (int j = nrow; j < (nrow + tileDimension); j++) {
        //         Color color = collagePicture.get(i, j);
        //         r = color.getRed();
        //         g = color.getGreen();
        //         b = color.getBlue();
        //         average = (r + g + b) / 3;
        //         collagePicture.set(i, j, new Color(average, average, average));
        //     }
        // }

        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {
                collagePicture.set((collageCol * tileDimension) + i, (collageRow * tileDimension) + j, toGray(collagePicture.get((collageCol * tileDimension) + i, (collageRow * tileDimension) + j)));
            }
        }
    }

    /**
     * Returns the monochrome luminance of the given color as an intensity
     * between 0.0 and 255.0 using the NTSC formula
     * Y = 0.299*r + 0.587*g + 0.114*b. If the given color is a shade of gray
     * (r = g = b), this method is guaranteed to return the exact grayscale
     * value (an integer with no floating-point roundoff error).
     *
     * @param color the color to convert
     * @return the monochrome luminance (between 0.0 and 255.0)
     */
    private static double intensity(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if (r == g && r == b) return r; // to avoid floating-point issues
        return 0.299 * r + 0.587 * g + 0.114 * b;
    }

    /**
     * Returns a grayscale version of the given color as a {@code Color} object.
     *
     * @param color the {@code Color} object to convert to grayscale
     * @return a grayscale version of {@code color}
     */
    private static Color toGray(Color color) {
        int y = (int)(Math.round(intensity(color))); // round to nearest int
        Color gray = new Color(y, y, y);
        return gray;
    }

    /*
     * Closes the image windows
     */
    public void closeWindow() {
        if (originalPicture != null) {
            originalPicture.closeWindow();
        }
        if (collagePicture != null) {
            collagePicture.closeWindow();
        }
    }
}