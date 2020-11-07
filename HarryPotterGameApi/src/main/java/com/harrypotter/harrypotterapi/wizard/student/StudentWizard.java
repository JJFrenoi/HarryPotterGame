package com.harrypotter.harrypotterapi.wizard.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harrypotter.harrypotterapi.MovableUnit;
import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.house.House;
import com.harrypotter.harrypotterapi.map.Box;
import com.harrypotter.harrypotterapi.message.Message;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.masters.MasterWizard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
//Classe fille de Wizards
public class StudentWizard extends Wizard implements MovableUnit {
    private Boolean wasAttacked = false;
    private String status = "alive";
    private Boolean hasObtainNewMessages = false;
    private int energiePoints = Game.MAX_ENERGIE;
    private short counterWasAttack = 0;
    private short counterDementorAttack = 0;
    private Boolean dementorAttacked = false;

    public StudentWizard(long id, House house, String name) {
        //Définit les elements nécessaire à l'initialisation
        super(id, house, name, false, house.getSafeZone().availibleBox());
        //Ajoute un message a la liste des messages
        getMessages().add(new Message(0,"Message"+getName()));
    }
    public Boolean getWasAttack() {
        return wasAttacked;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //Permet de réinitialiser la protection après une attaque
    public void resetWasAttack() {
        this.wasAttacked = false;
        counterWasAttack =0;
    }

    //Si dans la safezone régénère les points d'énergie + 100 par tours
    public void regenerationInSafeZone(){
        hasObtainNewMessages = false;
        if(energiePoints < Game.MAX_ENERGIE) energiePoints+=100 ;
    }

    public String getStatus() {
        return status;
    }

    public int getEnergiePoints() {
        return energiePoints;
    }

    public Boolean getDementorAttacked() {
        return dementorAttacked;
    }

    //Attaque de dementor
    public void setDementorAttacked(Boolean dementorAttacked) {
        this.energiePoints -= 25;
        this.dementorAttacked = dementorAttacked;//true
    }

    @Override
    public void run() {
        if (canMove()) {
            System.out.print("Wizard : " + this.getName());
            this.getCurrentBox().print();
            //Doit aller dans la safezone ?
            if (needToGoToSafeZone()) {
                //Aller dans la safezone
                goToBox(getHouse().getSafeZone().availibleBox());
            } else {
                //Si est attaqué on est protégé pendant 3 tours
                if (wasAttacked && counterWasAttack < 3) {
                    counterWasAttack++;
                } else if (wasAttacked && counterWasAttack == 3) {
                    //fin de la protection
                    resetWasAttack();
                }
                //Liste des possibilités de case autour de la case actuelle
                var lb = Game.map.availableBoxsArroud(this.getCurrentBox());
                //De cette liste on choisit une aléatoire
                Box tmp = getRandomBoxFromList(lb);
                if (tmp != null) {
                    //Aller sur la case aléatoire
                    moveToBox(tmp);
                } else System.out.println("Cant Move Wizard");
            }
            //Perte de points d'énergie a chaque mouvement
            energiePoints--;
        }else if(dementorAttacked){
            //Bloqué pendant 2 rounds
            if(counterDementorAttack == 2 ){
                dementorAttacked = false;
                counterDementorAttack = 0;
            }else {
                counterDementorAttack++;
            }
        }
    }

    //Retourne une case aléatoirement d'une liste
    @JsonIgnore
    public Box getRandomBoxFromList(List<Box> lb) {
        if (!lb.isEmpty()) {
            var shortedList = shortListWithHouse(lb);
            if (!shortedList.isEmpty()) {
                return shortedList.get(ThreadLocalRandom.current().nextInt(0, shortedList.size()));
            }
        }
        return null;
    }

    //Anime l'objet
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

    //Permet d'aller a une case donnée
    public void goToBox(Box box) {
        if (!this.getCurrentBox().equals(box) && this.status.equals("alive")) {
            //Case possible autour ?
            var lb = Game.map.availableBoxsArroud(this.getCurrentBox());
            if(lb.size()>= 1 ){
                //Choisit le chemin le plus court
                var toGo = lb.get(0);
                for (var b : lb) {
                    int distanceX = toGo.posX - box.posX;
                    distanceX = (distanceX < 0 ? -distanceX : distanceX);
                    int distanceY = toGo.posY - box.posY;
                    distanceY = (distanceY < 0 ? -distanceY : distanceY);
                    int distancebX = b.posX - box.posX;
                    distancebX = (distancebX < 0 ? -distancebX : distancebX);
                    int distancebY = b.posY - box.posY;
                    distancebY = (distancebY < 0 ? -distancebY : distancebY);
                    if (distancebX <= distanceX && distancebY <= distanceY) {
                        toGo = b;
                    }
                }
                //Va a la case
                moveToBox(toGo);
            }
        }
    }

    //lance un sort
    public int spellDraw() {
        energiePoints -= 50;
        //Nombre aléatoire entre 1 et 20
        return ThreadLocalRandom.current().nextInt(1, 20 + 1);
    }

    //Va sur une case donnée
    @Override
    public void moveToBox(Box box) {
        this.getCurrentBox().busy = false;
        this.setCurrentBox(box);
        this.getCurrentBox().busy = true;
    }

    //Methode qui ne renvoie que les cases où le student peut aller
    public List<Box> shortListWithHouse(@NotNull List<Box> boxes) {
        List<Box> shortedList = new ArrayList<>();
        for (var box : boxes) {
            //Ne peux aller uniquement dans les zone de combat (blanc) ou dans la safezone
            if (box.color.equals("white") || box.color.equals(getHouse().getSafeZone().getColor())) {
                shortedList.add(box);
            }
        }
        return shortedList;
    }

    //Vérifie qu'un etudiant est bien dans sa safezone
    public Boolean checkIfInSafeZone() {
        return this.getHouse().getSafeZone().getZone().contains(this.getCurrentBox());
    }

    //Rejoint la safezone si les points d'énergie, si la liste de messages est vide ou s'il a obtenu un nouveau message
    public Boolean needToGoToSafeZone(){return (checkEnergiePoints() || getMessages().isEmpty() || hasObtainNewMessages);}
    public Boolean isAttackable() {
        return (status.equals("alive") && !wasAttacked && !checkIfInSafeZone() && sizeOfMessages()>0);
    }

    //Methode utilisée par les dementors afin de savoir si un wizard est attaquable
    public Boolean isAttackableByDementor(){
        return (status.equals("alive") && !checkIfInSafeZone() && sizeOfMessages()>0);
    }

    //Verifie si le wizard peut se déplacer
    public Boolean canMove(){return Game.isGameStarted && status.equals("alive") && !dementorAttacked;}

    //En fonction de l'énergie que faire ?
    public Boolean checkEnergiePoints() {
        //Si l'énergie est < 20% = safezone
        if (energiePoints <= (Game.MAX_ENERGIE * 0.20) && energiePoints >= 0) {
            System.out.println(this.getName() + "Need to go back to the safezone");
            return true;
        }
        //Si l'énergie est vide
        else if (energiePoints <= 0) {
            //Mort
            status = "dead";
            return false;
        } else {
            return false;
        }
    }

    //Méthode de la classe mère permet d'obtenir un message
    @Override
    public void obtainMessage(@NotNull Wizard wizard) {
        //Si le message vient du master
        if(wizard instanceof MasterWizard) {
            //on appelle la fonction du maitre pour obtenir un message
            getMessages().add(wizard.dropMessage(0));
        }else {
            //On prend tous les messages du wizard ennemi
            System.out.println("Getting All messages from"+ wizard.getName());
            for(var i = 0 ; i < wizard.sizeOfMessages(); i++){
                var message = wizard.dropMessage(i);
                //Vérifie les doublons
                if(!getMessages().contains(message)){
                    getMessages().add(message);
                }
            }
            //Safezone parce qu'on a obtenu un message
            hasObtainNewMessages = true;
        }
    }

    //Methode de la classe mere, en cas de perte de combat
    @Override
    public Message dropMessage(int index) {
        if (!wasAttacked) {
            wasAttacked = true;
        }
        //renvoie en effacant un message de la liste
        return getMessages().remove(index);
    }
}
