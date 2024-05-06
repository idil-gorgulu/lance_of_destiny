package org.Controllers;

import org.Domain.BarrierType;
import org.Domain.Coordinate;
import org.Domain.Game;
import org.Domain.*;
import org.Utils.Database;
import org.bson.Document;

import java.util.ArrayList;

public class DataBaseController {
    private static Game gameSession;
    private static DataBaseController instance;
    public DataBaseController(){

        this.gameSession = Game.getInstance();
    }

    public void openFromDatabase(Document game){
        Game gameInstance = Game.getInstance();
        gameInstance.reset();
        String templateName = game.getString("gameName");
        int barrierAmount=game.getInteger("barrierAmount");
        for(int j=0; j<barrierAmount; j++) {
            String barrierInfo = game.getString("barrier_" + j);
            String[] parts = barrierInfo.split("-");
            // Extracting information from parts array
            int xCoordinate = Integer.parseInt(parts[0]);
            int yCoordinate = Integer.parseInt(parts[1]);
            BarrierType barrierType = BarrierType.valueOf(parts[2]);
            int numHits = Integer.parseInt(parts[3]);
            Coordinate co  =new Coordinate(xCoordinate, yCoordinate);
            gameInstance.addDetailedBarrier(co, barrierType, numHits);
        }

    }
    public void saveGameToDatabase(String gameName, Game game) {
        ArrayList<Barrier> barriers = game.getBarriers();
        Document gameSession = new Document();
        gameSession.put("email", User.getUserInstance().getEmail());
        gameSession.put("gameName", gameName);
        gameSession.put("barrierAmount", barriers.size());
        gameSession.put("chancesLeft", 3);
        gameSession.put("newlyCreated", "Yes");
        for(int i=0; i<barriers.size(); i++){
            gameSession.put("barrier_"+i, barriers.get(i).getCoordinate().getX() + "-"+barriers.get(i).getCoordinate().getY() +
                    "-"+ barriers.get(i).getType().toString()+ "-" + barriers.get(i).getnHits() +
                    "-"+ barriers.get(i).isMoving() + "-"+barriers.get(i).getVelocity() );
        }
        gameSession.put("played", "False");
        Database.getInstance().getGameCollection().insertOne(gameSession);
        System.out.println("Saved");
    }
    public static DataBaseController getInstance(){
        if (instance==null) {
            instance=new DataBaseController();
            return instance;
        }
        else{
            return instance;
        }
    }
}