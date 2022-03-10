package game.character;

import ea.Knoten;
import ea.Punkt;
import game.dataManagement.GameSaver;

/**
 * Klasse für aktive Spieler
 * Struktur aus dem Beispiel demo-netzwerkgame-master
 */
public class Player extends Knoten {

    private GameSaver gamesaver;

    private String name;
    private float posX;
    private float posY;
    private int money;

    private int speed; // Laufgeschwindigkeit
    private final int walkspeed;
    private final int bikespeed;
    private final int carspeed;
    private MovementState movement;
    private final ImageCollection walkingCollection;
    private final ImageCollection bikingCollection;
    private final ImageCollection carCollection;

    public Player(float posX, float posY, GameSaver gs) {
        this.gamesaver = gs;
        this.posX = posX;
        this.posY = posY;
        this.money = 0;


        carCollection = new ImageCollection(this.posX, this.posY, "./Assets/SpielerTest/Car/","Assets/SpielerTest/Car/still.png",1,1);
        carCollection.Init();
        this.add(carCollection);
        carCollection.sichtbarSetzen(false);

        bikingCollection = new ImageCollection(this.posX, this.posY, "./Assets/SpielerTest/Biking/","Assets/SpielerTest/Biking/still.png",1,1);
        bikingCollection.Init();
        this.add(bikingCollection);
        bikingCollection.sichtbarSetzen(false);

        walkingCollection = new ImageCollection(this.posX, this.posY, "./Assets/SpielerTest/BasicMale");
        walkingCollection.Init();
        this.add(walkingCollection);


        movement=MovementState.WALKING;

        walkspeed = gamesaver.getWalkspeed();
        speed=walkspeed;
        bikespeed=walkspeed*3;
        carspeed=bikespeed*2;
    }

    @Override
    public void verschieben(float dX, float dY) {
        super.verschieben(dX, dY);
        posX = posX + dX;
        posY = posY + dY;

    }

    @Override
    public void positionSetzen(float x, float y) {
        posX = x;
        posY = y;
        super.positionSetzen(x, y);
    }

    @Override
    public void positionSetzen(Punkt p) {
        super.positionSetzen(p);
    }

    /*
        @Override

        public void positionSetzen(float x, float y) {
            super.positionSetzen(x, y);
            posX= x;
            posY= y;
        }

         */
    float lastX = posX;

    public void WalkLeft() {
        walkingCollection.walkLeft(speed);
        bikingCollection.walkLeft(speed);
        carCollection.walkLeft(speed);
        //this.verschieben(-walkspeed, 0);
        this.posX = posX - speed;
        //IC.verschieben(-walkspeed,0);
    }

    public void WalkRight() {
        walkingCollection.walkRight(speed);
        bikingCollection.walkRight(speed);
        carCollection.walkRight(speed);
        //this.verschieben(walkspeed, 0);
        this.posX = posX + speed;
        //IC.verschieben(walkspeed,0);
    }

    public void WalkBottom() {
        walkingCollection.walkBottom(speed);
        carCollection.walkBottom(speed);
        bikingCollection.walkBottom(speed);
        this.posY = posY + speed;
        //this.verschieben(0, walkspeed);
        //IC.verschieben(0, walkspeed);
    }

    public void WalkTop() {
        walkingCollection.walkTop(speed);
        bikingCollection.walkTop(speed);
        carCollection.walkTop(speed);
        this.posY = posY - speed;
        //this.verschieben(0,-walkspeed);
        //IC.verschieben(0, -walkspeed);
    }

    public void standStill() {
        //System.out.println("STANDSTILL");
        walkingCollection.resetStep();
        walkingCollection.standStill();
        bikingCollection.resetStep();
        bikingCollection.standStill();
        carCollection.resetStep();
        carCollection.standStill();
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int x) {
        money += x;
    }


    public float getCenterX() {
        return (posX + this.getBreite() / 2);
    }

    public float getCenterY() {
        return (posY + this.getHoehe() / 2);
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    private void changeMovementTo(MovementState state){
        movement=state;
        switch(movement){
            case CAR:
                speed=carspeed;
                walkingCollection.sichtbarSetzen(false);
                bikingCollection.sichtbarSetzen(false);
                carCollection.sichtbarSetzen(true);
                break;
            case BIKE:
                speed=bikespeed;
                walkingCollection.sichtbarSetzen(false);
                bikingCollection.sichtbarSetzen(true);
                carCollection.sichtbarSetzen(false);
                break;
            case WALKING:
                speed=walkspeed;
                walkingCollection.sichtbarSetzen(true);
                bikingCollection.sichtbarSetzen(false);
                carCollection.sichtbarSetzen(false);
                break;
        }
    }
    public void toggleBike(){
        if(movement==MovementState.BIKE){
            changeMovementTo(MovementState.WALKING);
        }
        else{
            changeMovementTo(MovementState.BIKE);
        }
    }
    public void toggleCar(){
        if(movement==MovementState.CAR){
            changeMovementTo(MovementState.WALKING);
        }
        else{
            changeMovementTo(MovementState.CAR);
        }
    }
    public void setToWalking(){
        changeMovementTo(MovementState.WALKING);
    }
    public enum MovementState {
        WALKING,BIKE,CAR
    }
}

