package com.sockets.example;

/**
 * Created by Andrew on 12/3/17.
 */
public class ClientRunner {

    public static void main(String[] args){
        Client client = new Client();
        client.setServerMessage("Fuck you");
        client.run();
    }
}
