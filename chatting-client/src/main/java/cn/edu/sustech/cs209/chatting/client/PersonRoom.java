package cn.edu.sustech.cs209.chatting.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PersonRoom extends Application {

  Stage stage = new Stage();

  Client client;

  String username;

  String sendTo;

  Controller controller;

  private PersonController personController;


  public PersonRoom(Client client, String username, String sendTo, Controller controller) {
    this.client = client;
    this.username = username;
    this.controller = controller;
    this.sendTo = sendTo;
  }

  public void showWindow() throws Exception {
    start(stage);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Person.fxml"));
    stage.setScene(new Scene(fxmlLoader.load()));
    personController = fxmlLoader.getController();//在load之后调用
    personController.init(username, sendTo, client, controller);
    stage.setTitle("Chatting Room");
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        try {
          try {
            for (int i = 0; i < controller.personRoom.size(); i++) {
              if (controller.personRoom.get(i).sendTo.equals(sendTo)){
                controller.personRoom.remove(i);
                break;
              }
            }
          }catch (Exception e){
            System.out.println(e);
          }
          stage.close();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
    stage.show();
  }

  public PersonController getPersonController() {
    return personController;
  }
}
