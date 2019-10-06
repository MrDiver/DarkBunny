package DarkBunny.Brain.predictions;

import rlbot.Bot;

public abstract class SObject {
    public abstract void simulate(float time);
    public abstract void draw(Bot bot);
}
