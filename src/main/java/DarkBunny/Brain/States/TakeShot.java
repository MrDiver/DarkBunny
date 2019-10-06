package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Bool;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.Ball;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.Data.Mathics;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class TakeShot extends State{

    public TakeShot(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Take Shot";
    }

    boolean jumped = false;
    float time = -1;
    Vector3 noseExtended;
    Vector3 target;
    Ball ballPosition;
    Vector3 goalPoint;
    Vector3 diff;
    @Override
    public AbstractAction getAction() {
        float distanceToBall = (float) information.ball.location().make2D().distance(information.car.location().make2D());
        noseExtended = information.car.orientation().noseVector.scaledToMagnitude(distanceToBall);
        /*if (time == -1)
        {
            time = predictions.ballFutureState(distanceToBall/1500f).location().y/2000-0.3f;
        }
        Bool jump = ()-> this.elapsedSeoncds() < time;
        Value pitch = () -> (predictions.ballFutureState(1).location().z > noseExtended.z ? 1:-1)*0.3f;
        if(distanceToBall < 250)
            return action(100).add(part(0,100).withJump().withPitch(-1).withBoost());
        return action(0).add(part(0,100).withJump()).add(part(0,500).withPitch(pitch));*/
            float futureTime = (float) ((distanceToBall) / information.car.speed());
            ballPosition = predictions.ballFutureState(futureTime);
            if (ballPosition == null)
                ballPosition = information.ball;
            diff = ballPosition.location().plus(information.ball.location()).scaled(0.5);
            goalPoint = predictions.choosePointOnGoal(information.enemyGoal, diff);
            float angleToGoal = -information.car.transformToLocal(goalPoint).angle2D();
        if(Math.abs(information.car.location().x) < 2000) {
                return actionLibrary.dodge(1000, 150, 100, 100, Mathics.cap(angleToGoal * 3, -1, 1), true);
        }else {
            if(distanceToBall < 200)
            {
                return actionLibrary.dodge(1000, 150, 100, 100, Mathics.cap(angleToGoal * 3, -1, 1), true);
            }else{
                Value angleToBall = () -> information.car.transformToLocal(information.ball.location()).angle2D() < 0? -1:1;
                return action(10).add(part(0,100).withThrottle(0.5f).withSteer(angleToBall));
            }
        }
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        if(noseExtended != null)
        {
            r.drawLine3d(Color.green,information.car.location(),information.car.location().plus(noseExtended));
        }
    }

    @Override
    public boolean isAvailable() {
        //if the ball is on our back side don't take a shot
        if(information.ballQuad().ordinal()<3)
            return false;
        return true;
    }

    @Override
    public double getRating() {
        return predictions.rightSide()&&information.ball.location().make2D().distance(information.car.location().make2D())<400?6:0 - information.ball.location().y/1000;
    }
}
