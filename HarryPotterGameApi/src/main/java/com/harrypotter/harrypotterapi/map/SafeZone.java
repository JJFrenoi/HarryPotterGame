package com.harrypotter.harrypotterapi.map;

import java.util.ArrayList;
import java.util.List;
//Classe représentant une safezone pour une maison
public class SafeZone {
    private final int id;
    private final int height;
    private final int width;
    private final String color;
    //Liste des cases de la carte
    private final List<Box> zone;

    public SafeZone(int height, int width, int id) {
        String color1;
        this.id = id;
        this.height = height;
        this.width = width;
        zone = new ArrayList<>(height * width);
        switch (id) {
            default:
                color1 = "red";
            case 3:
                color1 = "blue";
                break;
            case 2:
                color1 = "green";
                break;
            case 1:
                color1 = "gold";
                break;
            case 0:
                color1 = "red";
                break;

        }
        color = color1;
    }

    public int getId() {
        return id;
    }

    public List<Box> getZone() {
        return zone;
    }

    public String getColor() {
        return color;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void pushToZone(Box b) {
        zone.add(b);
    }

    //Renvoie la première case disponible
    public Box availibleBox() {
        for (var box : zone) {
            if (!box.busy) {
                //box.busy = true;
                return box;
            }
        }
        return null;
    }
}
