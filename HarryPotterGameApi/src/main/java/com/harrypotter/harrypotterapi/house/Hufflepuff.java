package com.harrypotter.harrypotterapi.house;

import com.harrypotter.harrypotterapi.alliance.AllianceHR;
import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.masters.MasterWizardHufflepuff;

import java.util.Arrays;
import java.util.List;
//Singleton de la maison Hufflepuff de l'alliance  AllianceHR
public class Hufflepuff extends House implements AllianceHR {
    private static House instance = null;
   //Singleton
    public Hufflepuff() {
        super(1, 'H',
                Game.map.getSafezones().get(1), "hufflepuff");
    }
    //Permet d'initialiser si l'instance n'est pas initialisée et de récupérer l'instance
    public static Hufflepuff retrieveInstance() {
        if (instance == null) {
            instance = new Hufflepuff();
        }
        return (Hufflepuff) instance;
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
        this.masterWizard = MasterWizardHufflepuff.retrieveInstance();
    }
    //methode de l'alliance, communication entre sorcier
    @Override
    public void encounterOfStudent(Wizard wizard1, Wizard wizard2, House house) {
        encounterOfWizards(wizard1, wizard2, house, this);
    }
    //methode de l'alliance, retourne tous les membres de l'alliance
    @Override
    public List<House> memberOfAllianceGS() {
        return Arrays.asList(this, Ravenclaw.retrieveInstance());
    }
    //methode de l'allicance retourne le nom de l'alliance
    @Override
    public String getAllianceName() {
        return allianceName;
    }
}
