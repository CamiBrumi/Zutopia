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
	public static final double INITIAL_VX = 3 * 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 3 * 1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	private boolean vxChangedLastTime;
	private boolean vyChangedLastTime;
	private int counter; // TODO: 24/11/2018 eliminate this variable . only for prints.
	//private double d; // distance between the points
	private double xCol, yCol; //x and y of the last collision
	private String pastColWall;

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle() {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball() {
		x = GameImpl.WIDTH / 2;
		y = GameImpl.HEIGHT / 2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);

		vxChangedLastTime = false;
		vyChangedLastTime = false;
		counter = 0;
		//d = BALL_RADIUS;
		xCol = -1;
		yCol = -1;
		pastColWall = "";
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 *
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition(long deltaNanoTime, Paddle paddle) {
		
		final String colW = collisionWallOrPaddle(paddle);
		if (!colW.equals("")) {
			if (!colW.equals(pastColWall)) {
				System.out.println(colW);
				changeVelocity(colW);
				pastColWall = colW;
			}
		}

		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;

		x += dx;
		y += dy;

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));


	}

	private void changeVelocity(String wall) {
		/*if (wall.equals("V")) {
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
		}*/


		if (wall.equals("R") || wall.equals("L") ) { // if there is a collision with the right or left wall
			vx = -vx;

		} else if (wall.equals("U") || wall.equals("D") || wall.equals("P")) { // if there is a collision with the upper down wall, top or bottom of the paddle
			vy = -vy;

		} else { // wall.equals(corner), where corner can be a corner formed by two walls or by the paddle and a wall.
			vx = -vx;
			vy = -vy;
		}
	}

	private String collisionWallOrPaddle(Paddle paddle) {
		String collision = "";

		double topPady = paddle.getY() - paddle.PADDLE_HEIGHT/2;
		double bottomPady = paddle.getY() + paddle.PADDLE_HEIGHT/2;


		//corners of the paddle
		double ballBLx = x - BALL_RADIUS; // bottom left
		double ballBLy = y + BALL_RADIUS;

		double ballBRx = x + BALL_RADIUS; // bottom right
		double ballBRy = y + BALL_RADIUS;

		double ballULx = x - BALL_RADIUS; // upper left
		double ballULy = y - BALL_RADIUS;

		double ballURx = x + BALL_RADIUS; // upper right
		double ballURy = y - BALL_RADIUS;

		if (pointIsInThePaddle(ballBLx, ballBLy, paddle)
				|| pointIsInThePaddle(ballBRx, ballBRy, paddle)
				|| pointIsInThePaddle(ballULx, ballULy, paddle)
				|| pointIsInThePaddle(ballURx, ballURy, paddle)) {
			collision += "P";
		}

		/*if ((x <= (paddle.getX() + paddle.PADDLE_WIDTH / 2))
				&& (x >= (paddle.getX() - paddle.PADDLE_WIDTH / 2))) {
			//System.out.println("paddle?");

			if ((y + BALL_RADIUS <= (paddle.getY() - paddle.PADDLE_HEIGHT / 2))
					&& (y + BALL_RADIUS >= (paddle.getY() + paddle.PADDLE_HEIGHT / 2))) {
				System.out.println("T");
				collision += "T";
			} else if ((y - BALL_RADIUS <= (paddle.getY() + paddle.PADDLE_HEIGHT / 2))
					&& (y - BALL_RADIUS >= (paddle.getY() - paddle.PADDLE_HEIGHT / 2))) {
				System.out.println("B");
				collision += "B";
			}
		}*/

		if (x + BALL_RADIUS >= GameImpl.WIDTH) { // right wall
			collision += "R";
		} else if (x - BALL_RADIUS <= 0) { // left wall
			collision += "L";
		}

		if (y + BALL_RADIUS >= GameImpl.HEIGHT) { // upper wall
			collision += "D";
		} else if (y - BALL_RADIUS <= 0) { // down wall
			collision += "U";
		}

		return collision;
	}

	private boolean pointIsInThePaddle (double px, double py, Paddle pad) {
		return ((px >= (pad.getX() - pad.PADDLE_WIDTH/2))
				&& (px <= (pad.getX() + pad.PADDLE_WIDTH/2))
				&& (py >= (pad.getY() - pad.PADDLE_HEIGHT))
				&& (py <= (pad.getY() + pad.PADDLE_HEIGHT)));
	}

}
