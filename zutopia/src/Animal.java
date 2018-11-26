import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.media.*;
import java.util.*;

public class Animal {
    //Variables
    private Image _image;
    //private AudioClip _sound;
    private String _name;
    int _x;
    int _y;
    private int _numOfAnimals = 16; // TODO: 26/11/2018 see if it is good to define static methods. initializing this var here will affect other rounds after the first one is finished?

    //Constructors
    public Animal(Image image, String name, int x, int y, int numOfAnimals) { //AudioClip sound //String soundFile,
        _image = image;
        _name = name;
        _x = x;
        _y = y;
        _numOfAnimals = numOfAnimals;
    }

    //Methods

    /**
     *
     * @return the name of the animal
     */
    public String getName() {
        return _name;
    }

    /**
     *
     *
     */
    public Label getImage() {
        Label imageLabel = new Label("", new ImageView(_image));
        imageLabel.setLayoutX(_x);
        imageLabel.setLayoutY(_y);
        return imageLabel;
    }

    /**
     *
     * @return
     */
    public int getNumOfAnimals() {
        return _numOfAnimals;
    }

    /**
     *
     */
    public void remove() {
        _numOfAnimals--;

    }
    /*
    public void playSound() {
        _sound.play();
    }
    */
}
