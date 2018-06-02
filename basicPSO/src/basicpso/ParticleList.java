/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicpso;

import java.util.ArrayList;

/**
 *
 * @author kotic
 */
public class ParticleList {
    
    private ArrayList<Particle> particleList;

    public ParticleList() {
        particleList = new ArrayList<Particle>();
    }

    public ArrayList<Particle> getParticleList() {
        return particleList;
    }

    public void setParticleList(ArrayList<Particle> particleList) {
        this.particleList = particleList;
    }
}