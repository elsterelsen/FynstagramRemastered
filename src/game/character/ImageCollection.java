package game.character; /**
 * Organisiert die Animation von Laufbewegungen für >game.character.Player< und >Npc<
 * Sammelt Bilder für alle Gehrichtungen und entscheidet welches Bild anzuzeigen.
 * <p>
 * Benennung:
 * <p>
 * "FigurName" + "-" + "L/R/T/B" + "NUMBER"
 * Bsp.:  "Spieler-R0.png"
 * Bsp.2: "Torben-T0.png"
 * <p>
 * INDIZES VON 0 AN
 */


import ea.Bild;
import ea.Knoten;
import game.MAIN;

import java.util.Date;

class ImageCollection extends Knoten {

    enum Direction{
        Bottom,Right,Top,Left
    }

    private int ImageCount = 3;    //Anzahl der Bilder
    private int AllImageCount = ImageCount * 4;//vielleicht Count*4 + 1

    private float posX;
    private float posY;
    private String MainDir;

    private Direction lookingDirection;

    private int stepL = 0;
    private int stepR = 0;
    private int stepT = 0;
    private int stepB = 0;

    private int stepIdle=0;

    private int distanceL = 0;
    private int distanceR = 0;
    private int distanceT = 0;
    private int distanceB = 0;


    private int stepDistance = 20; // Distanz die einen Schritt ausmacht

    //Matrix containing the PictureArrays below
    private final Bild[][] Imgs;
    //Listen mit >Bild< Objekten
    private final Bild[] ImgL;  //Links
    private final Bild[] ImgR;  //Recht
    private final Bild[] ImgT;  //Top
    private final Bild[] ImgB;  //Bottom
    private final int idleAnimationLength;
    private long lastChangedIdle=0;
    //Matrix containing the PictureArrays below
    private final Bild[][] IdleImgs;
    //Listen mit >Bild< Objekten for idle animation
    private final Bild[] ImgBIL;  //Links
    private final Bild[] ImgBIR;  //Recht
    private final Bild[] ImgBIT;  //Top
    private final Bild[] ImgBIB;  //Bottom

    private final boolean idleAnimation;
    private final boolean movementAnimation;

    private Bild stillImg;


    /**
     *
     * @param x Position x
     * @param y Position y
     * @param MainDir Übergeordnetes Verzeichnis der Bilder
     */
    public ImageCollection(float x, float y, String MainDir) {
        movementAnimation=true;
        idleAnimation=true;
        this.posX = x;
        this.posY = y;
        this.MainDir = MainDir;
        Imgs = new Bild[4][];
        //Listen mit >Bild< Objekten
        ImgL = new Bild[4];  //Links
        ImgR = new Bild[4];  //Recht
        ImgT = new Bild[3];  //Top
        ImgB = new Bild[3];  //Bottom

        idleAnimationLength =4;
        lastChangedIdle=0;
        //Matrix containing the PictureArrays below
        IdleImgs = new Bild[4][];
        //Listen mit >Bild< Objekten for idle animation
        ImgBIL = new Bild[idleAnimationLength];  //Links
        ImgBIR = new Bild[idleAnimationLength];  //Recht
        ImgBIT = new Bild[idleAnimationLength];  //Top
        ImgBIB = new Bild[idleAnimationLength];  //Bottom

        lookingDirection=Direction.Bottom;

        String path = MAIN.playerStillImgPath;
        try {
            stillImg = new Bild(posX, posY, path);
            this.add(stillImg);
        } catch (Exception e) {
            System.out.println("game.ImageCollection2: Fehler beim Lesen des still-Bilds an der Stelle: + " + path);
        }
        Imgs[0]=ImgB;
        Imgs[1]=ImgR;
        Imgs[2]=ImgT;
        Imgs[3]=ImgL;

        IdleImgs[0]=ImgBIB;
        IdleImgs[1]=ImgBIR;
        IdleImgs[2]=ImgBIT;
        IdleImgs[3]=ImgBIL;



    }
    public ImageCollection(float x, float y, String MainDir,String stillImgPath,int movementPictures,int idlePictures) {
        this.posX = x;
        this.posY = y;
        this.MainDir = MainDir;
        idleAnimation=idlePictures>1;
        movementAnimation=movementPictures>1;
        Imgs = new Bild[4][];
        //Listen mit >Bild< Objekten
        ImgL = new Bild[movementPictures];  //Links
        ImgR = new Bild[movementPictures];  //Recht
        ImgT = new Bild[movementPictures];  //Top
        ImgB = new Bild[movementPictures];  //Bottom

        idleAnimationLength =idlePictures;
        lastChangedIdle=0;
        //Matrix containing the PictureArrays below
        IdleImgs = new Bild[4][];
        //Listen mit >Bild< Objekten for idle animation
        ImgBIL = new Bild[idleAnimationLength];  //Links
        ImgBIR = new Bild[idleAnimationLength];  //Recht
        ImgBIT = new Bild[idleAnimationLength];  //Top
        ImgBIB = new Bild[idleAnimationLength];  //Bottom

        lookingDirection=Direction.Bottom;

        String path = stillImgPath;
        try {
            stillImg = new Bild(posX, posY, path);
            this.add(stillImg);
        } catch (Exception e) {
            System.out.println("game.ImageCollection2: Fehler beim Lesen des still-Bilds an der Stelle: + " + path);
        }
        Imgs[0]=ImgB;
        Imgs[1]=ImgR;
        Imgs[2]=ImgT;
        Imgs[3]=ImgL;

        IdleImgs[0]=ImgBIB;
        IdleImgs[1]=ImgBIR;
        IdleImgs[2]=ImgBIT;
        IdleImgs[3]=ImgBIL;



    }

    public void HideAll() {
        /**
         //Old Code for hiding (By Joni), new One below
        for (int i = 0; i < AllImageCount; i++) {
            ImgAll[i].sichtbarSetzen(false);
            stillImg.sichtbarSetzen(false);
        }
         **/

        //Hide all Images (in Imgs matrix)
        for(Bild[] pictureArray:Imgs){
            for(Bild b:pictureArray){
                b.sichtbarSetzen(false);
            }
        }
        //Hide all Images (in IdleImgs matrix)
        for(Bild[] pictureArray:IdleImgs){
            for(Bild b:pictureArray){
                b.sichtbarSetzen(false);
            }
        }
        stillImg.sichtbarSetzen(false);
    }

    //  Run all Init Methods
    public void Init() {
        initL();
        initR();
        initT();
        initB();
        initBIR();
        initBIB();
        initBIL();
        initBIT();
    }

    public void initL() {
        for (int i = 0; i < ImgL.length; i++) {

            String Dir = MainDir + "-L" + (i) + ".png";


            ImgL[i] = new Bild(posX, posY, Dir);
            this.add(ImgL[i]);
        }

    }
    public void initBIL() {
        for (int i = 0; i < ImgBIL.length; i++) {

            String Dir = MainDir + "-BIL" + (i) + ".png";


            ImgBIL[i] = new Bild(posX, posY, Dir);
            this.add(ImgBIL[i]);
        }
    }

    public void initR() {
        for (int i = 0; i < ImgR.length; i++) {

            String Dir = MainDir + "-R" + (i) + ".png";


            ImgR[i] = new Bild(posX, posY, Dir);
            this.add(ImgR[i]);
        }
    }
    public void initBIR() {
        for (int i = 0; i < ImgBIR.length; i++) {

            String Dir = MainDir + "-BIR" + (i) + ".png";


            ImgBIR[i] = new Bild(posX, posY, Dir);
            this.add(ImgBIR[i]);
        }
    }

    public void initT() {
        for (int i = 0; i < ImgT.length; i++) {


            String Dir = MainDir + "-T" + (i) + ".png";

            ImgT[i] = new Bild(posX, posY, Dir);
            this.add(ImgT[i]);
        }
    }
    public void initBIT() {
        for (int i = 0; i < ImgBIT.length; i++) {

            String Dir = MainDir + "-BIT" + (i) + ".png";


            ImgBIT[i] = new Bild(posX, posY, Dir);
            this.add(ImgBIT[i]);
        }
    }

    public void initB() {
        for (int i = 0; i < ImgB.length; i++) {

            String Dir = MainDir + "-B" + (i) + ".png";


            ImgB[i] = new Bild(posX, posY, Dir);
            this.add(ImgB[i]);
        }
    }
    public void initBIB() {
        for (int i = 0; i < ImgBIB.length; i++) {

            String Dir = MainDir + "-BIB" + (i) + ".png";


            ImgBIB[i] = new Bild(posX, posY, Dir);
            this.add(ImgBIB[i]);
        }
    }



    public void walkLeft(int dis) {
        if(!movementAnimation){stepL=0;}
        lookingDirection=Direction.Left;
        distanceL = distanceL + dis;
        HideAll();
            ImgL[stepL].sichtbarSetzen(true);
            if (distanceL >= stepDistance) {

                stepL++;
                if (stepL >= ImgL.length) {
                    stepL = 1;
                }
                distanceL = 0;

            }

        this.verschieben(-dis, 0);
    }

    public void walkRight(int dis) {
        if(!movementAnimation){stepR=0;}
        lookingDirection=Direction.Right;
        distanceR = distanceR + dis;
        HideAll();
        ImgR[stepR].sichtbarSetzen(true);
        if (distanceR >= stepDistance) {

            stepR++;
            if (stepR >= ImgR.length) {
                stepR = 1;
            }
            distanceR = 0;

        }
        this.verschieben(dis, 0);
    }

    public void walkTop(int dis) {
        if(!movementAnimation){stepT=0;}
        lookingDirection=Direction.Top;
        distanceT = distanceT + dis;
        HideAll();
        ImgT[stepT].sichtbarSetzen(true);
        if (distanceT >= stepDistance) {

            stepT++;
            if (stepT >= ImgT.length) {
                stepT = 1;
            }
            distanceT = 0;

        }
        this.verschieben(0, -dis);
    }

    public void walkBottom(int dis) {
        if(!movementAnimation){stepB=0;}
        lookingDirection=Direction.Bottom;
        distanceB = distanceB + dis;
        HideAll();
        ImgB[stepB].sichtbarSetzen(true);
        if (distanceB >= stepDistance) {

            stepB++;
            if (stepB >= ImgB.length) {
                stepB = 1;
            }
            distanceB = 0;

        }
        this.verschieben(0, dis);
    }

    public void standStill(){
        Date date=new Date();
        //System.out.println(date.getTime());
        if(date.getTime()>lastChangedIdle+300) {
            lastChangedIdle=date.getTime();
            HideAll();
            IdleImgs[lookingDirection.ordinal()][stepIdle].sichtbarSetzen(true);
            stepIdle = (stepIdle + 1) % IdleImgs[lookingDirection.ordinal()].length;
        }
    }


    public void resetStep() {
        stepL = 1;
        stepB = 1;
        stepT = 1;
        stepR = 1;
        //HideAll();
        //stillImg.sichtbarSetzen(true);
    }

    /**
     *
     * @param dx Eig. nur ob der Spieler einen schritt in x Richtung mit Vorzeichen macht
     * @param dy Eig. nur ob der Spieler einen schritt in y Richtung mit Vorzeichen macht
     * Abstand wird vom Schritttempo angegeben
     */
    /*
    public void step(int dx, int dy){
        int abX=0;
        int abY=0;
        int distance = (int)Math.sqrt(dx*dx + dy*dy);//Diagonaler Abstand als INT


        abX = dx*walkspeed/(int)Math.sqrt(2);
        abY = dy*walkspeed/(int)Math.sqrt(2);
        System.out.println(abY);
        if (dx==1 && dy==1){
            walkRight();
            walkBottom();
            verschieben(abX,abY);
        }
        if (dx==-1 && dy==1){
            walkLeft();
            walkBottom();
            verschieben(abX,abY);
        }
        if (dx==1 && dy==-1){
            walkRight();
            walkTop();
            verschieben(abX,abY);
        }
        if (dx==-1 && dy==-1){
            walkLeft();
            walkRight();
            verschieben(abX,abY);
        }

        if (dx==-1){
            walkRight();
            verschieben(-walkspeed,0);
        }
        if (dx==1){
            walkLeft();
            verschieben(walkspeed,0);
        }
        if (dy==-1){
            walkTop();
            verschieben(0,-walkspeed);
        }
        if (dy==1){
            walkBottom();
            verschieben(0,walkspeed);
        }


    }
    */

    /**
     *  Wichtig für später game.character.Player Teleporation oder Resets
     * @param x s. Raum Doku
     * @param y s. Raum Doku
     */

    @Override
    public void positionSetzen(float x, float y) {
        positionSetzen(x, y);
        posX = x;
        posY = y;
    }

    @Override
    public void verschieben(float dX, float dY) {
        posX = posX + dX;
        posY = posY + dY;
        super.verschieben(dX, dY);

    }

}

