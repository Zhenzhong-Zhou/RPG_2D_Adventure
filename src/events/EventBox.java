package events;

import java.awt.*;

public class EventBox extends Rectangle {
    private int eventBoxDefaultX, eventBoxDefaultY;
    private boolean eventHappened;



    public int getEventBoxDefaultX() {
        return eventBoxDefaultX;
    }

    public void setEventBoxDefaultX(int eventBoxDefaultX) {
        this.eventBoxDefaultX = eventBoxDefaultX;
    }

    public int getEventBoxDefaultY() {
        return eventBoxDefaultY;
    }

    public void setEventBoxDefaultY(int eventBoxDefaultY) {
        this.eventBoxDefaultY = eventBoxDefaultY;
    }

    public boolean isEventHappened() {
        return eventHappened;
    }

    public void setEventHappened(boolean eventHappened) {
        this.eventHappened = eventHappened;
    }
}
