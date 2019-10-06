package DarkBunny.Brain.Data;

import DarkBunny.renderer.MyRenderer;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.*;
import rlbot.manager.BotLoopRenderer;

import java.awt.*;
import java.awt.Color;

public class Ball {
    private BallInfo bi;
    private Physics physics;
    public Ball(BallInfo bi)
    {
        update(bi);
    }

    public Ball(Physics physics) {
        bi = null;
        this.physics = physics;
    }

    public void update(BallInfo bi)
    {
        this.bi = bi;
        physics = bi.physics();
    }

    public Touch latestTouch()
    {
        if(bi!=null)
            return bi.latestTouch();
        return new Touch();
    }
    public Vector3 location()
    {
        //return new Vector3(bp.slices(2).physics().location());
        return new Vector3(physics.location());
    }
    public Vector3 angularVelocity()
    {
        //return new Vector3(bp.slices(2).physics().angularVelocity());
        return new Vector3(physics.angularVelocity());
    }
    public Vector3 velocity()
    {
        //return new Vector3(bp.slices(2).physics().velocity());
        return new Vector3(physics.velocity());
    }
    public Rotator rotation(){
        return physics.rotation();
    }

    public void draw(Color c,Bot bot)
    {
        MyRenderer r = new MyRenderer(BotLoopRenderer.forBotLoop(bot));
        r.drawSphere(c,92,location());
        //r.drawCenteredRectangle3d(Color.green,location,10,10,true);
    }
}
