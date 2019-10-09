package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.Data.Mathics;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;


public class Test extends State {
    public Test(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Test";
    }

    Vector3 destination;
    @Override
    public AbstractAction getAction() {
        if(information.car.hasWheelContact())
        {
            return action(20).add(part(0,10).withJump());
        }

        destination = new Vector3(0,0,1000);
        Vector3 input = Mathics.aerialInput(information.car.location(),destination,information.car,100f);
        return action(10).add(part(0,100).withRoll(input.x).withPitch(input.y).withYaw(input.z).withBoost());
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        if(destination!=null)
            destination.draw(Color.yellow,r);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        return 10;
    }
}
