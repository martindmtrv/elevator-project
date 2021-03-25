package scheduler.GUI;

import event.DirectionType;
import main.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class CarView {

    private JPanel[] floors;
    private int carID;
    private JLabel directionLabel;
    private Image down, up, stopped;

    public CarView(int carID){
        this.carID = carID;
        floors = new JPanel[Configuration.NUM_FLOORS+1];
        directionLabel = new JLabel(); //broken!
    }

    public void createFloors(JPanel carPanel){
        //setting the grid for the cars
        floors = new JPanel[Configuration.NUM_FLOORS+1];
        for(int j=0; j< Configuration.NUM_FLOORS+1;j++){
            floors[j] = new JPanel();
            floors[j].setBackground(Color.WHITE);
            floors[j].setBorder(BorderFactory.createLineBorder(Color.black));
            carPanel.add(floors[j]);
        }
    }

    public void resetFloorColor(){
        for(int j=0; j< Configuration.NUM_FLOORS;j++){
            floors[j].setBackground(Color.WHITE);
        }
    }

    public void setDirectionLabel(DirectionType directionType){
        if(directionType == DirectionType.DOWN){
            ImageIcon downIcon = new ImageIcon("resources/down-arrow.png");
            directionLabel.setIcon(downIcon);
        }else if(directionType == DirectionType.UP){
            ImageIcon upIcon = new ImageIcon("resources/up-arrow.png");
            directionLabel.setIcon(upIcon);
        }else{
            ImageIcon stopIcon = new ImageIcon("resources/stop.png");
            directionLabel = new JLabel(stopIcon);
        }
    }

    public void setFloor(int floorID, Color color, DirectionType directionType){
        resetFloorColor();
        floors[floorID-1].setBackground(color);
        setDirectionLabel(directionType);
        floors[Configuration.NUM_FLOORS].add(directionLabel);
    }
}

