package cn.edu.sustech.cs209.chatting.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

  @FXML
  TextField username;

  @FXML
  TextField password;

  @FXML
  Button enter;

  @FXML
  Button register;

  @FXML
  Button close;

  Login login;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }

  @FXML
  public void OK(){
    String[] nameAndPass = new String[2];
    nameAndPass[0] = username.getText();
    nameAndPass[1] = password.getText();
    login.setNameAndWord(nameAndPass);
    login.stage.close();
  }

  @FXML
  public void register(){

  }

  @FXML
  public void close(){
    System.exit(0);
  }

  public void setLogin(Login login){
    this.login = login;
  }


}
