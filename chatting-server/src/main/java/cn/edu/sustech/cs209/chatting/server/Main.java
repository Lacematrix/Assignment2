package cn.edu.sustech.cs209.chatting.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    System.out.println("Starting server");
    ServerSocket serverSocket = new ServerSocket(8999);
    while (true) {
      System.out.println("wait");
      Socket newUser = serverSocket.accept();
      System.out.println("OK");
      ServerUser serverUser = new ServerUser(newUser);
      if (!serverUser.init()) {
        serverUser = null;
        System.gc();
        continue;
      }
      ServerUser.setUserList();
      serverUser.start();
    }
  }
}
