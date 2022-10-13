package game;

import ea.*;
import game.character.*;
import game.dataManagement.GameSaver;
import game.debuging.DebugAnzeige;
import game.dialog.DialogController;
import game.dialog.HelpingArrow;
import game.item.ItemAnimation;
import game.item.ItemController;
import game.map_collision.GameMap;
import game.minimap.Minimap;
import game.screen.*;
import game.screen.minigame.BallTest;
import game.screen.minigame.Minigame2;
import game.sound.SoundController;

import java.util.ArrayList;
import java.util.Stack;

public class SPIEL extends Game implements TastenLosgelassenReagierbar, Ticker, KlickReagierbar {

    private int zaehler;
    private Player ActivePlayer;
    private game.dialog.DialogController DialogController;
    private GameMap map;
    private DebugAnzeige debugAnzeige1;
    private DebugAnzeige debugAnzeige2;
    private DebugAnzeige debugAnzeige3;
    private DebugAnzeige debugAnzeige4;
    private DebugAnzeige debugAnzeige5;
    private DebugAnzeige debugAnzeige6;
    private DebugAnzeige debugAnzeige7;
    private DebugAnzeige debugAnzeige8;
    private DebugAnzeige debugAnzeige9;
    private DebugAnzeige debugAnzeige10;

    //Items
    private ItemController itemController;
    private ItemAnimation itemAnimator;

    //Fade Screen
    private FadeScreen fadeScreen;

    //COMPUTER / Fynstergram
    private ComputerScreen computer;

    //sound
    private SoundController soundController;

    //SettingScreen
    private SettingScreen settingScreen;
    private AboutScreen aboutScreen;

    //END SCREEN
    private EndScreen endScreen;

    //game.MAIN LOADING
    private boolean initDone = false;

    //Helping Arrow
    private HelpingArrow helpingArrow;

    private DummyPlayer DP;
    private game.character.NpcController NpcController;
    private StartingScreen StartSc;

    //private game.screen.minigame.Minigame1 game.screen.minigame.Minigame1;
    private game.screen.minigame.Minigame2 Minigame2;
    private Pet pet1;

    public GameSaver gamesaver;

    private Bild cursor;
    private Punkt hotspot;
    private Maus maus;

    //zähler für 2.tick routine;
    public int tickCounter;

    //Collision
    private ArrayList<NPC>lastCollision=new ArrayList<NPC>();

    //Dialog Activation
    boolean startDialog;

    //Screens
    public static ScreenType currentScreen;
    //Minimap
    private Minimap minimap;

    public SPIEL() {
        super(MAIN.x, MAIN.y, "Fynstagram 2020");//windowsize kann nicht mit variable gemacht werden.


        startDialog=false;
        //Tracker.sendEventAsync(Collections.singletonMap("type", "starteSpiel"));
        soundController = new SoundController();
        soundController.startTitleMusic();
        StartSc = new StartingScreen();

        settingScreen = new SettingScreen(MAIN.settingsScreenImg);
        aboutScreen = new AboutScreen(MAIN.aboutScreenImg);
        statischeWurzel.add(StartSc);
        statischeWurzel.add(settingScreen, aboutScreen);
        StartSc.setActive(true);

        iconSetzen(new Bild(0,0,"./Assets/icon/icon.png"));

    }

    public void Konstruktor(String npcSaveFilePath,String gameSaveFilePath) {
        tickerAnmelden(this, 16);

        endScreen = new EndScreen(soundController);
        computer = new ComputerScreen();
        fadeScreen = new FadeScreen();

        zaehler = 0;
        gamesaver = new GameSaver(gameSaveFilePath); //game.dataManagement.GameSaver, der im Moment nur Spieler-Sachen speichert

        DP = new DummyPlayer(600, 400);


        ActivePlayer = new Player(gamesaver.getPosX(), gamesaver.getPosX(), gamesaver);
        //pet1 = new game.character.Pet(1100,1100);
        NpcController = new NpcController(ActivePlayer, gamesaver,npcSaveFilePath);

        DialogController = new DialogController(NpcController, gamesaver, endScreen, computer, fadeScreen);
        debugAnzeige1 = new DebugAnzeige(0, 0);
        debugAnzeige2 = new DebugAnzeige(200, 0);
        debugAnzeige3 = new DebugAnzeige(350, 0);
        debugAnzeige4 = new DebugAnzeige(500, 0);
        debugAnzeige5 = new DebugAnzeige(700, 0);
        debugAnzeige6 = new DebugAnzeige(1300, 0);
        debugAnzeige7 = new DebugAnzeige(0, 30); //dialogPos
        debugAnzeige8 = new DebugAnzeige(300, 30);
        debugAnzeige9 = new DebugAnzeige(650, 30);
        debugAnzeige10 = new DebugAnzeige(1000, 30); //LastSelfBoolean
        Minigame2 = new Minigame2(ActivePlayer);
        itemAnimator = new ItemAnimation();
        itemController = new ItemController(ActivePlayer, gamesaver, DialogController, itemAnimator, soundController);
        map = new GameMap(NpcController, soundController, ActivePlayer, gamesaver, itemController);
        helpingArrow = new HelpingArrow(DialogController, ActivePlayer, map);
        minimap=new Minimap();


        if (false) {
            //Beginn oben links, im Uhrzeigersinn
            Bild testauto1 = new Bild(220, 1550, "./Assets/Tests/Testauto.png");
            wurzel.add(testauto1);
            animationsManager.streckenAnimation(testauto1, 40000, new Punkt(220, 1550), new Punkt(5300, 1550), new Punkt(5300, 2500), new Punkt(220, 2500));
            //Auto 2 fährt um Häuserblock beide Reihen Wphnhäuser
            //Beginn unten rechts, im Uhrzeigersinn
            Bild testauto2 = new Bild(5300, 3550, "./Assets/Tests/Testauto.png");
            wurzel.add(testauto2);
            animationsManager.streckenAnimation(testauto2, 40000, new Punkt(5300, 3550), new Punkt(220, 3550), new Punkt(220, 1550), new Punkt(5300, 1550));
            //Auto 3 fährt um Häuserblock Schule, Baustelle, Polizei,...
            //Beginn unten links, im Uhrzeigersinn, gegen Uhrzeigersinn, im Uhrzeigersinn => fährt "liegende 8"
            Bild testauto3 = new Bild(220, 4800, "./Assets/Tests/Testauto.png");
            wurzel.add(testauto3);
            animationsManager.streckenAnimation(testauto3, 80000, new Punkt(220, 4800), new Punkt(220, 3640), new Punkt(5300, 3640), new Punkt(5300, 4900), new Punkt(7420, 4890), new Punkt(7420, 3550), new Punkt(5300, 3550), new Punkt(5300, 4800));
        }


        //wurzel.add(Autos);
        wurzel.add(DP);

        wurzel.add(map);
        wurzel.add(ActivePlayer);
        wurzel.add(NpcController);
        wurzel.add(itemController);


        //statischeWurzel.add(HouseLoader1);
        statischeWurzel.add(DialogController);

        statischeWurzel.add(minimap);
        statischeWurzel.add(Minigame2);


        statischeWurzel.add(debugAnzeige1);
        statischeWurzel.add(debugAnzeige2);
        statischeWurzel.add(debugAnzeige3);
        statischeWurzel.add(debugAnzeige4);
        statischeWurzel.add(debugAnzeige5);
        statischeWurzel.add(debugAnzeige6);
        statischeWurzel.add(debugAnzeige7);
        statischeWurzel.add(debugAnzeige8);
        statischeWurzel.add(debugAnzeige9);
        statischeWurzel.add(debugAnzeige10);

        statischeWurzel.add(computer);
        statischeWurzel.add(helpingArrow);

        statischeWurzel.add(itemAnimator);
        statischeWurzel.add(endScreen);
        statischeWurzel.add(fadeScreen);


        //tastenReagierbarAnmelden(this);
        tastenLosgelassenReagierbarAnmelden(this);


        DialogController.highLightReadyNpcs(); //einmal alle highlighten die können

        fokusInitialisieren();
        StartSc.hide();
        aboutScreen.hide();
        initDone = true;

        soundController.stopAllMusic();

        //endScreen.playEnding(true);

    }


    public void fokusSetzten(boolean insideHousing) {
        //System.out.println("FOKUS SETZEN");
        if(map==null)return;
        if(!insideHousing) {
            //Set Fokus on game.character.Player if outside
            cam.fokusSetzen(ActivePlayer);
            BoundingRechteck CamBounds = new BoundingRechteck(0, 0, map.getMapWidth(), map.getMapHeight());
            cam.boundsSetzen(CamBounds);
        }
        else if(map.getHouseNumber()==0){
            //set a weird mixup fokus if ure inside the school bc its too big
            //Schule zu groß für bildschirm, darum sehr kompliziert, hier nur die breite fest und die höhe durch die wände gekappt(all in all siehts einfach besser aus ;) )
            cam.fokusSetzen(ActivePlayer);
            Bild b= map.getHouseImage(map.getHouseNumber());
            BoundingRechteck CamBounds = new BoundingRechteck(b.getX()-(fensterGroesse().breite-b.getBreite())/2, b.getY(), b.getX()-(fensterGroesse().breite-b.getBreite())/2, b.getHoehe());

            cam.boundsSetzen(CamBounds);
        }
        else{
            //set fokus on House if ure inside
            cam.fokusSetzen(map.getHouseImage(map.getHouseNumber()));

        }
    }
    public void fokusInitialisieren() {
        //System.out.println("FOKUS SETZEN");
            cam.fokusSetzen(ActivePlayer);
            BoundingRechteck CamBounds = new BoundingRechteck(0, 0, map.getMapWidth(), map.getMapHeight());
            cam.boundsSetzen(CamBounds);
    }

    public void tick() {
        if (initDone) {
            if (itemController.checkForCollision()) {
                gamesaver.addItem(itemController.getCollidingItemName());
                itemController.hideCollidingItem();
            }

            int playerX = ActivePlayer.positionX();
            int playerY = ActivePlayer.positionY();

            debugAnzeige1.SetContent("Pos:" + playerX + "  -  " + playerY);
            debugAnzeige2.SetContent("Visiting:" + map.isVisiting());
            debugAnzeige3.SetContent("Geld:" + ActivePlayer.getMoney());
            //debugAnzeige4.SetContent("LastLineHadChoice?:" + DialogController.isLastLineHadChoice());
            debugAnzeige5.SetContent("ZeitPosition: " + DialogController.getGlobalTemporalPosition());
            //debugAnzeige6.SetContent("PlayingLastLine: " + DialogController.isPlayingLastLine());
            debugAnzeige7.SetContent("CurrentDialogCode: " + DialogController.getCurrentDialogCode());
            debugAnzeige8.SetContent("relativePos: " + map.getOffsetPosString());
            debugAnzeige9.SetContent("Speed: " + ActivePlayer.getSpeed());
            //debugAnzeige10.SetContent("LastSelf: " + DialogController.isPlayingLastLine());
            debugAnzeige10.SetContent("HouseNUmber: " + map.getHouseNumber());

            minimap.update(playerX,playerY);

            DP.positionSetzen(playerX, playerY);


            if (!DialogController.isActive() && !StartSc.isActive() && !itemAnimator.isActiv() && !settingScreen.isActive()) {
                int walkspeed = ActivePlayer.getSpeed();

                if (tasteGedrueckt(Taste.W)) {
                    DP.positionSetzen(ActivePlayer.getPosX(), ActivePlayer.getPosY() - walkspeed);
                    if (map.isWalkable2(DP, ActivePlayer)) {
                        ActivePlayer.WalkTop();
                    }
                }
                else if (tasteGedrueckt(Taste.S)) {
                    DP.positionSetzen(ActivePlayer.getPosX(), ActivePlayer.getPosY() + walkspeed);
                    if (map.isWalkable2(DP, ActivePlayer)) {
                        ActivePlayer.WalkBottom();
                    }
                }

                else if (tasteGedrueckt(Taste.A)) {
                    DP.positionSetzen(ActivePlayer.getPosX() - walkspeed, ActivePlayer.getPosY());
                    if (map.isWalkable2(DP, ActivePlayer)) {
                        ActivePlayer.WalkLeft();
                    }
                }

                else if (tasteGedrueckt(Taste.D)) {
                    DP.positionSetzen(ActivePlayer.getPosX() + walkspeed, ActivePlayer.getPosY());
                    if (map.isWalkable2(DP, ActivePlayer)) {
                        ActivePlayer.WalkRight();
                    }

                }

                if(!tasteGedrueckt(Taste.W) && !tasteGedrueckt(Taste.S) && !tasteGedrueckt(Taste.A) && !tasteGedrueckt(Taste.D)){ //Es wird keine der Tasten gedrückt
                    ActivePlayer.standStill();
                }
                else{//Wenn eine belibige Taste Gedrückt wird
                    startDialog=true;
                }
            }



            if (NpcController.checkForCollision(ActivePlayer) && !DialogController.isActive()) {
                String npcID = NpcController.getCollidingNPC(ActivePlayer);
                System.out.println("Der Spieler schneidet den NPC mit der ID: " + npcID);
                NPC npc= NpcController.getNPC2(npcID);
                npc.setTalkAbleState(true);
                lastCollision.add(npc);

                if(startDialog&&(tasteGedrueckt(Taste.LEERTASTE)||tasteGedrueckt(Taste.ENTER))){
                DialogController.startDialog(npcID);
                startDialog=false;
                }

                Stack<NPC> removeStack=new Stack<NPC>();
                for (NPC npc2 : lastCollision) {
                    if(npc2.equals(npc)){continue;}
                    npc2.setTalkAbleState(false);
                    removeStack.add(npc2);
                }
                for(NPC npc2:removeStack){
                    lastCollision.remove(npc2);
                }
            }
            else if(!lastCollision.isEmpty()&&!NpcController.checkForCollision(ActivePlayer)) {
                //wenn keine Collision stattfindet wird Ansprechbar-zustand vom letzten Kollidierten NPC auf false gesetzt
                for (NPC npc : lastCollision) {
                    npc.setTalkAbleState(false);
                }
                lastCollision.clear();
            }




            gamesaver.SavePlayer(ActivePlayer);
            //pet1.follow(ActivePlayer);

            Minigame2.tick();
            itemAnimator.tick();
            fadeScreen.tick();
            soundController.tickMusic();
            itemController.updateItemVisibility();
            endScreen.tick();
            helpingArrow.updateArrows(); //zeigt die Pfeile für die Orrientierung an

            tickCounter++;
            if (tickCounter > 400) { //alle vier sekunden
                tickCounter = 0;
                gamesaver.setTemporalPosition(DialogController.getGlobalTemporalPosition());
                //gamesaver.saveJSON();
            }
        }

        StartSc.tickLoadingAnimation();

    }



    //  https://engine-alpha.org/wiki/Tastaturtabelle
    public void tasteReagieren(int tastenkuerzel) {

        switch (currentScreen){

            case STARTINGSCREEN:
                if (tastenkuerzel == 0 ) {
                    StartSc.ShiftLeft();
                } else if (tastenkuerzel == 3) {
                    StartSc.ShiftRight();
                } else if (tastenkuerzel == 31||tastenkuerzel==Taste.LEERTASTE) { //enter
                    StartSc.select(this,settingScreen,aboutScreen);
                }
                else if(tastenkuerzel==Taste.N||tastenkuerzel==Taste.B){
                    StartSc.setStateToStandard();
                }
                else if (itemAnimator.isActiv()) {
                    if (tastenkuerzel == 31) {
                        System.out.println("game.SPIEL: ENTER GEDRÜCKt");
                        itemAnimator.hideEverything();
                    }
                }
            break;
            case ENDSCREEN:

            break;
            case FADESCREEN :
                break;
            case GAMESCREEN :
                if (DialogController.isWaitingForInput()) {
                    if (tastenkuerzel == 0) {
                        //System.out.println("Taste ist gedrückt und isWaitingForInputs = true");
                        DialogController.input("links");
                    } else if (tastenkuerzel == 3) {
                        DialogController.input("rechts");
                    } else if (tastenkuerzel == 31) {
                        System.out.println("game.SPIEL(Gamescreen): ENTER GEDRÜCKt");
                        DialogController.input("enter");
                    }

                }
                else if(!map.isVisiting()) {
                    if (tastenkuerzel == Taste.B) {
                        ActivePlayer.toggleBike();
                    } else if (tastenkuerzel == Taste.C) {
                        ActivePlayer.toggleCar();
                    }
                }
                break;
            case WINDOWSCREEN :

                break;
            case COMPUTERSCREEN:
                    System.out.println("game.SPIEL(Computerscreen): ENTER GEDRÜCKT");
                    computer.hide();
                    currentScreen=ScreenType.GAMESCREEN;
                    break;
            case ABOUTSCREEN:
                aboutScreen.hide();
                break;
            case SETTINGSSCREEN:
                settingScreen.hide();
            break;

        }


        if (!StartSc.isActive()) {
            if (tastenkuerzel == 8) {//I als in
                if (computer.isActiv()) {
                    computer.hide();
                    currentScreen=ScreenType.GAMESCREEN;
                } else {
                    computer.hide();
                }
            }
            if (tastenkuerzel == 14) {//o als out
                //HouseLoader1.HideView();
                map.leaveHouse();
            }
            if (tastenkuerzel == 12) { //Wenn M gedrückt muten
                soundController.toggleMute();
            }

            if (tastenkuerzel == 15) {//P FÜR SETTINGS
                settingScreen.toggleWindow();
            }

//            if (tastenkuerzel == 12) {//M für minigame
//                game.screen.minigame.Minigame2.startGame();
//            }

            if(true){ //test
                switch (tastenkuerzel){
                    case(6): // "G"
                        System.out.println("G");
                        map.enterHouse(0); //SCHULE eig.
                        break;
                    case(7): // "H"
                        System.out.println("H");
                        map.enterHouse(1); //KIOSIK eig.
                        break;
                    case(9): // "J"
                        System.out.println("J");
                        map.enterHouse(2); //POLIZEI eig.
                        break;
                    case(10): // "K"
                        System.out.println("K");
                        map.enterHouse(3); //ZuHause eig.
                        break;
                    case(11): // "L"
                        System.out.println("L");
                        map.enterHouse(4); //Sportverein eig.
                        break;

                }

            }


        }
    }


    public void warte(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tasteLosgelassen(int i) {
        //System.out.println(i);
    }


    @Override
    public void klickReagieren(Punkt punkt) {
        System.out.println("Klick bei (" + punkt + ").");
        int dx = (int) ActivePlayer.getPosX() - (int) punkt.x();
        int dy = (int) ActivePlayer.getPosY() - (int) punkt.y();
        System.out.println(dx + dy);
        BallTest ball1 = new BallTest((int) ActivePlayer.getPosX(), (int) ActivePlayer.getPosY(), dx, dy);
        wurzel.add(ball1);
    }
}
