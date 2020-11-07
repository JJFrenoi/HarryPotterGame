package com.harrypotter.harrypotterapi.wizard.masters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harrypotter.harrypotterapi.game.Game;
import com.harrypotter.harrypotterapi.house.House;
import com.harrypotter.harrypotterapi.message.Message;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.student.StudentWizard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

//Classe mère des maitres sorcier
public abstract class MasterWizard extends Wizard {
    private final AtomicLong counter = new AtomicLong();
    private final List<StudentWizard> students = new ArrayList<>();

    public MasterWizard(String name, House house, int line, int column) {
        super(house.getId(), house, name, true, Game.map.retrieveBoxFromPos(line, column));
    }

    //Chaque maitre a une liste d'étudiants sorciers
    @JsonIgnore
    public List<StudentWizard> getStudents() {
        return students;
    }

    //Initilisation des étudiants
    public void initStudents(int nbOfStudent) {
        for (var i = 0; i < nbOfStudent; i++) {
            var student = new StudentWizard(counter.incrementAndGet(), getHouse(), getHouse().randomCharactere().getString("name"));
            students.add(student);
        }
    }

    //Update tous les étudiants dans la safezone
    public void checkIfStudentIsInSafeZoneAndGetMessage() {
        students.forEach(
                studentWizard -> {
                    if (studentWizard.checkIfInSafeZone()) {
                        //N'est plus non attaquable
                        studentWizard.resetWasAttack();
                        //Remonte l'énergie des étudiants dans la safezone
                        studentWizard.regenerationInSafeZone();
                        //met a jour les messages obtenus par les étudiants
                        obtainMessage(studentWizard);
                        if (studentWizard.sizeOfMessages() == 0) {
                            //Donne un message si l'étudiant a perdu tous ses messages
                            studentWizard.obtainMessage(this);
                        }
                    }
                }
        );
    }

    //Permet de reset tous les singleton
    public abstract void reset();

    //Methode de Wizard, obtient les messages obtenu par les étudiants
    @Override
    public void obtainMessage(@NotNull Wizard wizard) {

        for (Message message : wizard.getMessages()) {
            if(!getMessages().contains(message)){
                getMessages().add(message);
            }
        }
    }

    //Methode de Wizard, renvoie un message aléatoire
    @Override
    public Message dropMessage(int index) {
        return randomMessageFromMessages();
    }
}
