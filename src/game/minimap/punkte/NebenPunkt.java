package game.minimap.punkte;

import ea.Bild;

public class NebenPunkt extends Punkt{

    public NebenPunkt(int x, int y) {
        super(x, y);
        bigImg=new Bild("Assets/Map/Minimap/NebenPunktGroß.png");
        smallImg=new Bild("Assets/Map/Minimap/NebenPunktKlein.png");
        add(smallImg);
        add(bigImg);
        smallImg.sichtbarSetzen(true);
        bigImg.sichtbarSetzen(true);
    }
}
