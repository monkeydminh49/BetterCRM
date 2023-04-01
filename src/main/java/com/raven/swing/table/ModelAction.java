package com.raven.swing.table;

import Model.ClassRoom;

public class ModelAction {

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public EventAction getEvent() {
        return event;
    }

    public void setEvent(EventAction event) {
        this.event = event;
    }

    public ModelAction(ClassRoom classRoom, EventAction event) {
        this.classRoom = classRoom;
        this.event = event;
    }

    public ModelAction() {
    }

    private ClassRoom classRoom;
    private EventAction event;
}
