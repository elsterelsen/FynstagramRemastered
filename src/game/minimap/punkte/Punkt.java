package game.minimap.punkte;

import ea.Bild;
import ea.Knoten;

import java.util.ArrayList;

public class Punkt extends Knoten {
    private final int x;
    private final int y;
    Bild bigImg=new Bild("Assets/Map/Minimap/HauptPunktKlein.png");
    Bild smallImg=new Bild("Assets/Map/Minimap/HauptPunktKlein.png");


    public Punkt(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void update(int playerX,int playerY, int mapMinimapRatio, int minimapCenterX,int minimapCenterY, int minimapRadius){
        int distance=(int)Math.sqrt((x-playerX)*(x-playerX)+(y-playerY)*(y-playerY));
        ea.Punkt p;
        boolean isInside=false;
        if(mapMinimapRatio<distance){
            p=new ea.Punkt(minimapCenterX+(x-playerX)*minimapRadius/distance,
                    minimapCenterY+(y-playerY)*minimapRadius/distance);
        }
        else{
            p=new ea.Punkt(minimapCenterX+(x-playerX)*minimapRadius/mapMinimapRatio,
                    minimapCenterY+(y-playerY)*minimapRadius/mapMinimapRatio);
            isInside=true;
        }
        smallImg.mittelpunktSetzen(p);
        bigImg.mittelpunktSetzen(p);

        bigImg.sichtbarSetzen(isInside);
        smallImg.sichtbarSetzen(!isInside);

    }

}
