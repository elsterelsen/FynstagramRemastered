package game.screen;
/*
 * Bereitet einen Startbildschim vor und zeigt diesen auch an.
 * Hier kann ausgewählt werden zwischen:
 * - Neues Spiel starten
 * - SaveGame Laden
 * - LEER
 * - Über Spiel
 * <p>
 * ButtonBenennung:
 * -ButtonX.png
 * -X von 0 bis ButtonCount(z.B 4)
 */

import ea.*;
import game.MAIN;
import game.SPIEL;
import game.dataManagement.ExtraInformation;
import game.dataManagement.NewGameLoader;
import game.dataManagement.SaveFileManager;


public class StartingScreen extends Knoten implements Screen{

    private Bild BackgroundPic;
    private Bild loadingPic;

    private final String gameSaveFilePathTemplate="Assets/Files/saveJsons/GameSave_?.json";
    private final String npcSaveFilePathTemplate="Assets/Files/saveJsons/NpcSave_?.json";
    private final String extraInformationPathTemplate="Assets/Files/saveJsons/ExtraInformation_?.json";
    private final int buttonCount = 5;
    private Bild[] standardButtons = new Bild[buttonCount];
    private Knoten standardButtonGroup;

    private Bild[] saveButtons = new Bild[buttonCount];
    private Knoten saveButtonGroup;

    private final int saveFileCount=3;
    private final boolean[] saveFileEmpty=new boolean[saveFileCount];

    private State state;

    private int selection = 0; //von 0 bis ButtonCount -1
    private int selectedGameSave;

    private boolean active = false;//Ob der game.screen.StartingScreen gerade offen ist, oder nicht.

    private Text tb0; //Text fuer Button 0 game.screen.StartingScreen
    private Text tb1; //Text fuer Button 1 game.screen.StartingScreen

    /*
    private Text tb2; //Text fuer Button 2 game.screen.StartingScreen
    private Text tb3; //Text fuer Button 3 game.screen.StartingScreen
     */

    private ExtraInformation[] extraInformations;

    private Text saveOverrideWarning;
    private Text newGameConfirmationText;

    public StartingScreen() {
        //initExtraInformations();
        loadExtraInformations();


        saveOverrideWarning=new Text("Achtung du bist gerade dabei den spielstand zu überschreiben! Zum bestätigen drücke Enter! zum Abbrechen Space/Leertaste",0,0);
        saveOverrideWarning.positionSetzen(300,400);
        saveOverrideWarning.farbeSetzen(Farbe.vonString("rot"));
        selectedGameSave=-1;
        state=State.STANDARD;
        BackgroundPic = new Bild(0, 0, "./Assets/StartingScreen/StartbilschirmOHNEButtonsundTitel.png");
        loadingPic = new Bild(0, 0, MAIN.playerStillImgPath);
        float xPos = MAIN.x / 2 - (loadingPic.getBreite() / 2);
        float yPos = MAIN.y / 2 - (loadingPic.getHoehe() / 2);
        loadingPic.positionSetzen(xPos, yPos);
        this.add(BackgroundPic);
        this.add(loadingPic);
        loadingPic.sichtbarSetzen(false);
        standardButtonGroup =new Knoten();
        saveButtonGroup=new Knoten();

        selection=0;
        FillButtonObjects();
        this.add(standardButtonGroup);
        newGameConfirmationText =new Text("Neues Spiel. Zum bestätigen ENTER",20,574);
        newGameConfirmationText.setzeFarbe("blau");
        add(newGameConfirmationText);
        newGameConfirmationText.sichtbarSetzen(false);
    }

    private void FillButtonObjects() {
        //System.out.println("FillButtonObjects");
        for (int i = 0; i < buttonCount; i++) {
            //System.out.println(i);
            if (i<3&&extraInformations[i].isEmpty()) {
                saveButtons[i] = new Bild(0, 0, "./Assets/StartingScreen/ButtonSet2_Empty_" + i + ".png"); //jedes Bild 200 pixel weiter rechts
            }
            else{
            saveButtons[i] = new Bild(0, 0, "./Assets/StartingScreen/ButtonSet2_" + i + ".png"); //jedes Bild 200 pixel weiter rechts
            }
            saveButtonGroup.add(saveButtons[i]);
        }
        for (int i = 0; i < buttonCount; i++) {
            //System.out.println(i);
            standardButtons[i] = new Bild(0, 0, "./Assets/StartingScreen/ButtonFinal" + i + ".png"); //jedes Bild 200 pixel weiter rechts
            standardButtonGroup.add(standardButtons[i]);
        }
        add(standardButtonGroup);
        add(saveButtonGroup);
        saveButtonGroup.sichtbarSetzen(false);
        UpdateButtons();
    }
    private void setButtonSetToStandard(boolean b){
        if(b){
            standardButtonGroup.sichtbarSetzen(true);
            saveButtonGroup.sichtbarSetzen(false);
        }
        else{
            saveButtonGroup.sichtbarSetzen(true);
            standardButtonGroup.sichtbarSetzen(false);
        }
        UpdateButtons();
    }
    private void loadExtraInformations(){
        extraInformations=new  ExtraInformation[3];
        for(int i=0;i<3;i++){
            extraInformations[i]=SaveFileManager.readExtraInformationJSON(new String(extraInformationPathTemplate).replace('?',(char)(i+48) ));
            System.out.println(extraInformations[i]);
        }

    }
    private void initExtraInformations(){
        for(int i=0;i<3;i++){
            SaveFileManager.saveExtraInformation(new String(extraInformationPathTemplate).replace('?', (char)(i+48) ),new ExtraInformation(true));
        }
    }


    /**
     * Updatet die Auswahl der Knöpfe(auch graphisch) nach der -selection- Variable
     */
    private void UpdateButtons() {
        this.TextStartScEntfernen();
        switch (state) {
            case OLD:
            case STANDARD:
            for (int i = 0; i < buttonCount; i++) {
                standardButtons[i].setOpacity(0.25f);//alle halb sichbar
            }
            System.out.println(selection);
            standardButtons[selection].setOpacity(1f);
            break;
            case NEW:
            case PLAY:
                for (int i = 0; i < buttonCount; i++) {
                    saveButtons[i].setOpacity(0.25f);//alle halb sichbar
                }
                System.out.println(selection);
                saveButtons[selection].setOpacity(1f);
                break;

        }
    }


    /**
     * Setzt den Linken Knopf "Aktiv"
     * und rechten nicht mehr "Aktiv"
     */

    public void ShiftLeft() {
        if(!state.isButtonState()){return;}
        selection--;
        if (selection < 0) {
            selection = 0;//stay at first
        }

        UpdateButtons();
    }

    public void ShiftRight() {
        if(!state.isButtonState()){return;}
        selection++;
        if (selection >= buttonCount) {
            selection = buttonCount - 1;//stay at last
        }
        UpdateButtons();
    }

    public boolean isActive() {
        return active;
    }

    public int getSelection() {
        return selection;

    }

    public void setActive(boolean active) {
        this.active = active;
        if(active){
            SPIEL.currentScreen=ScreenType.STARTINGSCREEN;
        }
        BackgroundPic.sichtbarSetzen(active);
        loadingPic.sichtbarSetzen(false);

        for (int i = 0; i < buttonCount; i++) {
            standardButtons[i].sichtbarSetzen(active);
        }
        for (int i = 0; i < buttonCount; i++) {
            saveButtons[i].sichtbarSetzen(active);
        }
    }

    public void tickLoadingAnimation() {
        if (this.active) {
            //System.out.println("game.screen.StartingScreen: tickLoadingAnimation();");
            loadingPic.drehenRelativ(6);
        }
    }


    //unbenutzt
    public void SelectButtons() { //Enter = 31
        switch (selection) {
            case 0: {
                TextStartScEntfernen();
                System.out.println("Button 1: Exit");
                this.setActive(false);
                //tb0 = new Text("Text fuer Button 0",150,350);
                //this.add(tb0);
            }
            break;
            case 1: {
                TextStartScEntfernen();
                System.out.println("Button 2: Play");
                //tb1 = new Text("Text fuer Button 1",350,350);
                //this.add(tb1);
            }
            break;
        }
    }


    public void TextStartScEntfernen() {
        //JF: Du kannst auch einfach tb0.sichtbarSetzen(false); machen
        //JF: Oder füg die einfach zur Mehtode setActive hinzu. Da wird alles ausgeblendet.
        this.entfernen(tb0);
        this.entfernen(tb1);
        //this.entfernen(tb2);
        //this.entfernen(tb3);
    }

    @Override
    public void show() {
        newGameConfirmationText.sichtbarSetzen(false);
        BackgroundPic.sichtbarSetzen(false);
        standardButtonGroup.sichtbarSetzen(false);
        saveButtonGroup.sichtbarSetzen(false);
        loadingPic.sichtbarSetzen(true);
        SPIEL.currentScreen=ScreenType.STARTINGSCREEN;
    }

    @Override
    public void hide() {
        this.setActive(false);
        SPIEL.currentScreen=ScreenType.GAMESCREEN;
    }
    public void select(SPIEL game,WindowScreen settingScreen,WindowScreen aboutScreen){
        int sel = selection;
        switch(state){
            case OLD:
                switch (sel) {
                    case (0):
                        System.out.println("PLAY: Spiel wird gestartet");
                        show();
                        game.Konstruktor(MAIN.npcFilePath,MAIN.gameSaveFilePath);
                        break;

                    case (1):
                        System.out.println("NEW GAME: Spiel überschriebt Dateien");
                        show();
                        NewGameLoader gl = new NewGameLoader();
                        //System.out.println("LADEN FERTIG!!");
                        game.Konstruktor(MAIN.npcFilePath,MAIN.gameSaveFilePath);
                        break;

                    case (2):
                        System.out.println("EXIT KNOPF GEDRÜCKT: Spiel wird geschlossen");
                        game.schliessen();
                        break;

                    case (3):
                        System.out.println("ABOUT GEDRÜCKT:AboutScreenWir gestartet");
                        settingScreen.hide();
                        aboutScreen.toggleWindow();
                        break;

                    case (4):
                        System.out.println("SETTINGS GEDRÜCKT:SettingScreen wird gestartet");
                        aboutScreen.hide();
                        settingScreen.toggleWindow();
                        break;
                }
                break;
            case STANDARD:
                switch (sel) {
                    case (0):
                        System.out.println("PLAY: Spielstand auswählen zum starten");
                        state=State.PLAY;
                        setButtonSetToStandard(false);
                        selection=0;
                        UpdateButtons();

                        break;

                    case (1):
                        System.out.println("NEW GAME: Spielstand auswählen um neues Spiel zu speichern");
                        state=State.NEW;
                        setButtonSetToStandard(false);
                        selection=0;
                        UpdateButtons();
                        break;

                    case (2):
                        System.out.println("EXIT KNOPF GEDRÜCKT: Spiel wird geschlossen");
                        game.schliessen();
                        break;

                    case (3):
                        System.out.println("ABOUT GEDRÜCKT:AboutScreen wird gestartet");
                        settingScreen.hide();
                        aboutScreen.toggleWindow();
                        state=State.ABOUT;
                        break;

                    case (4):
                        System.out.println("SETTINGS GEDRÜCKT:SettingScreen wird gestartet");
                        aboutScreen.hide();
                        settingScreen.toggleWindow();
                        break;
                }
                break;
            case WAITING:
                entfernen(saveOverrideWarning);
                    newGameConfirmationText.sichtbarSetzen(false);
                    System.out.println("NEW GAME: Spiel überschreibt Datei: " + new String(gameSaveFilePathTemplate).replace('?', (char) (selectedGameSave + 48)));
                    show();
                    NewGameLoader ngl = new NewGameLoader(new String(npcSaveFilePathTemplate).replace('?', (char) (selectedGameSave + 48)), new String(gameSaveFilePathTemplate).replace('?', (char) (selectedGameSave + 48)));
                    SaveFileManager.saveExtraInformation(new String(extraInformationPathTemplate).replace('?', (char) (selectedGameSave + 48)), new ExtraInformation(false));
                    //System.out.println("LADEN FERTIG!!");
                    game.Konstruktor(new String(npcSaveFilePathTemplate).replace('?', (char) (selectedGameSave + 48)), new String(gameSaveFilePathTemplate).replace('?', (char) (selectedGameSave + 48)));

                break;

            case NEW:
                switch(selection){
                    case (3):
                        System.out.println("Back GEDRÜCKT: state set to standard");
                        state=State.STANDARD;
                        setButtonSetToStandard(true);
                        selection=0;
                        UpdateButtons();
                        break;

                    case (4):
                        System.out.println("SETTINGS GEDRÜCKT:SettingScreen wird gestartet");
                        settingScreen.show();

                        break;

                        default:
                        if(!extraInformations[sel].isEmpty()){
                            saveOverrideWarning.setzeInhalt("Bist du sicher, dass du Spielstand "+selection+" überschreiben willst? JA: Enter NEIN: N");
                            saveOverrideWarning.leuchtetSetzen(true);
                            add(saveOverrideWarning);
                            saveOverrideWarning.sichtbarSetzen(true);
                            selectedGameSave=selection;
                            state=State.WAITING;
                        }
                        else{
                            System.out.println("NEW GAME: Spiel überschreibt Datei(die leer war): "+new String(gameSaveFilePathTemplate).replace('?',(char)(selection+48)));
                            show();
                            NewGameLoader gl = new NewGameLoader(new String(npcSaveFilePathTemplate).replace('?',(char)(selection+48)),new String(gameSaveFilePathTemplate).replace('?',(char)(selection+48)));
                            SaveFileManager.saveExtraInformation(new String(extraInformationPathTemplate).replace('?',(char)(selection+48)),new ExtraInformation(false));
                            //System.out.println("LADEN FERTIG!!");
                            game.Konstruktor(new String(npcSaveFilePathTemplate).replace('?',(char)(selection+48)),new String(gameSaveFilePathTemplate).replace('?',(char)(selection+48)));
                        }


                }

                break;
            case PLAY:
                switch(selection){
                    case (3):
                        System.out.println("Back GEDRÜCKT: state set to standard");
                        state=State.STANDARD;
                        setButtonSetToStandard(true);
                        selection=0;
                        UpdateButtons();
                        break;

                    case (4):
                        System.out.println("SETTINGS GEDRÜCKT:SettingScreen wird gestartet");
                        settingScreen.show();

                        break;

                    default:
                        if(extraInformations[sel].isEmpty()){
                            newGameConfirmationText.sichtbarSetzen(true);
                            newGameConfirmationText.positionSetzen(20+selection*300,574);
                            state=State.WAITING;
                            selectedGameSave=selection;

                        }
                        else{
                            System.out.println("Starte Spielstand von: "+new String(gameSaveFilePathTemplate).replace('?',(char)(selection+48)));
                            show();
                            game.Konstruktor(new String(npcSaveFilePathTemplate).replace('?',(char)(selection+48)),new String(gameSaveFilePathTemplate).replace('?',(char)(selection+48)));
                        }


                }
                break;
            case ABOUT:
                aboutScreen.hide();
                state=State.STANDARD;

                break;
            case SETTINGS:
                settingScreen.hide();
                state=State.STANDARD;
                break;


        }
    }
    public void setStateToStandard(){
        if(state!=State.ABOUT&&state!=State.SETTINGS){
            state=State.STANDARD;
            selection=0;
            saveOverrideWarning.sichtbarSetzen(false);
            saveButtonGroup.sichtbarSetzen(false);
            standardButtonGroup.sichtbarSetzen(true);
            newGameConfirmationText.sichtbarSetzen(false);
        }
    }
    enum State{
        STANDARD,NEW,PLAY,OLD,ABOUT,SETTINGS,WAITING;
        public boolean isButtonState(){
            return this.ordinal()<4;
        }
    }


}
