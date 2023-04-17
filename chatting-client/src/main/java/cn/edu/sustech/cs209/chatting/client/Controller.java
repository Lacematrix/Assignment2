package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    ListView<Message> chatContentList;

    @FXML
    ListView<String> chatList;

    @FXML
    TextArea inputArea;

    @FXML
    Label userChoose;

    @FXML
    Label currentOnlineCnt;

    @FXML
    Label currentUsername;

    String username;

    Client client;

    PersonRoom personRoom;

    GroupRoom groupRoom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Login login = new Login();
        String[] nameAndPass;
        try {
            nameAndPass = login.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        Dialog<String> dialog = new TextInputDialog();
//        dialog.setTitle("Login");
//        dialog.setHeaderText(null);
//        dialog.setContentText("Username:");
//
        //Optional<String> input = dialog.showAndWait();
        System.out.println(nameAndPass[0]);
        if (!nameAndPass[0].equals("")) {
            username = nameAndPass[0];
            currentUsername.setText(username);
            inputArea.setWrapText(true);
            userChoose.setText("choose who you want to talk");
            try {
                client = new Client(username, this);
                if (!client.init()){
                    new ShowDialog("there is a user with the same name among the currently logged-in users");
                    Platform.exit();
                }
                currentOnlineCnt.setText("OK");
            } catch (Exception e) {
                new ShowDialog("server not be available");
                e.printStackTrace();
                currentOnlineCnt.setText("LOST");
            }
        } else {
            System.out.println("Invalid username " + nameAndPass[0] + ", exiting");
            ShowDialog showDialog = new ShowDialog("Invalid username " + nameAndPass[0] + ", exiting");
            Platform.exit();
        }

        chatContentList.setCellFactory(new MessageCellFactory());
    }

    @FXML
    public void createPrivateChat() {//不能选自己
        if (client != null) {
            Stage stage = new Stage();
            ComboBox<String> userSel = new ComboBox<>();
            // FIXME: get the user list from server, the current user's name should be filtered out
            userSel.getItems().addAll(client.userList);
            Button okBtn = new Button("OK");
            okBtn.setOnAction(e -> {
                String s = userSel.getSelectionModel().getSelectedItem();
                if (s != null) {
                    stage.close();
                    if (personRoom == null) {
                        personRoom = new PersonRoom(client, username, s, this);
                        try {
                            personRoom.showWindow();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        personRoom.stage.toFront();
                    }
                }
            });

            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(20, 20, 20, 20));
            box.getChildren().addAll(userSel, okBtn);
            stage.setScene(new Scene(box));
            stage.showAndWait();

            // TODO: if the current user already chatted with the selected user, just open the chat with that user
            // TODO: otherwise, create a new chat item in the left panel, the title should be the selected user's name
        }else {
            new ShowDialog("You are lost, please try to notify SERVER ADMINISTRATOR and restart your client.");
        }
    }

    public void updateMsg(Message message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (personRoom == null) {
                    personRoom = new PersonRoom(client, username, message.getSentBy(), client.getController());
                    try {
                        personRoom.showWindow();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    personRoom.getPersonController().updateMsg(message);
                }else if (personRoom.sendTo.equals(message.getSentBy())){
                    personRoom.stage.toFront();
                    personRoom.getPersonController().updateMsg(message);
                }
            }
        });
    }

    public void serverExit(){
        String s = "Server gets some WRONGS! Please exit and try again";
        client = null;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new ShowDialog(s);
                personRoom.stage.close();
                personRoom = null;
                currentOnlineCnt.setText("LOST");
                chatList.setItems(null);
            }
        });
    }

    /**
     * A new dialog should contain a multi-select list, showing all user's name.
     * You can select several users that will be joined in the group chat, including yourself.
     * <p>
     * The naming rule for group chats is similar to WeChat:
     * If there are > 3 users: display the first three usernames, sorted in lexicographic order, then use ellipsis with the number of users, for example:
     * UserA, UserB, UserC... (10)
     * If there are <= 3 users: do not display the ellipsis, for example:
     * UserA, UserB (2)
     */
    @FXML
    public void createGroupChat() {
        if (client != null){
            Stage stage = new Stage();
            List<String> list = new ArrayList<>();
            ListView<CheckBox> listView = new ListView<>();
            ObservableList<CheckBox> checkBoxes = FXCollections.observableArrayList();
            for (int i = 0; i < client.userList.length; i++) {
                CheckBox checkBox = new CheckBox(client.userList[i]);
                checkBoxes.add(checkBox);
            }
            listView.setItems(checkBoxes);
            Button okBtn = new Button("OK");
            okBtn.setOnAction(actionEvent -> {
                listView.getItems().forEach(checkBox -> {
                    if (checkBox.isSelected()){
                        list.add(checkBox.getText());
                    }
                });
                if (list.size() != 0) {
                    list.add(username);
                    String[] groupList = list.toArray(new String[]{});
                    try {
                        client.sendMessage(new Message("GroupRequest", groupList));
                    } catch (IOException e) {
                        new ShowDialog("Server Lost");
                        e.printStackTrace();
                    }
                }else {
                    new ShowDialog("please select your Group member");
                }
                stage.close();
            });
            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(20, 20, 20, 20));
            box.getChildren().addAll(listView, okBtn);
            stage.setScene(new Scene(box));
            stage.setTitle("create a group");
            stage.showAndWait();
        }else {
            new ShowDialog("You are lost, please try to notify SERVER ADMINISTRATOR and restart your client.");
        }
    }

    public void createGroupRoom(Message message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                groupRoom = new GroupRoom(client, message.getUserList(), username);
                try {
                    groupRoom.showWindow();
                    groupRoom.stage.toFront();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void updateGroupMessage(Message message){
        if (groupRoom != null && Arrays.equals(groupRoom.memberList, message.getUserList())){
            groupRoom.updateMsg(message);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    groupRoom.stage.toFront();
                }
            });
        }else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    groupRoom = new GroupRoom(client,message.getUserList(),username);
                    try {
                        groupRoom.showWindow();
                        groupRoom.updateMsg(message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    groupRoom.stage.toFront();
                }
            });
        }
    }


    public void update(ObservableList<String> userList){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatList.setItems(userList);
                if (personRoom != null){
                    if (!userList.contains(personRoom.sendTo)){
                        new ShowDialog("You friend is offline");
                        personRoom.stage.close();
                    }
                }
            }
        });
    }

    /**
     * You may change the cell factory if you changed the design of {@code Message} model.
     * Hint: you may also define a cell factory for the chats displayed in the left panel, or simply override the toString method.
     */
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
}
