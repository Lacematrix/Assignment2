package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GroupRoomController implements Initializable {

    Client client;

    @FXML
    ListView<String> GroupList;

    @FXML
    ListView<Message> ShowGroupMessage;

    @FXML
    TextArea GroupMessage;

    @FXML
    Label YourName;

    String sendBy;

    String[] memberList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ShowGroupMessage.setCellFactory(new MessageCellFactory());
    }

    public void init(Client client, String sendBy, String[] memberList){
        this.client = client;
        this.sendBy = sendBy;
        this.memberList = memberList;
        YourName.setText("Your name: " + sendBy);
    }

    public void updateUserList(String[] userList){
        memberList = userList;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<String> groupUser = FXCollections.observableArrayList();
                groupUser.addAll(memberList);
                GroupList.setItems(groupUser);
            }
        });
    }

    @FXML
    public void doSendMessage() {
        // TODO
        String Message = GroupMessage.getText();
        Message message = new Message(Message, sendBy, memberList, System.currentTimeMillis());
        try {
            client.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
        GroupMessage.setText("");
        System.out.println(Message);
    }


    public void updateMsg(Message message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ShowGroupMessage.getItems().add(message);
            }
        });
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

                    if (client.username.equals(msg.getSentBy())) {
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
}
