package DarkBunny;

import DarkBunny.Brain.Controller.ActionController;
import DarkBunny.Brain.Controller.ActionLibrary;
import DarkBunny.Brain.Data.Information;
import DarkBunny.Brain.Data.Mathics;
import DarkBunny.Brain.States.*;
import DarkBunny.Brain.predictions.Predictions;
import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.GameTickPacket;
import DarkBunny.boost.BoostManager;
import DarkBunny.input.DataPacket;
import DarkBunny.output.ControlsOutput;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DarkBunny implements Bot {

    private final int playerIndex;
    private Information info = null;
    private Predictions predictions = null;
    private ActionController actionController = null;
    private ActionLibrary actionLibrary;
    private State state;
    private List<State> statesList;
    public DarkBunny(int playerIndex) {
        this.playerIndex = playerIndex;
        info = new Information(playerIndex);
        predictions = new Predictions(info);
        actionLibrary = new ActionLibrary(info,predictions);
        actionController = new ActionController(info);
        statesList = new ArrayList<>();
        state = new DriveForShotCenter(info,actionLibrary,predictions);
        //statesList.add(new TakeShot(info,actionLibrary,predictions));
        statesList.add(new DriveForShotCenter(info,actionLibrary,predictions));
        statesList.add(new DriveForAngledShotSide(info,actionLibrary,predictions));
        statesList.add(new DriveForShotSide(info,actionLibrary,predictions));
        //statesList.add(new Getback(info,actionLibrary,predictions));
        //statesList.add(new DefenseTransition(info,actionLibrary,predictions));
        //statesList.add(new Defense(info,actionLibrary,predictions));
        statesList.add(new Clear(info,actionLibrary,predictions));
        //statesList.add(new Kickoff(info,actionLibrary,predictions));


        /*for(int i = 0; i < 3000; i+=100)
        {
            System.out.println("i:"+i+"\tCurve: "+Mathics.curvature(i)+"\t Estimated: "+Mathics.maxSpeedForCurvature(Mathics.curvature(i)));
        }*/
    }


    public void draw() {
        info.draw(this);
        state.draw(this);
        actionController.draw(this);
        Renderer r = BotLoopRenderer.forBotLoop(this);
        int offx = 50;
        int offy = 300;

        r.drawRectangle2d(Color.green,new Point(offx,offy),10,400,true);
        r.drawString2d(state.name+ ": " +state.getRating(),Color.CYAN,new Point(offx+20,offy-20),1,1);
        r.drawRectangle3d(Color.white,new Vector3(),10,10,true);
        for(State s : statesList)
        {
            r.drawString2d(s.name + ": " +s.getRating(),s.isAvailable()?Color.white:Color.red,new Point(offx+20,(int)(offy+(400-s.getRating()*40))),1,1);
        }
    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the ball.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput(DataPacket input) {
        if(!state.locked())
        {
            State max = state;
            for(State s : statesList)
            {
                if(state.name.equals("Drive for shot side"))
                {
                    if(s.name.equals("Drive for angled shot"))
                        continue;
                }
                if(state.name.equals("Drive for angled shot"))
                    if(s.name.equals("Drive for shot side"))
                        continue;
                if(!max.isAvailable()||s.isAvailable()&&s.getRating()>max.getRating())
                    max=s;
            }
            state = max;
        }
        ControlsOutput co = new ControlsOutput();
        return actionController.execute(co,state);
    }

    @Override
    public int getIndex() {
        return this.playerIndex;
    }

    /**
     * This is the most important function. It will automatically get called by the framework with fresh data
     * every frame. Respond with appropriate controls!
     */
    @Override
    public ControllerState processInput(GameTickPacket packet) {

        if (packet.playersLength() <= playerIndex || packet.ball() == null || !packet.gameInfo().isRoundActive()) {
            return new ControlsOutput();
        }
        try {
            info.loadGameTickPacket(packet);
        } catch (RLBotInterfaceException e) {
            e.printStackTrace();
        }
        BoostManager.loadGameTickPacket(packet);
        DataPacket dataPacket = new DataPacket(packet, playerIndex);
        ControlsOutput controlsOutput = processInput(dataPacket);

        draw();
        return controlsOutput;
    }

    public void retire() {
        System.out.println("Retiring DarkBunny" + playerIndex);
    }
}
