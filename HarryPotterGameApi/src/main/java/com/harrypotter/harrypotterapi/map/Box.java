package com.harrypotter.harrypotterapi.map;
//Classe représentant une position dans laquelle un sorcier peut venir
public final class Box {
    public Boolean busy = false;
    //Crée un obstacle
    public Boolean isObstacle = false;
    public final Long id;
    public final int posX;
    public final int posY;
    public final String color;
    Box(long id, int posX, int posY, String color) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }
    public void print(){
        System.out.println("Box " + id + " pos "+ posX +","+ posY+" color "+color );
    }

    //Compare la distance entre deux case
    public int[] compare(Box otherBox){
        int [] compare = new int[2];
        int compareX = this.posX - otherBox.posX;
        int compareY = this.posY - otherBox.posY;
        compare[0] = (compareX < 0 ) ? -compareX : compareX;
        compare[1] = (compareY < 0 ) ? -compareY : compareY;
        return compare;
    }
}
