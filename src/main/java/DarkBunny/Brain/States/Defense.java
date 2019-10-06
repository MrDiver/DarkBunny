package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.Ball;
import DarkBunny.Brain.Data.CubicBezier;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;

public class Defense extends State {
    public Defense(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "DEFENSE";
    }


    CubicBezier c;
    Vector3 target;
    Ball ballPosition;
    Vector3 goalPoint;
    Vector3 diff;
    AbstractAction last;
    Vector3 hitVector;
    @Override
    public AbstractAction getAction() {
        double distanceToBall = information.car.location().distance(information.ball.location());
        double distanceToBallFlat = information.car.location().make2D().distance(information.ball.location().make2D());


        float futureTime = (float) ((distanceToBall)/information.car.speed());
        ballPosition = predictions.ballFutureState(futureTime);
        if(ballPosition == null)
            ballPosition = information.ball;
        diff = ballPosition.location().plus(information.ball.location()).scaled(0.5);
        goalPoint = predictions.choosePointOnGoal(information.enemyGoal,diff);
        float angleToGoal = information.car.transformToLocal(goalPoint).angle2D();
        Vector3 carVector = information.car.orientation().noseVector;
        hitVector = information.ownGoal.location().minus(information.ball.location()).scaledToMagnitude(distanceToBallFlat/1.5);

        float offset = Math.abs(information.car.location().y)>4000?1000:0;
        c = new CubicBezier(information.car.location(),information.ownGoal.direction().scaledToMagnitude(offset),ballPosition.location(),hitVector);

        target = c.point(0.4);
        Value angle = ()->(float)Math.pow(information.car.transformToLocal(target).angle2D()*8,3);
        Value throttle = ()-> distanceToBall < 1500? 1 : 0.1f;
        return action(10).add(part(0,100).withThrottle(throttle).withSteer(angle));
    }

    @Override
    public void draw(Bot bot) {
        if(c!=null)
            c.draw(bot);
    }

    @Override
    public boolean isAvailable() {
        return information.car.location().distance(information.ownGoal.location()) < 1000&&information.ball.location().distance(information.ownGoal.location())<2000;
    }

    @Override
    public double getRating() {
        return 6;
    }
}
