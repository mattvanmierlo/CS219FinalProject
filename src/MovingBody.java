import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by mattv on 4/29/2017.
 */
public class MovingBody {

    //all the instance variables for a MovingBody object
    private double mass;
    private double force;
    private double orbitalRadius;
    private Point3D positionComponents;
    private Point3D velocityComponents;
    private Point3D accelerationComponents;
    private Point3D forceComponents;
    private ArrayList<Point3D> allPositionComponents = new ArrayList<>();
    private Color color;
    private int tailSize = 100;

    //rather than storing these in a Point3D array, I am just going to
    //keep them as separate instance variables with descriptive names
    //for the sake of organization

    public MovingBody(double xValue, double yValue, double aMass, Point3D aVelocity){

        //assigning the orbital radius, mass, velocity, force, and position
        mass = aMass;
        velocityComponents = aVelocity;
        forceComponents = new Point3D(0,0,0);
        positionComponents = new Point3D(xValue, yValue, 0);
        allPositionComponents.add(positionComponents);
        Random random = new Random();
        color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

        //tested the constructor and how it assigns the components to the velocity
        //this methodology is sound
    }

    public Color getColor() {
        return color;
    }

    //here are a bunch of get methods for the MovingBody Class
    public double getMass() {

        return mass;
    }

    public double getForce() {

        return force;
    }

    public Point3D getPositionComponents() {

        return positionComponents;
    }

    public Point3D getForceComponents() {

        return forceComponents;
    }



    //here are a bunch of set methods for the MovingBody Class
    public void setMass(double mass) {

        this.mass = mass;
    }


    public void setForce(double force) {

        this.force = force;
    }


    public ArrayList<Point3D> getAllPositionComponents() {
        return allPositionComponents;
    }

    //need to modify
    public void setPositionComponents(double timeDifference) {
        //add the old coordinates in the object instance variable array

        this.positionComponents = new Point3D((velocityComponents.getX() * timeDifference) + positionComponents.getX(),
                (velocityComponents.getY() * timeDifference) + positionComponents.getY(),
                (velocityComponents.getZ() * timeDifference) + positionComponents.getZ());

        if(allPositionComponents.size() < tailSize){
            allPositionComponents.add(positionComponents);
        }

        if(allPositionComponents.size() == tailSize && allPositionComponents.size() != 0){
            allPositionComponents.remove(0);
        }

    }

    public void setTailSize(int tailSizeTemp) {

        tailSize = tailSizeTemp;

        if(allPositionComponents.size() >= tailSize){
            int tempSize = allPositionComponents.size();
           for(int i = 0; i < tempSize - tailSize; i++){
               allPositionComponents.remove(0);
           }
        }
        else{

        }
    }

    public Point3D getVelocityComponents() {

        return velocityComponents;
    }

    //need to modify
    public void setVelocityComponents(double timeDifference) {

        this.velocityComponents = new Point3D((accelerationComponents.getX() * timeDifference) + velocityComponents.getX(),
                (accelerationComponents.getY() * timeDifference) + velocityComponents.getY(),
                (accelerationComponents.getZ() * timeDifference) + velocityComponents.getZ());
    }

    public Point3D getAccelerationComponents() {

        return accelerationComponents;
    }

    //just need to set, do NOT need to add
    public void setAccelerationComponents() {

        this.accelerationComponents = new Point3D(forceComponents.getX() / getMass(), forceComponents.getY() / getMass(),
                forceComponents.getZ() / getMass());
    }



    public void setForceComponents(Point3D forceComponents) {

        this.forceComponents = this.forceComponents.add(forceComponents);
    }

    public void resetForceComponents(){
        forceComponents = new Point3D(0,0,0);
    }
}
