package it.unipi.dii.inginf.lsdb.justrecipe.controller;

import it.unipi.dii.inginf.lsdb.justrecipe.model.Session;
import it.unipi.dii.inginf.lsdb.justrecipe.model.User;
import it.unipi.dii.inginf.lsdb.justrecipe.persistence.Neo4jDriver;
import it.unipi.dii.inginf.lsdb.justrecipe.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class UserSnapshotController {
    private Neo4jDriver neo4jDriver;
    private Session appSession;
    @FXML private ImageView userSnapImg;
    @FXML private Label userSnapUsername;
    @FXML private Label userSnapFollower;
    @FXML private Label userSnapFirstName;
    @FXML private Label userSnapLastName;
    @FXML private AnchorPane userSnapMain;

    private User user;

    /**
     * Initialization functions
     */
    public void initialize()
    {
        neo4jDriver = Neo4jDriver.getInstance();
        appSession = Session.getInstance();
        userSnapMain.setOnMouseClicked(mouseEvent -> handleClickOnSnap(mouseEvent));
    }

    /**
     * Set the snapshot with the given user
     * @param u  Object user with the given information necessary to the snap
     */
    public void setUserSnap(User u)
    {
        this.user = u;
        String urlPicture = u.getPicture();
        if(urlPicture==null || urlPicture.isEmpty())
            userSnapImg.setImage(new Image("/img/genericUser.png"));
        else
            userSnapImg.setImage(new Image(urlPicture));

        userSnapUsername.setText(u.getUsername());
        userSnapFollower.setText(Integer.toString(u.getFollower()));
        userSnapFirstName.setText(u.getFirstName());
        userSnapLastName.setText(u.getLastName());
    }

    /**
     * Handle the click on the snap and it changes page going in the profile page of the user of the clicked snap
     * @param mouseEvent    Event that leads to the handler
     */
    private void handleClickOnSnap(MouseEvent mouseEvent)
    {
        ProfilePageController profilePageController = (ProfilePageController)
                Utils.changeScene("/profilePage.fxml", mouseEvent);
        profilePageController.setProfile(user);
    }
}
