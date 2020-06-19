import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
    public double xPos, yPos, xDir, yDir,xPlane, yPlane;
    public boolean left,right,forward, back;
    public final double MOVE_SPEED = 0.08; // anything more than .2 is jarring
    public final double ROTATION_SPEED = 0.045; // rotates by this angle (radians) each update

    public Camera(double x, double y, double xd, double yd, double xp, double yp) {
        xPos = x;
        yPos = y;
        xDir = xd; // sin
        yDir = yd; // cos
        xPlane = xp;
        yPlane = yp;
    }

    public void keyPressed(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyChar() == 'A' || key.getKeyChar() == 'a')
            left=true;
        if (key.getKeyCode() == KeyEvent.VK_RIGHT || key.getKeyChar() == 'D' || key.getKeyChar() == 'd')
            right=true;
        if (key.getKeyCode() == KeyEvent.VK_UP || key.getKeyChar() == 'W' || key.getKeyChar() == 'w')
            forward=true;
        if (key.getKeyCode() == KeyEvent.VK_DOWN || key.getKeyChar() == 'S' || key.getKeyChar() == 's')
            back=true;
    }
    public void keyReleased(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyChar() == 'A' || key.getKeyChar() == 'a')
            left = false;
        if (key.getKeyCode() == KeyEvent.VK_RIGHT || key.getKeyChar() == 'D' || key.getKeyChar() == 'd')
            right = false;
        if (key.getKeyCode() == KeyEvent.VK_UP || key.getKeyChar() == 'W' || key.getKeyChar() == 'w')
            forward = false;
        if (key.getKeyCode() == KeyEvent.VK_DOWN || key.getKeyChar() == 'S' || key.getKeyChar() == 's')
            back = false;
    }
    public void keyTyped(KeyEvent key) {}

    public void update(Map map) {
        if (forward) {
            if (map.at((int)(xPos + xDir * MOVE_SPEED), (int)yPos) == 0)
                xPos += xDir * MOVE_SPEED;
            if (map.at((int)(xPos), (int)(yPos + yDir * MOVE_SPEED)) == 0)
                yPos += yDir * MOVE_SPEED;
        }
        if (back) {
            if (map.at((int)(xPos - xDir * MOVE_SPEED),(int)yPos) == 0)
                xPos -= xDir * MOVE_SPEED;
            if (map.at((int)(xPos), (int)(yPos - yDir * MOVE_SPEED)) == 0)
                yPos -= yDir * MOVE_SPEED;
        }
        if(right) {
//            System.out.println(xPlane);
            double oldxDir=xDir;
            xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
            yDir=oldxDir * Math.sin(-ROTATION_SPEED) + yDir * Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
        }
        if(left) {
//            System.out.println(xPlane);
            double oldxDir=xDir;
            xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
            yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
        }
    }
}
