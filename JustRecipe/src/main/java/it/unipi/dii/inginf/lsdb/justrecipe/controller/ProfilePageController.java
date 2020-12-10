package it.unipi.dii.inginf.lsdb.justrecipe.controller;

import it.unipi.dii.inginf.lsdb.justrecipe.persistence.MongoDBDriver;
import it.unipi.dii.inginf.lsdb.justrecipe.persistence.Neo4jDriver;
import it.unipi.dii.inginf.lsdb.justrecipe.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class ProfilePageController {
    private Neo4jDriver neo4jDriver;
    private MongoDBDriver mongoDBDriver;
    private String username;
    @FXML private ImageView homepageIcon;
    @FXML private ImageView discoveryImg;

    public void initialize ()
    {
        neo4jDriver = Neo4jDriver.getInstance();
        //mongoDBDriver = MongoDBDriver.getInstance();
        homepageIcon.setOnMouseClicked(mouseEvent -> clickOnHomepageToChangePage(mouseEvent));
        discoveryImg.setOnMouseClicked(mouseEvent -> clickOnDiscImgtoChangePage(mouseEvent));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void clickOnHomepageToChangePage(MouseEvent mouseEvent){
        try{
            HomePageController homePageController = (HomePageController)
                    Utils.changeScene("/homepage.fxml", mouseEvent);
            homePageController.setUsername(username);
        }catch (NullPointerException n){System.out.println("homePageController is null!!!!");}
    }

    private void clickOnDiscImgtoChangePage(MouseEvent mouseEvent){
        try{
            DiscoveryPageController discoveryPageController = (DiscoveryPageController)
                    Utils.changeScene("/discoveryPage.fxml", mouseEvent);
            discoveryPageController.setUsername(username);
        }catch (NullPointerException n){System.out.println("homePageController is null!!!!");}
    }
}
