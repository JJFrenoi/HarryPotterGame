package com.harrypotter.harrypotterapi.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harrypotter.harrypotterapi.MovableUnit;
import com.harrypotter.harrypotterapi.dementor.Dementor;
import com.harrypotter.harrypotterapi.house.*;
import com.harrypotter.harrypotterapi.map.Map;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.masters.MasterWizard;
import com.harrypotter.harrypotterapi.wizard.student.StudentWizard;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
//Classe de controle de la partie
public class Game extends GameModel implements Runnable {
    public static int MAX_ENERGIE;
    public static int speed = 500;
    public static Map map;
    public static Boolean isGameStarted = false;
    private final List<House> houses;
    private final List<Wizard> wizards = new ArrayList<>();
    private final List<StudentWizard> studentWizards = new ArrayList<>();
    private final List<Dementor> dementors = new ArrayList<>();
    private final List<MovableUnit> movableUnit = new ArrayList<>();
    private final Boolean isInit;
    private House houseWinner;
    private final Boolean liveGame;
    private int round = 0;
    private final int MAXMESSAGE;

    //Création d'une partie avec les paramètres recuent d'une requête http
    public Game(
            @JsonProperty("nbOfRound") int nbOfRound,
            @JsonProperty("mapWidth") int mapWidth,
            @JsonProperty("mapHeight") int mapHeight,
            @JsonProperty("nbOfwizard") int nbOfwizard,
            @JsonProperty("nbObstacle") int nbObstacle,
            @JsonProperty("liveGame") Boolean liveGame,
            @JsonProperty("speed") int speeds,
            String winner) {
        super(UUID.randomUUID(), nbOfRound, mapWidth, mapHeight, nbOfwizard, nbObstacle, winner);
        speed = speeds;
        this.liveGame = liveGame;
        MAX_ENERGIE = mapHeight * mapWidth;
        MAXMESSAGE = 4 * nbOfwizard;
        map = new Map(0L, mapWidth, mapHeight, nbObstacle);
        resetHouseAndMaster();
        this.houses = new ArrayList<>(Arrays.asList(Gryffindor.retrieveInstance(), Hufflepuff.retrieveInstance(), Slytherin.retrieveInstance(), Ravenclaw.retrieveInstance()));
        initWizard(nbOfwizard);
        initDementor(4);
        System.out.println("Init Success * " + nbOfRound);
        isInit = true;
        if (!liveGame && !isGameStarted) {
            startGame();
        }
    }
    public List<Dementor> getDementors() {
        return dementors;
    }

    public House getHouseWinner() {
        return houseWinner;
    }

    public Boolean getInit() {
        return isInit;
    }

    public int getRound() {
        return round;
    }

    public List<Wizard> getWizards() {
        return wizards;
    }

    //Initialise tous les sorciers sur la carte
    private void initWizard(int nbOfwizard) {
        houses.forEach(house -> {
            house.setMasterWizard();
            wizards.add(house.getMasterWizard());
        });
        wizards.forEach(wizard -> {
            var master = (MasterWizard) wizard;
            master.initStudents(nbOfwizard);
            var students = master.getStudents();
            studentWizards.addAll(students);
        });
        movableUnit.addAll(studentWizards);
        wizards.addAll(studentWizards);
    }

    //Initialise tous les dementors sur la carte
    private void initDementor(int nbOfDementors){
        for (int i = 0; i <nbOfDementors ; i++) {
            dementors.add(new Dementor(i));
        }
        movableUnit.addAll(dementors);
    }

    //Lance la partie
    public Boolean startGame() {
        isGameStarted = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
        return true;
    }

    @Override
    public void run() {
        while (isGameStarted) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("Round number :" + round);
            updateMasterWizard();
            moveUnit();
            fights();
            getWinnerHouseAndRound();
            //Temps entre chaque round permet à l'utilisateur de regarder la partie
            if (liveGame) {
                try {
                    Thread.sleep(Game.speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Fonction appelée chaque round pour l'interraction entre les sorciers
    private synchronized void fights() {
        for (var wizard : studentWizards) {
            for (var wizard2 : studentWizards) {
                if (!wizard.equals(wizard2)) {
                    var compare = wizard.getCurrentBox().compare(wizard2.getCurrentBox());
                    if (compare[0] <= 1 && compare[1] <= 1) {
                        wizard.getAlliance().encounterOfStudent(wizard, wizard2, wizard2.getHouse());
                    }
                }
            }
        }
        //appelle la fonction pour que les dementors attaquent
        dementors.forEach(dementor -> dementor.attackNearWizards(studentWizards));
    }

    //Appelé chaque round permet de trouver un vainqueur si il y a
    @JsonIgnore
    private synchronized void getWinnerHouseAndRound(){
        var winner = getWinnerHouse();
        if (winner != null) {
            stopGame(winner);
        } else if (round == nbOfRound) {
            winner = getWinnerWithRound();
            stopGame(winner);
        } else {
            round++;
        }
    }

    //arrête la partie si un vainqueur est trouvé
    private synchronized void stopGame(House houseWinner){
        System.out.println("Winner is " +houseWinner.getId());
        isGameStarted = false;
        this.houseWinner = houseWinner;
        this.winner = houseWinner.getName();
    }

    //Trouve la première maison à avoir tous les messages
    @JsonIgnore
    private House getWinnerHouse() {
        Collections.shuffle(houses);
        AtomicReference<House> winner = new AtomicReference<>(null);
        houses.forEach(house -> {
            System.out.println(house.getName() + "Messages size" + house.sizeOfMessages() + "Max SIZE " + MAXMESSAGE);
            if (house.sizeOfMessages() == MAXMESSAGE) {
                winner.set(house);
            }
        });
        return winner.get();
    }

    //Si les rounds sont finis, renvoie la maison avec le plus grand nombre de messages
    @JsonIgnore
    private House getWinnerWithRound() {
        Collections.shuffle(houses);
        AtomicReference<House> winner = new AtomicReference<>(houses.get(0));
        houses.forEach(
                house -> {
                    System.out.println(house.getName() + "Messages size" + house.sizeOfMessages());
                    if (house.sizeOfMessages() >= winner.get().sizeOfMessages()) {
                        winner.set(house);
                    }
                }
        );
        return winner.get();
    }

    //Chaque round permet de mettre à jour les maitres sorciers
    private synchronized void updateMasterWizard() {
        houses.forEach(
                house -> {
                    var master = house.getMasterWizard();
                    master.checkIfStudentIsInSafeZoneAndGetMessage();
                }
        );
    }

    //Réinitialise les singletons afin de pouvoir instancier plusieurs parties
    public void resetHouseAndMaster(){
        try{
            Gryffindor.retrieveInstance().reset();
            Hufflepuff.retrieveInstance().reset();
            Ravenclaw.retrieveInstance().reset();
            Slytherin.retrieveInstance().reset();
        }catch (NullPointerException e){
            System.out.println("Singletons was null");
        }
    }

    //Anime tous les objets mobiles (students et dementor)
    private void moveUnit() {
        List<Thread> tmp = new ArrayList<>();
        Collections.shuffle(movableUnit);
        for (var move : movableUnit) {
            var t = move.move();
            if (t != null) {
                tmp.add(t);
            }
        }
        for (var t : tmp) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
