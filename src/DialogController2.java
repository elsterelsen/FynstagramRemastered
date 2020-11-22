import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.tools.javac.Main;
import ea.Bild;
import ea.Knoten;
import ea.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Diese Klasse soll ein Dialogfenster bestehend aus Formen/Texten/Knöpfen handeln.
 * Ausßerdem soll es aus Textdateien die Dialoge laden können und anzeigen
 * <p>
 * Liest möglicherweise eine XML aus mit Codes
 * Jeder Dialog hat außerdem max. 2 Optionen und die Codes für den anschließenden Dialog.
 * Lange Dialoge werden in kleinere unterteilt und haben dann nur 1 Option.
 */

public class DialogController2 extends Knoten {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private final String dialogLinesPath = "./Assets/Files/Dialoge.json";
    private final String dialogPacketsPath = "./Assets/Files/DialogPackets.json";


    //GLOBAL STUFF;
    private String globalTemporalPosition =  "01";
    ArrayList<String> foundItems = new ArrayList<String>();
    ArrayList<String> readLines = new ArrayList<String>();

    private boolean active = false;
    private boolean waitingForInput = false;


    private NpcController2 NPC_Controller2;

    private Map<String, DialogController2.DialogLine> dialogLines; //für die Json mit den DialogZeilen
    private Map<String, Map<String, DialogPacket>> dialogPackets; //für die Json mit den DialogPackets

    //VISIBLE STUFF;
    private String defaultPath = "./Assets/Dialoge/";
    private Text displayTextObject;
    private Bild displayDialogBackground;
    private Bild[] displayButtons;
    private final int textPosY = 800;

    //DIALOG LINE STUFF;
    private String currentDialogCode;

    //WAHL;
    private int buttonCursor = 0;


    public DialogController2(NpcController2 NPC_C2) {
        this.NPC_Controller2 = NPC_C2;
        //initialisert
        readJSON_DialogLines();
        readJSON_DialogPackets();

        addDisplayObjects();
    }

    private void addDisplayObjects() {
        int textPosX = MAIN.x / 2;
        displayButtons = new Bild[2];


        //Bilder mit try catch
        try {
            displayDialogBackground = new Bild(defaultPath + "DialogFenster.png");
            displayDialogBackground.positionSetzen(textPosX - displayDialogBackground.getBreite()/2, textPosX);
            displayButtons[0] = new Bild(defaultPath + "ButtonWahl0.png");
            displayButtons[0].positionSetzen(400,textPosY);
            displayButtons[1] = new Bild(defaultPath + "ButtonWahl1.png");
            displayButtons[1].positionSetzen(700,textPosY);
            this.add(displayDialogBackground);
            this.add(displayButtons[0], displayButtons[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DialogController2: FEHLER beim Importieren der Bilder");
        }

        //Text als letztes also ganz oben
        displayTextObject = new Text(textPosX, textPosY, "DEFAULT TEXT");
        this.add(displayTextObject);
    }

    private void showWindow() {
        displayButtons[0].sichtbarSetzen(true);
        displayButtons[1].sichtbarSetzen(true);
        displayTextObject.sichtbarSetzen(true);
        displayDialogBackground.sichtbarSetzen(true);
    }

    private void hideWindow() {
        displayButtons[0].sichtbarSetzen(false);
        displayButtons[1].sichtbarSetzen(false);
        displayTextObject.sichtbarSetzen(false);
        displayDialogBackground.sichtbarSetzen(false);
    }

    private void updateText() {
        buttonCursor = 0;
        DialogLine currentLine = dialogLines.get(currentDialogCode);
        displayTextObject.inhaltSetzen(currentLine.inhalt);

        int width = (int) displayTextObject.getBreite();
        int posX_new = MAIN.x / 2 - width / 2;
        displayTextObject.positionSetzen(posX_new, textPosY);
    }

    private void advanceDialogLine() {
        DialogLine currentLine = dialogLines.get(currentDialogCode);
        if (buttonCursor == 0) {
            currentDialogCode = currentLine.Wahl1;
        }
        if (buttonCursor == 1) {
            currentDialogCode = currentLine.Wahl2;
        }
        updateText();
    }

    public void openDialogPacket(String NpcID) {
        if (!active) {
            active = true;
            DialogController2.DialogPacket element = dialogPackets.get(globalTemporalPosition).get(NpcID);
            currentDialogCode = element.code;
            updateText();
            waitingForInput = true;
        } else {
            System.out.println("DialogController2: Dialog schon offen");
        }


    }

    public boolean isDialogPacketPlayable(String NpcID) {
        Map<String, DialogPacket> innnerPacketMap = dialogPackets.get(globalTemporalPosition);
        DialogPacket element = innnerPacketMap.get(NpcID);   //stellt jedes Element der Map einmal als "element" zur Verfügung
        if(element == null){
            System.out.println("DialogController2: KEIN NPC DER IN DER AN DIESEM ZEITPUNKT ABSPIELEN KANN");
        }

        if (foundItems.containsAll(element.requiredItems)) {
            if (readLines.containsAll(element.requiredLines)) {
                System.out.println("DialogController2: Es sind alle nötigen Items und Zeilen vorhanden");
                return true;
            } else {
                System.out.println("DialogController2: Es sind alle nötigen Items vorhanden, aber nicht alle Zeilen");
                return false;
            }
        } else {
            System.out.println("DialogController2: Es sind nicht alle nötigen Items gefunden worden");
            return false;
        }

    }

    public void input(String dir) {
        displayButtons[0].setOpacity(0.3f);
        displayButtons[1].setOpacity(0.3f);
        if (isWaitingForInput()) {
            switch (dir) {
                case "links":
                    buttonCursor--;
                    if (buttonCursor < 0) {
                        buttonCursor = 0;
                    }
                    break;
                case "rechts":
                    buttonCursor++;
                    if (buttonCursor > 1) {
                        buttonCursor = 1;
                    }
                    break;
                case "enter":
                    advanceDialogLine();
                    break;

                default:
                    System.out.println("DialogController2: Kein valider Input");
            }
            displayButtons[buttonCursor].setOpacity(1f);
        } else {
            System.out.println("DialogController2: WARTET NICHT AUF INPUT");
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    private void readJSON_DialogLines() {
        Gson gson = new Gson();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dialogLinesPath));

            Type MapType = new TypeToken<Map<String, DialogController2.DialogLine>>() {
            }.getType();
            dialogLines = gson.fromJson(bufferedReader, MapType);
            System.out.println();
            System.out.println(ANSI_GREEN + "DialogController2: JSON(" + dialogLinesPath + ")  erfolgreich gelesen" + ANSI_RESET);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ANSI_PURPLE + "DialogController2: Ein Fehler beim Lesen der Json Datei(" + dialogLinesPath + " ). Entweder Pfad flasch, oder JSON Struktur." + ANSI_RESET);

        }

    }

    private void readJSON_DialogPackets() {
        Gson gson = new Gson();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dialogPacketsPath));

            Type MapType = new TypeToken<Map<String, Map<String, DialogPacket>>>() {
            }.getType();
            dialogPackets = gson.fromJson(bufferedReader, MapType);
            System.out.println(ANSI_GREEN + "DialogController2: JSON(" + dialogPacketsPath + ")  erfolgreich gelesen" + ANSI_RESET);
            //System.out.println("ANTWORT: " + dialogPackets.get("01").get("11").NpcID);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ANSI_PURPLE + "DialogController2: Ein Fehler beim Lesen der Json Datei(" + dialogPacketsPath + " ). Entweder Pfad flasch, oder JSON Struktur." + ANSI_RESET);

        }

    }


    /**
     * JF:
     * Das ist die Klasse die als Muster zum Auslesen des JSON (mit GSON) dient.
     * Alle Methoden hierdrinn sind also nur für eine Textzeile im allgemeinen verwendbar und selten brauchbar.
     * Eigentlich muss in dieser Klasse nicht geändert werden
     */
    public class DialogLine {

        String inhalt; //Text der Dialog Zeile

        String Wahl1; // Code der Ersten Wahl
        String Wahl2; // Code der Zweiten Wahl

    }

    public class DialogPacket {
        //key Time also "01!

        ArrayList<String> requiredItems;
        ArrayList<String> requiredLines;

        String NpcID; //Spieler bei dem der Dialog anfängt
        String code; // erster Code des Dialogs

    }
}