package com.harrypotter.harrypotterapi.dementor;

import com.harrypotter.harrypotterapi.MovableUnit;
import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.map.Box;
import com.harrypotter.harrypotterapi.wizard.student.StudentWizard;

import java.util.List;
//Classe de l'objet dementor
public class Dementor implements MovableUnit {
    private final int id;
    private final String name;
    private Box currentBox;

    public Dementor(int id) {
        this.id = id;
        this.name = "dementor" + id;
        moveRandomlyOnMap();
    }

    public int getId() {
        return id;
    }

    public Box getCurrentBox() {
        return currentBox;
    }

    public String getName() {
        return name;
    }

    public char getIcon() {
        return 'D';
    }

    //Fonction exécutée par un thread
    @Override
    public void run() {
        //Si la partie a commencé
        if (Game.isGameStarted) {
            moveRandomlyOnMap();
        }
    }

    //fige tous les wizards 1 case à côté
    public void attackNearWizards(List<StudentWizard> wizards) {
        wizards.forEach(studentWizard -> {
            var compare = currentBox.compare(studentWizard.getCurrentBox());
            if (compare[0] <= 1 && compare[1] <= 1 && studentWizard.isAttackableByDementor()) {
                studentWizard.setDementorAttacked(true);
            }
        });
    }

    //Change de position de manière aléatoire
    public void moveRandomlyOnMap() {
        var randomBox = Game.map.randomBoxOnMap();
        if (randomBox != null) {
            moveToBox(randomBox);
        }
    }

    //Thread animant l'objet
    @Override
    public Thread move() {
        try {
            var t = new Thread(this);
            t.start();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Change de position avec la Box donnée en paramètre
    @Override
    public void moveToBox(Box box) {
        if (this.currentBox != null) {
            this.currentBox.busy = false;
        }
        currentBox = box;
        this.currentBox.busy = true;
    }
}
