package com.harrypotter.harrypotterapi;

import com.harrypotter.harrypotterapi.map.Box;
//Interface des objets mobiles
public interface MovableUnit extends Runnable {
    Thread move();
    void moveToBox(Box box);
}
