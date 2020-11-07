package com.harrypotter.harrypotterapi.map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
//Classe créant la carte dans laquelle évolue les sorciers
public class Map {
    private final AtomicLong counter = new AtomicLong();
    private final long id;
    //Tableau de zone de non combat
    private final List<SafeZone> safezones = new ArrayList<>(4);
    //Zones de combat
    private final List<Box> whiteZone = new ArrayList<>();
    public int width;
    public int height;
    //Matrice représentant la carte
    private Box[][] zone;

    public Map(long id, int width, int height, int numberOfObstacle)
    {
        this.id = id;
        this.width = width;
        this.height = height;
        zone = new Box[height][width];
        //Crée les safezones pour chaque maison
        for (var i = 0; i < 4; i++) {
            safezones.add(i, new SafeZone(4, 4, i));
        }
        //Crée l'ensemble de la carte
        for (var i = 0; i < height; i++) {
            for (var j = 0; j < width; j++) {
                //Crée une matrice 4 * 4  de la maison Gryffindor
                if (i < 4 && j < 4) {
                    Box tmp = new Box(counter.incrementAndGet(), i, j, "red");
                    safezones.get(0).pushToZone(tmp);
                    zone[i][j] = tmp;
                }
                //Crée une matrice 4 * 4  de la maison Hufflepuff
                else if (i > height - 5 && j < 4) {
                    Box tmp = new Box(counter.incrementAndGet(), i, j, "yellow");
                    safezones.get(1).pushToZone(tmp);
                    zone[i][j] = tmp;
                }
                //Crée une matrice 4 * 4  de la maison Slytherin
                else if (i < 4 && j > width - 5) {
                    Box tmp = new Box(counter.incrementAndGet(), i, j, "green");
                    safezones.get(2).pushToZone(tmp);
                    zone[i][j] = tmp;
                }
                //Crée une matrice 4 * 4  de la maison Ravenclaw
                else if (i > height - 5 && j > width - 5) {
                    Box tmp = new Box(counter.incrementAndGet(), i, j, "blue");
                    safezones.get(3).pushToZone(tmp);
                    zone[i][j] = tmp;
                }
                //Crée le reste de la carte, zone de combat
                else {
                    Box tmp = new Box(counter.incrementAndGet(), i, j, "white");
                    zone[i][j] = tmp;
                    whiteZone.add(tmp);
                }
            }
        }
        spawnObstacle(numberOfObstacle);
        printZone();
        printBusyMap();
    }

    public Box[][] getZone() {
        return zone;
    }

    public long getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<SafeZone> getSafezones() {
        return safezones;
    }

    //Affichage terminal de la carte
    private void printZone() {
        for (var r : zone) {
            for (var b : r) {
                System.out.print(b.posX + "," + b.posY + " ");
            }
            System.out.println();
        }
    }

    //Fonction qui renvoie toutes les cases disponibles autour d'une case
    public List<Box> availableBoxsArroud(Box b) {
        List<Box> lb = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                var tmpB = retrieveBoxFromPos(b.posX - i, b.posY - j);
                if (tmpB != null && !tmpB.id.equals(b.id) && !tmpB.busy) {
                    lb.add(tmpB);
                }
            }
        }
        return lb;
    }

    //Fonction qui renvoie une case en fonction d'une position
    public Box retrieveBoxFromPos(int posX, int posY) {
        for (Box[] r : zone) {
            for (Box b : r) {
                if (b.posX == posX && b.posY == posY) {
                    return b;
                }
            }
        }
        return null;
    }

    //Crée un obstacle à partir d'une case
    @JsonIgnore
    public void setBoxToObstacle(Box box) {
        box.busy = true;
        box.isObstacle = true;
    }

    //Crée des obstacles sur la carte
    public void spawnObstacle(int number) {
        for (var i = 0; i < number; i++) {
            Box obstacle = whiteZone.get(ThreadLocalRandom.current().nextInt(0, whiteZone.size()));
            setBoxToObstacle(obstacle);
        }
    }

    //Renvoie une case aléatoire si possible
    public Box randomBoxOnMap() {
        var nonBusyBox = nonBusyBox();
        return nonBusyBox.size() > 0 ? nonBusyBox.get(ThreadLocalRandom.current().nextInt(0, nonBusyBox.size())) : null;
    }

    //Affiche la carte en fonction des cases occupées
    public void printBusyMap() {
        for (var r : zone) {
            for (var b : r) {
                System.out.print(b.busy + " ");
            }
            System.out.println();
        }
    }

    //Recherche toutes les cases non occupées
    public List<Box> nonBusyBox() {
        return whiteZone.stream().filter(box -> !box.busy).collect(Collectors.toList());
    }
}
