import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import java.util.*;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;

	//Constants
	final Image HORSE = new Image("file:/Users/camibrumar/Documents/CS2102/zutopia/res/horse.jpg"); //(getClass().getResourceAsStream()); //(getFilename())); ("/Users/cadydiehl/Downloads/Project4/horse.jpg"));
	final Image DUCK = new Image("file:/Users/camibrumar/Documents/CS2102/zutopia/res/duck.jpg"); //(getClass().getResourceAsStream()); //(getFilename())); ("/Users/cadydiehl/Downloads/Project4/horse.jpg"));
	final Image GOAT = new Image("file:/Users/camibrumar/Documents/CS2102/zutopia/res/goat.jpg"); //(getClass().getResourceAsStream()); //(getFilename())); ("/Users/cadydiehl/Downloads/Project4/horse.jpg"));

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	public ArrayList<Animal> _animals =  new ArrayList<>();

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		ball = new Ball(this);
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		displayAnimals();

		// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent> () {
			@Override
			public void handle (MouseEvent e) {
				GameImpl.this.setOnMouseClicked(null);

				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();
			}
		});

		setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() <= WIDTH && e.getY() >= 0 && e.getY() <= HEIGHT) {
					paddle.moveTo(e.getSceneX(), e.getSceneY());
				}
			}
		});

	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {

		return ball.updatePosition(deltaNanoTime, paddle);
		//return GameState.ACTIVE;
	}

	/**
	 * Displays the images of the animals in the original format at the beginning of the game
	 */
	public void displayAnimals() {
		Animal horse1 = new Animal(HORSE, "horse", HEIGHT / 20 + 2 * (HEIGHT / 6), WIDTH / 20, 5);
		Animal horse2 = new Animal(HORSE, "horse", HEIGHT / 20 + 3 * HEIGHT / 20, 5 * WIDTH / 20, 5);
		Animal horse3 = new Animal(HORSE, "horse", HEIGHT / 20, 10 * WIDTH / 20, 5);
		Animal horse4 = new Animal(HORSE, "horse", HEIGHT / 20 + 3 * (HEIGHT / 6), 10 * WIDTH / 20, 5);
		Animal horse5 = new Animal(HORSE, "horse", HEIGHT / 20 + 2 * (HEIGHT / 6), 15 * WIDTH / 20, 5);

		_animals.add(horse1);
		_animals.add(horse2);
		_animals.add(horse3);
		_animals.add(horse4);
		_animals.add(horse5);

		Animal duck1 = new Animal(DUCK, "duck", HEIGHT / 20, WIDTH / 20, 6);
		Animal duck2 = new Animal(DUCK, "duck", HEIGHT / 20 + 3 * (HEIGHT / 6), WIDTH / 20, 6);
		Animal duck3 = new Animal(DUCK, "duck", HEIGHT / 20 + 2 * (HEIGHT / 6), 5 * WIDTH / 20, 6);
		Animal duck4 = new Animal(DUCK, "duck", HEIGHT / 20 + HEIGHT / 6, 10 * WIDTH / 20, 6);
		Animal duck5 = new Animal(DUCK, "duck", HEIGHT / 20, 15 * WIDTH / 20, 6);
		Animal duck6 = new Animal(DUCK, "duck", HEIGHT / 20 + 3 * (HEIGHT / 6), 15 * WIDTH / 20, 6);

		_animals.add(duck1);
		_animals.add(duck2);
		_animals.add(duck3);
		_animals.add(duck4);
		_animals.add(duck5);
		_animals.add(duck6);

		Animal goat1 = new Animal(GOAT, "goat", HEIGHT / 20 + HEIGHT / 6, WIDTH / 20, 5);
		Animal goat2 = new Animal(GOAT, "goat", HEIGHT / 20, 5 * WIDTH / 20, 5);
		Animal goat3 = new Animal(GOAT, "goat", HEIGHT / 20 + 3 * (HEIGHT / 6), 5 * WIDTH / 20, 5);
		Animal goat4 = new Animal(GOAT, "goat", HEIGHT / 20 + 2 * (HEIGHT / 6), 10 * WIDTH / 20, 5);
		Animal goat5 = new Animal(GOAT, "goat", HEIGHT / 20 + HEIGHT / 6, 15 * WIDTH / 20, 5);

		_animals.add(goat1);
		_animals.add(goat2);
		_animals.add(goat3);
		_animals.add(goat4);
		_animals.add(goat5);

		for(int i = 0; i < _animals.size(); i++) {
			getChildren().add(_animals.get(i).getImage());
		}
	}
}
