package com.example.slimemolds.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Agent extends Circle {
    
    private double angle;
    
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
}
