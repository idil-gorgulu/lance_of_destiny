package org.Controllers;

import org.Domain.BarrierType;
import org.Domain.Coordinate;
import org.Domain.Game;
import org.bson.Document;

public class DataBaseController {
    public DataBaseController(){

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
}
