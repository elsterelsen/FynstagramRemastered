package game.minimap;

import ea.Knoten;
import game.minimap.punkte.HauptPunkt;
import game.minimap.punkte.NebenPunkt;
import game.minimap.punkte.Punkt;


import java.util.ArrayList;

public class Minimap extends Knoten {
    private ArrayList<Punkt> punkte;
    private final int minimapToMapRatio=2000;//-> minimapt zeigt auf ihrem radius 2000 Pixel der Map


    public Punkt addPunkt(int x,int y,boolean istHauptPunkt){
        Punkt newPunkt;
        if(istHauptPunkt){
            newPunkt=new HauptPunkt(x,y);
        }
        else{
            newPunkt=new NebenPunkt(x,y);
        }
        punkte.add(newPunkt);
        add(newPunkt);
        return newPunkt;
    }
    public void update(int playerX,int playerY){

    }

}
