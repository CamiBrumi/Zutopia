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
	private String pastColWall; // the wall that the ball has previously collisioned with.
	private int nCollisionsBottomWall; // the number of collisions with the lower wall.
	private final GameImpl _gi;
	private final double VELOCITY_INCREM_MULTIPLIER = 1.2;

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
	public Ball(GameImpl gi) {
		x = GameImpl.WIDTH / 2;
		y = GameImpl.HEIGHT / 2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);

		pastColWall = "";
		nCollisionsBottomWall = 0; // initially the number of times that the ball hit the lower wall is zero.
		_gi = gi;

	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 *
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 * @param paddle the paddle that is in the game.
	 * @return the state of the game. If the ball hit the lower wall 5 times then we return GameState.LOST. Otherwise
	 * 			we return GameState.ACTIVE.
	 */
	public GameImpl.GameState updatePosition(long deltaNanoTime, Paddle paddle) {
		checkCollisionAnimalAndRemove();


		final String colW = collisionWallOrPaddle(paddle);
		if (!colW.equals("")) {
			if ((colW.equals("P") && pastColWall.equals("P"))  || !colW.equals(pastColWall)) {

				if (colW.equals("D")) {
					nCollisionsBottomWall++;
					if (nCollisionsBottomWall >= 5) {
						return GameImpl.GameState.LOST;
					}
				}
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
		return GameImpl.GameState.ACTIVE;

	}

	private void checkCollisionAnimalAndRemove() { // TODO: 26/11/2018 finish it
		//corners of the ball
		double ballBLx = x - BALL_RADIUS; // bottom left
		double ballBLy = y + BALL_RADIUS;

		double ballBRx = x + BALL_RADIUS; // bottom right
		double ballBRy = y + BALL_RADIUS;

		double ballULx = x - BALL_RADIUS; // upper left
		double ballULy = y - BALL_RADIUS;

		double ballURx = x + BALL_RADIUS; // upper right
		double ballURy = y - BALL_RADIUS;

		for (Animal a : _gi._animals) {
			if (ballCornerIsHittingAnimal(ballBLx, ballBLy, a)
					|| ballCornerIsHittingAnimal(ballBRx, ballBRy, a)
					|| ballCornerIsHittingAnimal(ballULx, ballULy, a)
					|| ballCornerIsHittingAnimal(ballURx, ballURy, a)) {
				a.remove();
				vx = vx * VELOCITY_INCREM_MULTIPLIER;
				vy = vy * VELOCITY_INCREM_MULTIPLIER;
				// TODO: 26/11/2018 after removing an animal just increment the speed of the ball!
			}

		}

	}

	private boolean ballCornerIsHittingAnimal(double cx, double cy, Animal a) {
		return (cx >= a._x - a.getImage().getWidth()/2
				&& cx <= a._x + a.getImage().getWidth()/2
				&& cy >= a._y - a.getImage().getHeight()/2
				&& cy <= a._y + a.getImage().getHeight()/2);
	}

	/**
	 * Changes the velocity, the vertical, the horizontal or both, with the negated velocity, depending on which
	 * wall did the ball hit or if it hit the paddle..
	 *
	 * @param wall the wall/paddle that the ball has hit.
	 */
	private void changeVelocity(String wall) {

		if (wall.equals("R") || wall.equals("L") ) { // if there is a collision with the right or left wall
			vx = -vx;

		} else if (wall.equals("U") || wall.equals("D") || wall.equals("P")) { // if there is a collision with the upper down wall, top or bottom of the paddle
			vy = -vy;

		} else { // wall.equals(corner), where corner can be a corner formed by two walls or by the paddle and a wall.
			vx = -vx;
			vy = -vy;
		}

	}

	/**
	 * Determines whether there is a collision with the walls and/or the paddle and returns them in a string.
	 *
	 * @param paddle the paddle in this game.
	 * @return the collision wall(s) and/or paddle or an empty string if there is no collision.
	 */
	private String collisionWallOrPaddle(Paddle paddle) {
		String collision = "";

		//corners of the ball
		double ballBLx = x - BALL_RADIUS; // bottom left
		double ballBLy = y + BALL_RADIUS;

		double ballBRx = x + BALL_RADIUS; // bottom right
		double ballBRy = y + BALL_RADIUS;

		double ballULx = x - BALL_RADIUS; // upper left
		double ballULy = y - BALL_RADIUS;

		double ballURx = x + BALL_RADIUS; // upper right
		double ballURy = y - BALL_RADIUS;

		if (pointIsInThePaddle(ballBLx, ballBLy, paddle) // if one of the corners of the box that surrounds the ball is inside the paddle rectangle.
				|| pointIsInThePaddle(ballBRx, ballBRy, paddle)
				|| pointIsInThePaddle(ballULx, ballULy, paddle)
				|| pointIsInThePaddle(ballURx, ballURy, paddle)) {
			collision += "P";
		}

		if (x + BALL_RADIUS >= GameImpl.WIDTH) { // if the ball hit the right wall
			collision += "R";
		} else if (x - BALL_RADIUS <= 0) { // if the ball hit the left wall
			collision += "L";
		}

		if (y + BALL_RADIUS >= GameImpl.HEIGHT) { // if the ball hit the lower wall
			collision += "D";
		} else if (y - BALL_RADIUS <= 0) { // if the ball hit the upper wall
			collision += "U";
		}

		// NOTE: it is impossible that the ball hits the right and the left walls at the same time, that's why
		// I used if+else. The same thing happens with the upper and the lower wall. However it is possible for
		// the ball to hit the lower and the right wall simultaneously, which means that the ball has just hit
		// lower-left corner of the game pane.

		return collision;
	}

	private boolean pointIsInThePaddle (double px, double py, Paddle pad) {
		return ((px >= (pad.getX() - pad.PADDLE_WIDTH/2))
				&& (px <= (pad.getX() + pad.PADDLE_WIDTH/2))
				&& (py >= (pad.getY() - pad.PADDLE_HEIGHT))
				&& (py <= (pad.getY() + pad.PADDLE_HEIGHT)));
	}

}

// TODO: 25/11/2018 remove system.outs!