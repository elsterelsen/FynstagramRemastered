package game.minimap;

import ea.Bild;
import ea.Knoten;
import game.minimap.punkte.HauptPunkt;
import game.minimap.punkte.NebenPunkt;
import game.minimap.punkte.Punkt;


import java.util.ArrayList;

public class Minimap extends Knoten {
    private final ArrayList<Punkt> punkte;
    private final int minimapToMapRatio=3000;//-> minimapt zeigt auf ihrem radius 2000 Pixel der Map
    private final Bild img =new Bild("Assets/Map/Minimap/Circle.png");

    public Minimap(){
        img.positionSetzen(10,510);
        img.sichtbarSetzen(true);
        punkte=new ArrayList<Punkt>();
        //test
        this.add(img);
        addPunkt(820,2500,false);
        addPunkt(5500,3500,true);
    }
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
        for(Punkt p : punkte){
            p.update(playerX,playerY,minimapToMapRatio, img.mittelPunkt().x(), img.mittelPunkt().y(), img.normaleBreite()/2);
        }
    }
    public void setOrientation(){
        img.drehenAbsolut(90);
    }
}
