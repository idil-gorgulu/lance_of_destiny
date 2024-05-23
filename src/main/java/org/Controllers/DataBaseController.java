package org.Controllers;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.Domain.BarrierType;
import org.Domain.Coordinate;
import org.Domain.Game;
import org.Domain.*;
import org.Utils.Database;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

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
        String gameDate = game.getString("gameDate");
        gameInstance.setGameName(templateName);
        gameInstance.setDate(gameDate);
        int chancesLeft=game.getInteger("chancesLeft");
        gameInstance.setChance(chancesLeft);
        int barrierAmount=game.getInteger("barrierAmount");
        for(int j=0; j<barrierAmount; j++) {
            String barrierInfo = game.getString("barrier_" + j);
            String[] parts = barrierInfo.split("/");
            // Extracting information from parts array
            int xCoordinate = Integer.parseInt(parts[0]);
            int yCoordinate = Integer.parseInt(parts[1]);
            BarrierType barrierType = BarrierType.valueOf(parts[2]);
            int numHits = Integer.parseInt(parts[3]);
            boolean isMoving= Boolean.parseBoolean(parts[4]);
            int velocity = Integer.parseInt(parts[5]);
            Coordinate co  =new Coordinate(xCoordinate, yCoordinate);
            gameInstance.addDetailedBarrierFromDb(co, barrierType, numHits, isMoving, velocity);
        }
    }
    public void saveGameToDatabase(String gameName, Game game, boolean played) {
        ArrayList<Barrier> barriers = game.getBarriers();
        Document gameSession = new Document();
        gameSession.put("email", User.getUserInstance().getEmail());
        gameSession.put("gameName", gameName);
        gameSession.put("gameDate", game.getDate());
        gameSession.put("barrierAmount", barriers.size());
        gameSession.put("chancesLeft", game.getChance().getRemainingChance());
        for(int i=0; i<barriers.size(); i++){
            gameSession.put("barrier_"+i, barriers.get(i).getCoordinate().getX() + "/"+barriers.get(i).getCoordinate().getY() +
                    "/"+ barriers.get(i).getType().toString()+ "/" + barriers.get(i).getnHits() +
                    "/"+ barriers.get(i).isMoving() + "/"+barriers.get(i).getVelocity() );
        }
        HashMap<SpellType, Integer> inventory = game.getInventory();
        gameSession.put("spellFelixFelicis",inventory.get(SpellType.FELIX_FELICIS));
        gameSession.put("spellStaffExpansion",inventory.get(SpellType.STAFF_EXPANSION));
        gameSession.put("spellHex",inventory.get(SpellType.HEX));
        gameSession.put("spellOverwhelming",inventory.get(SpellType.OVERWHELMING_FIREBALL));

        Fireball fireball = game.getFireball();
        gameSession.put("fireball", fireball.getCoordinate().getX() + "/"+
                fireball.getCoordinate().getY() + "/" +
                fireball.getxVelocity()+ "/" +
                fireball.getyVelocity());

        if(played)gameSession.put("played", "True");
        else gameSession.put("played", "False");

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