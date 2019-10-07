package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Bool;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.*;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.renderer.MyRenderer;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class DriveForAngledShotSide extends State {
    public DriveForAngledShotSide(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Drive for angled shot";
    }

    CubicBezier c;
    Vector3 target;
    Ball ballFuture;
    Vector3 hitVector;
    Vector3 goalTarget;
    Vector3 hitLocation;
    @Override
    public AbstractAction getAction() {
        Car car = information.car;
        Ball ball = information.ball;
        float carToBall = (float) car.location().distance(ball.location());
        float carToGoal = (float) car.location().distance(information.enemyGoal.location());

        ballFuture = predictions.ballFutureState((float) (car.speed()/2300*Mathics.cap((carToBall),0,3000)/3000));
        goalTarget = predictions.choosePointOnGoal(information.enemyGoal,ballFuture.location());

        Vector3 vectogoal = goalTarget.minus(ballFuture.location());
        double angleBall = Math.atan2(vectogoal.x,vectogoal.y);

        /*
            CALCULATING THE HIT VECTOR VERY IMPORTANT
         */
        hitVector = ballFuture.location().minus(goalTarget).scaledToMagnitude(carToBall/3);

        hitVector = hitVector.plus(new Vector3(ball.velocity().x/15,0,0));
        /*hitVector = new Vector3(hitVector.x*Mathics.cap((float) Math.abs(angleBall),0.5f,2f),hitVector.y,hitVector.z);*/
        if(Math.abs(angleBall)<0.5)
            hitVector = new Vector3(hitVector.x/(car.speed()/8),hitVector.y,hitVector.z);

        if(Math.abs(angleBall)>1)
        {
            hitVector = new Vector3(hitVector.x*1.5,hitVector.y*4,hitVector.z);
        }

        if(Math.abs(angleBall)>1)
        {
            float teamthing = car.team() == Team.Blue? 70:-70;
            hitLocation = ballFuture.location().plus(new Vector3(-45*angleBall,teamthing,0));
        }else{
            hitLocation = ballFuture.location();
        }

        Vector3 extraAngle;
        if(Math.abs(hitLocation.x)> 3000&& Math.abs(ball.location().x)-100<= Math.abs(ballFuture.location().x)&&Math.abs(car.location().x)+400<Math.abs(hitLocation.x)) {
            extraAngle = new Vector3(ballFuture.location().x, 0, 0);
        }else {
            extraAngle= new Vector3();
        }
        c = new CubicBezier(car.location(),extraAngle,hitLocation,hitVector);

        float choose = 0.4f*Mathics.cap(carToBall,500,1000)/1000;

        if(carToBall<1000)
        {
            target = hitLocation;
        }
        else
        {
            target = c.point(choose);
        }
        target = new Vector3(Mathics.cap(target.x,-4000,4000),target.y,target.z);
        Value angle = ()-> Mathics.cap(car.transformToLocal(target).angle2D()*8,-1,1);
        //Value throttle = ()-> Mathics.cap((float) (c.turnradius(choose)/1000)-1,-1,1);
        Value throttle = ()-> carToBall <400? -0.1f:car.speed()>2200?0.7f:5*(1-Math.abs(angle.val()/4));

        if(carToBall < 400)
        {
            lock = true;
        }else{
            lock = false;
        }

        Bool boost = ()-> throttle.val()>=0.8;
        return action(10).add(part(0,100).withSteer(angle).withThrottle(throttle).withBoost(boost));
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        MyRenderer r2 = new MyRenderer(r);
        if(c!=null)
        {
            c.draw(bot);
        }
        if(target != null)
        {
            target.draw(Color.red,r);
            r.drawString3d("S: "+Mathics.maxSpeedForCurvature(c.curvature(0.4f)),Color.white,c.point(0.4),1,1);
        }
        if(ballFuture !=null)
            ballFuture.draw(Color.cyan,bot);

        if(hitLocation!=null)
        {
            r2.drawSphere(Color.green,100,hitLocation);
        }
        if(goalTarget!=null)
        {
            r2.drawSphere(Color.yellow,100,goalTarget);
            Vector3 vectogoal = goalTarget.minus(ballFuture.location());
            r.drawLine3d(Color.yellow,ballFuture.location(),ballFuture.location().plus(vectogoal));
            r.drawString3d("A:"+Math.atan2(vectogoal.x,vectogoal.y),Color.white,goalTarget.plus(new Vector3(0,0,150)),1,1);
        }
    }

    @Override
    public boolean isAvailable() {
        //if the ball is on our back side don't take a shot
        if(information.ballQuad().ordinal()<3)
            return false;
        if(information.ball.location().z>150)
            return false;
        if(Math.abs(predictions.ballFutureState(1).location().x)+100 < Math.abs(information.ball.location().x))
            return false;
        return Math.abs(information.ball.location().x)>2500;
    }

    @Override
    public double getRating() {
        return predictions.rightSide()&&information.car.boost()>40?7:1;
    }
}
