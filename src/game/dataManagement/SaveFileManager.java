package game.dataManagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Date;

public class SaveFileManager {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";


    private final static String gameSaveFilePathTemplate="Assets/Files/saveJsons/GameSave_?.json";
    private final static String npcSaveFilePathTemplate="Assets/Files/saveJsons/NpcSave_?.json";
    private final static String extraInformationPathTemplate="Assets/Files/saveJsons/ExtraInformation_?.json";


    public static ExtraInformation readExtraInformationJSON(String path) {
        ExtraInformation information;
            Gson gson = new GsonBuilder().create();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                Type JsonType = new TypeToken<ExtraInformation>() {
                }.getType();

                information = gson.fromJson(bufferedReader, JsonType);
                System.out.println(ANSI_GREEN + "game.dataManagement.GameSaver: JSON(" + path + ") erfolgreich gelesen" + ANSI_RESET);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ANSI_PURPLE + "game.dataManagement.GameSaver: Ein Fehler beim Lesen der Json Datei(" + path + "). Entweder Pfad flasch, oder JSON Struktur." + ANSI_RESET);
                return null;
            }
        return information;
    }
    public static void saveExtraInformation(String path,ExtraInformation info){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileOutputStream fout = new FileOutputStream(path);
            fout.write(gson.toJson(info).getBytes());
            fout.close();
            System.out.println(ANSI_GREEN + "game.dataManagement.GameSaver: JSON(" +  path + ") erfolgreich gespeichert" + ANSI_RESET);
        }
        catch(Exception e) {
            System.out.println(ANSI_PURPLE + "Ein Fehler beim Schreiben der Json Datei. Entweder Pfad flasch, oder JSON Struktur." + ANSI_RESET);
        }
        //readJSON(); //update saveState
        System.out.println("Speichervorgang beendet um: " + new Date().toString() );
    }

    public static void main(String[] args) {
        System.out.println("Starte Zurücksetzen der Gamesavedaten");
        resetData();
        System.out.println("Zurücksetzen beendet");
    }
    public static void resetData() {
        for (int i = 0; i <3;i++) {
            new NewGameLoader(new String(npcSaveFilePathTemplate).replace('?',(char)(i+48)),new String(gameSaveFilePathTemplate).replace('?',(char)(i+48)));
            saveExtraInformation(new String(extraInformationPathTemplate).replace('?',(char)(i+48)),new ExtraInformation(true));
        }
    }
}
