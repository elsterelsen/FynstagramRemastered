package game.minimap.punkte;

import ea.Bild;
import ea.Knoten;

import java.util.ArrayList;

public class Punkt extends Knoten {
    private final int x;
    private final int y;
    public final Bild bigImg=new Bild("Assets/Map/Minimap/HauptPunktKlein.png");
    public final Bild smallImg=new Bild("Assets/Map/Minimap/HauptPunktKlein.png");


    public Punkt(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void update(int playerX,int playerY, int mapMinimapRatio, int minimapX,int minimapY, int minimapRadius){
        int distance=(int)Math.round(Math.sqrt(Math.pow(playerX-x,2)+Math.pow(playerY-y,2)));
        ea.Punkt p;
        boolean isInside=false;
        if(minimapRadius<distance/mapMinimapRatio){
            p=new ea.Punkt(minimapX+(x-playerX)/distance*minimapRadius,
                    minimapY+(y-playerY)/distance*minimapRadius);
        }
        else{
            p=new ea.Punkt(minimapX+(x-playerX)/mapMinimapRatio,
                    minimapY+(y-playerY)/mapMinimapRatio);
            isInside=true;
        }
        smallImg.positionSetzen(p);

        bigImg.sichtbarSetzen(isInside);
        smallImg.sichtbarSetzen(!isInside);

    }

}
