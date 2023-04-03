package com.raven.swing.table;

import Model.ClassRoom;


public interface EventAction {

    public void delete(int row);

    public void update(int row);
    
    public void detail(int row);

    public void doneAction(int row);
}
