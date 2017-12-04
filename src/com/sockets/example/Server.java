package com.sockets.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Andrew on 12/3/17.
 */
public class Server {
    //this is shitty, cheap code. These shouldn't be member vars
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String incomingMessage;

    public ServerSocket getProviderSocket() {
        return providerSocket;
    }

    public void setProviderSocket(ServerSocket providerSocket) {
        this.providerSocket = providerSocket;
    }

    public Socket getConnection() {
        return connection;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public String getIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(String incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    void run()
    {
        try{
            // creating a server socket
            providerSocket = new ServerSocket(2004, 10);
            // Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            // get Input and Output streams (output is where we'll send outgoing data to the client, input is where
            //we'll take data in. Always be sure to flush the stream after opening and after sending
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");
            do{
                try{
                    //read message in from the client, we'll have sent a message to the client already,
                    //so the client knows to send a response
                    incomingMessage = (String)in.readObject();
                    //now print the incoming message
                    System.out.println("client>" + incomingMessage);


                    //conditionally terminate
                    if (incomingMessage.equals("bye"))
                        sendMessage("bye");
                    else
                        sendMessage("Message recieved \""+ incomingMessage +"\"");
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!incomingMessage.equals("bye"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();

        } finally{
            // close connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Utility function to send a message to the client
     * @param msg
     */
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

}
