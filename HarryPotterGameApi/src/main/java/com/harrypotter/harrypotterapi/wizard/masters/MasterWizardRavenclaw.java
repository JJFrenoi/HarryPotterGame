package com.harrypotter.harrypotterapi.wizard.masters;

import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.house.Ravenclaw;

//Singleton maitre de la maison Ravenclaw
public class MasterWizardRavenclaw extends MasterWizard {
    private static MasterWizard instance = null;
    private MasterWizardRavenclaw() {
        super("Filius Flitwick",Ravenclaw.retrieveInstance(), Game.map.height-1,Game.map.width-1);
    }
    //Permet d'initialiser si l'intance n'est pas initialisée et de récupérer l'instance
    public static MasterWizardRavenclaw retrieveInstance() {
        if(instance == null){
            instance = new MasterWizardRavenclaw();
        }
        return (MasterWizardRavenclaw) instance;
    }
    //Appelée pour créer une nouvelle partie
    @Override
    public void reset() {
        instance = null;
    }
}
