package scheduler.GUI;

import main.Configuration;
import scheduler.GUI.SchedulerView;
import scheduler.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchedulerController implements ActionListener {
    private SchedulerView schedulerView;
    private Scheduler schedulerModel;

    public SchedulerController(SchedulerView schedulerView, Scheduler schedulerModel){
        this.schedulerView = schedulerView;
        this.schedulerModel = schedulerModel;

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() instanceof JButton){
            for(int i= 0; i<= Configuration.NUM_CARS;i++){
                if(e.getActionCommand().equals("" +i)){
                    schedulerView.getElevatorInfoView(i).setCarInfo(schedulerModel.getElevatorStatus(i));
                }
            }
        }
    }
}
