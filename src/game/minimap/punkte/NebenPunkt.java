package game.minimap.punkte;

import ea.Bild;

public class NebenPunkt extends Punkt{
    public final Bild bigImg=new Bild("Assets/Map/Minimap/NebenPunktGroß.png");
    public final Bild smallImg=new Bild("Assets/Map/Minimap/NebenPunktKlein.png");
    public NebenPunkt(int x, int y) {
        super(x, y);
        add(smallImg);
        smallImg.sichtbarSetzen(true);
    }
}
