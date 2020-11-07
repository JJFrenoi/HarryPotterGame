package com.harrypotter.harrypotterapi.alliance;

import com.harrypotter.harrypotterapi.house.House;
import com.harrypotter.harrypotterapi.wizard.Wizard;

import java.util.List;

public interface Alliance {
    //Appelée chaque round quand des sorciers se rencontrent
    void encounterOfStudent(Wizard wizard1, Wizard wizard2, House house);

    //Liste des membres de l'alliance
    List<House> memberOfAllianceGS();

    //Nom de l'alliance
    String getAllianceName();
}
