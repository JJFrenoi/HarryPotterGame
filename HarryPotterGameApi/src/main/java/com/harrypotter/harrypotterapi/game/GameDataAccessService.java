package com.harrypotter.harrypotterapi.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//Classe de communication avec la BDD
@Repository
public class GameDataAccessService {
    private final List<Game> games = new ArrayList<>();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean addGame(Game game) {
        games.add(game);
        return true;
    }

    public Game returnLastCreatedGame() {
        return games.get(games.size() - 1);
    }

    public List<GameModel> historyGame() {
        final String SQLCOMMAND = "SELECT id, nbOfRound, mapWidth, mapHeight, nbOfwizard, nbObstacle, winner FROM game ORDER BY posting_date DESC";
        List<GameModel> games = jdbcTemplate.query(SQLCOMMAND, (resultSet, i) -> new GameModel(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getInt("nbOfRound"),
                resultSet.getInt("mapWidth"),
                resultSet.getInt("mapHeight"),
                resultSet.getInt("nbOfWizard"),
                resultSet.getInt("nbObstacle"),
                resultSet.getString("winner")));
        for (var game : games) System.out.println(game.gameId);
        return games;
    }

    public void insertGame(GameModel game) {
        try {
            assert game.winner != null;
            final String SQLCOMMAND = "INSERT INTO game (id, nbOfRound, mapWidth, mapHeight, nbOfwizard, nbObstacle, winner) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(SQLCOMMAND, game.getGameId(), game.getNbOfRound(), game.getMapWidth(), game.getMapHeight(), game.getNbOfwizard(), game.getNbObstacle(), game.getWinner().toUpperCase());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
