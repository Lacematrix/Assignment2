package cn.edu.sustech.cs209.chatting.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
  public void OK() {
    String[] nameAndPass = new String[2];
    nameAndPass[0] = username.getText();
    nameAndPass[1] = password.getText();
    if (nameAndPass[0].equals("") || nameAndPass[1].equals("")) {
      new ShowDialog("username or password can not be null");
      return;
    }
    login.setNameAndWord(nameAndPass);
    login.stage.close();
  }

  @FXML
  public void register() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("register-R.fxml"));
    login.stage.setScene(new Scene(loader.load()));
    LoginController loginController = loader.getController();
    loginController.setLogin(login);
  }

  @FXML
  public void sure() {
    String[] nameAndPass = new String[2];
    nameAndPass[0] = username.getText();
    nameAndPass[1] = password.getText();
    if (nameAndPass[0].equals("") || nameAndPass[1].equals("")) {
      new ShowDialog("username or password can not be null");
      return;
    }
    try {
      Client client = new Client();
      if (client.init(nameAndPass)) {
        new ShowDialog("Success!");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        login.stage.setScene(new Scene(loader.load()));
        LoginController loginController = loader.getController();
        loginController.setLogin(login);
      } else {
        new ShowDialog("There already exists the same user");
      }
    } catch (SocketException e) {
      new ShowDialog("Server has error now, please wait and try again");
    } catch (IOException | ClassNotFoundException e) {
      new ShowDialog("application error");
      System.out.println(e);
    }

  }

  @FXML
  public void back() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
    login.stage.setScene(new Scene(loader.load()));
    LoginController loginController = loader.getController();
    loginController.setLogin(login);
  }

  @FXML
  public void close() {
    System.exit(0);
  }

  public void setLogin(Login login) {
    this.login = login;
  }


}
