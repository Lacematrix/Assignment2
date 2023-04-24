package cn.edu.sustech.cs209.chatting.common;

import java.io.Serializable;

public class Message implements Serializable {

  private Long timestamp;

  private String sentBy;

  private String sendTo;

  private String data;

  private String[] userList;

  private String[] passAndWord;

  private byte[] file;

  private String fileType;

  private boolean reply;
  private final int model; //0表示用户列表,1表示个人信息,2表示群列表,3系统信息,4表示群信息,5注册信息,6表示文件

  public Message(Long timestamp, String sentBy, String sendTo, String data) {
    this.timestamp = timestamp;
    this.sentBy = sentBy;
    this.sendTo = sendTo;
    this.data = data;
    model = 1;
  }

  public Message(String[] userList) {
    this.userList = userList;
    model = 0;
  }

  public Message(String data) {
    this.data = data;
    model = 3;
  }

  public Message(String data, String[] list) {
    this.data = data;
    this.userList = list;
    model = 2;
  }

  public Message(String data, String sentBy, String[] list, Long timestamp) {
    this.data = data;
    this.sentBy = sentBy;
    this.userList = list;
    this.timestamp = timestamp;
    model = 4;
  }

  public Message(String[] passAndWord, boolean reply) {
    this.passAndWord = passAndWord;
    this.reply = reply;
    model = 5;
  }

  public Message(byte[] fileBytes, String sendBy, String fileType, String sendTo){
    this.file = fileBytes;
    this.sentBy = sendBy;
    this.sendTo = sendTo;
    this.fileType = fileType;
    model = 6;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public String getSentBy() {
    return sentBy;
  }

  public String getSendTo() {
    return sendTo;
  }

  public String getData() {
    return data;
  }

  public String[] getUserList() {
    return userList;
  }

  public int getModel() {
    return model;
  }

  public String[] getPassAndWord() {
    return passAndWord;
  }

  public boolean isReply() {
    return reply;
  }

  public byte[] getFile() {
    return file;
  }

  public String getFileType() {
    return fileType;
  }
}
