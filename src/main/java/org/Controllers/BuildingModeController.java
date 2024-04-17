package org.Controllers;

import org.Domain.Barrier;
import org.Domain.Coordinate;
import org.Domain.Game;
import org.Views.BuildingModePage;

public class BuildingModeController {
    private BuildingModePage buildingModePage;
    private static Game gameSession;

    public BuildingModeController(BuildingModePage buildingModePage) {
        this.buildingModePage = buildingModePage;
        this.gameSession = Game.getInstance();;
    }

    public static Coordinate addBarrier(Coordinate mouseCoordinates, int type){
        int mouseX=mouseCoordinates.getX();
        int mouseY = mouseCoordinates.getY();

        // Calculate the grid cell coordinates based on mouse coordinates
        int cellX = mouseX / 40;
        int cellY = mouseY / 15;

        int barrierX = cellX * 40;
        int barrierY = cellY * 15;

        Coordinate barrierCoordinates = new Coordinate(barrierX, barrierY);

        for (Barrier barrier : gameSession.getBarriers()) {
            //System.out.println("existing barrier" + " " + barrier.getCoordinates().getX() + " "+barrier.getCoordinates().getY() );
            if (barrier.getCoordinates().getX()==barrierCoordinates.getX() & barrier.getCoordinates().getY()==barrierCoordinates.getY() ) {
                System.out.println("A barrier already exists at these coordinates.");
                return null; // Exit the function without adding a new barrier
            }
        }
        System.out.println("New barrier of type added: " + type);
        System.out.println("Barrier coordinates: " + barrierX + " " + barrierY);

        gameSession.addBarrier(barrierCoordinates, type);
        return barrierCoordinates;



    }
    public void removeBarrier(Coordinate coordinates){
        gameSession.removeBarrier(coordinates);
    }


}
