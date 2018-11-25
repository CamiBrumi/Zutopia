import java.awt.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static java.lang.StrictMath.sqrt;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 4*1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 4*1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	private boolean vxChangedLastTime;
	private boolean vyChangedLastTime;
	private int counter; // TODO: 24/11/2018 eliminate this variable . only for prints
	private double d; // distance between the points
	private double xCol, yCol; //x and y of the last collision

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);

		vxChangedLastTime = false;
		vyChangedLastTime = false;
		counter = 0;
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime, Paddle paddle) {
		final String colW = collisionWallOrPaddle(paddle);

		if (!colW.equals(""))
			changeVelocity(colW);

		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;

		x += dx;
		y += dy;

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));

		//Point pC = collisionPoint();

		//if (pC != null)


	}

	private void changeVelocity(String wall) {
		if (wall.equals("V")) {
			if (!vxChangedLastTime) {
				vx = -vx;
				vxChangedLastTime = true;
			} else {
				vxChangedLastTime = false;
			}
		} else if (wall.equals("H")) {
			if (!vyChangedLastTime) {
				vy = -vy;
				vyChangedLastTime = true;
			} else {
				vyChangedLastTime = false;
			}
		} else { // wall.equals("VH")
			vx = -vx;
			vy = -vy;
		}


		if (wall.equals("V")) { // if there is a collision with the right wall
			vx = -vx;

		} else if ( wall.equals("H")) { // if there is a collision with the upper wall
			vy = -vy;

		} else { // wall.equals("VH")
			vx = -vx;
			vy = -vy;
		}
	}

	private String collisionWallOrPaddle(Paddle paddle) {
	    /*if ((y + BALL_RADIUS <= (paddle.getY() - paddle.PADDLE_HEIGHT/2))
                && (y + BALL_RADIUS >= (paddle.getY() + paddle.PADDLE_HEIGHT/2))
                && (x <= (paddle.getX() + paddle.PADDLE_WIDTH/2))
                && (x >= (paddle.getX() - paddle.PADDLE_WIDTH/2))) { // the bottom of the ball collides with the paddle
	        System.out.println("DP");
	        return "D";
        } else if ((y - BALL_RADIUS <= (paddle.getY() + paddle.PADDLE_HEIGHT/2))
                && (y - BALL_RADIUS >= (paddle.getY() - paddle.PADDLE_HEIGHT/2))
                && (x <= (paddle.getX() + paddle.PADDLE_WIDTH/2))
                && (x >= (paddle.getX() - paddle.PADDLE_WIDTH/2))) { // the top of the ball collides with the paddle
            System.out.println("UP");
            return "U";
        }*/
	    String collision = "";
        if (x + BALL_RADIUS >= GameImpl.WIDTH || x - BALL_RADIUS <= 0) { // a vertical wall
            //System.out.println(counter++ + "V");
			collision += "V";
		}
		if (y + BALL_RADIUS >= GameImpl.HEIGHT || y - BALL_RADIUS <= 0) { // a horizontal wall
            //System.out.println(counter++ + "H");
			collision += "H";
		}
		if (!collision.equals(""))
			d = 0;
			xCol = x;
			yCol = y;
			System.out.println(collision);
		return collision;
	}
}
