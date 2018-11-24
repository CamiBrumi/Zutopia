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
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	//private final double boxCornerCalcu = BALL_RADIUS * sqrt(2);

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
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime, Paddle paddle) {
		final String colW = collisionWallOrPaddle(paddle);

		if (colW != null)
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
		if (wall.equals("R") || wall.equals("L")) { // if there is a collision with the right wall
			vx = -vx;
		} else if (wall.equals("U") || wall.equals("D")) { // if there is a collision with the upper wall
			vy = -vy;
		}
	}

	private String collisionWallOrPaddle(Paddle paddle) {
	    if (y + BALL_RADIUS <= (paddle.getY() + paddle.PADDLE_HEIGHT/2)
                && y + BALL_RADIUS >= (paddle.getY() - paddle.PADDLE_WIDTH/2)
                && x <= (paddle.getX() + paddle.PADDLE_WIDTH/2)
                && x >= (paddle.getX() - paddle.PADDLE_WIDTH/2)) { // the bottom of the ball collides with the paddle
	        return "L";
        }
        if (y - BALL_RADIUS <= (paddle.getY() + paddle.PADDLE_HEIGHT/2)
                && y - BALL_RADIUS >= (paddle.getY() - paddle.PADDLE_WIDTH/2)
                && x <= (paddle.getX() + paddle.PADDLE_WIDTH/2)
                && x >= (paddle.getX() - paddle.PADDLE_WIDTH/2)) { // the top of the ball collides with the paddle
            return "U";
        } else if (x + BALL_RADIUS >= GameImpl.WIDTH) { // right wall
			return "R";
		} else if (x - BALL_RADIUS <= 0) { // left wall
			return "L";
		}else if (y + BALL_RADIUS >= GameImpl.HEIGHT) { // upper wall
			return "U";
		} else if (y - BALL_RADIUS <= 0) { // down wall
			return "D";
		}
		return null;
	}
}
