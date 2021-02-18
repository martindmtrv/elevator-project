package event;

import java.util.Arrays;

/**
 * Author: Alex Cameron
 */
public class ElevatorTripUpdateEvent extends Event {

    private int car;
    private int floor, destinationFloor;
    private DirectionType direction;
    private boolean elevatorUpdate;

    /**
     * This is an event to notify the elevator before reaching a floor if the car
     * should prepare to either continue or stop before reaching the next floor
     * @param f - current floor number car is approaching
     * @param df - closest destination floor
     * @param c - car number
     * @param d - direction elevator should go
     */
    public ElevatorTripUpdateEvent(int c,int f,int df, DirectionType d) {
        super("", EventType.ELEVATOR_ARRIVAL_SENSOR);
        car = c;
        floor = f;
        destinationFloor =df;
        direction = d;
        elevatorUpdate = elevatorUpdate();
    }

    /**
     * Note: If there are no destination floors I assumed the elevator should stop at next floor
     * @return Boolean value of true if the next floor in specified direction is on destination list
     */
    public boolean elevatorUpdate(){
        if(destinationFloor == -1){
            return true;
        }else {
            if (direction == DirectionType.DOWN) {
                if(floor-- == destinationFloor){
                    return true;
                }else{
                    return false;
                }
            } else {
                if(floor++ == destinationFloor){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }

    public boolean getElevatorUpdate(){
        return elevatorUpdate;
    }

    public int getCar() {
        return car;
    }

    public int getFloor() {
        return floor;
    }
    public int getDestinationFloor() {
        return destinationFloor;
    }

    public DirectionType getDirection() {
        return direction;
    }

}
