package application;

import controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Cadastro de Pessoas");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
