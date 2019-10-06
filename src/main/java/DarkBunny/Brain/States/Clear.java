package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Bool;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.Ball;
import DarkBunny.Brain.Data.CubicBezier;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.Data.Mathics;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;

public class Clear extends State{
    public Clear(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Clear";
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
        hitVector = information.ownGoal.location().minus(information.ball.location()).scaledToMagnitude(distanceToBallFlat/4);

        hitVector.scaledToMagnitude(5000);

        c = new CubicBezier(information.car.location(),new Vector3(),ballPosition.location(),hitVector);

        target = c.point(0.4);
        Value angle = ()->(float)Math.pow(information.car.transformToLocal(target).angle2D()*8,3);

       /* Value throttle = ()-> Mathics.cap(1- Mathics.cap(Math.abs(angle.val()),0,0.4f)
                        - Mathics.cap((information.ball.location().z/2000),0,0.3f)
                        -(c.turnradius(0.5)<Mathics.turnRadius(information.car.speed())?0.7f:0f)
                ,0,1);*/
        Value throttle = ()-> Mathics.turnRadius(information.car.speed())>c.curvature(0.6)?1:-0.1f;
        Bool boost = () -> Math.abs(angle.val()) < 0.6 && throttle.val() > 0.7f;
        return action(10).add(part(0,100).withThrottle(throttle).withSteer(angle).withBoost(boost));
    }

    @Override
    public void draw(Bot bot) {
        if(c!=null)
            c.draw(bot);
    }

    @Override
    public boolean isAvailable() {
        return information.ballQuad().ordinal()<3;
    }

    @Override
    public double getRating() {
        return 6.5;
    }
}
