package scheduler.GUI;

import main.Configuration;

import javax.swing.*;
import java.awt.*;

public class FloorView extends JFrame {
    public FloorView(){
        super("FloorViewTest");
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initFloorView();
    }

    public void initFloorView(){
        JPanel elevatorView = new JPanel();
        elevatorView.setLayout(new GridLayout(Configuration.NUM_FLOORS+1,Configuration.NUM_CARS+1));
        //set first row floors
        elevatorView.add(new JButton("Floors"));
        for(int i = 0; i<Configuration.NUM_CARS;i++){
            elevatorView.add(new JButton("Car" + i));
        }


        this.add(elevatorView, BorderLayout.CENTER);
    }
}
