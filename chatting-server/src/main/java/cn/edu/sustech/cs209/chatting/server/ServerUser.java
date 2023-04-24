package cn.edu.sustech.cs209.chatting.server;

import cn.edu.sustech.cs209.chatting.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ServerUser extends Thread {

  static Map<String, ServerUser> userList = new HashMap<>();

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
    if (usernameInit.getModel() != 5) {
      this.username = usernameInit.getUserList()[0];
      Map<String, String> nameAndPass = UserLog.read();
      if (ServerUser.user.contains(username)) {
        Message message = new Message("invalid");
        msgOut.writeObject(message);
        return false;
      } else {
        if (nameAndPass.containsKey(username)) {
          if (nameAndPass.get(username).equals(usernameInit.getUserList()[1])) {
            Message message = new Message("OK");
            msgOut.writeObject(message);
            ServerUser.user.add(username);
            ServerUser.userList.put(username, this);
            ServerUser.setUserList();
            return true;
          } else {
            Message message = new Message("PassWordFalse");
            msgOut.writeObject(message);
            return false;
          }
        } else {
          Message message = new Message("noUser");
          msgOut.writeObject(message);
          return false;
        }
      }
    } else {
      Map<String, String> user = UserLog.read();
      String[] nameAndPass = usernameInit.getPassAndWord();
      if (user.containsKey(nameAndPass[0])) {
        msgOut.writeObject(new Message(new String[0], false));
        return false;
      } else {
        UserLog.write(nameAndPass);
        msgOut.writeObject(new Message(new String[0], true));
        return false;
      }
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
      while (true) {
        Message message = (Message) msgIn.readObject();
        if (message.getModel() == 1 || message.getModel() ==6) {
          String sendTo = message.getSendTo();
          ServerUser send = ServerUser.userList.get(sendTo);
          send.msgOut.writeObject(message);
        } else if (message.getModel() == 2) {
          List<String> GroupUser = new ArrayList<>(Arrays.asList(message.getUserList()));
          GroupUser.forEach(s -> {
            ServerUser serverUser = ServerUser.userList.get(s);
            serverUser.Group = GroupUser;
            try {
              serverUser.msgOut.writeObject(message);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
        } else if (message.getModel() == 4) {
          Group.forEach(s -> {
            ServerUser serverUser = ServerUser.userList.get(s);
            try {
              serverUser.msgOut.writeObject(message);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
        }
      }
    } catch (SocketException e) {
      if (Group != null) {
        Group.remove(username);
        String[] memberList = Group.toArray(new String[]{});
        Message message = new Message("updateList", memberList);
        Group.forEach(s -> {
          ServerUser serverUser = ServerUser.userList.get(s);
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
      System.out.println("Client: " + username + " out");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
