package DarkBunny.Brain.predictions;

import DarkBunny.Brain.Data.*;
import DarkBunny.vector.Vector3;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.flat.PredictionSlice;

public class Predictions {
    Information info;
    public Predictions(Information info) {
        this.info = info;
    }

    public Ball ballFutureState(float gs)
    {
        gs = gs + info.secondsElapsed();
        PredictionSlice slice;
        try {
            BallPrediction bp = RLBotDll.getBallPrediction();
            for (int i = 0; i <bp.slicesLength() ; i++) {
               slice = bp.slices(i);
               if(slice.gameSeconds() > gs)
                   return new Ball(slice.physics());
            }
        } catch (RLBotInterfaceException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vector3 choosePointOnGoal(Goal goal, Vector3 ball)
    {
        float x = ball.x;
        float gx = goal.location().x;
        float result = Mathics.cap(x,gx-goal.width()/4,gx+goal.width()/4);
        Vector3 vector3 = new Vector3(result, goal.location().y, goal.location().z);
        return vector3;
    }

    public boolean rightSide()
    {
        if(info.car.team() == Team.Blue)
        {
            return info.ball.location().y > info.car.location().y;
        }else
        {
            return info.ball.location().y < info.car.location().y;
        }
    }

    public boolean facing(Vector3 location)
    {
        return Math.abs(info.car.transformToLocal(location).angle2D())< 0.3;
    }
}
