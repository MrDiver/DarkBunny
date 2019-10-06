package DarkBunny.Brain.predictions;

import DarkBunny.renderer.MyRenderer;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;

import java.awt.*;

public class SBall extends SObject{
    float radius;
    Vector3 location;
    Vector3 velocity;
    Vector3 angularVelocity;
    float time;
    public static final float mass = 30.0f;

    public static final float drag = -0.0305f; //viscous damping
    public static final float mu = 2.0f; //coefficient of friction
    public static final float restitution = 0.6f;

    public static final float v_max = 4000.f; //maximum velocity
    public static final float w_max = 6.0f; //maximum angular velocity

    public final float soccar_radius = 91.25f;
    public final float soccar_collision_radius = 93.15f;

    public final float collision_radius = soccar_collision_radius;
    public final float I = 0.4f * mass * radius * radius; // moment of inertia

    public SBall(Vector3 location, Vector3 velocity, Vector3 angularVelocity) {
        this.radius = soccar_radius;
        this.location = location;
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
    }

    public float radius() {
        return radius;
    }

    public Vector3 location() {
        return location;
    }

    public Vector3 velocity() {
        return velocity;
    }

    public Vector3 angularVelocity() {
        return angularVelocity;
    }

    public void simulate(float time) {

    }

    public void draw(Bot bot) {
        MyRenderer r = new MyRenderer(BotLoopRenderer.forBotLoop(bot));
        r.drawSphere(Color.red,radius,location);
        //r.drawCenteredRectangle3d(Color.green,location,10,10,true);
    }
}
