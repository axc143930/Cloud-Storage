import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.omg.CORBA.portable.OutputStream;

public class Server implements Runnable {

 private ServerSocket serverSocket = null;
 private Socket socket = null;
 private ObjectInputStream inStream = null;

 public Server() {

 }

 @Override
 public void run() {

     try {
    serverSocket = new ServerSocket(4445);
    socket = serverSocket.accept();
    DataInputStream dIn = new DataInputStream(socket.getInputStream());
   // OutputStream os = socket.getOutputStream();
    DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

    System.out.println("Connected");
    File myFile = new File("C:\\Users\\Akash\\Desktop\\image.jpg");
    long flength = myFile.length();
    System.out.println("File Length"+flength);
    outToClient.writeLong(flength);
    FileInputStream fis;
    BufferedInputStream bis;
    byte[] mybytearray = new byte[8192];
    fis = new FileInputStream(myFile);
    bis = new BufferedInputStream(fis);
    int theByte = 0;
    System.out.println("Sending " + myFile.getAbsolutePath() + "(" + myFile.length() + " bytes)");
       while ((theByte = bis.read()) != -1) {
     outToClient.write(theByte);
     // bos.flush();
     }
    /*int count;
    BufferedOutputStream bos= new BufferedOutputStream(os);
    while ((count = bis.read(mybytearray))>0) {
        bos.write(mybytearray, 0, count);
    }*/

    bis.close();
    socket.close();

} catch (SocketException se) {

    System.exit(0);
} catch (IOException e) {
    e.printStackTrace();
}
 }

 public static void main(String[] args) {
     Thread t = new Thread(new Server());
     t.start();
 }
 }
