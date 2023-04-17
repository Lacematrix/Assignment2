package cn.edu.sustech.cs209.chatting.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {

  private String[] nameAndWord;

  Stage stage = new Stage();

  public String[] start() throws Exception {
    start(stage);
    return nameAndWord;
  }

  public void setNameAndWord(String[] nameAndWord){
    this.nameAndWord = nameAndWord;
  }


  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
    stage.setScene(new Scene(fxmlLoader.load()));
    LoginController loginController = fxmlLoader.getController();
    loginController.setLogin(this);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.showAndWait();
  }
}
