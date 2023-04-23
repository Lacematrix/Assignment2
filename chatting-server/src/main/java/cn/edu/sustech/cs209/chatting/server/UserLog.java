package cn.edu.sustech.cs209.chatting.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserLog {

  static String fileLocation = "chatting-server\\src\\main\\log\\nameAndPass.txt";

  public static Map<String, String> read() throws IOException {
    Map<String, String> nameAndPass = new HashMap<>();
    File file = new File(fileLocation);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileInputStream fin = new FileInputStream(fileLocation);
    InputStreamReader reader = new InputStreamReader(fin);
    BufferedReader buffReader = new BufferedReader(reader);
    String s = "";
    while ((s = buffReader.readLine()) != null) {
      String[] np = s.split(" ");
      nameAndPass.put(np[0], np[1]);
    }
    return nameAndPass;
  }

  public static void write(String[] nameAndPass) throws IOException {
    File file = new File(fileLocation);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileWriter fileWriter = new FileWriter(fileLocation, true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    String s = nameAndPass[0] + " " + nameAndPass[1] + "\n";
    bufferedWriter.write(s);
    bufferedWriter.close();
    System.out.println("write");
  }
}
