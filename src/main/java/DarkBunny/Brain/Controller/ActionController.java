package DarkBunny.Brain.Controller;

import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.States.State;
import DarkBunny.output.ControlsOutput;
import rlbot.Bot;
import rlbot.gamestate.GameState;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;

/**
 * ActionController switches between Actions given by a state and applies them to the given ControlsOutput
 */
public class ActionController {
    AbstractAction current;
    ControlsOutput o;
    Information information;

    GameState gameState;
    /**
     * Creates an ActionController with a empty Action
     */
    public ActionController(Information information)
    {
        current = new Action(0,information);
        this.information = information;
    }

    /**
     * Chooses a new Action from the given State if the current Action is not active
     * @param output actions get applied to this
     * @param state some state
     * @return returns the new ControlsOutput with new settings if something has changed
     */
    int a = 20;
    float testtime;
    private State last;
    private boolean wheelContact;



    public ControlsOutput execute(ControlsOutput output,State state)
    {
        if(current.isActive()==false || state != last)
        {
            current = state.getAction();
            current.start();
            wheelContact = information.car.hasWheelContact();
        }
        output = current.execute(output);
        o = output;
        //saving boost measures
        if(information.car.speed() > 2200)
            output.withBoost(false);
        last = state;
        return output;
    }

    /**
     * Draws Information about the current state of the Controller draws all Information available to the Controller
     */
    public void draw(Bot bot)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        int offsetx = 1500;
        int offsety = 10;
        r.drawRectangle2d(Color.white,new Point(offsetx-5,offsety-5),500,200,true);
        r.drawString2d("Steer: "+o.getSteer(), Color.red,new Point(offsetx,offsety),1,1);
        r.drawString2d("Pitch: "+o.getPitch(), Color.red,new Point(offsetx,offsety+20),1,1);
        r.drawString2d("Yaw: "+o.getYaw(), Color.red,new Point(offsetx,offsety+40),1,1);
        r.drawString2d("Roll: "+o.getRoll(), Color.red,new Point(offsetx,offsety+60),1,1);
        r.drawString2d("Throttle: "+o.getThrottle(), Color.red,new Point(offsetx,offsety+80),1,1);
        r.drawString2d("Jump: "+o.holdJump(),  o.holdJump()? Color.green: Color.red,new Point(offsetx,offsety+100),1,1);
        r.drawString2d("Boost: "+o.holdBoost(),  o.holdBoost()? Color.green: Color.red,new Point(offsetx,offsety+120),1,1);
        r.drawString2d("Handbrake: "+o.holdHandbrake(),  o.holdHandbrake()? Color.green: Color.red,new Point(offsetx,offsety+140),1,1);
        r.drawString2d("WheelContact: "+wheelContact,  wheelContact? Color.green: Color.red,new Point(offsetx,offsety+160),1,1);
        r.drawString2d("Speed: "+information.car.velocity().magnitude(), Color.red,new Point(offsetx,offsety+180),1,1);
        r.drawRectangle2d(Color.green,new Point(offsetx,offsety+250),300,10,false);
        r.drawRectangle2d(Color.red,new Point(offsetx,offsety+254),(int)(current.getElapsed()/current.getExecutionLength()*300),6,false);
    }
}
