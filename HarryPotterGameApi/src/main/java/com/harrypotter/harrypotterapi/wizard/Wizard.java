package com.harrypotter.harrypotterapi.wizard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harrypotter.harrypotterapi.alliance.Alliance;
import com.harrypotter.harrypotterapi.house.House;
import com.harrypotter.harrypotterapi.map.Box;
import com.harrypotter.harrypotterapi.message.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
//Classe mère de tous les sorciers
public abstract class Wizard {
    private final long id;
    private final String name;
    private final House house;
    private final ArrayList<Message> messages;
    private final Boolean isMaster;
    //private final String picUrl;
    private final Box starterBox;
    private Box currentBox;
    private final Alliance alliance;

    public Wizard(long id, House house, String name, Boolean isMaster, Box starterBox) {
        this.house = house;
        this.alliance = (Alliance) house;
        this.id = id;
        this.name = name;
        this.isMaster = isMaster;//Pour le front
        this.starterBox = starterBox;
        this.currentBox = this.getStarterBox();
        //Game.map.setBoxToBusy(this.currentBox);
        this.currentBox.busy = true;
        this.messages = new ArrayList<>();
        //this.picUrl = data.getString("image").replace("http://hp-api.herokuapp.com/images/", "");
        //System.out.println(this.picUrl);
    }

    public Box getCurrentBox() {
        return currentBox;
    }

    @JsonIgnore
    public Alliance getAlliance() {
        return alliance;
    }

    public void setCurrentBox(Box currentBox) {
        this.currentBox = currentBox;
    }

    public House getHouse() {
        return house;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getMaster() {
        return isMaster;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Box getStarterBox() {
        return starterBox;
    }

    @Override
    public String toString() {
        return "Wizard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", house=" + house +
                ", messages=" + messages +
                ", starterBox=" + starterBox +
                '}';
    }

    //Obtient un message
    public abstract void obtainMessage(@NotNull Wizard wizard) ;

    //Donne un message
    public abstract Message dropMessage(int index);

    //Renvoie un message aléatoire
    protected Message randomMessageFromMessages(){
        if (sizeOfMessages()>0){
            return getMessages().get(ThreadLocalRandom.current().nextInt(0, getMessages().size()));
        }else {
            return null;
        }
    }

    //Nombre de message
    public int sizeOfMessages() {
        return getMessages().size();
    }
}
