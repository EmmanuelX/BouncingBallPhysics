import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by Emmanuel on 9/23/19.
 */
public class BouncingBall extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BallPhysics ball = new BallPhysics();
        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(ball.getBallPane());
        borderPane.setRight(ball.getSettingsPane());

        // code to increase and decrease animation
        ball.getBallPane().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                ball.increaseAnimationSpeed();
            }
            else if (e.getCode() == KeyCode.DOWN) {
                ball.decreaseAnimationSpeed();
            }
            else if (e.getCode() == KeyCode.SPACE) {    // This allows me to pause screen
                ball.pause();
            }
        });

        Scene scene = new Scene(borderPane, 750, 750);

        primaryStage.setTitle("Pinball Engine");
        primaryStage.setScene(scene);
        primaryStage.show();

        ball.getBallPane().requestFocus();

    }
    public static void main(String[] args){
        launch(args);
    }
}
