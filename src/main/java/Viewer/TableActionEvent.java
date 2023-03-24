/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Viewer;

import Controller.RequestAPI;
import Model.ClassRoom;
import java.util.List;

/**
 *
 * @author MinhDunk
 */
public interface TableActionEvent {

    /**
     *
     */
    public List<ClassRoom> classRoomList = RequestAPI.getInstance().getClassRoomList();;
    public void onUpdate(int row);
    public void onDelete(int row);
    public void onView(int row);
}
