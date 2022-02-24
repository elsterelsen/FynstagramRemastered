package game.screen;

import ea.Bild;
import ea.Knoten;
import game.SPIEL;


public class ComputerScreen extends Knoten implements Screen{

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private Bild mainOverlayImg;
    private Bild post1;
    private Bild post2;

    private boolean activ = false;


    public ComputerScreen() {

        //Bilder
        mainOverlayImg = new Bild(0, 0, "./Assets/Computer/overlay.png");
        mainOverlayImg.sichtbarSetzen(false);
        this.add(mainOverlayImg);
        post1 = new Bild(0, 0, "./Assets/Computer/post1.png");
        post2 = new Bild(0, 0, "./Assets/Computer/post2.png");
        this.add(post1, post2);
        post1.sichtbarSetzen(false);
        post2.sichtbarSetzen(false);

    }

    @Override
    public void show() {
        this.activ=true;
        mainOverlayImg.sichtbarSetzen(true);
        SPIEL.currentScreen=ScreenType.COMPUTERSCREEN;
    }

    public void viewPost1(){
        System.out.println("game.screen.ComputerScreen: viewPost1() aufgerufen" );
        hide();
        this.activ = true;
        show();
        post1.sichtbarSetzen(true);
    }
    public void viewPost2(){
        System.out.println("game.screen.ComputerScreen: viewPost2() aufgerufen" );
        hide();
        this.activ = true;
        show();
        post2.sichtbarSetzen(true);
    }

    @Override
    public void hide() {
        this.activ = false;
        mainOverlayImg.sichtbarSetzen(false);
        post1.sichtbarSetzen(false);
        post2.sichtbarSetzen(false);

    }

    public boolean isActiv() {
        return activ;
    }

}
