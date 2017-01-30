/*
 * @copyright 2016 Vaios
 */
package mypackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import mypackage.graphics.Screen;

public class Game extends Canvas implements Runnable {   //Canvas is like a blank square we can draw in
    
    public static int width = 300;
    public static int height = 168;
    public static int scale = 3;
    public static String title = "Rain";
    
    private Thread thread;                               //we already have one thread that starts the program,
                                                         //we make another thread to run the game itself
    private boolean running = false;
    private Screen screen;
    private JFrame frame;                                //our window   
    
    private BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB); //final rended view-an image with a buffer
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); //array of pixels with buffer
    
    public Game(){                                       //everything here executed once   
        Dimension size = new Dimension(width*scale,height*scale); //size of our game
        setPreferredSize(size);  
        screen = new Screen(width,height);
        frame = new JFrame();
    }
    
    public synchronized void start(){                    //preventing thread inteferences
        running = true;
        thread = new Thread(this,"Display");             //this thread contains this Game class
        thread.start();
        
    }
    
    public synchronized void stop() {  
        running = false;                                 //Java needs a way to stop in an applet
        try{                                             //we need to shut down our game properly
            thread.join();                               //especially in browser.
        } catch (InterruptedException e){                //Thrown when a thread is waiting and the 
            e.printStackTrace();                         //thread is interrupted before or after  
        }  
                                                         //A stack trace is a list of method calls 
                                                         //the application was doing.
    }                                                    //when the exception was thrown 
    
    public void run() {
        long lastTime = System.nanoTime();               //current system time
        long timer = System.currentTimeMillis();
        final double nanoseconds = 1000000000.0 / 60.0;  
        double delta = 0;
        int frames = 0;
        int updates = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nanoseconds;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;
            
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                //(updates + "ups," + frames + "fps");
                frame.setTitle(title + "  |  " + updates + " ups," + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }
    
    public void update() { //logic calculations
        
    }
    
    public void render() {                               
        
        BufferStrategy bs = getBufferStrategy();         
        
        if (bs == null) {
            createBufferStrategy(3);                     
            return;                                      
        }
        
        screen.clear();
        screen.render();
        
        for ( int i = 0; i < pixels.length; i++){
            pixels[i] = screen.pixels[i];
        }
        
        Graphics g = bs.getDrawGraphics(); //creates a link between g and our buffer
        //RENDER HERE                   
        g.drawImage(image, 0, 0, getWidth(),getHeight(),null);
        //END RENDER
        g.dispose();    //releases resources after rendering everything                                 
        bs.show();      //shows the buffer
        
    }
    
    
    
       
    public static void main(String[] args){              
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle(Game.title);
        game.frame.add(game);                             //adding an instance of game to the window
        game.frame.pack();                                //size up the frame to be the same size as our game(dimension)  
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        
        game.start();                                     //STARTS THE THREAD-OUR GAME
    }
    
    
}
