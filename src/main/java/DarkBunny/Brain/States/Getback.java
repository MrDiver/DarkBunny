package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Controller.Bool;
import DarkBunny.Brain.Controller.Value;
import DarkBunny.Brain.Data.CubicBezier;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class Getback extends State{

    public Getback(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "GetBack";
    }
    CubicBezier c;
    Vector3 target;
    @Override
    public AbstractAction getAction() {
        c = new CubicBezier(information.car.location(),information.car.orientation().noseVector.scaledToMagnitude(100),
                information.ownGoal.location(),new Vector3(-information.ball.location().scaledToMagnitude(400).x,information.ownGoal.direction().scaledToMagnitude(200).y,0));
        target = c.point(0.3);
        Value angle = ()-> information.car.transformToLocal(target).angle2D()*8;
        Bool boost = ()-> information.car.boost() > 30;
        float distanceToGoal = (float) information.car.location().distance(information.ownGoal.location());
        if(distanceToGoal > 2500 && information.car.speed() < 2200 && information.car.speed()>600&& Math.abs(angle.val()) < 0.2)
        {
            if(information.car.hasWheelContact() && information.car.boost() > 30)
                return actionLibrary.diagonalFlick(angle.val(),50,true);
            else
                return actionLibrary.dodge(100,angle.val(),true);
        }
        return action(10).add(part(0,100).withSteer(angle).withThrottle(1).withBoost(boost));
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        if(c!=null)
            c.draw(bot);
        Value angle = ()-> information.car.transformToLocal(target).angle2D()*8;
        r.drawString3d("Angle: "+angle.val(), Color.cyan,information.car.location().plus(new Vector3(0,0,150)),1,1);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating()
    {
        if(information.ball.location().z>300&&information.ballQuad().ordinal()>5)
            return 8;
        return !predictions.rightSide()?7:3;
    }
}
