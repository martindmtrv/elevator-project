package scheduler.GUI;

import scheduler.ElevatorStatus;
import scheduler.Scheduler;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
/**
 * This view displays the information about each elevator car which includes the elevator status, 
 * current location, direction and destinations.
 * 
 * @author: Alex Cameron
 *
 */
public class ElevatorInfoView extends JPanel {
	
    public ElevatorInfoView(){
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));;
    }
    /**
     * Updates the Jlabels with the requried information gotten from the ElevatorStatus on the scheduler side.
     * @param elevatorStatus
     */
    public void setCarInfo(ElevatorStatus elevatorStatus){
        this.removeAll(); //remove any previous info
        this.repaint();
        //Elevator Car ID
        JLabel carIDLabel = new JLabel("Car " + elevatorStatus.getId());
        carIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carIDLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        carIDLabel.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(carIDLabel,BorderLayout.PAGE_START);
        
        //init car info panel
        JPanel carInfoPanel = new JPanel();
        carInfoPanel.setLayout(new BoxLayout(carInfoPanel,BoxLayout.PAGE_AXIS));
        //init car status label
        JLabel carStatusLabel = new JLabel("Status: " + elevatorStatus.getStatus());
        carStatusLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        carStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //init the destinations label
        JLabel destinationsLabel = new JLabel("Destinations: " + printDirections(elevatorStatus.getDestinations()));
        destinationsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        destinationsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //init the direction label
        JLabel directionLabel = new JLabel("Direction: " + elevatorStatus.getDirection());
        directionLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        directionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //init the location label
        JLabel locationLabel = new JLabel("Current Location: " + elevatorStatus.getLocation());
        locationLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //add all label components to the info panel
        carInfoPanel.add(carStatusLabel);
        carInfoPanel.add(locationLabel);
        carInfoPanel.add(directionLabel);
        carInfoPanel.add(destinationsLabel);
        
        this.add(carInfoPanel,BorderLayout.CENTER);
        repaint();
        revalidate();
    }
    /**
     * Helper method to print all destinations in the Hashset of destinations taken from elevatorstatus from
     * scheduler.
     * 
     * @param destinations Hashset of destination floors the elevator has on route
     * @return A string of the elevators destinations
     */
    public StringBuilder printDirections(HashSet<Integer> destinations){
        StringBuilder s = new StringBuilder();
        for (Integer i : destinations){
            s.append(i).append(" ");

        }
        return s;
    }
}
