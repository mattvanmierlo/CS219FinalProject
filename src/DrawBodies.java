import javafx.geometry.Point3D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by mattv on 4/29/2017.
 */
public class DrawBodies extends JPanel implements ActionListener, ChangeListener, MouseMotionListener, MouseListener {

    //Instance variable components
    private int windowHeight, windowWidth;
    private BodyCollection collection;
    private Color color;
    private double maxOrbit = 100;
    private double scalingFactor;
    private Point mouseLocation = new Point(0,0);
    private double userMass;
    private Point3D userSpeed = new Point3D(0,0,0);

    private int objectSize = 20;

    //need to keep track of the JButton and the speedSlider
    private JSlider speedSlider;
    private JButton startButton;
    private JComboBox<String> fileSelection;
    private JSlider sizeSlider;
    private JSlider tailSlider;
    private JTextField massInput;
    private JTextField velocityXInput;
    private JTextField velocityYInput;

    //Timer for the animation
    private Timer timer;

    private int timeDifference = 10000;

    //constructor for the animation panel
    public DrawBodies(int awidth, int aheight,
                       JSlider aslide, JButton abutton, JComboBox<String> aCombo, JSlider sizeSlider, JSlider tailSlider,
                        JTextField aMassInput, JTextField aXVelocityInput, JTextField aYVelocityInput){

        //update the instance variables in the class
        windowWidth = awidth;
        windowHeight = aheight;
        scalingFactor = (Math.sqrt(Math.pow(windowWidth /2,2)+ Math.pow(windowHeight /2,2)))/(2*maxOrbit);
        //collection = acollection;
        collection = new BodyCollection();
        speedSlider = aslide;
        startButton = abutton;
        color = Color.BLACK;
        fileSelection = aCombo;
        this.sizeSlider = sizeSlider;
        this.tailSlider = tailSlider;
        massInput = aMassInput;
        velocityXInput = aXVelocityInput;
        velocityYInput = aYVelocityInput;

        //addActionListener to the fileSelection comboBox
        fileSelection.addActionListener(this);

        //set the size of the window
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        startButton.addActionListener(this);

        //set ChangeListener for speedSlider
        speedSlider.addChangeListener(this);

        //set the ChangeListener for the sizeSlider
        this.sizeSlider.addChangeListener(this);

        this.tailSlider.addChangeListener(this);

        //add listener for the mouse clicks
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        fileSelection.setSelectedItem(0);


        //who's going to be listening for an event from the button
        //that is going to be this class

        //start the timer
        timer = new Timer(100,this);

        timer.start();
    }

    //overriding the default protected void paintComponent method

    protected void paintComponent(Graphics page){


        //rely on the default constructor at first
        super.paintComponent(page);

        //set the color/paintbrush to the specified color
        page.setColor(color);

        //load the default file here!


        //draw each circle on the page
        for(int i = 0; i < collection.getBodyCollection().size() ; i++){
            page.setColor(color);
            page.fillOval((int)(collection.getBodyCollection().get(i).getPositionComponents().getX() * scalingFactor) + (windowWidth /2),
                    (int)(collection.getBodyCollection().get(i).getPositionComponents().getY() * scalingFactor) + (windowHeight /2),
                    objectSize,objectSize);

            //draw a trailing line behind the objects
            if(collection.getBodyCollection().get(i).getAllPositionComponents().size() != 1 &&
                    collection.getBodyCollection().get(i).getAllPositionComponents().size() != 0) {
                for (int b = 0; b < collection.getBodyCollection().get(i).getAllPositionComponents().size() - 1 ; b++) {
                    int x1 = (int) (collection.getBodyCollection().get(i).getAllPositionComponents().get(b).getX() * scalingFactor) + (windowWidth / 2) + objectSize/2;
                    int x2 = (int) (collection.getBodyCollection().get(i).getAllPositionComponents().get(b + 1).getX() * scalingFactor) + (windowWidth / 2) + objectSize/2;
                    int y1 = (int) (collection.getBodyCollection().get(i).getAllPositionComponents().get(b).getY() * scalingFactor) + (windowHeight / 2) + objectSize/2;
                    int y2 = (int) (collection.getBodyCollection().get(i).getAllPositionComponents().get(b + 1).getY() * scalingFactor) + (windowHeight / 2) + objectSize/2;
                    page.setColor(collection.getBodyCollection().get(i).getColor());
                    page.drawLine(x1, y1, x2, y2);
                }
            }
        }

        //write method here to draw lines of where everything has been going


    }

    @Override
    //overriding the default actionPerformed method for this class
    //this class is listening for multiple events
    //one is the timer, the other is a button press

    //these two loops identify the source of the event
    //and act accordingly
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
            //if the start button is clicked, check the values
            //start the timer
            if(startButton.getText().equals("Start")){
                startButton.setText("Pause");
                timer.start();
                System.out.println("Timer Started");
            }
            else{
                //change the textbox and stop the timer
                startButton.setText("Start");
                timer.stop();
            }
        }

        //update the timer and the force
        //update the position of each of the objects
        if(e.getSource() == timer){
            collection.force();
            collection.update(timeDifference);
            super.repaint();
        }

        //if the source was a fileselection from the dropdown menu
        if(e.getSource() == fileSelection){

            System.out.println(e.getSource());
            //get which item was selected
            String selectedItem = (String)fileSelection.getSelectedItem();

            //make a new File object
            File simulationFile;

            //assign the simulation file
            simulationFile = new File("./resources/" + (String)fileSelection.getSelectedItem() + ".txt");

                //switch statement for the various selections
                //array of file names
                //name of the choice is actually the name of the file
                //can concatenate the pathway

                //stop the timer so that you see the page refresh
                timer.stop();
                startButton.setText("Start");
                //if the document exists...
                try {
                    //possible this will not find an error
                    Scanner scanner = new Scanner(simulationFile);

                    //scan for the following indicated values to add objects to the BodyCollections object
                    int index = scanner.nextInt();
                    double maxRadius = scanner.nextDouble();
                    maxOrbit = maxRadius;

                    collection.getBodyCollection().clear();

                    for (int i = 0; i < index; i++) {
                        //sequentially add MovingBody objects to the BodyCollections object
                        double xPos = scanner.nextDouble();
                        double yPos = scanner.nextDouble();
                        Point3D velocity = new Point3D(scanner.nextDouble(), scanner.nextDouble(), 0);
                        double mass = scanner.nextDouble();
                        String tempString = scanner.next();
                        collection.add(new MovingBody(xPos, yPos, mass, velocity));
                    }

                    System.out.println(collection.getBodyCollection().size() + " added to the list");
                    scalingFactor = windowWidth / 2 / maxOrbit;
                    //scalingFactor = (Math.sqrt(Math.pow(windowWidth/2,2)+ Math.pow(windowHeight/2,2)))/(maxOrbit);
                    super.repaint();

                    System.out.println(collection.getBodyCollection().size());

                    collection.force();

                } catch (FileNotFoundException exception) {
                    System.err.println("File Not Found");
                }

        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int speed = 0;
        if(e.getSource() == speedSlider){
            //if the speedSlider is not adjusting, what to do...
            if (speedSlider.getValueIsAdjusting()){
                //System.out.println((int)speedSlider.getValue());
                speed = (int)speedSlider.getValue();
                timer.setDelay(speed);
            }else{
                //print out the delay just to make sure that it is the correct value
                //System.out.println(timer.getDelay());
            }
        }

        if(e.getSource() == sizeSlider){
            //if the speedSlider is not adjusting, what to do...
            if (sizeSlider.getValueIsAdjusting()){
                objectSize = (int)sizeSlider.getValue();
                super.repaint();
            }else{
                //print out the delay just to make sure that it is the correct value
            }
        }

        if(e.getSource() == tailSlider){
            //if the speedSlider is not adjusting, what to do...
            if (!tailSlider.getValueIsAdjusting()){


                System.out.println(tailSlider.getValue());
                for(int i = 0; i < collection.getBodyCollection().size(); i++){
                    collection.getBodyCollection().get(i).setTailSize((int)tailSlider.getValue());
                }



            }else{
                //print out the delay just to make sure that it is the correct value
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if(e.getButton() == 1) {
            try {
                collection.add(new MovingBody((mouseLocation.x - (windowWidth / 2)) / scalingFactor,
                        (mouseLocation.y - (windowHeight / 2)) / scalingFactor, Double.parseDouble(massInput.getText()),
                        new Point3D(Double.parseDouble(velocityXInput.getText()), Double.parseDouble(velocityYInput.getText()), 0)));
                super.repaint();
            } catch (RuntimeException exception) {
                System.err.println("Please Enter Valid Values for Mass and Velocity");
                JOptionPane.showMessageDialog(this, "Please Enter Valid Values for Mass and Velocity");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();

    }
}
