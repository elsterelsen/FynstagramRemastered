package game.character;

import ea.Bild;
import ea.Knoten;

public class Pet extends Knoten {

    private Bild img;
    float posX;
    float posY;
    int walkspeed = 2; //max 1 in einem teil des Vectors

    public Pet(float x, float y){
        this.posX = x;
        this.posY = y;
        System.out.println("game.character.Pet: neues game.character.Pet bei "+ posX+ "|" + posY );
        InitImg();

    }

    private void InitImg(){
        try{
            img = new Bild("./Assets/game.character.Player.png");
            this.add(img);
            img.sichtbarSetzen(true);
            img.positionSetzen(posX,posY);
            System.out.println("game.character.Pet: Bild erfolgreich geladen");
        } catch (Exception e){
            System.out.println("game.character.Pet: Bild kann nicht geladen werden");
            System.out.println(e);
        }
    }

    public void follow(Player AP){

        float playerX = AP.getPosX();
        float playerY = AP.getPosY();
        float diffX = playerX-posX;
        float diffY = playerY-posY;
        if(diffX>walkspeed){
            if(diffX>diffY){
                diffY = diffY/diffX;
                diffX = walkspeed;
            }
        }
        else if(diffX<-walkspeed){
            if(diffX>diffY){
                diffY = diffY/diffX;
                diffX = walkspeed;
            }
        }
        //System.out.println("game.character.Pet: DIFF bei " + diffX + "|" + diffY);
        this.move(diffX,diffY);
    }


    public void move(float x, float y){
        //System.out.println("game.character.Pet: POS  bei " + posX + "|" + posY);
        posX = posX + x;
        posY = posY + y;
        img.verschieben(x,y);
    }
}
