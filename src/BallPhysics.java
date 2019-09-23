import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class BallPhysics{

    private float ballRadius; // Ball's radius
    private float ballX = 100; // Ball's center (x, y)
    private float ballY = 100;
    private float ballSpeedX = 4;   // Ball's speed for x and y
    private float ballSpeedY = 2;
    private ImageView circle = new ImageView("Ball.png");
    private Timeline animation;

    private Pane ballPane = new Pane();
    private VBox settingsPane = new VBox();
    private HBox scorePane = new HBox();

    private boolean atRightBorder;
    private boolean atLeftBorder;
    private boolean atBottomBorder;
    private boolean atTopBorder;

    private Label settingsLabel = new Label();
    private Label settingsTitleLabel = new Label();
    private Label scoreLabel = new Label();
    private Label pauseLabel = new Label();
    private int score = 0;

    private float speedVector;
    private float angleVector;
    private float mass;
    private float kineticenergy;

    private float gravity = (float) .8;
    private float frictionX = (float) .9;
    private float frictionY = (float) .7;

    public BallPhysics() {
        ballPane.setStyle("-fx-border-color: black; -fx-background-color: WHITE");

        pauseLabel.setText("");
        pauseLabel.setAlignment(Pos.CENTER);
        pauseLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 100));

        createGameScore();createGameSettings();setBallInMotion();

    }

    public void setBallInMotion(){
        circle.setFitHeight(25);circle.setFitWidth(25);
        circle.relocate(ballX, ballY);
        ballRadius = (float) (circle.getFitHeight() / 2);
        ballPane.getChildren().addAll(circle, pauseLabel);

        animation = new Timeline(new KeyFrame(Duration.millis(25), e -> moveBall()));

        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play(); // Start animation
    }

    public HBox createGameScore() {                                        //This function creates the top title in the game
        scoreLabel.setText(getScoreStatus());

        scorePane.setStyle("-fx-border-color: white; -fx-background-color: black");  //Create a black pane with white border
        scoreLabel.setTextFill(Color.WHITE);                                 //Sets the text white so it is visible in black screen
        scoreLabel.setFont(Font.font(30));                                   //Sets a bigger font

        scorePane.getChildren().add(scoreLabel);
        scorePane.setAlignment(Pos.TOP_CENTER);
        //Sets the settingsLabel at the center
        scorePane.setPadding(new Insets(10));                                 //Sets a padding so the settingsLabel looks better placed in the top pane

        return scorePane;
    }
    public VBox createGameSettings(){                                       // This allows me to see what is going on with the code
        settingsTitleLabel.setText("Ball Settings:");
        settingsTitleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        settingsPane.setPrefWidth(200);
        settingsPane.setSpacing(10);
        settingsPane.setPadding(new Insets(2, 2, 2, 10));
        settingsPane.setStyle("-fx-border-color: BLACK ");
        settingsPane.getChildren().addAll(settingsTitleLabel, settingsLabel);

        return settingsPane;
    }
    public void pause() {
        if(animation.getStatus() == Animation.Status.RUNNING){  // If screen is playing, then stop it, or else, do opposite
            animation.pause();
            pauseLabel.setText("\n\n|| PAUSE");
        }else if(animation.getStatus() == Animation.Status.PAUSED){ // This allows me to use one key to control Pause
            animation.play();
            pauseLabel.setText("");
        }

    }

    public void stop() {
        animation.stop();
    }

    public void increaseAnimationSpeed() {
        animation.setRate(animation.getRate() + .1);
    }   // Increases animation speed, but not the speed of the ball

    public void decreaseAnimationSpeed() {
        animation.setRate(animation.getRate() > 0 ? animation.getRate() - .1 : 0);
    }

    protected void moveBall() {
        ballX = (float) (circle.getLayoutX() + ballSpeedX);                     //First add x, y vectors
        ballY = (float) (circle.getLayoutY() + ballSpeedY);

        final Bounds bounds = ballPane.getBoundsInLocal();                      // Then check if this new vectors collide with walls
        atRightBorder = ballX > (bounds.getMaxX() - circle.getFitHeight());
        atLeftBorder = ballX < (bounds.getMinX());
        atBottomBorder = ballY > (bounds.getMaxY() - circle.getFitHeight());
        atTopBorder = ballY < (bounds.getMinY());


        if (atRightBorder) {                                            // If we have collision then perform action
            ballSpeedX = (float) (-ballSpeedX * frictionX);
            ballX = (float) (bounds.getMaxX() - circle.getFitHeight());
        } else if (atLeftBorder) {
            ballSpeedX = (float) (-ballSpeedX * frictionX);
            ballX = (float) bounds.getMinX();;
        }
        if (atBottomBorder) {
            ballSpeedY = (float) (-ballSpeedY * frictionY);
            ballSpeedX = (float) (ballSpeedX * frictionX);
            ballY = (float) (bounds.getMaxY() - circle.getFitHeight());
        }else if(atTopBorder){
            ballSpeedY = -ballSpeedY;
            ballY = (float) bounds.getMinY();
        } else{                                                             // If no collisions, then apply gravity vector
            ballSpeedY += gravity;
        }

        circle.setLayoutX(ballX);                                       // Finally, move ball
        circle.setLayoutY(ballY);

        setStatus(bounds);
    }

    public void setStatus(Bounds bounds){
        // Refresh the display
        settingsLabel.setText(getGameStatus());
        scoreLabel.setText(getScoreStatus());

        // These functions/variables I
        speedVector = (float)Math.sqrt(ballSpeedX * ballSpeedX + ballSpeedY * ballSpeedY);      // Code to find speed vector
        angleVector = (float)Math.toDegrees(Math.atan2(-ballSpeedY, ballSpeedX));               // Find angle of movement
        mass = (float) (ballRadius * ballRadius * ballRadius / 1000f);  // Normalize by a factor
        kineticenergy = (float) (0.5f * getMass() * (ballSpeedX * ballSpeedX + ballSpeedY * ballSpeedY));
    }
    private String getGameStatus() {
        return ("Ball X: " + ballX
                + "\nBall Y value: " + ballY
                + "\nSpeed X: " + ballSpeedX
                + "\nSpeed Y: " + ballSpeedY
                + "\nSpeed Vector: " + speedVector
                + "\nMove Angle: " + angleVector
                + "\nMass: " + mass
                + "\nKinetic Energy: " + kineticenergy);
    }
    // Add points if ball collides with walls
    private String getScoreStatus() {
        return(atLeftBorder || atTopBorder || atRightBorder || atBottomBorder) ? "Bounces: " + (score += 1):
                "Bounces: " + score;
    }
    // Following functions are not needed but I still have them because I used them in earlier demo
    public float getSpeed() {
        speedVector = (float)Math.sqrt(ballSpeedX * ballSpeedX + ballSpeedY * ballSpeedY);
        return speedVector;
    }

    public float getMoveAngle() {
        angleVector = (float)Math.toDegrees(Math.atan2(-ballSpeedY, ballSpeedX));
        return angleVector;
    }


    public float getMass() {
        mass = (float) (ballRadius * ballRadius * ballRadius / 1000f);  // Normalize by a factor
        return mass;
    }


    public float getKineticEnergy() {
        kineticenergy = (float) (0.5f * getMass() * (ballSpeedX * ballSpeedX + ballSpeedY * ballSpeedY));
        return kineticenergy;
    }

    public Pane getBallPane(){
        return ballPane;
    }
    public Pane getSettingsPane(){
        return settingsPane;
    }
    public Pane getScorePane(){
        return scorePane;
    }

}