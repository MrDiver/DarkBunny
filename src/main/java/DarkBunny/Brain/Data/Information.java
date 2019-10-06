package DarkBunny.Brain.Data;

import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.GameInfo;
import rlbot.flat.GameTickPacket;
import rlbot.flat.GoalInfo;
import rlbot.flat.PlayerInfo;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Vector;
import static DarkBunny.Brain.Data.FieldLocation.*;
/**
This class is holding information about all objects currently in the game and is the base for all calculations that happen.
this class should be updated everytime with a new GameTickPacket
 */
public class Information {
    private int playerIndex;
    public GameTickPacket gtp;
    public Ball ball;
    public Car car;
    public List<Car> carList;
    public Goal ownGoal;
    public Goal enemyGoal;
    /**
     * Creating a new Information packet. This will initialize all objects in the Information packet. Later updates on this packet like new players wont be updated
     */
    public Information(int playerIndex) {
        this.playerIndex = playerIndex;
        carList = new Vector<>();
    }

    public void loadGameTickPacket(GameTickPacket gtp) throws RLBotInterfaceException {
        this.playerIndex = playerIndex;
        this.gtp = gtp;
        this.ball = new Ball(gtp.ball());
        if(gtp.playersLength()>0)
            this.car = new Car(gtp.players(playerIndex),playerIndex);
        else
            this.car = new Car();
        if(carList.size() != gtp.playersLength())
        {
            carList.clear();
            for (int i = 0; i < gtp.playersLength(); i++) {
                carList.add(new Car(gtp.players(i),i));
            }
        }else
        {
            int size = carList.size();
            for (int i = 0; i < size; i++) {
                carList.get(i).update(gtp.players(i));
            }
        }

        if(ownGoal == null || enemyGoal == null)
        for (int i = 0; i < RLBotDll.getFieldInfo().goalsLength(); i++) {
            GoalInfo gi = RLBotDll.getFieldInfo().goals(i);
            if(car.team().ordinal() == gi.teamNum())
            {
                ownGoal = new Goal(gi);
            }
            if(car.team().ordinal() != gi.teamNum())
            {
                enemyGoal = new Goal(gi);
            }
        }

    }

    public FieldLocation ballQuad()
    {
        //Blue side left
        if (ball.location().x > 1366)
        {
            if(ball.location().y < -1977)
            {
                if(car.team() == Team.Blue)
                    return BackLeft;
                else
                    return FarRight;
            }else if(ball.location().y> 1977)
            {
                if(car.team() == Team.Blue)
                    return FarLeft;
                else
                    return BackRight;
            }else{
                if(car.team() == Team.Blue)
                    return MidLeft;
                else
                    return MidRight;
            }
        }else
        if (ball.location().x < -1366)
        {
            if(ball.location().y < -1977)
            {
                if(car.team() == Team.Blue)
                    return BackRight;
                else
                    return FarLeft;
            }else if(ball.location().y> 1977)
            {
                if(car.team() == Team.Blue)
                    return FarRight;
                else
                    return BackLeft;
            }else{
                if(car.team() == Team.Blue)
                    return MidRight;
                else
                    return MidLeft;
            }
        }else
        {
            if(ball.location().y < -1977)
            {
                if(car.team() == Team.Blue)
                    return BackCenter;
                else
                    return FarCenter;
            }else if(ball.location().y> 1977)
            {
                if(car.team() == Team.Blue)
                    return FarCenter;
                else
                    return BackCenter;
            }else{
                if(car.team() == Team.Blue)
                    return MidCenter;
                else
                    return MidCenter;
            }
        }
    }

    public void draw(Bot bot)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        ball.draw(Color.red,bot);
        /*for (Car car: carList) {
            car.draw(bot);
        }*/
        ;
        r.drawString3d(ballQuad().name(),Color.white,ball.location().plus(new Vector3(0,0,120)),1,1);
    }

    public float secondsElapsed() {
        return gtp.gameInfo().secondsElapsed();
    }

    public boolean isKickoffPause() {
        return gtp.gameInfo().isKickoffPause();
    }
}
