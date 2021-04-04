package scheduler.GUI;

import main.Configuration;
import scheduler.GUI.SchedulerView;
import scheduler.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * SchedulerController class which controls actionevents for the scheduler view.
 * @author: Alex Cameron
 *
 */
public class SchedulerController implements ActionListener {
    private SchedulerView schedulerView;
    private Scheduler schedulerModel;
    /**
     * Constructor for SchedulerController
     * @param schedulerView - The SchedulerView
     * @param schedulerModel - The SchedulerModel
     */
    public SchedulerController(SchedulerView schedulerView, Scheduler schedulerModel){
        this.schedulerView = schedulerView;
        this.schedulerModel = schedulerModel;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() instanceof JButton){
        	//gets the carview info for the specified car's Jbutton if the carview has not been updated yet
            for(int i= 0; i<= Configuration.NUM_CARS;i++){ 
                if(e.getActionCommand().equals("" +i)){
                    schedulerView.getElevatorInfoView(i).setCarInfo(schedulerModel.getElevatorStatus(i)); 
                }
            }
        }
    }
}
