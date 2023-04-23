package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {

  private Receiver receiver;

  private Sender sender;

  ObjectOutputStream write;

  ObjectInputStream read;

  private Socket socket;

  String username;

  String[] nameAndPass;

  String[] userList;

  List<String> friend;

  History history;

  private Controller controller;


  public Client(String[] nameAndPass, Controller controller) throws IOException {
    this.controller = controller;
    this.username = nameAndPass[0];
    this.nameAndPass = nameAndPass;
  }

  public Client() throws IOException, ClassNotFoundException {
    this.socket = new Socket("localhost", 8999);
  }

  public int init() throws IOException, ClassNotFoundException {
    history = new History(username);
    friend = history.readFriend();
    this.socket = new Socket("localhost", 8999);
    write = new ObjectOutputStream(socket.getOutputStream());
    read = new ObjectInputStream(socket.getInputStream());
    write.writeObject(new Message(nameAndPass));
    Message message = (Message) read.readObject();
    if (message.getData().equals("invalid")) {
      return 1;
    } else if (message.getData().equals("PassWordFalse")) {
      return 2;
    } else if (message.getData().equals("noUser")) {
      return 3;
    }
    receiver = new Receiver(this);
    sender = new Sender(this);
    receiver.start();
    return 0;
  }

  public boolean init(String[] nameAndPass) throws IOException, ClassNotFoundException {
    write = new ObjectOutputStream(socket.getOutputStream());
    read = new ObjectInputStream(socket.getInputStream());
    write.writeObject(new Message(nameAndPass, true));
    Message message = (Message) read.readObject();
    return message.isReply();
  }

  public void sendMessage(Message message) throws IOException {
    sender.send(message);
  }

  public Controller getController() {
    return controller;
  }


  public void updateUserList() {//有问题
    ObservableList<String> strList = FXCollections.observableArrayList();
    strList.addAll(userList);
    controller.update(strList);
  }

  public void updateGroupUserList(String[] userList) {
    if (controller.groupRoom != null) {
      controller.groupRoom.updateUserList(userList);
    }
  }

  public void createGroup(Message message) {
    controller.createGroupRoom(message);
  }

  public void updateGroupMessage(Message message) {
    controller.updateGroupMessage(message);
  }

  public void updateMessagePerson(Message msg) {
    controller.updateMsg(msg);
  }

  public void serverExit() {
    controller.serverExit();
  }

  public void updateFriend() {

  }

}
