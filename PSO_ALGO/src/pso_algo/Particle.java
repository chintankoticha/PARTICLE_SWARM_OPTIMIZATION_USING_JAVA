package pso_algo;

public class Particle {

    private double[] position;
    private double[] velocity;
    private double cost;

    public Particle(double[] position, double[] velocity, double cost) {
        this.position = position;
        this.velocity = velocity;
        this.cost = cost;
    }

    public Particle() {
        super();
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double getCost() {
        cost = CostFunction.evaluate(position);
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
