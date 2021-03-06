package it.unipi.dii.inginf.lsdb.justrecipe.controller;

import it.unipi.dii.inginf.lsdb.justrecipe.model.Comment;
import it.unipi.dii.inginf.lsdb.justrecipe.model.Recipe;
import it.unipi.dii.inginf.lsdb.justrecipe.model.Session;
import it.unipi.dii.inginf.lsdb.justrecipe.persistence.MongoDBDriver;
import it.unipi.dii.inginf.lsdb.justrecipe.persistence.Neo4jDriver;
import it.unipi.dii.inginf.lsdb.justrecipe.utils.Utils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.Date;

/**
 * Controller for the page of the recipe
 */
public class RecipePageController {

    @FXML private ImageView homeImg;
    @FXML private ImageView discoveryImg;
    @FXML private ImageView profileImg;
    @FXML private Text recipeInstructions;
    @FXML private ImageView recipeLikeImg;
    @FXML private ImageView recipePicture;
    @FXML private ImageView logoutPic;
    @FXML private Text recipeTitle;
    @FXML private Text recipeUsername;
    @FXML private Text recipeCarbs;
    @FXML private Text recipeCalories;
    @FXML private Text recipeFat;
    @FXML private ImageView recipeEditImg;
    @FXML private Text recipeCategories;
    @FXML private Text recipeIngredients;
    @FXML private Text recipeProtein;
    @FXML private Label recipeLikes;
    @FXML private Label recipeDate;
    @FXML private VBox recipeVBox;
    @FXML private ImageView recipeDelete;
    @FXML private TextArea commentsArea;
    @FXML private Button sendButton;
    @FXML private Button cancelButton;

    private Recipe recipe;
    private Session appSession;
    private Neo4jDriver neo4jDriver;
    private MongoDBDriver mongoDBDriver;

    /**
     * Initialization functions
     */
    public void initialize ()
    {
        homeImg.setOnMouseClicked(mouseEvent -> clickOnHomepageToChangePage(mouseEvent));
        profileImg.setOnMouseClicked(mouseEvent -> clickOnProfileToChangePage(mouseEvent));
        discoveryImg.setOnMouseClicked(mouseEvent -> clickOnDiscoveryToChangePage(mouseEvent));
        logoutPic.setOnMouseClicked(mouseEvent -> clickOnLogoutImg(mouseEvent));
        recipeVBox.setAlignment(Pos.CENTER);
        appSession = Session.getInstance();
        neo4jDriver = Neo4jDriver.getInstance();
        mongoDBDriver = MongoDBDriver.getInstance();
        sendButton.setOnAction(actionEvent -> handleSendButtonAction());
        cancelButton.setOnAction(actionEvent -> handleCancelButtonAction());
        recipeLikeImg.setOnMouseClicked(mouseEvent -> handleClickOnLike());
        recipeUsername.setOnMouseClicked(mouseEvent -> handleClickOnUsername(mouseEvent));
        recipeEditImg.setOnMouseClicked(mouseEvent -> clickOnEditButton(mouseEvent));
    }

    /**
     * Handler function for the click on the like
     */
    private void handleClickOnLike()
    {
        if(neo4jDriver.isThisRecipeLikedByOne(recipeTitle.getText(),appSession.getLoggedUser().getUsername()))
        {
            neo4jDriver.unlike(appSession.getLoggedUser().getUsername(),recipeTitle.getText());
            recipeLikeImg.setImage(new Image("img/like.png"));
        }
        else
        {
            neo4jDriver.like(appSession.getLoggedUser().getUsername(),recipeTitle.getText());
            recipeLikeImg.setImage(new Image("img/alreadyliked.png"));
        }

        recipeLikes.setText(String.valueOf(neo4jDriver.howManyLikes(recipeTitle.getText())));
    }

    /**
     * Function who handle the adding comments, and upload on mongoDB
     */
    private void handleSendButtonAction(){
        if(commentsArea.getText().equals("")) {
            Utils.showErrorAlert("No Comments in the CommentsArea");
            return;
        }
        Comment comment = new Comment(appSession.getLoggedUser().getUsername(), commentsArea.getText(), new Date());
        Utils.showComment(recipeVBox, comment, recipe);

        if(mongoDBDriver.addComment(recipe, comment))
        {
            Utils.showInfoAlert("Comment successfully added");
            commentsArea.setText("");
        }
    }

    /**
     * Function who handle the editButton
     * @param mouseEvent    Event that leads to the handler
     */
    private void clickOnEditButton(MouseEvent mouseEvent){
        AddRecipePageController addRecipePageController = (AddRecipePageController)
                Utils.changeScene("/addRecipe.fxml", mouseEvent);
        addRecipePageController.setRecipeToUpdate(recipe);
    }

    /**
     * Cancelling the comment textArea by clicking on the cancel Button
     */
    private void handleCancelButtonAction(){
        if(!commentsArea.getText().equals("")) commentsArea.setText("");
    }

    /**
     * Setters for the recipe, in which we also set the correct value to show
     * @param r    Recipe to show
     */
    public void setRecipe(Recipe r) {
        this.recipe = r;
        recipeTitle.setText(recipe.getTitle());
        recipeInstructions.setText(recipe.getInstructions());
        if (recipe.getPicture() != null)
        {
            recipePicture.setImage(new Image(recipe.getPicture()));
        }
        else
        {
            recipePicture.setImage(new Image("img/genericRecipe.png"));
        }
        recipeUsername.setText(recipe.getAuthorUsername());
        if (recipe.getCarbs() != 0)
            recipeCarbs.setText(recipe.getCarbs() + " g");
        else
            recipeCarbs.setText(" -- ");
        if (recipe.getCalories() != 0)
            recipeCalories.setText(recipe.getCalories() + " Kcal");
        else
            recipeCalories.setText(" -- ");
        if (recipe.getFat() != 0)
            recipeFat.setText(recipe.getFat() + " g");
        else
            recipeFat.setText(" -- ");
        if (recipe.getProtein() != 0)
            recipeProtein.setText(recipe.getProtein() + " g");
        else
            recipeProtein.setText(" -- ");
        recipeCategories.setText(Utils.fromListToString(recipe.getCategories()));
        recipeIngredients.setText(Utils.fromListToString(recipe.getIngredients()));
        recipeDate.setText("Published on: " + Utils.fromDateToString(recipe.getCreationTime()));
        recipeLikes.setText(String.valueOf(neo4jDriver.howManyLikes(recipe.getTitle())));
        if(neo4jDriver.isThisRecipeLikedByOne(recipe.getTitle(),appSession.getLoggedUser().getUsername()))
            recipeLikeImg.setImage(new Image("img/alreadyliked.png"));

        if(recipe.getComments() != null && recipe.getComments().size() != 0) {
            Label commentsTitle = new Label("Comments:");
            commentsTitle.setFont(Font.font(24));
            recipeVBox.getChildren().add(commentsTitle);
            Utils.showCommentsOfRecipe(recipeVBox, recipe.getComments(), recipe);
        }

        if(appSession.getLoggedUser().getRole()!=2 && !appSession.getLoggedUser().getUsername().equals(recipe.getAuthorUsername()))
            recipeDelete.setVisible(false);
        else
            recipeDelete.setOnMouseClicked(mouseEvent -> handleDeleteButtonAction(mouseEvent));

        if(!appSession.getLoggedUser().getUsername().equals(recipe.getAuthorUsername()))
            recipeEditImg.setVisible(false);
    }

    /**
     * Handler for deleting this recipe
     */
    private void handleDeleteButtonAction(MouseEvent mouseEvent) {
        if(mongoDBDriver.deleteRecipe(recipe))
        {
            // if mongo operation is successfully executed then the neo4j op is performed
            if(!neo4jDriver.deleteRecipe(recipe))
            {
                // If neo4j fails I have to restore the initial condition in mongo
                mongoDBDriver.addRecipe(recipe);
                Utils.showErrorAlert("Error in delete the recipe");
            }
            else
            {
                Utils.showInfoAlert("Recipe correctly deleted");
            }
        }

        // Go to profile page
        ProfilePageController profilePageController =
                (ProfilePageController) Utils.changeScene("/profilePage.fxml", mouseEvent);
        profilePageController.setProfile(appSession.getLoggedUser());
    }

    /**
     * Function used to handle the click on the homepage icon
     * @param mouseEvent    event that represents the click on the icon
     */
    private void clickOnHomepageToChangePage(MouseEvent mouseEvent){
        Utils.changeScene("/homepage.fxml", mouseEvent);
    }

    /**
     * Function used to handle the click on the recipe's owner
     * @param mouseEvent    event that represents the click on the recipe's owner
     */
    private void handleClickOnUsername(MouseEvent mouseEvent){
        ProfilePageController profilePageController = (ProfilePageController)
                Utils.changeScene("/profilePage.fxml", mouseEvent);
        profilePageController.setProfile(neo4jDriver.getUserByUsername(recipeUsername.getText()));
    }

    /**
     * Function used to handle the click on the profile icon
     * @param mouseEvent    event that represents the click on the icon
     */
    private void clickOnProfileToChangePage(MouseEvent mouseEvent){
        ProfilePageController profilePageController = (ProfilePageController)
                Utils.changeScene("/profilePage.fxml", mouseEvent);
        profilePageController.setProfile(Session.getInstance().getLoggedUser());
    }

    /**
     * Function used to handle the click on the discovery icon
     * @param mouseEvent    event that represents the click on the icon
     */
    private void clickOnDiscoveryToChangePage(MouseEvent mouseEvent){
        Utils.changeScene("/discoveryPage.fxml", mouseEvent);
    }

    /**
     * Function that let the logout action, by going into the welcome page
     * @param mouseEvent event that represents the click on the icon
     */
    private void clickOnLogoutImg(MouseEvent mouseEvent){
        Utils.changeScene("/welcome.fxml", mouseEvent);
    }
}

