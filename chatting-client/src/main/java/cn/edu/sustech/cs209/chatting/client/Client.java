package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Receiver receiver;

    private Sender sender;

    ObjectOutputStream write;

    ObjectInputStream read;

    private Socket socket;

    String username;

    String[] userList;

    private Controller controller;


    public Client(String username, Controller controller) throws IOException{
        this.socket = new Socket("localhost", 8999);
        this.controller = controller;
        this.username = username;
    }

    public boolean init() throws IOException, ClassNotFoundException {
        write = new ObjectOutputStream(socket.getOutputStream());
        read = new ObjectInputStream(socket.getInputStream());
        write.writeObject(new Message(username));
        Message message = (Message) read.readObject();
        if (message.getData().equals("invalid")){
            return false;
        }
        receiver = new Receiver(this);
        sender = new Sender(this);
        receiver.start();
        return true;
    }

    public void sendMessage(Message message) throws IOException {
        sender.send(message);
    }

    public Controller getController() {
        return controller;
    }


    public void updateUserList(){//有问题
        ObservableList<String> strList = FXCollections.observableArrayList();
        strList.addAll(userList);
        controller.update(strList);
    }

    public void updateGroupUserList(String[] userList){
        if (controller.groupRoom != null){
            controller.groupRoom.updateUserList(userList);
        }
    }

    public void createGroup(Message message){
        controller.createGroupRoom(message);
    }

    public void updateGroupMessage(Message message){
        controller.updateGroupMessage(message);
    }

    public void updateMessagePerson(Message msg){
        controller.updateMsg(msg);
    }

    public void serverExit(){
        controller.serverExit();
    }

}
