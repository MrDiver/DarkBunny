package DarkBunny.Brain.Controller;

import DarkBunny.Brain.Data.Car;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.flat.Rotator;
public class ActionLibrary {
    protected Information information;
    protected Predictions predictions;

    public ActionLibrary(Information information, Predictions predictions)
    {
        this.information = information;
        this.predictions = predictions;
    }

    /**
     * Standard action for dodge
     * @param time length of the dodge (not delay between jumps)
     * @return
     */
    public Action dodge(float time)
    {
        Action a = dodge(time,0,true);
        return a;
    }

    public Action dodge(float time,double angle,boolean tillGround)
    {
        Action a = dodge(time,100,50,50,angle,true);
        if(tillGround)
            a.addCondition(()->information.car.hasWheelContact());
        return a;
    }

    public Action dodge(float time,float first,float wait,float second,double angle,boolean withThrottle)
    {
        Action a = new Action(time,information).add(new ActionPart(0,first).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(first,first+wait).withJump(false).withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(first+wait,first+wait+second).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(0,time).withThrottle(withThrottle?1:0));
        return a;
    }

    /**
     * Standard action for delayed dodge.
     * time between 800 and 1300 works best
     * @param time length of the dodge (not delay between jumps)
     * @param delay delay between first and second jump
     * @return
     */
    public Action delayeddodge(float time,long delay,Information information)
    {
        Action a = new Action(time,information).add(new ActionPart(0,delay-100).withJump().withPitch(0))
                .add(new ActionPart(delay-100,delay).withJump(false).withPitch(-1))
                .add(new ActionPart(delay,delay+100).withJump().withPitch(-1));
        return a;

    }

    /**
     * Standard wavedash action
     * @param time
     * @return
     */
    public Action wavedash(long time,float roll,Information information)
    {
        /*Action a = new Action(time)
                .add(new ActionPart(0,time).withThrottle(1))
                .add(new ActionPart(50,60).withPitch(1).withJump())
                .add(new ActionPart(60,280).withJump(false).withPitch(1))
                .add(new ActionPart(850,950).withJump().withPitch(-1))
                .add(new ActionPart(950,1000).withJump(false).withPitch(0)); /*
                .add(new ActionPart(20,500).withJump(false).withPitch(1))
                .add(new ActionPart(500,800).withJump().withPitch(-1));*/
        Action a = new Action(time,information)
                .add(new ActionPart(0,2000).withSlide().withThrottle(1))
                .add(new ActionPart(0,2).withJump())
                .add(new ActionPart(10,200).withPitch(1).withYaw(0).withRoll(roll))
                .add(new ActionPart(850,900).withPitch(-1).withYaw(0).withRoll(-roll))
                .add(new ActionPart(850,890).withJump());
        return a;
    }

    /**
     * standard drive action
     * @param steer
     * @param throttle
     * @param boost
     * @return
     */
    public Action drive(float steer,float throttle,boolean boost)
    {
        Action a = new Action(1,information).add(new ActionPart(0,100).withSteer(steer).withThrottle(throttle).withBoost(boost));
        return a;
    }

    public ActionChain diagonalFlick(float angle,float delay,boolean boost)
    {
        ActionChain a = chain(5000);
        a = a.addAction(action(16).add(part(0,32).withJump().withThrottle(1).withBoost()))
                .addAction(action(delay).add(part(0,delay).withThrottle(1).withBoost()))
                .addAction(action(16).add(part(0,32).withPitch(-1).withYaw((float) Math.cos(angle)).withJump().withThrottle(1).withBoost()))
                .addAction(action(1000).add(part(0,4000).withPitch(1).withThrottle(1).withBoost()));
        return a;
    }

    protected ActionChain chain(float time)
    {
        return new ActionChain(time,information);
    }

    protected Action action(float time)
    {
        return new Action(time,information);
    }

    protected ActionPart part(float start,float end)
    {
        return new ActionPart(start,end);
    }

}
