package DarkBunny.Brain.Controller;


import DarkBunny.Brain.Data.Information;
import DarkBunny.output.ControlsOutput;

import java.util.ArrayDeque;

public class ActionChain extends AbstractAction{

    ArrayDeque<AbstractAction> chain;
    AbstractAction current;

    public ActionChain(float executionLength, Information information) {
        super(executionLength, information);
        chain = new ArrayDeque<>();
    }

    public ControlsOutput execute(ControlsOutput output) {

            if(current == null || current.isActive() == false)
            {
                if(chain.size()>0) {
                    current = chain.poll();
                    current.start();
                }else
                {
                    active = false;
                }
            }
        if(current != null)
            current.execute(output);

        return output;
    }

    public ActionChain addAction(AbstractAction a)
    {
        chain.add(a);
        return this;
    }

    @Override
    public boolean isActive()
    {
        if(getElapsed() > executionLength)
            active = false;

        /*if(chain.size()==0)
        {
            active = false;
        }*/
        return active;
    }
}
