package com.example.slimemolds.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Agent extends Circle {
    
    private double angle;

    static final double SPEED = 1.0;

    public Agent(double x, double y, double radius) {
        super(x, y, radius);
        this.setFill(Color.BLUE);
        this.angle = Math.random() * 2 * Math.PI;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void move(){
        double dx = Math.cos(angle) * SPEED;
        double dy = Math.sin(angle) * SPEED;
        setCenterX(getCenterX() + dx);
        setCenterY(getCenterY() + dy);
    }

    public void addRandomVariation(double maxAngleVariation) {
        angle += (Math.random() - 0.5) * maxAngleVariation;
    }

    public void handleBorders(int maxX, int maxY) {
        setCenterX(normalizeCoordinate(getCenterX(), maxX));
        setCenterY(normalizeCoordinate(getCenterY(), maxY));
    }

    private double normalizeCoordinate(double value, int max) {
        return ((value % max) + max) % max;
    }

    public void update(Grid grid){
        move();
        handleBorders(grid.getWidth(), grid.getHeight());
        addRandomVariation(0.3);

        grid.depositTrailPheromone(getCenterX(), getCenterY(), 10.0);
    }

    private double[] senseGrid(Grid grid){
        // Cette méthode sera implémentée ultérieurement
        return null;
    }
}
