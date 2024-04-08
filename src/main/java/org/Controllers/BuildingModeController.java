package org.Controllers;

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

    public static void addBarrier(Coordinate coordinates, int type){
        System.out.println("New barrier of type added:"+ type);
        gameSession.addBarrier(coordinates, type);
    }

    public void removeBarrier(Coordinate coordinates){
        gameSession.removeBarrier(coordinates);
    }


}
