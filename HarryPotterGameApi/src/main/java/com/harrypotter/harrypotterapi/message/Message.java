package com.harrypotter.harrypotterapi.message;
//Classe représentant un message
public final class Message {
    private final int id;
    //Contenu du message
    private final String message;

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
