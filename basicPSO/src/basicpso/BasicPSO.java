/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicpso;

import java.applet.Applet;
import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import java.awt.Graphics;
import java.util.Random;
import java.util.Arrays;
import java.util.stream.DoubleStream;

/**
 *
 * @author kotic
 */
public class BasicPSO extends Applet implements Runnable {

    int swarmSize;    //Swarm Size (Population in swarm)        
//    double w;         //Inertia Coefficient(Weight)
//    double wdamp;    //Damping Inertia Coefficient(Weight)
    double c1;         //Personal Acceleration Coefficient
    double c2;         //Social Acceleration Coefficient
    double[] BestCosts;

    static int noOfIterations = 1000;   //Number of iterations
    static int noOfVariables = 2;      //Number of unknown(decision) variables

    static double[] VarSize = new double[noOfVariables]; //Array of decision variables
    static int VarMin = -100;                       //Lower Bound of Decision variables
    static int VarMax = 100;                        //Upper Bound of Decision variables

    static BestParticle bp = new BestParticle();
    public static Sphere s = new Sphere();
    public static GlobalBest gb = new GlobalBest();
    static ParticleList pl = new ParticleList();
    ///////////////////////////////
    Color[] colors = new Color[]{BLUE}; // array holding available colors

    static int width, height; // variables for applet dimensions

    int particleCount; // number of balls to be created, set by html parameter

    //////////////////////////////
    /**
     * @param args the command line arguments
     */
    public void init() {
        initializeSwarm();
        start();
        width = 5;
        height = 5;
    }

    public void start() {
        for (Particle p : pl.getParticleList()) {
            Thread t;
            t = new Thread(this);
            t.start();
        } // end for
    }

    public void initializeSwarm() {
        swarmSize = 50;    //Swarm Size (Population in swarm)        
        c1 = 2;            //Personal Acceleration Coefficient
        c2 = 2;            //Social Acceleration Coefficient

        //Global Best is infinity since it has to be minimised
        gb.setCost(Double.POSITIVE_INFINITY);

        for (int i = 0; i < swarmSize; i++) {
            Particle p = new Particle(bp);
            double[] position = new double[noOfVariables];
            double[] velocity = new double[noOfVariables];
            p.setPosition(position);
            p.setVelocity(velocity);
            p.getBest().setPostion(position);
            pl.getParticleList().add(p);
        }

        int count = 0;
        for (Particle p : pl.getParticleList()) {
            double[] arr = p.getPosition();
            count++;
        }

        System.out.println("No of particles:" + count);

        //Initial Velocity
        double[] initialVelocity = {0, 0};

        for (int i = 1; i < swarmSize; i++) {
            //Generate Random Position
            double[] pstn = doubles(noOfVariables, VarMin, VarMax);

            //Assign randomg position to particle
            pl.getParticleList().get(i).setPosition(pstn);

            //Evaluate Cost
            pl.getParticleList().get(i).setCost(s.cost(pl.getParticleList().get(i)));

            //Initialize Velocity
            pl.getParticleList().get(i).setVelocity(initialVelocity);

            //Update the Initial Personal Best
            bp.setPostion(pl.getParticleList().get(i).getPosition());
            bp.setCost(pl.getParticleList().get(i).getCost());
            pl.getParticleList().get(i).setBest(bp);

            //Update Global Best
            if (pl.getParticleList().get(i).getCost() < gb.getCost()) {
                gb.setCost(pl.getParticleList().get(i).getCost());
                gb.setPosition(pl.getParticleList().get(i).getPosition());      //Check if this is needed later on
            }
        }

        //Printing Random Position
        for (Particle p : pl.getParticleList()) {
            double[] arr = p.getPosition();
            System.out.println("");
            for (int i = 0; i < arr.length; i++) {
                System.out.print("Particle X:\t" + arr[i]);
                System.out.print("Particle Y:\t");
            }
        }

        //Printing Cost Functon of Each Particle
        for (Particle p : pl.getParticleList()) {
            double cost = p.getCost();
            System.out.println("Current Particle's Cost:\t" + cost);
        }

        //Printing current global best
        System.out.println("Current Global Best:\t" + gb.getCost());

        //Creating an array to hold best costs of all iterations
        BestCosts = new double[noOfIterations];
        Arrays.fill(BestCosts, 0);
    }

    public void run() {
        ////////////////////////////MAIN LOOP OF PSO////////////////////////////
        for (int i = 0; i < noOfIterations; i++) {

            for (int j = 0; j < swarmSize; j++) {
                double w = 1;             //Inertia Coefficient(Weight)
                double wdamp = 0.99;      //Damping Inertia Coefficient(Weight)

                //Update Velocity of particle in Global Best Direction
                double[] vel = updateParticleVelocity(j, pl, w, c1, c2, gb, VarSize, swarmSize);
                pl.getParticleList().get(j).setVelocity(vel);

                //Update Position of particle in Global Best Direction
                double[] pos = updateParticlePosition(j, pl);
                pl.getParticleList().get(j).setPosition(pos);

                //Evaluate Cost for the particles
                pl.getParticleList().get(j).setCost(s.cost(pl.getParticleList().get(j)));

                //Update Personal Best 
                if (pl.getParticleList().get(j).getCost() < pl.getParticleList().get(j).getBest().getCost()) {
                    pl.getParticleList().get(j).getBest().setPostion(pl.getParticleList().get(j).getPosition());
                    pl.getParticleList().get(j).getBest().setCost(pl.getParticleList().get(j).getCost());

                    //Update Global Best
                    if (pl.getParticleList().get(j).getCost() < gb.getCost()) {
                        gb.setCost(pl.getParticleList().get(j).getCost());
                        gb.setPosition(pl.getParticleList().get(j).getPosition());   //Check if this is needed later on
                    }
                }

                if (i % 5 == 0) {
                    //Update Interia Coefficient
                    w = w * wdamp;
                }
            }

            BestCosts[i] = gb.getCost();

            // set ball repaint delay using Thread sleep method
            try {
                Thread.sleep(600); // wait 600 msec before continuing
            } catch (InterruptedException e) {
                return;
            }

            repaint(); // call paint method to draw circle in new location
            System.out.println("Iteration:\t" + i + "\tCost:\t" + BestCosts[i]);
        }
    }

    public static double[] doubles(int noOfVariables, int VarMin, int VarMax) {
        Random r = new Random();
        return r.doubles(noOfVariables, VarMin, VarMax).toArray();
    }

    public static double[] updateParticleVelocity(int particlenum, ParticleList pl, double w, double c1, double c2, GlobalBest gb, double[] VarSize, int swarmsize) {
        double[] vel = new double[noOfVariables];
        Random generator = new Random();
        double[] sub1 = new double[noOfVariables];
        double[] sub2 = new double[noOfVariables];
        Arrays.setAll(VarSize, x -> {
            return generator.nextDouble();
        });
        double[] result2 = DoubleStream.of(VarSize).map(d1 -> d1 * c1).toArray();
        double[] result3 = DoubleStream.of(VarSize).map(d1 -> d1 * c2).toArray();
        double[] result1 = DoubleStream.of(pl.getParticleList().get(particlenum).getVelocity()).map(d -> d * w).toArray();

        for (int j = 0; j < noOfVariables; j++) {
            sub1[j] = ((pl.getParticleList().get(particlenum).getBest().getPostion())[j]
                    - (pl.getParticleList().get(particlenum).getPosition())[j]);
            sub2[j] = (gb.getPosition())[j] - (pl.getParticleList().get(j).getPosition())[j];
        }

        for (int j = 0; j < noOfVariables; j++) {
            result2[j] = result2[j] * sub1[j];
            result3[j] = result3[j] * sub2[j];
        }

        for (int j = 0; j < noOfVariables; j++) {
            vel[j] = result1[j] + result2[j] + result3[j];
        }

        return vel;
    }

    private static double[] updateParticlePosition(int particlenum, ParticleList pl) {
        double[] pos = new double[noOfVariables];
        for (int i = 0; i < noOfVariables; i++) {
            pos[i] = (pl.getParticleList().get(particlenum).getPosition())[i] + (pl.getParticleList().get(particlenum).getVelocity())[i];
        }
        return pos;
    }

    public void paint(Graphics g) {
        resize(1000, 1000);
        setBackground(BLACK);
        super.paint(g);
        int gbestx = (int) gb.getPosition()[0];
        int gbesty = (int) gb.getPosition()[1];
        g.setColor(YELLOW);
        g.fillOval(gbestx, gbesty, 10, 10);
        g.setColor(GREEN);
        g.fillRect(0, 0, 30, 30);
        int i = 0;
        for (Particle p : pl.getParticleList()) {

            // set current color
            // draw filled oval using current x and y coordinates and diameter
            double[] loc = p.getPosition();

            int x = Integer.valueOf((int) Math.round(loc[0]));
            int y = Integer.valueOf((int) Math.round(loc[0]));
            System.out.println("X Coords:\t" + x);
            System.out.println("Y Coords:\t" + y);
            g.setColor(WHITE);
//            int pbestx = (int) pBestLocation.get(i).getLoc()[0];
//            int pbesty = (int) pBestLocation.get(i).getLoc()[1];
//            g.drawLine(pbestx, pbesty, gbestx, gbesty);
//            g.setColor(RED);
//            g.drawLine(x, y, pbestx, pbesty);
//            g.setColor(PINK);
//            g.drawLine(x, y, gbestx, gbesty);
//            g.setColor(BLUE);
            g.fillOval(x, y, 20, 20);
            i++;

        }
    }

}
