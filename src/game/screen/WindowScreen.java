package game.screen;

import ea.Bild;
import ea.Knoten;
import game.SPIEL;

public class WindowScreen extends Knoten implements Screen{

    private Bild mainImg;
    //private String path = game.MAIN.settingsScreenImg;
    private boolean active = false;
    private final ScreenType type;


    public WindowScreen(String path){
        mainImg = new Bild(0,0,path);
        mainImg.sichtbarSetzen(false);
        this.add(mainImg);
        type=ScreenType.WINDOWSCREEN;
    }
    public WindowScreen(String path,ScreenType type){
        mainImg = new Bild(0,0,path);
        mainImg.sichtbarSetzen(false);
        this.add(mainImg);
        this.type=type;
    }

    public void show(){
        active = true;
        mainImg.sichtbarSetzen(true);
        SPIEL.currentScreen=type;
    }

    public void hide(){
        active = false;
        mainImg.sichtbarSetzen(false);
        SPIEL.currentScreen=ScreenType.GAMESCREEN;
    }
    public void toggleWindow(){
        if(active){
            hide();
        }else{
            show();
        }
    }

    public boolean isActive() {
        return active;
    }
}
