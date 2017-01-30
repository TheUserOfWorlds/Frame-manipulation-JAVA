/*
 * @copyright 2016 Vaios
 */
package mypackage.graphics;

/**
 *
 * @author Alpha
 */
public class Screen {
    
    private int width,height;
    public int[] pixels;
    int xtime = 0;
    int ytime = 0;
    int counter = 0;
    
    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width*height]; //the number of pixels
    }
    
    public void clear() {
        for ( int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
    
    public void render(){
        counter++;
        if (counter % 10 == 0) xtime++;
        if (counter % 10 == 0) ytime++;
                
        for (int y = 0; y < height; y++){
            if (ytime < 0 || ytime >= height) break;
            for(int x = 0; x < width; x++){
                if (xtime < 0 || x >= width) break;
                pixels[xtime + ytime*width] = 0xff00ff; //its one dimensional, so we make our own coordinates using math
            }                                         //we put 0x before hexadecimal
        }
    }
    
}
