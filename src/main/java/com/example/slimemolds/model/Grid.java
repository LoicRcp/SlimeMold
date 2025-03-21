package com.example.slimemolds.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grid {
    private int width;           // Largeur du monde en pixels
    private int height;          // Hauteur du monde en pixels
    private int cellSize;        // Taille d'une cellule en pixels
    private int gridWidth;       // Nombre de cellules en largeur
    private int gridHeight;      // Nombre de cellules en hauteur



    // Grilles pour les phéromones de sentier (déposées par les agents)
    private double[][] trailGrid;
    private double[][] nextTrailGrid;

    // Grilles pour les phéromones de nourriture (émises par les sources)
    private double[][] foodGrid;
    private double[][] nextFoodGrid;

    private int[][] directions = new int[][]{
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    private double[] weights = new double[]{
            0.1, 0.15, 0.1,
            0.15, 0.15,
            0.1, 0.15, 0.1
    };


    private double diffusionRate;
    private double evaporationRate;

    public Grid(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;

        this.gridWidth = (width + cellSize - 1) / cellSize;
        this.gridHeight = (height + cellSize - 1) / cellSize;

        this.trailGrid = new double[gridWidth][gridHeight];
        this.nextTrailGrid = new double[gridWidth][gridHeight];

        this.foodGrid = new double[gridWidth][gridHeight];
        this.nextFoodGrid = new double[gridWidth][gridHeight];

        this.diffusionRate = 0.1;
        this.evaporationRate = 0.95;
    }

    private int toGridX(double worldX) {
        return Math.min(gridWidth - 1, Math.max(0, (int) (worldX / cellSize)));
    }

    private int toGridY(double worldY) {
        return Math.min(gridHeight - 1, Math.max(0, (int) (worldY / cellSize)));
    }

    private double toWorldX(int x){
        return (x * cellSize) + (cellSize/2.0);
    }

    private double toWorldY(int y){
        return (y * cellSize) + (cellSize/2.0);
    }


    public void depositTrailPheromone(double worldX, double worldY, double value){
        int gridX = toGridX(worldX);
        int gridY = toGridY(worldY);
        trailGrid[gridX][gridY] += value;
    }

    public void depositFoodPheromone(double worldX, double worldY, double value){
        int gridX = toGridX(worldX);
        int gridY = toGridY(worldY);
        foodGrid[gridX][gridY] += value;
    }

    public double senseTrailPheromone(double worldX, double worldY){
        int gridX = toGridX(worldX);
        int gridY = toGridY(worldY);
        return trailGrid[gridX][gridY];
    }

    public double senseFoodPheromone(double worldX, double worldY){
        int gridX = toGridX(worldX);
        int gridY = toGridY(worldY);
        return foodGrid[gridX][gridY];
    }

    public void updateGrids(){
        diffuseGrid(trailGrid, nextTrailGrid);
        diffuseGrid(foodGrid, nextFoodGrid);
        swapBuffers();
    }

    private void diffuseGrid(double[][] sourceGrid, double[][] targetGrid) {
        // vider le nextBuffer
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                targetGrid[x][y] = 0;
            }
        }


        // boucle sur toute les cellules de la grille
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {

                double currentValue = sourceGrid[x][y];
                double remaining = currentValue * (1 - diffusionRate);
                double toShare = currentValue * diffusionRate;

                double totalWeight = 0;
                for (int d = 0; d < directions.length; d++) {
                    int nx = x + directions[d][0];
                    int ny = y + directions[d][1];

                    if (nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight) {
                        totalWeight += weights[d];
                    }
                }

                double scaleFact = (totalWeight > 0) ? diffusionRate / totalWeight : 0;


                for (int d = 0; d < directions.length; d++) {
                    int nx = x + directions[d][0];
                    int ny = y + directions[d][1];

                    if (nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight) {
                        targetGrid[nx][ny] += sourceGrid[x][y] * weights[d] * scaleFact;
                    }
                }
                targetGrid[x][y] += remaining;

                // evaporation
                targetGrid[x][y] *= evaporationRate;
            }
        }
    }


    public void swapBuffers(){
        double[][] tempTrail = trailGrid;
        trailGrid = nextTrailGrid;
        nextTrailGrid = tempTrail;

        double[][] tempFood = foodGrid;
        foodGrid = nextFoodGrid;
        nextFoodGrid = tempFood;
    }


    public void render(GraphicsContext gc) {
        // Pour chaque cellule de la grille
        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                double worldX = gridX * cellSize;
                double worldY = gridY * cellSize;

                // Valeurs des phéromones à cette cellule
                double trailValue = trailGrid[gridX][gridY];
                double foodValue = foodGrid[gridX][gridY];

                // Visualisation des phéromones (par exemple, mélange de couleurs)
                if (trailValue > 0 || foodValue > 0) {
                    // Normaliser les valeurs pour l'affichage
                    double maxTrail = 10.0; // Valeur max attendue, à ajuster
                    double maxFood = 10.0;  // Valeur max attendue, à ajuster

                    double trailIntensity = Math.min(1.0, trailValue / maxTrail);
                    double foodIntensity = Math.min(1.0, foodValue / maxFood);

                    // Mélange de bleu (sentier) et de vert (nourriture)
                    int blue = (int)(255 * trailIntensity);
                    int green = (int)(255 * foodIntensity);
                    Color cellColor = Color.rgb(0, green, blue, 0.7);

                    gc.setFill(cellColor);
                    gc.fillRect(worldX, worldY, cellSize, cellSize);
                }
            }
        }
    }








}
