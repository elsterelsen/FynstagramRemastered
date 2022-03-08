package game.screen;

import game.SPIEL;

public class SettingScreen extends WindowScreen{

    public SettingScreen(String path) {
        super(path, ScreenType.SETTINGSSCREEN);
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
