package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class History {

  String username;

  String fileLocation = "chatting-client\\src\\main\\log";

  public History(String username){
    this.username = username;
  }

  public void writePerson(Message message,String from, String to) throws IOException {
    String otherName = username.equals(from) ? to : from;
    File file = new File(fileLocation + "\\" + username + "\\" + otherName + ".txt");
    file.createNewFile();
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write(message.getTimestamp() + "##" + from + message.getData() + "##" + to);
  }

  public void read(String otherName){
    File file = new File(fileLocation + "\\" + username + "\\" + otherName + ".txt");

  }

  public String[] preview(){ return null;}

}
