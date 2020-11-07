package com.harrypotter.harrypotterapi.game;

import com.harrypotter.harrypotterapi.dementor.Dementor;
import com.harrypotter.harrypotterapi.house.House;
import com.harrypotter.harrypotterapi.map.Map;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//Service liant les partie à la BDD et à l'api
@Service
public class GameService {
    private final GameDataAccessService gameDataAccessService;

    @Autowired
    public GameService(GameDataAccessService gameDataAccessService) {
        this.gameDataAccessService = gameDataAccessService;
    }

    public Boolean isGameStarted() {
        return Game.isGameStarted;
    }

    public Boolean isGameInit() {
        return gameDataAccessService.returnLastCreatedGame().getInit();
    }

    public Boolean addNewGame(Game game) {
        return gameDataAccessService.addGame(game);
    }

    public List<Wizard> getWizardList() {
        return gameDataAccessService.returnLastCreatedGame().getWizards();
    }

    public Map getCurrentMap() {
        return Game.map;
    }

    public List<Dementor> getDementors() {
        return gameDataAccessService.returnLastCreatedGame().getDementors();
    }

    public Boolean startGame() {
        return gameDataAccessService.returnLastCreatedGame().startGame();
    }

    public Boolean stopGame() {
        Game.isGameStarted = false;
        return true;
    }

    public House getWinner() {
        System.out.println("Adding to Game");
        gameDataAccessService.insertGame(gameDataAccessService.returnLastCreatedGame());
        return gameDataAccessService.returnLastCreatedGame().getHouseWinner();
    }

    public void addGame(GameModel game) {
        gameDataAccessService.insertGame(game);
    }

    public List<GameModel> listGame() {
        return gameDataAccessService.historyGame();
    }
    public int round(){
        return gameDataAccessService.returnLastCreatedGame().getRound();
    }
}
