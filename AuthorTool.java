package author_tool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthorTool extends Application {

    private Stage stage;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("ECHO Content Author Tool");

        initLayout();
    }

    public Stage getStage() { return stage; }

    public void initLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AuthorTool.class.getResource("AuthorTool.fxml"));
            root = (BorderPane) loader.load();
            ATController controller = loader.getController();
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
            stage.setScene(new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
            stage.setMaximized(true);
            stage.show();
            controller.setStage(this.stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
