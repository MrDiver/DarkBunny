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
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class DriveForShotCenter extends State {
    public DriveForShotCenter(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Drive for shot center";
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
        hitVector = diff.minus(goalPoint).scaledToMagnitude(distanceToBall/1.5);

        float offset = 1;
        /*if(Math.abs(information.ball.location().x)>2500)
            offset = 1.1f;*/
        hitVector = new Vector3(hitVector.x/offset,hitVector.y,hitVector.z);

        hitVector.scaledToMagnitude(5000);

        c = new CubicBezier(information.car.location(),carVector,ballPosition.location(),hitVector);

        Value angle = ()->(float)Math.pow(information.car.transformToLocal(target).angle2D()*8,3);
        target = c.point(0.4);


        if(distanceToBallFlat > 1500 && c.turnradius(0.6)>1000 && information.car.speed()<1700 && angle.val() < 0.2 && information.car.speed()>1000)
        {
            return actionLibrary.dodge(100,angle.val(),false);
        }
        //0.4
        Value throttle = ()->Mathics.cap(1- Mathics.cap(Math.abs(angle.val()),0,0.4f)
                - Mathics.cap((information.ball.location().z/2000),0,0.3f)
                -(c.turnradius(0.5)<Mathics.turnRadius(information.car.speed())?0.7f:0f)
                ,0,1);
        //Value throttle = ()-> Mathics.turnRadius(information.car.speed())>c.curvature(0.4)?1:-0.1f;
        Bool boost = () -> Math.abs(angle.val()) < 0.6 && throttle.val() > 0.7f;

        /*if(distanceToBallFlat < 300&&Math.abs(angleToGoal)<0.4f || last != null && last.isActive())
        {
             last = actionLibrary.dodge(500);
        }
        else*/
        {
            last = action(10).add(part(0,100).withThrottle(throttle).withSteer(angle).withBoost(boost));
        }
        if(last == null)
            return action(0);
        return last;
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        if(c!=null)
            c.draw(bot);
        if(target != null)
        {
            target.draw(Color.red,r);
        }
        if(ballPosition!=null)
            ballPosition.draw(Color.cyan,bot);


        information.ball.location().plus(hitVector).draw(Color.cyan,r);
        r.drawString3d("D:"+(int)information.ball.location().distance(information.car.location()),Color.white,information.ball.location().plus(new Vector3(0,0,200)),1,1);
        if(goalPoint!=null)
            goalPoint.draw(Color.green,r);

        if(diff != null)
            diff.draw(Color.yellow,r);

        float angleToGoal = information.car.transformToLocal(goalPoint).angle2D();
        r.drawString3d("A:"+angleToGoal,Color.white,information.car.location().plus(new Vector3(0,0,50)),1,1);
        float tmpangle = information.car.transformToLocal(target).angle2D()*8;
        r.drawString3d("Try:"+c.turnradius(0.5),Color.cyan,information.car.location().plus(new Vector3(0,0,150)),1,1);
        r.drawString3d("Can:"+Mathics.turnRadius(information.car.speed()),Color.cyan,information.car.location().plus(new Vector3(0,0,120)),1,1);
    }

    @Override
    public boolean isAvailable() {
        //if the ball is on our back side don't take a shot
        if(information.ballQuad().ordinal()<3)
            return false;
        return Math.abs(information.ball.location().x)<=2000;
    }

    @Override
    public double getRating() {
        return predictions.rightSide()&&predictions.facing(information.ball.location())?5:1;
    }
}
