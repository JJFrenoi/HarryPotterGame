package com.harrypotter.harrypotterapi.wizard.masters;

import com.harrypotter.harrypotterapi.house.Gryffindor;

//Singleton maitre de la maison Gryffindor
public class MasterWizardGryffindor extends MasterWizard {
    private static MasterWizard instance = null;

    private MasterWizardGryffindor() {
        super("Minerva McGonagall", Gryffindor.retrieveInstance(), 0, 0);
    }

    //Permet d'initialiser si l'intance n'est pas initialisée et de récupérer l'instance
    public static MasterWizardGryffindor retrieveInstance() {
        if (instance == null) {
            instance = new MasterWizardGryffindor();
        }
        return (MasterWizardGryffindor) instance;
    }
    //Appelée pour créer une nouvelle partie
    @Override
    public void reset() {
        instance = null;
    }
}
