package DarkBunny.renderer;

import DarkBunny.Brain.Data.Car;
import DarkBunny.vector.Vector3;
import rlbot.flat.BoxShape;
import rlbot.render.NamedRenderer;
import rlbot.render.Renderer;

import java.awt.*;

public class MyRenderer{
    public Renderer renderer;
    public MyRenderer(Renderer r) {
        this.renderer = r;
    }

    public void drawSphere(Color c, float r, Vector3 location)
    {
        r = r+10;
        for(double i = 0; i < 9; i+= 1){
            double off = 2*Math.PI/8;
            double x = Math.cos(i*off)*r;
            double y = Math.sin(i*off)*r;
            double x2 = Math.cos((i+1)*off)*r;
            double y2 = Math.sin((i+1)*off)*r;
            renderer.drawLine3d(c,location.plus(new Vector3(x,y,0)),location.plus(new Vector3(x2,y2,0)));
            renderer.drawLine3d(c,location.plus(new Vector3(x,0,y)),location.plus(new Vector3(x2,0,y2)));
            renderer.drawLine3d(c,location.plus(new Vector3(0,x,y)),location.plus(new Vector3(0,x2,y2)));
        }
    }

    public void drawHitbox(BoxShape hitbox, Car car) {
        Vector3 backRight = car.location().plus(new Vector3(-hitbox.width()/2,-hitbox.length()/2,0));
        Vector3 backLeft = car.location().plus(new Vector3(hitbox.width()/2,-hitbox.length()/2,0));
        Vector3 frontRight = car.location().plus(new Vector3(-hitbox.width()/2,hitbox.length()/2,0));
        Vector3 frontLeft = car.location().plus(new Vector3(hitbox.width()/2,hitbox.length()/2,0));

        Vector3 backRightTop = car.location().plus(new Vector3(-hitbox.width()/2,-hitbox.length()/2,hitbox.height()));
        Vector3 backLeftTop = car.location().plus(new Vector3(hitbox.width()/2,-hitbox.length()/2,hitbox.height()));
        Vector3 frontRightTop = car.location().plus(new Vector3(-hitbox.width()/2,hitbox.length()/2,hitbox.height()));
        Vector3 frontLeftTop = car.location().plus(new Vector3(hitbox.width()/2,hitbox.length()/2,hitbox.height()));
        backRight.draw(Color.red,renderer);
        backLeft.draw(Color.green,renderer);
        frontLeft.draw(Color.blue,renderer);
        frontRight.draw(Color.yellow,renderer);
        renderer.drawLine3d(Color.white,backRight,backLeft);
        renderer.drawLine3d(Color.white,backLeft,frontLeft);
        renderer.drawLine3d(Color.white,frontRight,frontLeft);
        renderer.drawLine3d(Color.white,frontRight,backRight);

        renderer.drawLine3d(Color.white,backRightTop,backLeftTop);
        renderer.drawLine3d(Color.white,backLeftTop,frontLeftTop);
        renderer.drawLine3d(Color.white,frontRightTop,frontLeftTop);
        renderer.drawLine3d(Color.white,frontRightTop,backRightTop);


    }
}
