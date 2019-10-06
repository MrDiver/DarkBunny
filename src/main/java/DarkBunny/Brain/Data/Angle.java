package DarkBunny.Brain.Data;

import DarkBunny.vector.Vector3;
import rlbot.Bot;

import java.awt.*;

public class Angle {
    Vector3 location;
    Vector3 direction;
    float angle;
    public Angle(Vector3 location,Vector3 direction, float angle)
    {
        this.location = location;
        this.direction = direction;
        this.angle = angle;
    }

    public void draw(Color c, Bot bot)
    {

    }
}
