/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicpso;

/**
 *
 * @author kotic
 */
public class Particle {
    private BestParticle best;

    public Particle(BestParticle bp) {
        this.best = bp;
    }
    
    public void Particle(){
        best = new BestParticle();
    }
    
    private double[] position;
    private double[] velocity;
    
    double cost;

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
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public BestParticle getBest() {
        return best;
    }

    public void setBest(BestParticle best) {
        this.best = best;
    }    
}