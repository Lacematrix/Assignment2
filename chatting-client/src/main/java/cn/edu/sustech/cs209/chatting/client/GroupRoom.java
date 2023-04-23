package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GroupRoom extends Application {

  Stage stage = new Stage();

  Client client;


  String sendBy;

  String[] memberList;

  GroupRoomController groupRoomController;

  public GroupRoom(Client client, String[] userList, String sendBy) {
    this.client = client;
    this.sendBy = sendBy;
    this.memberList = userList;
  }

  public void showWindow() throws Exception {
    start(stage);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Group.fxml"));
    stage.setScene(new Scene(fxmlLoader.load()));
    groupRoomController = fxmlLoader.getController();//在load之后调用
    groupRoomController.init(client, sendBy, memberList);
    updateUserList(memberList);
    stage.setTitle("Chatting Room");
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        try {
          client.getController().groupRoom = null;
          stage.close();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
    stage.show();
  }

  public void updateUserList(String[] userList) {
    memberList = userList;
    groupRoomController.updateUserList(memberList);
  }

  public void updateMsg(Message message) {
    groupRoomController.updateMsg(message);
  }
}
