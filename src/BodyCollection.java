import javafx.geometry.Point3D;

import java.util.ArrayList;

/**
 * Created by mattv on 4/29/2017.
 */
public class BodyCollection {
    private ArrayList<MovingBody> bodyCollection = new ArrayList<MovingBody>();

    private final double g = 6.673E-11;

    public BodyCollection(){

    }

    public void add(MovingBody aBody){
        bodyCollection.add(aBody);
    }

    public ArrayList<MovingBody> getBodyCollection(){
        return bodyCollection;
    }

    public void force(){
        for(int b = 0; b < bodyCollection.size(); b++){
            bodyCollection.get(b).resetForceComponents();
        }

       if(bodyCollection.size() == 0){
            //throw new RuntimeException("Please add at least one planet to the list. ");
        }


        for(int i = 0; i < bodyCollection.size() - 1; i++){

            int j = i+1;

            while(j < bodyCollection.size()){
                if(i==j){
                    j++;
                }
                else{
                   /* System.out.println(i + " " + j);*/

                    double distanceXi = bodyCollection.get(i).getPositionComponents().getX();
                    double distanceYi = bodyCollection.get(i).getPositionComponents().getY();

                    double distanceXj = bodyCollection.get(j).getPositionComponents().getX();
                    double distanceYj = bodyCollection.get(j).getPositionComponents().getY();

                    double differenceX = distanceXj - distanceXi;
                    double differenceY = distanceYj - distanceYi;

                    double distanceBetween = Math.sqrt(Math.pow(differenceX,2)+Math.pow(differenceY,2));

                    double totalForce = (g*bodyCollection.get(i).getMass()*bodyCollection.get(j).getMass())
                            /Math.pow(distanceBetween,2);

                    bodyCollection.get(i).setForce(totalForce);
                    bodyCollection.get(j).setForce(totalForce);

                    bodyCollection.get(i).setForceComponents(new Point3D(differenceX/distanceBetween*totalForce,
                            differenceY/distanceBetween*totalForce, 0));

                    bodyCollection.get(j).setForceComponents(new Point3D(-differenceX/distanceBetween*totalForce,
                            -differenceY/distanceBetween*totalForce, 0));

                    j++;


                }
            }
        }


    }

    public void update(double timeDifference){
        for(int i = 0; i<bodyCollection.size(); i++){
            bodyCollection.get(i).setAccelerationComponents();
            bodyCollection.get(i).setVelocityComponents(timeDifference);
            bodyCollection.get(i).setPositionComponents(timeDifference);
        }
    }
}
