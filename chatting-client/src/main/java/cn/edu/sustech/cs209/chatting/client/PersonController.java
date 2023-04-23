package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PersonController implements Initializable {

  @FXML
  ListView<Message> personChatList;

  @FXML
  Label personName;

  @FXML
  Label chatUserName;

  @FXML
  TextArea inputArea;

  String username;

  String sendTo;

  Client client;

  Controller controller;

  public void init(String username, String sendTo, Client client, Controller controller) {
    this.username = username;
    this.client = client;
    this.controller = controller;
    this.sendTo = sendTo;
    chatUserName.setText("send to: " + sendTo);
    personName.setText("send by: " + username);
  }

  public PersonController() {//必须要有无参方法

  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    personChatList.setCellFactory(new MessageCellFactory());
  }

  private class MessageCellFactory implements Callback<ListView<Message>, ListCell<Message>> {

    @Override
    public ListCell<Message> call(ListView<Message> param) {
      return new ListCell<Message>() {

        @Override
        public void updateItem(Message msg, boolean empty) {
          super.updateItem(msg, empty);
          if (empty || Objects.isNull(msg)) {
            return;
          }

          HBox wrapper = new HBox();
          Label nameLabel = new Label(msg.getSentBy());
          Label msgLabel = new Label(msg.getData());

          nameLabel.setPrefSize(50, 20);
          nameLabel.setWrapText(true);
          nameLabel.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

          if (username.equals(msg.getSentBy())) {
            wrapper.setAlignment(Pos.TOP_RIGHT);
            wrapper.getChildren().addAll(msgLabel, nameLabel);
            msgLabel.setPadding(new Insets(0, 20, 0, 0));
          } else {
            wrapper.setAlignment(Pos.TOP_LEFT);
            wrapper.getChildren().addAll(nameLabel, msgLabel);
            msgLabel.setPadding(new Insets(0, 0, 0, 20));
          }

          setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          setGraphic(wrapper);
        }
      };
    }
  }

  /**
   * Sends the message to the <b>currently selected</b> chat.
   * <p>
   * Blank messages are not allowed. After sending the message, you should clear the text input
   * field.
   */
  @FXML
  public void doSendMessage() {
    // TODO
    String Message = inputArea.getText();
    Message message = new Message(System.currentTimeMillis(), username, sendTo, Message);
    personChatList.getItems().add(message); // 这里是引用类，可以直接用
    try {
      client.sendMessage(message);
      client.history.writePerson(message, username, sendTo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    inputArea.setText("");
    System.out.println(Message);
  }

  public void updateMsg(Message message) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        personChatList.getItems().add(message);
        try {
          client.history.writePerson(message, message.getSentBy(), message.getSendTo());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
