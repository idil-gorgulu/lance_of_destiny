package org.Controllers;

import org.Domain.Coordinate;
import org.Domain.Game;
import org.Views.BuildingModePage;

public class BuildingModeController {
    private BuildingModePage buildingModePage;
    private Game gameSession;

    public BuildingModeController(BuildingModePage buildingModePage, Game gameSession) {
        this.buildingModePage = buildingModePage;
        this.gameSession = gameSession;
    }

    public void addBarrier(Coordinate coordinates, int type){
        gameSession.addBarrier(coordinates, type);
    }

    public void removeBarrier(Coordinate coordinates){
        gameSession.removeBarrier(coordinates);
    }


}
