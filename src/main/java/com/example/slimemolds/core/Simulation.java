package com.example.slimemolds.core;

import com.example.slimemolds.model.Agent;
import com.example.slimemolds.ui.SimulationCanvas;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.List;

public class Simulation {
    
    private List<Agent> agents;
    private SimulationCanvas canvas;;
    private AnimationTimer timer;
    
    public Simulation(List<Agent> agents, SimulationCanvas canvas) {
        this.agents = agents;
        this.canvas = canvas;
    }
    
    public void start() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
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

    private void render() {
        canvas.draw();
        canvas.drawAgents(agents);
    }
}
