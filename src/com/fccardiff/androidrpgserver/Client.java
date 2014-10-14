package com.fccardiff.androidrpgserver;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
        implements Runnable
{
    private Socket socket;
    String userId;
    String message;
    String password;
    public Client(Socket s)
    {
        this.socket = s;
    }
    public void run() {
        try {
            Scanner in = new Scanner(this.socket.getInputStream());
            DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            while (true)
                if (in.hasNext()) {
                    String input = in.nextLine();
                    System.out.println(input);
                    out.println(input);
                    out.flush();
                    Byte messageType = dataInputStream.readByte();
                    // TODO: Check UserID via checkUserId() method. Then checkPermissions();.
                    switch (messageType){
                        case 1:
                            System.out.println("UserId was determined to be: " + dataInputStream.readUTF());
                            userId = dataInputStream.readUTF();
                            break;
                        case 2:
                            password = dataInputStream.readUTF();
                            System.out.println("REMOVE THIS VERY SOON!!!! Password is: " + password);
                            break;
                        case 3:
                            System.out.print("Message was determined to be: " + dataInputStream.readUTF());
                            message = dataInputStream.readUTF();
                            break;
                        case 4:
                            if(dataInputStream.readUTF().equalsIgnoreCase("createPermissions")){
                                if(SQLConnection.checkUserPermissions(userId) == true){
                                    break;
                                }
                                else{
                                    SQLConnection.createPermissions(userId, password);
                                    break;
                                }
                            }
                    }
                    if(message instanceof String){
                        // TODO Handle Chat Tagging, test password functionality
                    }
                    System.out.println(userId + ": " + message);
                    out.println(userId + ": " + message);
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}