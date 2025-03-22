package com.example.slimemolds.ui;

import com.example.slimemolds.model.Agent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class SimulationCanvas extends Canvas {

    private GraphicsContext gc;
    private int width;
    private int height;
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    public SimulationCanvas(int width, int height) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.gc = getGraphicsContext2D();

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return width;
    }

    @Override
    public double prefHeight(double width) {
        return height;
    }

    public void draw(){
        scaleX = getWidth() / width;
        scaleY = getHeight() / height;

        // Fond noir au lieu de clear
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    public void drawAgents(List<Agent> agents){
        for (Agent agent : agents) {
            double x = agent.getCenterX() * scaleX;
            double y = agent.getCenterY() * scaleY;
            double radius = agent.getRadius() * Math.min(scaleX, scaleY);

            // Dessiner le corps de l'agent (blanc sur fond noir pour meilleure visibilité)
            gc.setFill(Color.YELLOW);
            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            // Dessiner une indication de la direction de l'agent
            double dirX = Math.cos(agent.getAngle()) * radius;
            double dirY = Math.sin(agent.getAngle()) * radius;
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.strokeLine(x, y, x + dirX, y + dirY);
        }

    }

    public void drawColony(double colonyX, double colonyY, double radius) {
        double x = colonyX * scaleX;
        double y = colonyY * scaleY;
        radius = radius * Math.min(scaleX, scaleY);

        gc.setFill(Color.GREEN);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }





}
