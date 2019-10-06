package DarkBunny.Brain.Data;

import DarkBunny.vector.Vector3;
import rlbot.flat.GoalInfo;


public class Goal {

    GoalInfo gi;
    public Goal(GoalInfo gi)
    {
        this.gi = gi;

    }

    public Vector3 location()
    {
        return new Vector3(gi.location());
    }

    public Vector3 direction()
    {
        return new Vector3(gi.direction());
    }

    public float width()
    {
        return gi.width();
    }

    public float height()
    {
        return gi.height();
    }
}
