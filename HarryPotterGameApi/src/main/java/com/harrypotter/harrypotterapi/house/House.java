package com.harrypotter.harrypotterapi.house;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harrypotter.harrypotterapi.alliance.Alliance;
import com.harrypotter.harrypotterapi.map.SafeZone;
import com.harrypotter.harrypotterapi.wizard.Wizard;
import com.harrypotter.harrypotterapi.wizard.masters.MasterWizard;
import com.harrypotter.harrypotterapi.wizard.student.StudentWizard;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
//Classe mère des maisons
public abstract class House {
    private final int id;
    private final char icon;
    private final String name;
    private final SafeZone safeZone;
    protected MasterWizard masterWizard;
    private JSONArray data;

    public House(int id, char icon,
                 SafeZone safeZone, String name) {
        this.id = id;
        this.icon = icon;
        this.safeZone = safeZone;
        this.name = name;

        OkHttpClient client = new OkHttpClient();
        //Requête sur api externe
        Request request = new Request.Builder()
                .url("http://hp-api.herokuapp.com/api/characters/house/" + name)
                .build();

        try (Response response = client.newCall(request).execute()) {
            var responseBody = response.body();
            assert responseBody != null;
            data = new JSONArray(responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Réinitialise les maitre sorciers
    public void reset() {
        this.masterWizard.reset();
    }

    public abstract void setMasterWizard();

    //Retourne le nom de la maison
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public MasterWizard getMasterWizard() {
        return this.masterWizard;
    }

    public SafeZone getSafeZone() {
        return this.safeZone;
    }

    public char getIcon() {
        return this.icon;
    }

    //Génére un personnage aléatoire de l'api externe
    public JSONObject randomCharactere() {
        return data.getJSONObject(ThreadLocalRandom.current().nextInt(0, data.length()));
    }

    //retourne le nombres de message des messages
    public int sizeOfMessages() {
        return getMasterWizard().sizeOfMessages();
    }

    //methode si deux sorciers de maison différentes et d'alliance différentes sont a une case d'écart
    public void attack(StudentWizard wizard, StudentWizard wizard2) {
        //Si attaqué par dementor puissance du sort = 0
        var spellA = wizard.getDementorAttacked() ? 0 : wizard.spellDraw();
        var spellB = wizard2.getDementorAttacked() ? 0 : wizard2.spellDraw();
        if (spellA < spellB) {
            wizard2.obtainMessage(wizard);
        } else if (spellA > spellB) {
            wizard.obtainMessage(wizard2);
        }
    }

    //méthode appelé afin de partager des message entre sorcier de même maison ou de même alliance
    public void shareMessages(Wizard wizard1, Wizard wizard2, int maxMessages) {
        //La liste de message a partager
        var subList = wizard1.getMessages().subList(0, maxMessages);
        subList.forEach(message -> {
            if (!wizard2.getMessages().contains(message)) {
                wizard2.getMessages().add(message);
            }
        });
    }

    //méthode appelé à chaque round dans le cas d'une rencontre entre sorcier
    public void encounterOfWizards(Wizard wizard1, Wizard wizard2, House house, Alliance alliance) {
        try {
            //Si les sorcier font parti de la meme maison
            if (this.equals(house)) {
                System.out.println("Same House");
                if (wizard1.sizeOfMessages() > 0) {
                    //Partage de tous les messages
                    shareMessages(wizard1, wizard2, wizard1.sizeOfMessages());
                } else {
                    System.out.println("Wizard messages = 0");
                }
                //Si les sorciers font parti de la même alliance
            } else if (alliance.memberOfAllianceGS().contains(house)) {
                System.out.println("Same Alliance");
                if (wizard1.sizeOfMessages() > 0 && wizard2.sizeOfMessages() > 0) {
                    //Partage un nombre aléatoire de message
                    shareMessages(wizard1, wizard2, ThreadLocalRandom.current().nextInt(0, wizard1.getMessages().size()));
                } else {
                    System.out.println("Wizard messages = 0");
                }
                //Si les sorciers sont ennemies
            } else {
                System.out.println("Ennemie");
                var student1 = (StudentWizard) wizard1;
                var student2 = (StudentWizard) wizard2;
                //Vérifie que les deux sorciers sont attaquable
                if (student1.isAttackable() && student2.isAttackable()) {
                    attack(student1, student2);
                }else {
                    System.out.println("Not Attackable");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
