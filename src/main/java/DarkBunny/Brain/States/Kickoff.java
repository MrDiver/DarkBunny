package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.Car;
import DarkBunny.Brain.Data.CubicBezier;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;

public class Kickoff extends State{
    public Kickoff(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Kickoff";
    }

    CubicBezier c;
    @Override
    public AbstractAction getAction() {
        Car car = information.car;
        double distanceToBall = car.location().distance(information.ball.location());
        c = new CubicBezier(car.location(),car.orientation().noseVector.scaledToMagnitude(200),information.ball.location(),information.ball.location().minus(information.enemyGoal.location()).scaledToMagnitude(distanceToBall/2));
        AbstractAction a = chain(1000);


        /*if(car.inArea(10,new Vector3(-2048,-2560,0))||car.inArea(10,new Vector3(2048,2560,0)))
        {
            //RightCorner Kickoff

        }else
        if(car.inArea(10,new Vector3(2048,-2560,0))||car.inArea(10,new Vector3(-2048,2560,0)))
        {
            //System.out.println("Left Corner Kickoff");
            a = actionLibrary.diagonalFlick((float) (Math.PI/4),100,true);
        }else
        if(car.inArea(10,new Vector3(-256,-3840,0))||car.inArea(10,new Vector3(256,3840,0)))
        {
            //System.out.println("Back Right Kickoff");

        }else
        if(car.inArea(10,new Vector3(256,-3840,0))||car.inArea(10,new Vector3(-256,3840,0)))
        {
            //System.out.println("Back Left Kickoff");

        }else
        if(car.inArea(10,new Vector3(0,-4608,0))||car.inArea(10,new Vector3(0,4608,0))) {
            //System.out.println("Far Back Kickoff");
        }*/
        return a;
    }

    @Override
    public void draw(Bot bot) {
        if(c!=null)
            c.draw(bot);
    }

    @Override
    public boolean isAvailable() {
        return information.isKickoffPause()||true;
    }

    @Override
    public double getRating() {
        return information.isKickoffPause()?10:10;
    }
}
