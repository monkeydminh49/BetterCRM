package com.raven.swing.table;


import Model.ClassRoom;

public interface EventAction {

    public void delete(ClassRoom classRoom);

    public void update(ClassRoom classRoom);
}
