package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class History {

  String username;

  String fileLocation = "src\\main\\log";

  public History(String username){
    this.username = username;
  }

  public void writePerson(Message message,String from, String to) throws IOException {
    String otherName = username.equals(from) ? to : from;
    File file = new File(fileLocation + "\\" + username + "\\" + otherName + ".txt");
    if (!file.exists()) {
      file.createNewFile();
    }
    FileOutputStream writerStream = new FileOutputStream(file, true);
    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(writerStream, StandardCharsets.UTF_8));
    if (message.getModel() != 6) {
      String msg = message.getData();
      msg = msg.replace("\n", "%next");
      bufferedWriter.write(message.getTimestamp() + "##" + from + "##" + msg + "##" + to + "\n");
      bufferedWriter.close();
    }else {

    }
  }

  public List<Message> read(String otherName) throws IOException {
    List<Message> messages = new ArrayList<>();
    File file = new File(fileLocation + "\\" + username + "\\" + otherName + ".txt");
    if (!file.exists()){
      return messages;
    }
    FileInputStream fin = new FileInputStream(file);
    InputStreamReader reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
    BufferedReader buffReader = new BufferedReader(reader);
    String s = "";
    while ((s = buffReader.readLine()) != null){
      String[] temp = s.split("##");
      Long time = Long.valueOf(temp[0]);
      String msgChange = temp[2].replace("%next", "\n");
      Message msg = new Message(time, temp[1], temp[3], msgChange);
      messages.add(msg);
    }
    return messages;
  }

  public List<String> readFriend() throws IOException {
    List<String> friend = new ArrayList<>();
    File file = new File(fileLocation + "\\" + username + "\\" + "friend.txt");
    File fileParentStep = file.getParentFile();
    if (!fileParentStep.exists()) {
      fileParentStep.mkdirs();
    }
    if (!file.exists()){
      file.createNewFile();
      System.out.println("createOK");
    }
    FileInputStream fin = new FileInputStream(file.getPath());
    InputStreamReader reader = new InputStreamReader(fin);
    BufferedReader buffReader = new BufferedReader(reader);
    String s = "";
    while ((s = buffReader.readLine()) != null){
      friend.add(s);
    }
    return friend;
  }

  public void writeFrenid(String name) throws IOException{
    File file = new File(fileLocation + "\\" + username + "\\" + "friend.txt");
    FileWriter fileWriter = new FileWriter(file, true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    String s = name + "\n";
    bufferedWriter.write(s);
    bufferedWriter.close();
    System.out.println("write");
  }

}
