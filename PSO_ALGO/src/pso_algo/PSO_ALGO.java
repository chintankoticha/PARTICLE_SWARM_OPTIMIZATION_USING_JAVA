package pso_algo;

import java.applet.Applet;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PSO_ALGO extends Applet implements Runnable {

    //INITIAL CONSTRAINTS
    static int swarmSize = 50;
    int iterations = 100;
    int unknowns = 2;
    double c1 = 2.0;
    double c2 = 2.0;
    double wup = 1.0;
    double wlb = 0.0;

    //INITIALIZATION OF POSITION ARRAYS
    static final double posXLow = 0;
    static final double posXHigh = 4;
    static final double posYLow = 0;
    static final double posYHigh = -4;
    static final double velLow = -1;
    static final double velHigh = 1;

    //PSO PROCESS INIT
    private List<Particle> pl = new ArrayList<Particle>();
    private double[] pBest = new double[swarmSize];
    private List<double[]> pBestPositions = new ArrayList<double[]>();
    private double gBest;
    private static double[] gBestPosition;
    private double[] costList = new double[swarmSize];
    private List<Double> bestListGraph = new ArrayList<Double>();
    static boolean flag = true;
    private Set<Double> set = new LinkedHashSet<Double>();

    static int width, height; // variables for applet dimensions

    Random random = new Random(); // random number generator
    Random generator = new Random();

    public void init() {
        initializeSwarm();  //Initialize initial positions and velocities of all particles and add to particle list
        updateCostList();   //Calculate Initial Costs of Particles
        width = 900;
        height = 900;
    }

    public void start() {
        for (Particle p : pl) {
            Thread t;
            t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        for (int i = 0; i < swarmSize; i++) {
            pBest[i] = costList[i];
            try{
                pBestPositions.add(pl.get(i).getPosition());
            }catch(Exception e){
                System.out.println("-->\t"+e.getMessage());
            }
        }

        int t = 0;
        double w;       //INERTIA COEFFICIENT
        boolean flag = true;

        while (t < iterations) {
            // UPDATE PARTICLE'S BEST
            for (int i = 0; i < swarmSize; i++) {
                if (costList[i] < pBest[i]) {
                    pBest[i] = costList[i];
                    pBestPositions.set(i, pl.get(i).getPosition());
                }
            }

            // UPDATE GLOBAL BEST
            int bestParticleTemp = getMinPos(costList);
            if (t == 0 || costList[bestParticleTemp] < gBest) //FOR THE FIRST TIME OF THREAD AND IF PBEST GOES LESS THAN GBEST
            {
                gBest = costList[bestParticleTemp];
                gBestPosition = pl.get(bestParticleTemp).getPosition();
            }

            // UPDATE INERTIA COEFFICIENT EVERY INSTANCE
            w = wup - (((double) t) / iterations) * (wup - wlb);

            for (int i = 0; i < swarmSize; i++) {
                double r1 = generator.nextDouble();
                double r2 = generator.nextDouble();

                Particle p = pl.get(i);

                // UPDATE VELOCITY BASED ON LOCATION OF GLOBAL BEST AND PERSONAL BEST LOCATION
                double[] velocity1 = new double[unknowns];
                velocity1[0] = (w * p.getVelocity()[0])
                        + (r1 * c1) * (pBestPositions.get(i)[0] - p.getPosition()[0])
                        + (r2 * c2) * (gBestPosition[0] - p.getPosition()[0]);
                velocity1[1] = (w * p.getVelocity()[1])
                        + (r1 * c1) * (pBestPositions.get(i)[1] - p.getPosition()[1])
                        + (r2 * c2) * (gBestPosition[1] - p.getPosition()[1]);

                // ASSIGN NEW VELOCITY TO PARTICLES
                double[] vel = velocity1;
                p.setVelocity(vel);

                // UPDATE LOCATION BASED ON NEW VELOCITY
                double[] position1 = new double[swarmSize];
                position1[0] = p.getPosition()[0] + velocity1[0];
                position1[1] = p.getPosition()[1] + velocity1[1];

                // ASSIGN NEW POSITION TO PARTICLES
                double[] pos = position1;
                p.setPosition(position1);
            }

            repaint(); // call paint method to draw new positions

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                return;
            }
            
            
            System.out.println("ITERATION " + t + ": ");
            System.out.println("     Best X: " + gBestPosition[0]);
            System.out.println("     Best Y: " + gBestPosition[1]);
            System.out.println("     Value: " + CostFunction.evaluate(gBestPosition));
            
            try{
                Double dObj = new Double(CostFunction.evaluate(gBestPosition));
                bestListGraph.add(dObj);
            }catch(Exception e){
                
            }

            t++;
            updateCostList();
        }
        flag = false;
        System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
        System.out.println("Best X:\t" + gBestPosition[0]);
        System.out.println("Best Y:\t" + gBestPosition[1]);

        if (!flag) {
            try{
                convertToLHS(bestListGraph);
            }catch(Exception e){
                System.out.println("-->\t"+e.getMessage());
            }
            System.out.println("--------------PARTICLE BEST FOR EACH ITERATION---------------");
            System.out.println("PRINTING SET ELEMENTS!!");
            for (Double d : set) {
                System.out.println(d);
            }

            try {
                PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\kotic\\Desktop\\CODE OUTPUT\\SET_OUTPUT.txt"));
                for (Double d : set) {
                    if (d != null) {
                        out.println(d);
                    }
                }
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        resize(2048, 1024);
        setBackground(BLACK);
        super.paint(g);
        try {
            int gbestx = (int) gBestPosition[0];
            int gbesty = (int) gBestPosition[1];
            g.setColor(YELLOW);
            g.fillOval(gbestx, gbesty, 10, 10);
            g.setColor(GREEN);
            g.fillRect(900, 500, 20, 20);
            int i = 0;
            for (Particle p : pl) {
                double loc[] = p.getPosition();
                Long lx = Math.round(loc[0]);
                int x = lx.intValue();
                Long ly = Math.round(loc[1]);
                int y = ly.intValue();
                g.setColor(WHITE);
                //int pbestx = (int) pBestLocation.get(i).getLoc()[0];
                //int pbesty = (int) pBestLocation.get(i).getLoc()[1];
                //g.drawLine(pbestx, pbesty, gbestx, gbesty);
                //g.setColor(RED);
                //g.drawLine(x, y, pbestx, pbesty);
                //g.setColor(PINK);
                //g.drawLine(x, y, gbestx, gbesty);
                g.setColor(BLUE);
                g.fillOval(Math.abs(x), Math.abs(y), 20, 20);
                i++;
            }
        } catch (Exception e) {
            System.out.println("GLOBAL BEST WOULD NOT HAVE BEEN SET!!\t" + e.getMessage());
        }
    }

    public static int getMinPos(double[] list) {
        int tempindex = 0;
        double minValue = list[0];

        for (int i = 0; i < list.length; i++) {
            if (list[i] < minValue) {
                tempindex = i;
                minValue = list[i];
            }
        }
        return tempindex;
    }

    public void initializeSwarm() {
        Particle p;
        for (int i = 0; i < swarmSize; i++) {
            p = new Particle();

            //ALLOCATE RANDOM POSITIONS TO PARTICLES IN A DEFINED SPACE SET
            double[] pos = new double[unknowns];
            pos[0] = posXLow + generator.nextDouble() * (posXHigh - posXLow);
            pos[1] = posYLow + generator.nextDouble() * (posYHigh - posYLow);
            double[] position = pos;

            //ALLOCATE RANDOM VELOCITIES TO PARTICLES IN A DEFINED SPACE SET
            double[] vel = new double[unknowns];
            vel[0] = velLow + generator.nextDouble() * (velHigh - velLow);
            vel[1] = velLow + generator.nextDouble() * (velHigh - velLow);
            double[] velocity = vel;

            //ASSIGN RANDOM POSITION AND VELOCITIES TO PARTICLES AND ADD TO PARTICLE LIST
            p.setPosition(position);
            p.setVelocity(velocity);
            pl.add(p);
        }
    }

    public void updateCostList() {
        for (int i = 0; i < swarmSize; i++) {
            costList[i] = pl.get(i).getCost();
        }
    }

    private void convertToLHS(List<Double> bestListGraph) {
        set = new LinkedHashSet<Double>(bestListGraph);
    }
}
