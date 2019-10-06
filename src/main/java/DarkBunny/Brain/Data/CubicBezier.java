package DarkBunny.Brain.Data;

import DarkBunny.vector.Vector3;
import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class CubicBezier {
    private Vector3 start;
    private Vector3 startDir;
    private Vector3 end;
    private Vector3 endDir;

    public CubicBezier(Vector3 start, Vector3 startDir, Vector3 end, Vector3 endDir) {
        this.start = start;
        this.startDir = startDir;
        this.end = end;
        this.endDir = endDir;
    }

    public Vector3 point(double t)
    {
        Vector3 p0 = start;
        Vector3 p1 = start.plus(startDir);
        Vector3 p2 = end.plus(endDir);
        Vector3 p3 = end;
        return p0.scaled(Math.pow(1-t,3)).plus(p1.scaled(3*Math.pow(1-t,2)*t).plus(p2.scaled(3*(1-t)*t*t).plus(p3.scaled(t*t*t))));
    }


    public double turnradius(double t)
    {
        if(t < 0.01)
            return 0;
        else if(t > 0.99)
            return 0;
        return Math.abs(1/curvature(t));
    }

    public double curvature(double t)
    {
        final double resolution = 0.01;
        Vector3 p0 = point(t-resolution);
        Vector3 p1 = point(t);
        Vector3 p2 = point(t+resolution);

        double dx1 = p1.x - p0.x;
        double dy1 = p1.y - p0.y;
        double dx2 = p2.x - p0.x;
        double dy2 = p2.y - p0.y;
        double area = dx1 * dy2 - dy1 * dx2;
        double len0 = p0.distance(p1);
        double len1 = p1.distance(p2);
        double len2 = p2.distance(p0);
        return 4 * area / (len0 * len1 * len2);
    }

    public void draw(Bot bot)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        if(start != null)
        {
            r.drawCenteredRectangle3d(Color.green,start,10,10,true);
        }
        if(startDir!=null)
        {
            r.drawLine3d(Color.green,start,start.plus(startDir));
        }
        if(end != null)
        {
            r.drawCenteredRectangle3d(Color.red,end,10,10,true);
        }
        if(endDir!=null)
        {
            r.drawLine3d(Color.red,end,end.plus(endDir));
        }
        double res = 10;
        double off = 1/res;
        for (double i = 0; i < 1; i+=off) {
            r.drawLine3d(Color.yellow,point(i),point(i+off));
        }
    }
}
