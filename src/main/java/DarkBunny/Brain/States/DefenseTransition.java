package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Bool;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.Data.Mathics;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;

public class DefenseTransition extends State{
    public DefenseTransition(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "defense Transition";
    }

    @Override
    public AbstractAction getAction() {
        Value angle = () -> Mathics.cap(information.car.transformToLocal(information.ball.location()).angle2D()*2,-1,1);
        Value throttle = ()-> (information.car.speed()>100)? -1:0.2f;
        Bool slide = ()-> throttle.val()>0;
        return action(10).add(part(0,100).withThrottle(throttle).withSteer(angle).withSlide(slide));
    }

    @Override
    public void draw(Bot bot) {

    }

    @Override
    public boolean isAvailable() {
        if(Math.abs(information.car.transformToLocal(new Vector3()).angle2D()) < 0.5)
            return false;
        return !predictions.facing(information.ball.location())&&information.car.location().distance(information.ownGoal.location()) < 1600;
    }

    @Override
    public double getRating() {
        return information.car.location().distance(information.ownGoal.location()) < 800? 6.1:0;
    }
}
