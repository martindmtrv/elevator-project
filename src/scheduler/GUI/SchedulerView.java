package scheduler.GUI;

import elevator.ElevatorSubsystem;
import event.DirectionType;
import event.ElevatorTripUpdateEvent;
import floor.FloorSubsystem;
import main.Configuration;
import scheduler.ElevatorTripUpdate;
import scheduler.Scheduler;

import javax.swing.*;
import java.awt.*;

public class SchedulerView extends JFrame implements SchedulerViewListener {

    private SchedulerController sc;
    private ElevatorSubsystem elevatorSubsystem;
    private FloorSubsystem floorSubsystem;
    private Scheduler schedulerModel;
    private CarView[] carViews;
    private NotificationView notificationView;

    public SchedulerView(Scheduler schedulerModel){
        super("ElevatorSubsystem");
        this.setLayout(new BorderLayout());
        this.setSize(500,500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.schedulerModel = schedulerModel;

        //initialize Scheduler Controller
        sc = new SchedulerController(this);

        //set listeners
        schedulerModel.addSchedulerViewListeners(this);

        //initialize the floors
        initTestBar();
        initCars();
        //initFloors(); not useful

        //initialize notification view
        this.notificationView = new NotificationView();
        this.add(notificationView, BorderLayout.LINE_END);
    }

    public void initTestBar(){
        JPanel testBar = new JPanel();
        JLabel testLabel = new JLabel("Group 7: Prototype GUI for Iteration 5.      LEGEND:");
        JPanel legendPanel = new JPanel();
        JLabel redLabel = new JLabel("Car Stopped");
        JLabel greenLabel = new JLabel("Car Continuing");
        JPanel redPanel = new JPanel();
        JPanel greenPanel = new JPanel();
        redPanel.setBackground(Color.red);
        greenPanel.setBackground(Color.GREEN);
        redPanel.add(redLabel);
        greenPanel.add(greenLabel);
        legendPanel.add(redPanel);
        legendPanel.add(greenPanel);
        testBar.add(testLabel);
        testBar.add(legendPanel);
        this.add(testBar, BorderLayout.PAGE_START);
    }

    public void initFloors(){
        JPanel floorPanel = new JPanel();
        floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.PAGE_AXIS));
        JPanel[] floors = new JPanel[Configuration.NUM_FLOORS];
        JLabel[] floorLabels = new JLabel[Configuration.NUM_FLOORS];

        JPanel floorHeader = new JPanel();
        floorHeader.setBorder(BorderFactory.createLineBorder(Color.black));
        floorHeader.add(new JLabel("Floors:"));
        floorHeader.setBackground(Color.lightGray);
        floorPanel.add(floorHeader);

        for(int i=0; i< Configuration.NUM_FLOORS;i++){
            floors[i] = new JPanel();
            floors[i].setBorder(BorderFactory.createLineBorder(Color.black));
            floorLabels[i] = new JLabel("Floor " + i);
            floors[i].add(floorLabels[i]);
            floorPanel.add(floors[i]);
        }
        floorPanel.setSize(100,50);
        this.add(floorPanel,BorderLayout.CENTER);
    }

    public void initCars(){
        JPanel carPanel = new JPanel();
        carPanel.setLayout(new FlowLayout());
        JPanel[] cars = new JPanel[Configuration.NUM_CARS];
        carViews = new CarView[Configuration.NUM_CARS];
        JLabel[] carLabels = new JLabel[Configuration.NUM_CARS];

        for(int i=0; i< Configuration.NUM_CARS;i++){
            cars[i] = new JPanel();
            cars[i].setLayout(new BoxLayout(cars[i],BoxLayout.PAGE_AXIS));
            cars[i].setBorder(BorderFactory.createLineBorder(Color.black));
            cars[i].setBackground(Color.lightGray);
            carLabels[i] = new JLabel("Car " + i);
            cars[i].add(carLabels[i]);
            carPanel.add(cars[i]);
        }

        //setting the grid for the cars
        for(int i = 0; i<cars.length;i++){
            carViews[i] = new CarView(i); //create new Car view
            carViews[i].createFloors(cars[i]); //init all floors
        }
        this.add(carPanel,BorderLayout.LINE_START);
    }

    public NotificationView getNotificationView(){
        return notificationView;
    }

    @Override
    public void handleElevatorStatusUpdate(ElevatorTripUpdateEvent e) {
        if(e.getUpdate() == ElevatorTripUpdate.CONTINUE){
            carViews[e.getCar()].setFloor(e.getApproachingFloor(),Color.GREEN, DirectionType.DOWN);
        }else{
            carViews[e.getCar()].setFloor(e.getApproachingFloor(),Color.RED,DirectionType.UP);
        }
    }

    public static void main(String[] args) {
    }
}
