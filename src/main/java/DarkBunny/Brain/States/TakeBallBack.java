package DarkBunny.Brain.States;

import DarkBunny.Brain.Controller.AbstractAction;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.predictions.Predictions;
import rlbot.Bot;

public class TakeBallBack extends State {
    public TakeBallBack(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
    }

    @Override
    public AbstractAction getAction() {
        return action(0);
    }

    @Override
    public void draw(Bot bot) {

    }

    @Override
    public boolean isAvailable() {
        return information.car.location().distance(information.ball.location())<2000;
    }

    @Override
    public double getRating() {
        return (100.0-information.car.boost())/15.0;
    }
}
