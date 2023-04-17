package cn.edu.sustech.cs209.chatting.client;

import cn.edu.sustech.cs209.chatting.common.Message;

import java.io.IOException;

public class Sender{

    Client client;

    public Sender(Client client) {
        this.client = client;
    }

    public void send(Message message) throws IOException {
        client.write.writeObject(message);
    }
}
