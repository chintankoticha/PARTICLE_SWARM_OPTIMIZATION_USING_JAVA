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
import static java.awt.Color.GREEN;
import static java.awt.Color.PINK;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author ayush
 */
public class PSOProcess extends Applet implements PSOConstants, Runnable {

    private Vector<Particle> swarm = new Vector<Particle>();
    private double[] pBest = new double[SWARM_SIZE];
    private Vector<Location> pBestLocation = new Vector<Location>();
    private double gBest;
    private Location gBestLocation;
    private double[] fitnessValueList = new double[SWARM_SIZE];

    Color[] colors = new Color[]{BLUE}; // array holding available colors

    static int width, height; // variables for applet dimensions

    int ballCount; // number of balls to be created, set by html parameter

    Random random = new Random(); // random number generator
    Random generator = new Random();

    public void init() {
        initializeSwarm();
        updateFitnessList();
        start();
        width = 900;
        height = 900;
    }

    public void start() {

        for (Particle p : swarm) {

            Thread t;
            t = new Thread(this);
            t.start();

        } // end for

    }

    public void run() {

        for (int i = 0; i < SWARM_SIZE; i++) {
            pBest[i] = fitnessValueList[i];
            pBestLocation.add(swarm.get(i).getLocation());
        }

        int t = 0;
        double w;
        double err = 9999;

        while (t < MAX_ITERATION && err > 0) {
            // step 1 - update pBest
            for (int i = 0; i < SWARM_SIZE; i++) {
                if (fitnessValueList[i] < pBest[i]) {
                    pBest[i] = fitnessValueList[i];
                    pBestLocation.set(i, swarm.get(i).getLocation());
                }
            }

            // step 2 - update gBest
            int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
            if (t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
                gBest = fitnessValueList[bestParticleIndex];
                gBestLocation = swarm.get(bestParticleIndex).getLocation();
            }

            w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);

            for (int i = 0; i < SWARM_SIZE; i++) {
                double r1 = generator.nextDouble();
                double r2 = generator.nextDouble();

                Particle p = swarm.get(i);

                // step 3 - update velocity
                double[] newVel = new double[PROBLEM_DIMENSION];
                newVel[0] = (w * p.getVelocity().getPos()[0])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[0] - p.getLocation().getLoc()[0])
                        + (r2 * C2) * (gBestLocation.getLoc()[0] - p.getLocation().getLoc()[0]);
                newVel[1] = (w * p.getVelocity().getPos()[1])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1])
                        + (r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);
                Velocity vel = new Velocity(newVel);
                p.setVelocity(vel);

                // step 4 - update location
                double[] newLoc = new double[PROBLEM_DIMENSION];
                newLoc[0] = p.getLocation().getLoc()[0] + newVel[0];
                newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];
                Location loc = new Location(newLoc);
                p.setLocation(loc);
            }

            err = ProblemSet.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0

            repaint(); // call paint method to draw circle in new location

            // set ball repaint delay using Thread sleep method
            try {
                Thread.sleep(600); // wait 20 msec before continuing
            } catch (InterruptedException e) {
                return;
            }

            System.out.println("ITERATION " + t + ": ");
            System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
            System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
            System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));

            t++;
            updateFitnessList();
        }

        System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
        System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
        System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
    }

    public void paint(Graphics g) {
        resize(2048, 1024);
        setBackground(BLACK);
        super.paint(g);
        int gbestx = (int) gBestLocation.getLoc()[0];
        int gbesty = (int) gBestLocation.getLoc()[1];
        g.setColor(YELLOW);
        g.fillOval(gbestx, gbesty, 10, 10);
        g.setColor(GREEN);
        g.fillRect(1200, 400, 40, 20);
        int i = 0;
        for (Particle p : swarm) {

            // set current color
            // draw filled oval using current x and y coordinates and diameter
            double loc[] = p.getLocation().getLoc();
            Long lx = Math.round(loc[0]);
            int x = Integer.valueOf(lx.intValue());
            Long ly = Math.round(loc[1]);
            int y = Integer.valueOf(ly.intValue());
            g.setColor(WHITE);
            int pbestx = (int) pBestLocation.get(i).getLoc()[0];
            int pbesty = (int) pBestLocation.get(i).getLoc()[1];
            g.drawLine(pbestx, pbesty, gbestx, gbesty);
            g.setColor(RED);
            g.drawLine(x, y, pbestx, pbesty);
            g.setColor(PINK);
            g.drawLine(x, y, gbestx, gbesty);
            g.setColor(BLUE);
            g.fillOval(x, y, 20, 20);
            i++;

        }
    }

    public void initializeSwarm() {
        Particle p;
        for (int i = 0; i < SWARM_SIZE; i++) {
            p = new Particle();

            // randomize location inside a space defined in Problem Set
            double[] loc = new double[PROBLEM_DIMENSION];
            loc[0] = ProblemSet.LOC_X_LOW + generator.nextDouble() * (ProblemSet.LOC_X_HIGH - ProblemSet.LOC_X_LOW);
            loc[1] = ProblemSet.LOC_Y_LOW + generator.nextDouble() * (ProblemSet.LOC_Y_HIGH - ProblemSet.LOC_Y_LOW);
            Location location = new Location(loc);

            // randomize velocity in the range defined in Problem Set
            double[] vel = new double[PROBLEM_DIMENSION];
            vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
            vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
            Velocity velocity = new Velocity(vel);

            p.setLocation(location);
            p.setVelocity(velocity);
            swarm.add(p);
        }
    }

    public void updateFitnessList() {
        for (int i = 0; i < SWARM_SIZE; i++) {
            fitnessValueList[i] = swarm.get(i).getFitnessValue();
        }
    }
}
