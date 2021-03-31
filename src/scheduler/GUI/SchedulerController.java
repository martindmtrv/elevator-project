package scheduler.GUI;

import scheduler.GUI.SchedulerView;
import scheduler.Scheduler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchedulerController implements ActionListener {
    private SchedulerView schedulerView;
    private Scheduler schedulerModel;

    public SchedulerController(SchedulerView schedulerView){
        this.schedulerView = schedulerView;

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("start")){

        }
    }
}
