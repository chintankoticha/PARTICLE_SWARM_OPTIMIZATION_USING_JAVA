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
public class Sphere {

    public double cost(Particle p) {
        double cost = 0;
        double[] position = p.getPosition();
        for (int i = 0; i < position.length; i++) {
            cost = cost + Math.pow(position[i],2);
        }
        return cost;
    }
}