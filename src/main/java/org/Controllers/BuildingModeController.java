package org.Controllers;

import org.Domain.*;
import org.Domain.BarrierType;
import org.Utils.Database;
import org.Views.BuildingModePage;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class BuildingModeController {
    private static Game gameSession;

    public BuildingModeController() {
        this.gameSession = Game.createNewGame();
        //gameSession.reset(); needed for being able to create a new template.
    }

    public static Coordinate addBarrier(Coordinate mouseCoordinates, BarrierType type){
        int mouseX=mouseCoordinates.getX();
        int mouseY = mouseCoordinates.getY();

        int cellX = mouseX / 50;
        int cellY = mouseY / 20;

        int barrierX = cellX * 50;
        int barrierY = cellY * 20;

        Coordinate barrierCoordinates = new Coordinate(barrierX, barrierY);

        for (Barrier barrier : gameSession.getBarriers()) {
            System.out.println("existing barrier" + " " + barrier.getCoordinate().getX() + " "+barrier.getCoordinate().getY() );
            if (barrier.getCoordinate().getX()==barrierCoordinates.getX() & barrier.getCoordinate().getY()==barrierCoordinates.getY() ) {
                System.out.println("A barrier already exists at these coordinates.");
                removeBarrier(barrierCoordinates,barrier.getType());
                return null;
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

    public Game getGameSession() {
        return gameSession;
    }

    public boolean initialPopulation(int simpleNum, int firmNum, int exNum, int giftNum){
        boolean b = gameSession.initialPopulation(simpleNum,firmNum,exNum,giftNum);
        return b;
    }

    public static boolean getReady(){
        if (gameSession.getNumSimpleBarrier() >= 75 &&
                gameSession.getNumFirmBarrier() >= 10 &&
                gameSession.getNumExplosiveBarrier()>=5 &&
                gameSession.getNumrewardingBarrier()>=10){
            return true;
        }
        else{
            return false;
        }
    }

    public void saveGameToDatabase(String gameName) {
        ArrayList<Barrier> barriers = gameSession.getBarriers();
        Document gameSession = new Document();
        gameSession.put("email", User.getUserInstance().getEmail());
        gameSession.put("gameName", gameName);
        gameSession.put("barrierAmount", barriers.size());
        for(int i=0; i<barriers.size(); i++){
            gameSession.put("barrier_"+i, barriers.get(i).getCoordinate().getX() + "-"+barriers.get(i).getCoordinate().getY() +
                    "-"+ barriers.get(i).getType().toString()+ "-" + barriers.get(i).getnHits());
        }
        gameSession.put("played", "False");
        Database.getInstance().getGameCollection().insertOne(gameSession);
        System.out.println("Saved");
    }

}
