package game.screen;

import game.SPIEL;

public class AboutScreen extends WindowScreen{

    public AboutScreen(String path) {
        super(path, ScreenType.ABOUTSCREEN);
    }

    @Override
    public void show() {
        showSecret();
    }

    @Override
    public void hide() {
        hideSecret();
    }

    @Override
    public void toggleWindow() {
        super.toggleWindow();
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }
}
