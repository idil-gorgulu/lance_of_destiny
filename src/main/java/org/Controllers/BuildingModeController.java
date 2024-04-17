package org.Controllers;

import org.Domain.Barrier;
import org.Domain.Coordinate;
import org.Domain.Game;
import org.Views.BuildingModePage;

public class BuildingModeController {
    private BuildingModePage buildingModePage;
    private static Game gameSession;
    private static BuildingModeController instance;

    public BuildingModeController(BuildingModePage buildingModePage) {
        this.buildingModePage = buildingModePage;
        this.gameSession = Game.getInstance();
    }

    public static Coordinate addBarrier(Coordinate mouseCoordinates, int type){
        int mouseX=mouseCoordinates.getX();
        int mouseY = mouseCoordinates.getY();

        // Calculate the grid cell coordinates based on mouse coordinates
        int cellX = mouseX / 50;
        int cellY = mouseY / 20;

        int barrierX = cellX * 50;
        int barrierY = cellY * 20;

        Coordinate barrierCoordinates = new Coordinate(barrierX, barrierY);

        for (Barrier barrier : gameSession.getBarriers()) {
            //System.out.println("existing barrier" + " " + barrier.getCoordinates().getX() + " "+barrier.getCoordinates().getY() );
            if (barrier.getCoordinates().getX()==barrierCoordinates.getX() & barrier.getCoordinates().getY()==barrierCoordinates.getY() ) {
                System.out.println("A barrier already exists at these coordinates.");
                removeBarrier(barrierCoordinates);
                return null; // Exit the function without adding a new barrier
            }
        }
        System.out.println("New barrier of type added: " + type);
        System.out.println("Barrier coordinates: " + barrierX + " " + barrierY);

        gameSession.addBarrier(barrierCoordinates, type);
        return barrierCoordinates;



    }
    public static void removeBarrier(Coordinate coordinates){
        gameSession.removeBarrier(coordinates);
    }

//    public static BuildingModeController getInstance(){
//        if(instance==null){
//            instance=new BuildingModeController();
//            return instance;
//        }
//        else{
//            return instance;
//        }
//    }


    public static Game getGameSession() {
        return gameSession;
    }

    public static void setGameSession(Game gameSession) {
        BuildingModeController.gameSession = gameSession;
    }
}
