package com.harrypotter.harrypotterapi.game;

import com.harrypotter.harrypotterapi.dementor.Dementor;
import com.harrypotter.harrypotterapi.house.House;
import com.harrypotter.harrypotterapi.map.Map;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
//Classe liant les requêtes à l'api et au objet du programme
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game/map")
    public Map getMap() {
        return gameService.getCurrentMap();
    }

    @GetMapping("/game/wizards")
    public List<Wizard> wizards() {
        return gameService.getWizardList();
    }

    @PostMapping("/game/init")
    public Boolean addNewGame(@NonNull @RequestBody @Validated Game game) {
        gameService.addNewGame(game);
        return true;
    }
    @GetMapping("/game/round")
    public int round(){
        return gameService.round();
    }

    @GetMapping("/game/init/status")
    public Boolean getInitStatus() {
        return gameService.isGameInit();
    }

    @GetMapping("/game/status")
    public Boolean getIsGameStarted() {
        return gameService.isGameStarted();
    }

    @GetMapping("/game/stop")
    public Boolean stopGame() {
        return gameService.stopGame();
    }

    @GetMapping("/game/start")
    public Boolean startCurrentGame() {
        System.out.println("GAME STARTING");
        return gameService.startGame();
    }
    @GetMapping("/history")
    public List<GameModel> history(){
        return gameService.listGame();
    }

   @GetMapping("/game/winner")
    public House getWinner() {
        return gameService.getWinner();
    }

    @GetMapping("/game/dementors")
    public List<Dementor> dementors(){return gameService.getDementors();}
}
