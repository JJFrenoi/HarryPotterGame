package com.harrypotter.harrypotterapi.house;

import com.harrypotter.harrypotterapi.alliance.AllianceHR;
import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.masters.MasterWizardRavenclaw;

import java.util.Arrays;
import java.util.List;
//Singleton de la maison  Ravenclaw de l'alliance  AllianceHR
public class Ravenclaw extends House implements AllianceHR {
    private static House instance = null;

    public Ravenclaw() {
        super(3, 'R',
                Game.map.getSafezones().get(3), "ravenclaw");

    }
    //Permet d'initialiser si l'instance n'est pas initialisée et de récupérer l'instance
    public static Ravenclaw retrieveInstance() {
        if (instance == null) {
            instance = new Ravenclaw();
        }
        return (Ravenclaw) instance;
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
        this.masterWizard = MasterWizardRavenclaw.retrieveInstance();
    }
    //methode de l'alliance, communication entre sorcier
    @Override
    public void encounterOfStudent(Wizard wizard1, Wizard wizard2, House house) {
        encounterOfWizards(wizard1, wizard2, house, this);
    }
    //methode de l'alliance, retourne tous les membres de l'alliance
    @Override
    public List<House> memberOfAllianceGS() {
        return Arrays.asList(this, Hufflepuff.retrieveInstance());
    }
    //methode de l'allicance retourne le nom de l'alliance
    @Override
    public String getAllianceName() {
        return allianceName;
    }
}
