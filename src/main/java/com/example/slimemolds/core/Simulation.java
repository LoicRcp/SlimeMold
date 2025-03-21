package com.example.slimemolds.core;

import com.example.slimemolds.model.Agent;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.List;

public class Simulation {
    
    private List<Agent> agents;
    private Pane root;
    private AnimationTimer timer;
    
    public Simulation(List<Agent> agents, Pane root) {
        this.agents = agents;
        this.root = root;
    }
    
    public void start() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }
    
    private void update() {
        // Cette méthode sera implémentée plus tard pour mettre à jour la position des agents
        // et implémenter les comportements de la simulation de slime mold
    }
}
