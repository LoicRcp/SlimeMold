package com.example.slimemolds;

import com.example.slimemolds.core.Simulation;
import com.example.slimemolds.model.Agent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationApp extends Application {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int NUM_AGENTS = 10;
    private static final double AGENT_RADIUS = 5;
    
    private List<Agent> agents = new ArrayList<>();
    private Random random = new Random();
    private Simulation simulation;
    
    @Override
    public void start(Stage stage) {
        // Création du panneau principal
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);
        
        // Création des agents (cercles) à des positions aléatoires
        for (int i = 0; i < NUM_AGENTS; i++) {
            double x = random.nextDouble() * (WIDTH - 2 * AGENT_RADIUS) + AGENT_RADIUS;
            double y = random.nextDouble() * (HEIGHT - 2 * AGENT_RADIUS) + AGENT_RADIUS;
            
            Agent agent = new Agent(x, y, AGENT_RADIUS);
            agents.add(agent);
            root.getChildren().add(agent);
        }
        
        // Configuration de la scène
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Simulation de Slime Mold");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        // Configuration et démarrage de la simulation
        simulation = new Simulation(agents, root);
        
        // Gestion de la fermeture de l'application
        stage.setOnCloseRequest(event -> {
            if (simulation != null) {
                simulation.stop();
            }
            Platform.exit();
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
