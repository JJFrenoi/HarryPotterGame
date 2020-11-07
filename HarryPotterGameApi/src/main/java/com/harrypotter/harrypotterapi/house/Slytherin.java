package com.harrypotter.harrypotterapi.house;

import com.harrypotter.harrypotterapi.alliance.AllianceGS;
import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.masters.MasterWizardSlytherin;

import java.util.Arrays;
import java.util.List;
//Singleton de la maison Slytherin  de l'alliance  AllianceGS
public class Slytherin extends House implements AllianceGS {
    private static House instance = null;
    //Singleton
    private Slytherin() {
        super(2, 'S',
                Game.map.getSafezones().get(2), "slytherin");
    }
    //Permet d'initialiser si l'instance n'est pas initialisée et de récupérer l'instance
    public static Slytherin retrieveInstance() {
        if (instance == null) {
            instance = new Slytherin();
        }
        return (Slytherin) instance;
    }
    //Réinisialise pour pouvoir relancer des parties
    @Override
    public void reset() {
        super.reset();
        instance = null;
    }
    //Initialise le maitre
    @Override
    public void setMasterWizard() {
        this.masterWizard = MasterWizardSlytherin.retrieveInstance();

    }
    //methode de l'alliance, communication entre sorcier
    @Override
    public void encounterOfStudent(Wizard wizard1, Wizard wizard2, House house) {
        encounterOfWizards(wizard1, wizard2, house, this);
    }
    //methode de l'alliance, retourne tous les membres de l'alliance
    @Override
    public List<House> memberOfAllianceGS() {
        return Arrays.asList(this, Gryffindor.retrieveInstance());
    }
    //methode de l'allicance retourne le nom de l'alliance
    @Override
    public String getAllianceName() {
        return allianceName;
    }
}
