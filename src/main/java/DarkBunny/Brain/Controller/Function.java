package DarkBunny.Brain.Controller;

import DarkBunny.output.ControlsOutput;

/**
 * Holda a Controlsoutput function that can be applied in the ActionController chain
 */
public interface Function {
    public ControlsOutput apply(ControlsOutput output);
}
