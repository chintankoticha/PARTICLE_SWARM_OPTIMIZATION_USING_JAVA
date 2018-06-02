/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package try1;

import java.applet.Applet;
import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.CYAN;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.ORANGE;
import static java.awt.Color.PINK;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ayush
 */
public class bouncingballs extends Applet implements Runnable {

    List<Ball> ballList = new ArrayList(); // holds Ball objects

    Color[] colors = new Color[]{BLACK, GRAY, WHITE, PINK, RED, ORANGE, YELLOW,
            GREEN, BLUE, CYAN}; // array holding available colors

    static int width, height; // variables for applet dimensions

    int ballCount; // number of balls to be created, set by html parameter

    Random random = new Random(); // random number generator


    public void init() {

        // get window dimensions
        width = 800;
        height = 800;

        //get number of balls from html
        String ballCountString = this.getParameter("ballCount");

        try {
            ballCount = Integer.parseInt(ballCountString);
        } catch (NumberFormatException e) {
            ballCount = 10; // set to 10 by default
        }

        for (int i = 0; i < ballCount; i++) {

            // randomly assign ballDiameter between 1 and 20
            int ballDiameter = random.nextInt(80) + 1;

            // create and add balls to ballList
            ballList.add(new Ball(
                    random.nextInt(width - ballDiameter), // set x coordinate
                    random.nextInt(height - ballDiameter), // set y coordinate
                    ballDiameter, // set ballDiameter
                    random.nextInt(ballDiameter) + 1, // deltaX <= ballDiameter
                    random.nextInt(ballDiameter) + 1, // deltaY <= ballDiameter
                    colors[i % 10] // use remainder to choose colors[] element
                    )
            );

        } // end for

    } // end init


    public void start() {

        for (Ball ball: ballList) {

            Thread t;
            t = new Thread(this);
            t.start();

        } // end for

    } // end start


    public void run() {

        while (true) {
        for (Ball ball : ballList) {

            // infinite loop: ball moves until applet is closed
            

                ball.move();

                repaint(); // call paint method to draw circle in new location

                // set ball repaint delay using Thread sleep method
                try {
                    Thread.sleep(20); // wait 20 msec before continuing
                } catch (InterruptedException e) {
                    return;
                }

            } // end while

        } // end for

    } // end run

    public void paint(Graphics g) {

        super.paint(g);

        for (Ball ball : ballList) {

            // set current color
            g.setColor(ball.ballColor);

            // draw filled oval using current x and y coordinates and diameter
            g.fillOval(ball.x, ball.y, ball.diameter, ball.diameter);

        } // end for

    } // end paint
}

class Ball {

    int x, y, // coordinates of upper-left corner of circle
        diameter, // circle diameter
        deltaX, deltaY; // number of pixels ball moves each time it's repainted
    Color ballColor;


    public Ball(int x, int y, int diameter, int deltaX, int deltaY,
                Color ballColor) {

        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.ballColor = ballColor;

    } // end Ball constructor


    public void move() {

        // update x and y coordinates using delta values
        x += deltaX;
        y += deltaY;

        // reverse x direction when ball reaches boundary
        if (x >= bouncingballs.width - diameter || x <= 0){
            deltaX = -deltaX;
        } // end if

        // reverse y direction when ball reaches boundary
        if (y >= bouncingballs.height - diameter || y <= 0) {
            deltaY = -deltaY;
        } // end if

    } // end move

} // end BouncingBalls
