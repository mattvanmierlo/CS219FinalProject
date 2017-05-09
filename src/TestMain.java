import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;

/**
 * Created by mattv on 4/29/2017.
 * N Body Simulation Using Classic Gravity
 * Matthijs van Mierlo
 * mtvan15@stlawu.edu
 *
 * Main Class that draws JPanel
 */
public class TestMain {
    public static void main(String[] args){
        JFrame myFrame = new JFrame("N-Body Simulation Art");
        JPanel animationPanel = new JPanel();
        JPanel selectionPanel = new JPanel();

        //read the files names out of the directory
        File folder = new File("./resources");
        File[] listofFiles = folder.listFiles();

        String[] generatedSelections = new String[listofFiles.length];


        for(int i = 0; i < generatedSelections.length; i++){
            generatedSelections[i] = listofFiles[i].getName();
            generatedSelections[i] = generatedSelections[i].substring(0,generatedSelections[i].length() - 4);
        }

        //just read this from the directory
        //make sure to not have to modify the files
        JComboBox<String> simulationFile = new JComboBox<String>(generatedSelections);
        JButton startButton = new JButton("Start");
        JSlider speedSlider = new JSlider();
        JLabel speedSliderDescription = new JLabel("Change the Refresh Rate", SwingConstants.CENTER);
        JLabel bodySize = new JLabel("Change the Body Size", SwingConstants.CENTER);
        JSlider sizeSlider = new JSlider();
        JLabel changeTailDescription = new JLabel("Change the Tail Size", SwingConstants.CENTER);
        JSlider tailSlider = new JSlider();
        JLabel massDescription = new JLabel("Add Body of Custom Mass: ", SwingConstants.CENTER);
        JTextField massInput = new JTextField("10000", SwingConstants.CENTER);
        JLabel velocityXDescription = new JLabel("Vx: ", SwingConstants.CENTER);
        JLabel velocityYDescription = new JLabel("Vy: ", SwingConstants.CENTER);
        JTextField velocityXInput = new JTextField("0", SwingConstants.CENTER);
        JTextField velocityYInput = new JTextField("2000", SwingConstants.CENTER);



        //modify the slider ever so slightly
        speedSlider.setMaximum(100);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        sizeSlider.setMaximum(20);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);

        tailSlider.setMaximum(1000);
        tailSlider.setMajorTickSpacing(100);
        tailSlider.setMinorTickSpacing(10);
        tailSlider.setPaintTicks(true);
        tailSlider.setPaintLabels(true);

        //set the layouts of the inner and the outer panels
        animationPanel.setLayout(new BorderLayout());
        selectionPanel.setLayout(new GridLayout(7,2));

        //add all elements to the panel
        selectionPanel.add(startButton);
        selectionPanel.add(simulationFile);
        selectionPanel.add(speedSliderDescription);
        selectionPanel.add(speedSlider);
        selectionPanel.add(bodySize);
        selectionPanel.add(sizeSlider);
        selectionPanel.add(changeTailDescription);
        selectionPanel.add(tailSlider);
        selectionPanel.add(massDescription);
        selectionPanel.add(massInput);
        selectionPanel.add(velocityXDescription);
        selectionPanel.add(velocityYDescription);
        selectionPanel.add(velocityXInput);
        selectionPanel.add(velocityYInput);


        simulationFile.setSelectedIndex(0);

        //make a new BodyCollections object
        BodyCollection collection = new BodyCollection();

        DrawBodies draw = new DrawBodies(1000,1000, speedSlider, startButton, simulationFile, sizeSlider, tailSlider, massInput, velocityXInput, velocityYInput);

        //add one panel within the other one
        animationPanel.add(selectionPanel, BorderLayout.PAGE_START);
        animationPanel.add(draw, BorderLayout.CENTER);

        myFrame.getContentPane().add(animationPanel);
        myFrame.pack();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
