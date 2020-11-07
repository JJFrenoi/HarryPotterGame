package com.harrypotter.harrypotterapi.wizard.masters;

import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.house.Slytherin;
//Singleton maitre de la maison Slytherin
public class MasterWizardSlytherin extends MasterWizard {
    private static MasterWizard instance = null;

    public MasterWizardSlytherin() {
        super("Severus Snape",Slytherin.retrieveInstance(),0, Game.map.width-1);
    }

    //Permet d'initialiser si l'intance n'est pas initialisée et de récupérer l'instance
    public static MasterWizardSlytherin retrieveInstance() {
        if (instance == null) {
            instance = new MasterWizardSlytherin();
        }
        return (MasterWizardSlytherin) instance;
    }

    //Appelée pour créer une nouvelle partie
    @Override
    public void reset() {
        instance = null;
    }
}
