package game.minimap.punkte;

import ea.Bild;

public class HauptPunkt extends Punkt{
    public final Bild bigImg=new Bild("Assets/Map/Minimap/HauptPunktGroß.png");
    public final Bild smallImg=new Bild("Assets/Map/Minimap/HauptPunktKlein.png");

    public HauptPunkt(int x, int y) {
        super(x, y);
        add(smallImg);
        smallImg.sichtbarSetzen(true);
    }
}
