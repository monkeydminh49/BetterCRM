/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Viewer;

import Model.ClassRoom;

/**
 *
 * @author MinhDunk
 */
public interface TableActionEvent {
    public void onUpdate(ClassRoom classRoom);
    public void onDelete(int row);
    public void onView(int row);
}
