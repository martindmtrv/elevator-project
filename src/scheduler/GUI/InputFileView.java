package scheduler.GUI;

import main.Configuration;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Flow;

/**
 * This class is the view which shows the input file that is read by the floor subsystem and the
 * configuration settings used in the run.
 *
 * @Author: Alex Cameron
 */
public class InputFileView extends JPanel {

    private static final int COLUMNS = 20;
    private static final int ROWS = 20;
    private Color RED = new Color(249,65,68);
    private Color ORANGE = new Color(248,132,74);
    private Color YELLOW = new Color(249,199,79);
    private Color BLUE = new Color(15,141,176);
    private Color GREEN = new Color(82,230,109);
    private Color PURPLE = new Color(134,117,214);

    
    public InputFileView(){
        super();
        this.setLayout(new FlowLayout());
        initTitleBar();
        this.add(Box.createRigidArea(new Dimension(100,0))); //adding white space between views
        initInputFile();
        this.add(Box.createRigidArea(new Dimension(100,0))); //adding white space between views
        initConfigView();
    }
    
    /**
     * Initialize The legend view.
     */
    public void initTitleBar(){
    		
        JLabel titleLabel = new JLabel("Elevator Legend:");
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        
        //init legend panels & labels
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.PAGE_AXIS));
        legendPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel redLabel = new JLabel("Car Stopped");
        redLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel greenLabel = new JLabel("Car Continuing");
        greenLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel orangeLabel = new JLabel("Critical Error");
        orangeLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel purpleLabel = new JLabel("Transient Error");
        orangeLabel.setFont(new Font("Serif", Font.BOLD, 14));
        //direction labels
        JLabel yellowLabel = new JLabel("Floor Pickup Request DOWN");
        yellowLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel blueLabel = new JLabel("Floor Pickup Request UP");
        blueLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel cyanLabel = new JLabel("Floor Pickup Request UP and DOWN");
        cyanLabel.setFont(new Font("Serif", Font.BOLD, 14));
        
        //init the legend content panels
        JPanel redPanel = new JPanel();
        JPanel greenPanel = new JPanel();
        JPanel yellowPanel = new JPanel();
        JPanel bluePanel = new JPanel();
        JPanel orangePanel = new JPanel();
        JPanel purplePanel = new JPanel();
        JPanel cyanPanel = new JPanel();
        
        redPanel.setBackground(RED);
        greenPanel.setBackground(GREEN);
        yellowPanel.setBackground(YELLOW);
        orangePanel.setBackground(ORANGE);
        purplePanel.setBackground(PURPLE);
        bluePanel.setBackground(BLUE);
        cyanPanel.setBackground(Color.cyan);
        
        redPanel.add(redLabel);
        greenPanel.add(greenLabel);
        yellowPanel.add(yellowLabel);
        bluePanel.add(blueLabel);
        cyanPanel.add(cyanLabel);
        orangePanel.add(orangeLabel);
        purplePanel.add(purpleLabel);
        
        legendPanel.add(titleLabel);
        legendPanel.add(redPanel);
        legendPanel.add(Box.createRigidArea(new Dimension(10,10))); //adding white space between views
        legendPanel.add(greenPanel);
        legendPanel.add(Box.createRigidArea(new Dimension(10,10))); //adding white space between views
        legendPanel.add(yellowPanel);
        legendPanel.add(Box.createRigidArea(new Dimension(10,10))); //adding white space between views
        legendPanel.add(bluePanel);
        legendPanel.add(Box.createRigidArea(new Dimension(10,10))); //adding white space between views
        legendPanel.add(cyanPanel);
        legendPanel.add(Box.createRigidArea(new Dimension(10,10))); //adding white space between views
        legendPanel.add(orangePanel);
        legendPanel.add(Box.createRigidArea(new Dimension(10,10))); //adding white space between views
        legendPanel.add(purplePanel);
        
        this.add(legendPanel);
    }

    /**
     * The view for the input file 
     */
    public void initInputFile(){
        JPanel inputFilePanel = new JPanel();
        inputFilePanel.setLayout(new BoxLayout(inputFilePanel,BoxLayout.PAGE_AXIS));
        inputFilePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel inputLabel = new JLabel("Input File Events");
        inputLabel.setAlignmentX(CENTER_ALIGNMENT);
        inputLabel.setFont(new Font("Serif", Font.BOLD, 30));

        StringBuilder data = new StringBuilder();
        try {
            File myObj = new File("Test.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine() + "\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            return;
        }
        //Jtextarea with input file data
        //JTextArea inputText = new JTextArea(data.toString(),ROWS,COLUMNS);
        JTextArea inputText = new JTextArea(data.toString());
        inputText.setEditable(false);
        inputFilePanel.add(inputLabel);
        inputFilePanel.add(inputText);
    	JScrollPane scrollPane = new JScrollPane(inputFilePanel);
    	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	scrollPane.setPreferredSize(new Dimension(300,300));
        this.add(scrollPane);
    }

    /**
     * The view for the configuration settings
     */
    public void initConfigView(){
        JPanel configView = new JPanel();
        configView.setBorder(BorderFactory.createLineBorder(Color.black));
        configView.setLayout(new BorderLayout());
        JLabel configNumLabel = new JLabel("Configuration Data");
        configNumLabel.setAlignmentX(CENTER_ALIGNMENT);
        configNumLabel.setFont(new Font("Serif", Font.BOLD, 30));
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.PAGE_AXIS));
        JLabel configFileLabel = new JLabel("Input File: " + Configuration.INPUT_FILE);
        JLabel numFloorsLabel = new JLabel("Number of Floors: " + Configuration.NUM_FLOORS);
        JLabel numCarsLabel = new JLabel("Number of Cars: " + Configuration.NUM_CARS);

        JLabel initCarFloorLabel = new JLabel("Initial Car Floor: " + Configuration.INIT_CAR_FLOOR);
        JLabel loadTimeLabel = new JLabel("Load Time: " + Configuration.LOAD_TIME/1000 +"s");
        JLabel timeBetweenFloorsLabel = new JLabel("Time Between Floors: " + Configuration.TRAVEL_TIME_BETWEEN_FLOOR/1000 +"s");

        JLabel elevatorPortLabel = new JLabel("Elevator Port: "+ Configuration.ELEVATOR_PORT );
        JLabel floorPortLabel = new JLabel("Floor Port: " + Configuration.FLOOR_PORT);
        JLabel schedulerListenPortLabel = new JLabel("Scheduler Port: " + Configuration.FLOOR_PORT);

        //Filler label
        JLabel fillerLabel = new JLabel("____________________________________");


        //setting backgrounds
        configFileLabel.setBackground(Color.WHITE);
        configNumLabel.setBackground(Color.WHITE);
        numFloorsLabel.setBackground(Color.WHITE);
        numCarsLabel.setBackground(Color.WHITE);
        initCarFloorLabel.setBackground(Color.WHITE);
        loadTimeLabel.setBackground(Color.WHITE);
        timeBetweenFloorsLabel.setBackground(Color.WHITE);
        elevatorPortLabel.setBackground(Color.WHITE);
        floorPortLabel.setBackground(Color.WHITE);
        schedulerListenPortLabel.setBackground(Color.WHITE);
        fillerLabel.setBackground(Color.darkGray);

        //adding labels to panel
        configView.add(configNumLabel,BorderLayout.PAGE_START);
        settingsPanel.add(fillerLabel);
        settingsPanel.add(configFileLabel);
        settingsPanel.add(numFloorsLabel);
        settingsPanel.add(numCarsLabel);

        settingsPanel.add(initCarFloorLabel);
        settingsPanel.add(loadTimeLabel);
        settingsPanel.add(timeBetweenFloorsLabel);

        settingsPanel.add(elevatorPortLabel);
        settingsPanel.add(floorPortLabel);
        settingsPanel.add(schedulerListenPortLabel);
        configView.add(settingsPanel,BorderLayout.LINE_START);
        configView.add(Box.createRigidArea(new Dimension(0,20))); //adding white space between views
        this.add(configView);
    }
}
