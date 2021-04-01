package scheduler.GUI;

import event.DirectionType;
import main.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

/**
 * This class is the column that represents a car in the elevator system and
 * displays the floors and is updated when the elevator travels and performs events.
 *
 * @Author: Alex Cameron
 */
public class CarView {

    private JPanel[] floors;
    private int carID;
    private Image down, up, stopped;

    public CarView(int carID){
        this.carID = carID;
        floors = new JPanel[Configuration.NUM_FLOORS+2];
    }

    public void createFloors(JPanel carPanel){
        //setting the grid for the cars
        floors = new JPanel[Configuration.NUM_FLOORS+2];
        for(int j=1; j<= Configuration.NUM_FLOORS;j++){
            floors[j] = new JPanel();
            JLabel floorLabel = new JLabel("" + j);
            floorLabel.setFont(new Font("Serif", Font.BOLD, 14));
            floors[j].add(floorLabel);
            floors[j].setBackground(Color.WHITE);
            floors[j].setBorder(BorderFactory.createLineBorder(Color.black));
            floors[j].setSize(100,50);
            carPanel.add(floors[j]);
        }
    }

    public void resetFloorColor(){
        for(int j=1; j<= Configuration.NUM_FLOORS;j++){
            floors[j].setBackground(Color.WHITE);
        }
    }

    public void setFloor(int floorID, Color color){
        resetFloorColor();
        floors[floorID].setBackground(color);
    }
}

