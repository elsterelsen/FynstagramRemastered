package game.dataManagement;

public class ExtraInformation{
    private boolean empty;

    public ExtraInformation(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "ExtraInformation{ \n empty: "+empty+"\n}";

    }
}
