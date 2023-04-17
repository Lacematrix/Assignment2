package cn.edu.sustech.cs209.chatting.server;

import cn.edu.sustech.cs209.chatting.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ServerUser extends Thread{

    static Map<String,ServerUser> userList = new HashMap<>();

    static List<String> user = new ArrayList<>();

    private String username;

    private List<String> Group;

    ObjectInputStream msgIn;
    ObjectOutputStream msgOut;


    public ServerUser(Socket socket) throws IOException {
        msgIn = new ObjectInputStream(socket.getInputStream());
        msgOut = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean init() throws IOException, ClassNotFoundException {
        Message usernameInit = (Message) msgIn.readObject();
        this.username = usernameInit.getData();
        if (ServerUser.user.contains(username)){
            Message message = new Message("invalid");
            msgOut.writeObject(message);
            return false;
        }else {
            Message message = new Message("OK");
            msgOut.writeObject(message);
            ServerUser.user.add(username);
            ServerUser.userList.put(username,this);
            ServerUser.setUserList();
            return true;
        }
    }

    public static void setUserList() {
        String[] userSend = ServerUser.user.toArray(new String[]{});
        Message message = new Message(userSend);
        ServerUser.userList.forEach((s, serverUser) -> {
            try {
                serverUser.msgOut.writeObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        try {
            while (true){
                Message message = (Message) msgIn.readObject();
                if (message.getModel() == 1) {
                    String sendTo = message.getSendTo();
                    ServerUser send = userList.get(sendTo);
                    send.msgOut.writeObject(message);
                }else if (message.getModel() == 2){
                    List<String> GroupUser = new ArrayList<>(Arrays.asList(message.getUserList()));
                    GroupUser.forEach(s -> {
                        ServerUser serverUser = userList.get(s);
                        serverUser.Group = GroupUser;
                        try {
                            serverUser.msgOut.writeObject(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }else if (message.getModel() == 4){
                    Group.forEach(s -> {
                        ServerUser serverUser = userList.get(s);
                        try {
                            serverUser.msgOut.writeObject(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
             }
        }catch (SocketException e){
            if (Group != null){
                Group.remove(username);
                String[] memberList = Group.toArray(new String[]{});
                Message message = new Message("updateList", memberList);
                Group.forEach(s -> {
                    ServerUser serverUser = userList.get(s);
                    try {
                        serverUser.msgOut.writeObject(message);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            ServerUser.userList.remove(username);
            ServerUser.user.remove(username);
            ServerUser.setUserList();
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
