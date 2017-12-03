package armsim;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class gui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        
        Scene scene = new Scene(root, 700, 700);
    
        primaryStage.setTitle("Arm Sim");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
