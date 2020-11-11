//Daniel Alvarado
//Al Manrique
package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import songlib.view.Controller;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/songlib/view/songlib.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		Controller listController = 
				loader.getController();
		listController.start(primaryStage);
		Scene scene = new Scene(root, 600, 500);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
			
		primaryStage.show(); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
