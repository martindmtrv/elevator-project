package scheduler.GUI;

import elevator.ElevatorSubsystem;
import event.DirectionType;
import event.ElevatorButtonPressEvent;
import event.ElevatorTripUpdateEvent;
import event.FloorButtonPressEvent;
import floor.FloorSubsystem;
import main.Configuration;
import scheduler.ElevatorJobState;
import scheduler.ElevatorStatus;
import scheduler.ElevatorTripUpdate;
import scheduler.Scheduler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class SchedulerView extends JFrame implements SchedulerViewListener {

    private SchedulerController sc;
    private ElevatorSubsystem elevatorSubsystem;
    private FloorSubsystem floorSubsystem;
    private Scheduler schedulerModel;
    private CarView[] carViews;
    private NotificationView notificationView;
    private ElevatorInfoView[] elevatorInfoViews;
    private JPanel carPanel, elevatorInfoPanel, inputFilePanel;
    private JPanel[] floors, carDirections;
    private JLabel directionLabel;
    private JSplitPane splitPane;


    public SchedulerView(Scheduler schedulerModel){
        super("ElevatorSubsystem");
        this.setLayout(new BorderLayout());
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize,ySize); //set size to max screen
        this.setMinimumSize(new Dimension(xSize-100,ySize-100));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.schedulerModel = schedulerModel;

        //initialize Scheduler Controller
        sc = new SchedulerController(this, schedulerModel);

        //set listeners
        schedulerModel.addSchedulerViewListeners(this);
        //initialize the
        initTitleBar();
        //initialize the floors and cars
        initFloors();
        initCars();
        directionLabel = new JLabel();
        //initialize car info view
        initElevatorInfoViews();
        //initialize input file view
        initInputFileView();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, elevatorInfoPanel, inputFilePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        this.add(splitPane,BorderLayout.CENTER);
        //initialize notification view
        this.notificationView = new NotificationView();
        this.add(notificationView, BorderLayout.LINE_END);
    }

    public void initTitleBar(){
        JPanel titleBar = new JPanel();
        titleBar.setBackground(Color.darkGray);
        JLabel titleLabel = new JLabel("SYSC3303 Final Project - Group 7: Elevator System.      Legend:");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(Color.WHITE);

        JPanel legendPanel = new JPanel();
        legendPanel.setBackground(Color.lightGray);
        JLabel redLabel = new JLabel("Car Stopped");
        redLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel greenLabel = new JLabel("Car Continuing");
        greenLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel yellowLabel = new JLabel("Floor Pickup Request");
        yellowLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel orangeLabel = new JLabel("Critical Error");
        orangeLabel.setFont(new Font("Serif", Font.BOLD, 14));

        JPanel redPanel = new JPanel();
        JPanel greenPanel = new JPanel();
        JPanel yellowPanel = new JPanel();
        JPanel orangePanel = new JPanel();
        redPanel.setBackground(Color.red);
        greenPanel.setBackground(Color.GREEN);
        yellowPanel.setBackground(Color.YELLOW);
        orangePanel.setBackground(Color.ORANGE);

        redPanel.add(redLabel);
        greenPanel.add(greenLabel);
        yellowPanel.add(yellowLabel);
        orangePanel.add(orangeLabel);

        legendPanel.add(redPanel);
        legendPanel.add(greenPanel);
        legendPanel.add(yellowPanel);
        legendPanel.add(orangePanel);

        titleBar.add(titleLabel);
        titleBar.add(legendPanel);
        this.add(titleBar, BorderLayout.PAGE_START);
    }

    public void initFloors(){
        JPanel floorPanel = new JPanel(); //floor panel
        floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.PAGE_AXIS));
        floors = new JPanel[Configuration.NUM_FLOORS+1]; //The rows for floors and floor header
        JLabel[] floorLabels = new JLabel[Configuration.NUM_FLOORS+1];

        //Creating floor header
        JPanel floorHeader = new JPanel();
        floorHeader.setBorder(BorderFactory.createLineBorder(Color.black));
        floorHeader.add(new JLabel("Floors:"));
        floorHeader.setFont(new Font("Serif", Font.BOLD, 14));
        floorHeader.setBackground(Color.lightGray);
        floorPanel.add(floorHeader);

        //Initializing all panels for each floor
        for(int i=Configuration.NUM_FLOORS; i>=1 ;i--){
            floors[i] = new JPanel();
            floors[i].setBorder(BorderFactory.createLineBorder(Color.black));
            floorLabels[i] = new JLabel("Floor " + i);
            floorLabels[i].setFont(new Font("Serif", Font.BOLD, 14));
            floors[i].add(floorLabels[i]);
            floors[i].setBackground(Color.WHITE);
            floorPanel.add(floors[i]);
        }

        //Direction label
        JPanel directionFloorPanel = new JPanel();
        JLabel directionFloorLabel = new JLabel("Direction:");
        directionFloorPanel.setBackground(Color.white);
        directionFloorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        directionFloorLabel.setFont(new Font("Serif", Font.BOLD, 14));
        directionFloorPanel.add(directionFloorLabel);
        floorPanel.add(directionFloorPanel);

        carPanel = new JPanel();
        carPanel.setLayout(new FlowLayout());
        carPanel.add(floorPanel);
    }

    public void initCars(){
        JPanel[] cars = new JPanel[Configuration.NUM_CARS]; //panel for each car
        carViews = new CarView[Configuration.NUM_CARS]; //car views for each car which contain the floors
        JButton[] carLabelButtons = new JButton[Configuration.NUM_CARS]; //labels at top representing each car
        carDirections = new JPanel[Configuration.NUM_CARS]; //Direction panels for each car with icons specifying direction

        //create car labels for each elevator car
        for(int i=0; i< Configuration.NUM_CARS;i++){
            if(i==0){
                carPanel.add(Box.createRigidArea(new Dimension(20,0))); //adding white space between views
            }
            cars[i] = new JPanel();
            cars[i].setLayout(new BoxLayout(cars[i],BoxLayout.PAGE_AXIS));
            cars[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cars[i].setBackground(Color.lightGray);
            JPanel carLabelPanel = new JPanel();
            carLabelButtons[i] = new JButton("Car " + i);
            carLabelButtons[i].setFont(new Font("Serif", Font.BOLD, 14));
            carLabelButtons[i].setFocusPainted(false);
            carLabelButtons[i].setBackground(Color.lightGray);
            carLabelPanel.add(carLabelButtons[i]);
            cars[i].add(carLabelPanel);
            carPanel.add(cars[i]);

            if(i!=Configuration.NUM_CARS-1){
                carPanel.add(Box.createRigidArea(new Dimension(20,0))); //adding white space between views
            }

            //actionlisteners for car buttons
            carLabelButtons[i].addActionListener(sc);
            carLabelButtons[i].setActionCommand("" +i);
        }

        //setting up carviews which represent each floor for each elevator car
        for(int i = 0; i<cars.length;i++){
            carViews[i] = new CarView(i); //create new Car view
            carViews[i].createFloors(cars[i]); //init all floors
        }

        //setup direction panels
        for(int i = 0; i<cars.length;i++){
            carDirections[i] = new JPanel();
            carDirections[i].add(new JLabel("IDLE"));
            carDirections[i].setFont(new Font("Serif", Font.BOLD, 14));
            cars[i].add(carDirections[i]);
        }

        this.add(carPanel,BorderLayout.LINE_START);
    }

    public void initElevatorInfoViews(){
        elevatorInfoPanel = new JPanel();
        elevatorInfoPanel.setLayout(new FlowLayout());
        elevatorInfoViews = new ElevatorInfoView[Configuration.NUM_CARS];
        for(int i=0; i<Configuration.NUM_CARS; i++){
            elevatorInfoViews[i] = new ElevatorInfoView();
            elevatorInfoPanel.add(elevatorInfoViews[i]);

            if(i!=Configuration.NUM_CARS-1){
                elevatorInfoPanel.add(Box.createRigidArea(new Dimension(20,0))); //adding white space between views
            }
        }
    }

    public void initInputFileView(){
        inputFilePanel = new InputFileView();
    }

    public NotificationView getNotificationView(){
        return notificationView;
    }

    public ElevatorInfoView getElevatorInfoView(int carID) {
        return elevatorInfoViews[carID];
    }

    public void setDirectionLabel(DirectionType directionType, JPanel panel){
        if(directionType==DirectionType.DOWN){
            directionLabel = new JLabel("DOWN");
        }else if(directionType==DirectionType.UP){
            directionLabel = new JLabel("UP");
        }else {
            directionLabel = new JLabel("STOPPED");
        }
        directionLabel.setFont(new Font("Serif", Font.BOLD, 14));
        panel.removeAll();
        panel.add(directionLabel);
    }

    @Override
    public void handleElevatorStatusUpdate(ElevatorTripUpdateEvent e) {
        if(e.getUpdate() == ElevatorTripUpdate.CONTINUE){
            carViews[e.getCar()].setFloor(e.getApproachingFloor(),Color.GREEN);
        }else{
            carViews[e.getCar()].setFloor(e.getApproachingFloor(),Color.RED);
        }
    }

    @Override
    public void handleFloorButtonPressUpdate(FloorButtonPressEvent e){
        floors[e.getFloor()].setBackground(Color.YELLOW);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void handleElevatorButtonPressUpdate(ElevatorButtonPressEvent e){
        floors[e.getState().getFloorNum()].setBackground(Color.WHITE); //reset floor panel that previously requested a pickup
        //NOTE: UP vs DOWN Pickups
    }

    @Override
    public void handleElevatorStateUpdate(ElevatorStatus elevatorStatus){
        elevatorInfoViews[elevatorStatus.getId()].setCarInfo(elevatorStatus);

        if(elevatorStatus.getStatus() == ElevatorJobState.IDLE){
            carViews[elevatorStatus.getId()].setFloor(elevatorStatus.getLocation(),Color.RED);
        }

        if(elevatorStatus.isFaulty()){
            carViews[elevatorStatus.getId()].setFloor(elevatorStatus.getLocation(),Color.ORANGE);
        }

        //set Elevator car direction
        setDirectionLabel(elevatorStatus.getDirection(),carDirections[elevatorStatus.getId()]);
    }
}
