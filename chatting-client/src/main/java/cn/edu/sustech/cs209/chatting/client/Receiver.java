package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Receiver extends Thread {

  Client client;


  public Receiver(Client client) {
    this.client = client;
  }

  @Override
  public void run() {
    while (true) {
      Message message = null;
      try {
        message = (Message) client.read.readObject();
        if (message.getModel() == 0) {
          String[] userList = message.getUserList();
          List<String> user = Arrays.asList(userList);
          List<String> userReal = new ArrayList<>(user);
          userReal.remove(client.getController().username);
          userList = userReal.toArray(new String[]{});
          client.userList = userList;
          client.updateUserList();
        } else if (message.getModel() == 1) {
          // TODO:show message;
          client.updateMessagePerson(message);
        } else if (message.getModel() == 2) {
          if (message.getData().equals("GroupRequest")) {
            client.createGroup(message);
          } else {
            client.updateGroupUserList(message.getUserList());
          }
        } else if (message.getModel() == 4) {
          client.updateGroupMessage(message);
        }
      } catch (SocketException e) {
        client.serverExit();
        e.printStackTrace();
        break;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
