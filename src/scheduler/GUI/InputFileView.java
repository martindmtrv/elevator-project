package scheduler.GUI;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class InputFileView extends JPanel {

    private static final int COLUMNS = 20;


    public InputFileView(){
        super();
        initInputFile();
    }

    public void initInputFile(){
        JPanel inputFilePanel = new JPanel();
        inputFilePanel.setLayout(new BoxLayout(inputFilePanel,BoxLayout.PAGE_AXIS));
        inputFilePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel inputLabel = new JLabel("Input File Events");
        inputLabel.setAlignmentX(CENTER_ALIGNMENT);
        inputLabel.setFont(new Font("Serif", Font.BOLD, 20));

        StringBuilder data = new StringBuilder();
        int counter = 0;
        try {
            File myObj = new File("Test.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                counter++;
                data.append(myReader.nextLine() + "\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            return;
        }
        JTextArea inputText = new JTextArea(data.toString(),counter-1,COLUMNS);
        inputText.setEditable(false);
        inputFilePanel.add(inputLabel);
        inputFilePanel.add(inputText);

        this.add(inputFilePanel);
    }
}
