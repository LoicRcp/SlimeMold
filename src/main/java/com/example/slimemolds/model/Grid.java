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

        this.diffusionRate = 0.10;  // Réduit de 0.1 à 0.05 pour diffusion plus lente
        this.evaporationRate = 0.995; // Augmenté de 0.95 à 0.99 pour évaporation bien plus lente
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
        // Trouver les valeurs max pour normalisation dynamique
        double maxTrailValue = 0.01; // Petite valeur non-zéro pour éviter division par zéro
        double maxFoodValue = 0.01;
        
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                maxTrailValue = Math.max(maxTrailValue, trailGrid[x][y]);
                maxFoodValue = Math.max(maxFoodValue, foodGrid[x][y]);
            }
        }
        
        // Pour chaque cellule de la grille
        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                double worldX = gridX * cellSize;
                double worldY = gridY * cellSize;

                // Valeurs des phéromones à cette cellule
                double trailValue = trailGrid[gridX][gridY];
                double foodValue = foodGrid[gridX][gridY];

                // Visualisation des phéromones (par exemple, mélange de couleurs)
                if (trailValue > 0.001 || foodValue > 0.001) { // Seuil minimal pour le rendu
                    // Normaliser les valeurs pour l'affichage
                    double trailIntensity = Math.min(1.0, trailValue / maxTrailValue);
                    double foodIntensity = Math.min(1.0, foodValue / maxFoodValue);

                    // Mélange de couleurs: bleu pour trail, vert pour food
                    double blue = 255 * trailIntensity;
                    double green = 255 * foodIntensity;
                    
                    // Ajout d'un peu de rouge si les deux types sont présents pour un effet visuel
                    double red = (trailIntensity > 0 && foodIntensity > 0) ? 
                                  Math.min(trailIntensity, foodIntensity) * 128 : 0;
                    
                    Color cellColor = Color.rgb((int)red, (int)green, (int)blue, 0.7);

                    gc.setFill(cellColor);
                    gc.fillRect(worldX, worldY, cellSize, cellSize);
                }
            }
        }
        
        // Afficher les valeurs max pour debug (à commenter en production)
        //System.out.println("Max Trail: " + maxTrailValue + " | Max Food: " + maxFoodValue);
    }








}
