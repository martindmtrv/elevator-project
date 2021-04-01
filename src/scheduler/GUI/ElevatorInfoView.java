package scheduler.GUI;

import scheduler.ElevatorStatus;
import scheduler.Scheduler;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class ElevatorInfoView extends JPanel {

    public ElevatorInfoView(){
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));;
    }

    public void setCarInfo(ElevatorStatus elevatorStatus){
        this.removeAll(); //remove any previous info
        this.repaint();

        JLabel carIDLabel = new JLabel("Car " + elevatorStatus.getId());
        carIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carIDLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        carIDLabel.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(carIDLabel,BorderLayout.PAGE_START);

        JPanel carInfoPanel = new JPanel();
        carInfoPanel.setLayout(new BoxLayout(carInfoPanel,BoxLayout.PAGE_AXIS));

        JLabel carStatusLabel = new JLabel("Status: " + elevatorStatus.getStatus());
        carStatusLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        carStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel destinationsLabel = new JLabel("Destinations: " + printDirections(elevatorStatus.getDestinations()));
        destinationsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        destinationsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel directionLabel = new JLabel("Direction: " + elevatorStatus.getDirection());
        directionLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        directionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel locationLabel = new JLabel("Current Location: " + elevatorStatus.getLocation());
        locationLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        carInfoPanel.add(carStatusLabel);
        carInfoPanel.add(locationLabel);
        carInfoPanel.add(directionLabel);
        carInfoPanel.add(destinationsLabel);

        this.add(carInfoPanel,BorderLayout.CENTER);
        repaint();
        revalidate();
    }

    public StringBuilder printDirections(HashSet<Integer> destinations){
        StringBuilder s = new StringBuilder();
        for (Integer i : destinations){
            s.append(i).append(" ");

        }
        return s;
    }
}
