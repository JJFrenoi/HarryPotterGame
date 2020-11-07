package com.harrypotter.harrypotterapi.wizard.masters;

import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.house.Hufflepuff;
//Singleton maitre de la maison Hufflepuff
public class MasterWizardHufflepuff extends MasterWizard {
    private static MasterWizard instance = null;

    private MasterWizardHufflepuff() {
        super("Pomona Sprout",Hufflepuff.retrieveInstance(),Game.map.height-1, 0);
    }

    //Permet d'initialiser si l'intance n'est pas initialisée et de récupérer l'instance
    public static MasterWizardHufflepuff retrieveInstance() {
        if (instance == null) {
            instance = new MasterWizardHufflepuff();
        }
        return  (MasterWizardHufflepuff) instance;
    }

    //Appelée pour créer une nouvelle partie
    @Override
    public void reset() {
        instance = null;
    }
}
