package game.minimap.punkte;

import ea.Bild;

public class HauptPunkt extends Punkt{

    public HauptPunkt(int x, int y) {
        super(x, y);
        bigImg=new Bild("Assets/Map/Minimap/HauptPunktGroß.png");
        smallImg=new Bild("Assets/Map/Minimap/HauptPunktKlein.png");
        add(smallImg);
        add(bigImg);
        smallImg.sichtbarSetzen(true);
        bigImg.sichtbarSetzen(true);
    }
}
