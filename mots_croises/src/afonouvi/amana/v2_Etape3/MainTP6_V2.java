package afonouvi.amana.v2_Etape3;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainTP6_V2 extends Application {
	@Override
	public void start(Stage primaryStage) {
		try
		{
	        primaryStage.setTitle("TP6 AFONOUVI-AMANA");
	        
			FXMLLoader loader = new FXMLLoader() ;
            loader.setLocation(MainTP6_V2.class.getResource("VueTP6_V2.fxml"));
            Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			
			primaryStage.setX(primaryScreenBounds.getMinX()+50);
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth()/2);
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			
			
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
