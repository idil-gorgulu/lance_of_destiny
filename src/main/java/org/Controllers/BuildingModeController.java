package org.Controllers;

import org.Domain.BarrierType;
import org.Domain.Barrier;
import org.Domain.BarrierType;
import org.Domain.Coordinate;
import org.Domain.Game;
import org.Views.BuildingModePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class BuildingModeController {
    private BuildingModePage buildingModePage;
    private static Game gameSession;
    // Create a 2D array
    private static BuildingModeController instance;

    public BuildingModeController(BuildingModePage buildingModePage) {
        this.buildingModePage = buildingModePage;
        this.gameSession = Game.getInstance();
    }

    /*public static void addBarrier(Coordinate coordinates, BarrierType type){
    public static void addBarrier(Coordinate coordinates, BarrierType type){
        System.out.println("New barrier of type added:"+ type);
        gameSession.addBarrier(coordinates, type);
    }*/
    public static Coordinate addBarrier(Coordinate mouseCoordinates, BarrierType type){
        int mouseX=mouseCoordinates.getX();
        int mouseY = mouseCoordinates.getY();

        // Calculate the grid cell coordinates based on mouse coordinates
        int cellX = mouseX / 50;
        int cellY = mouseY / 20;

        int barrierX = cellX * 50;
        int barrierY = cellY * 20;

        Coordinate barrierCoordinates = new Coordinate(barrierX, barrierY);

        for (Barrier barrier : gameSession.getBarriers()) {
            System.out.println("existing barrier" + " " + barrier.getCoordinates().getX() + " "+barrier.getCoordinates().getY() );
            if (barrier.getCoordinates().getX()==barrierCoordinates.getX() & barrier.getCoordinates().getY()==barrierCoordinates.getY() ) {
                System.out.println("A barrier already exists at these coordinates.");
                removeBarrier(barrierCoordinates,barrier.getType());
                return null; // Exit the function without adding a new barrier
            }
        }
        System.out.println("New barrier of type added: " + type);
        System.out.println("Barrier coordinates: " + barrierX + " " + barrierY);
        gameSession.addBarrier(barrierCoordinates, type);
        return barrierCoordinates;

    }
    public static void removeBarrier(Coordinate coordinates, BarrierType type){
        gameSession.removeBarrier(coordinates, type);
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
    public static boolean initialPopulation(int simpleNum, int firmNum, int exNum, int giftNum){
        boolean b=gameSession.initialPopulation(simpleNum,firmNum,exNum,giftNum);
        return b;
    }

    public static boolean getReady(){
        if(gameSession.getNumSimpleBarrier()>=75 && gameSession.getNumFirmBarrier()>=10 && gameSession.getNumExplosiveBarrier()>=5 && gameSession.getNumrewardingBarrier()>=10){
            return true;
        }
        else{
            return false;
        }
    }

}
