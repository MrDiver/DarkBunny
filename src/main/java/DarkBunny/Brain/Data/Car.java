package DarkBunny.Brain.Data;

import DarkBunny.input.CarOrientation;
import DarkBunny.renderer.MyRenderer;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.PlayerInfo;
import rlbot.flat.RLBotPlayer;
import rlbot.flat.Rotator;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.NamedRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class Car {
    private PlayerInfo pi;
    private int playerIndex;
    public Car(PlayerInfo pi,int playerIndex)
    {
        update(pi);
        this.playerIndex = playerIndex;
    }
    public Car()
    {
        update(new PlayerInfo());
    }

    public void update(PlayerInfo pi)
    {
        this.pi = pi;
    }

    public int boost()
    {
        return pi.boost();
    }

    public boolean doubleJumped()
    {
        return pi.doubleJumped();
    }

    public boolean hasWheelContact()
    {
        return pi.hasWheelContact();
    }

    public boolean isDemolished()
    {
        return pi.isDemolished();
    }

    public boolean isSuperSonic()
    {
        return pi.isSupersonic();
    }

    public boolean jumped()
    {
        return pi.jumped();
    }

    public Vector3 location()
    {
        return new Vector3(pi.physics().location())/*.plus(velocity().scaled(0.04f))*/;
    }

    public Vector3 velocity()
    {
        return new Vector3(pi.physics().velocity());
    }

    public Vector3 angularVelocity()
    {
        return new Vector3(pi.physics().angularVelocity());
    }

    public Rotator rotation()
    {
        return pi.physics().rotation();
    }

    public Team team()
    {
        return Team.values()[pi.team()];
    }

    public double speed()
    {
        return this.velocity().magnitude();
    }

    public CarOrientation orientation()
    {
        return CarOrientation.fromFlatbuffer(pi);
    }

    public <T>Vector3 transformToLocal(Vector3 target)
    {
        double x = target.minus(this.location()).dotProduct(this.getMatrix()[0]);
        double y = target.minus(this.location()).dotProduct(this.getMatrix()[1]);
        double z = target.minus(this.location()).dotProduct(this.getMatrix()[2]);
        return new Vector3(x,y,z);
    }

    public Vector3[] getMatrix()
    {
        double CR,SR,CP,SP,CY,SY;
        //Cos Sin Roll
        CR = Math.cos(rotation().roll());
        SR = Math.sin(rotation().roll());
        //Cos Sin Pitch
        CP = Math.cos(rotation().pitch());
        SP = Math.sin(rotation().pitch());
        //Cos Sin Yaw
        CY = Math.cos(rotation().yaw());
        SY = Math.sin(rotation().yaw());
        Vector3 matrix[] = new Vector3[3];
        matrix[0] = new Vector3(CP*CY, CP*SY, SP);
        matrix[1] = new Vector3(CY*SP*SR-CR*SY, SY*SP*SR+CR*CY, -CP * SR);
        matrix[2] = new Vector3(-CR*CY*SP-SR*SY, -CR*SY*SP+SR*CY, CP*CR);
        return matrix;
    }

    public boolean inArea(float radius,Vector3 location)
    {
        return this.location().make2D().distance(location.make2D())<radius;
    }

    public void draw(Bot bot)
    {
        MyRenderer r = new MyRenderer(BotLoopRenderer.forBotLoop(bot));
        //r.drawHitbox(pi.hitbox(),this);
        r.renderer.drawCenteredRectangle3d(Color.green,location(),10,10,true);
    }
}
