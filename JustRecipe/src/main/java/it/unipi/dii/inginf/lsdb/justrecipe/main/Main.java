package it.unipi.dii.inginf.lsdb.justrecipe.main;
import it.unipi.dii.inginf.lsdb.justrecipe.persistence.MongoDBDriver;
import it.unipi.dii.inginf.lsdb.justrecipe.persistence.Neo4jDriver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class used to start the application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
        primaryStage.setTitle("JustRecipe");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();

        // close the connection to Neo4J when the app closes
        primaryStage.setOnCloseRequest(actionEvent -> {
            Neo4jDriver.getInstance().closeConnection();
            //MongoDBDriver.getInstance().closeConnection();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}