package com.example.slimemolds.core;

import com.example.slimemolds.model.Agent;
import com.example.slimemolds.model.Grid;
import com.example.slimemolds.ui.SimulationCanvas;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Random;

public class Simulation {
    
    private List<Agent> agents;
    private SimulationCanvas canvas;
    private AnimationTimer timer;
    private Grid pheromoneGrid;
    private Random random = new Random();
    
    public Simulation(List<Agent> agents, SimulationCanvas canvas) {
        this.agents = agents;
        this.canvas = canvas;
        
        // Créer la grille de phéromones avec une résolution adaptée
        // La taille de la cellule (10) peut être ajustée selon les besoins
        this.pheromoneGrid = new Grid((int)canvas.getWidth(), (int)canvas.getHeight(), 25);
        
        // Ajouter quelques phéromones pour tester le rendu
        addTestPheromones();
    }
    
    // Méthode pour ajouter des phéromones de test
    private void addTestPheromones() {
        // Ajouter des phéromones de sentier en motif circulaire
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double distance = 100;
            double x = centerX + Math.cos(angle) * distance;
            double y = centerY + Math.sin(angle) * distance;
            
            pheromoneGrid.depositTrailPheromone(x, y, 5.0 + random.nextDouble() * 5.0);
        }
        
        // Ajouter des phéromones de nourriture en quelques points
        for (int i = 0; i < 5; i++) {
            double x = random.nextDouble() * canvas.getWidth();
            double y = random.nextDouble() * canvas.getHeight();
            pheromoneGrid.depositFoodPheromone(x, y, 10.0 + random.nextDouble() * 10.0);
        }
        
        // Ajouter un chemin de phéromones
        double startX = canvas.getWidth() * 0.2;
        double startY = canvas.getHeight() * 0.2;
        double endX = canvas.getWidth() * 0.8;
        double endY = canvas.getHeight() * 0.8;
        
        for (double t = 0; t <= 1.0; t += 0.02) {
            double x = startX + (endX - startX) * t;
            double y = startY + (endY - startY) * t;
            pheromoneGrid.depositTrailPheromone(x, y, 7.0 + random.nextDouble() * 3.0);
        }
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
        // Mettre à jour la grille de phéromones
        pheromoneGrid.updateGrids();
        
        // Mettre à jour les agents
        for (Agent agent : agents) {
            agent.update(pheromoneGrid);
        }
    }

    private void render() {
        canvas.draw();
        
        // Rendre la grille de phéromones
        pheromoneGrid.render(canvas.getGraphicsContext2D());
        
        // Rendre les agents par-dessus la grille de phéromones
        canvas.drawAgents(agents);
    }
}
