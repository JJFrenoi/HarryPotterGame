package com.harrypotter.harrypotterapi.game;

import java.util.UUID;
//Model utilisé par la BDD
public class GameModel {


    protected final UUID gameId;
    protected final int nbOfRound;
    protected final int mapWidth;
    protected final int mapHeight;
    protected final int nbOfwizard;
    protected final int nbObstacle;
    protected String winner;

    public GameModel(UUID gameId, int nbOfRound, int mapWidth, int mapHeight, int nbOfwizard, int nbObstacle, String winner) {
        this.gameId = gameId;
        this.nbOfRound = nbOfRound;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.nbOfwizard = nbOfwizard;
        this.nbObstacle = nbObstacle;
        this.winner = winner;
    }
    public UUID getGameId() {
        return gameId;
    }

    public int getNbOfRound() {
        return nbOfRound;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getNbOfwizard() {
        return nbOfwizard;
    }

    public int getNbObstacle() {
        return nbObstacle;
    }

    public String getWinner() {
        return winner;
    }
}
