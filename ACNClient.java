import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ACNClient {
	
	public final static int SOCKET_PORT = 4444; 
	 String SERVER = "52.10.255.201"; 
	 
	 public static void main(String[] args) throws ClassNotFoundException, NoSuchAlgorithmException {
	     new ACNClient();
	 }
	 
	 public ACNClient() throws ClassNotFoundException, NoSuchAlgorithmException{
		 try (Socket sock = new Socket(SERVER, SOCKET_PORT)) {  //creating sockets 
			  System.out.println("Connecting...");  
	     try {
	    	 
	    DataInputStream dIn = new DataInputStream(sock.getInputStream());
	    DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());

	    ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
	 	ObjectInputStream ois = new ObjectInputStream(sock.getInputStream()); 
	 	
	 	//code to get the list of files and their checksum from server
	 	
	 	File cFiles = new File("C:\\Users\\Akash\\Desktop\\ACNDemo\\"); // path of the folder at desktop client 
		File[] cFileList = cFiles.listFiles();		
		System.out.println(cFileList.length);
		
		
		
		Checksum checksum = new Checksum();
		List<String> filesToBeSent= new ArrayList<String>();
		
		
		Map <String,Date> cDetails = new HashMap<String,Date>();
		cDetails =(Map<String, Date>) ois.readObject();//new HashMap<String,Map<Long,StringBuffer> >();
		System.out.println("got details");
		List<StringBuffer> cDetails1 = new ArrayList<StringBuffer>();
		cDetails1 = (List<StringBuffer>) ois.readObject(); //get the list of files from the server
		System.out.println("got details2");
		StringBuffer s = new StringBuffer();
		Set<String> keyMap = new HashSet<String>();
		keyMap=cDetails.keySet();
		int i=0;
		for(File file : cFileList){
								
				System.out.println("inside for");
				if(keyMap.contains(file.getName())){
					//find if checksum is same or the client file is edited after sent to server
					System.out.println(keyMap.toString());
					System.out.println(cDetails.toString());
					System.out.println(cDetails1.toString());
					
					Date d = new Date(file.lastModified());
					if(checksum.calculateChecksum(file.getPath()) != cDetails1.get(i)){ //if checksum is not same add to files to sending list
						filesToBeSent.add(file.getName());	
					}else if ( d.compareTo((Date)cDetails.get(file.getPath()))> 0){ //if file is updated at client then add it to sending list
						filesToBeSent.add(file.getName());
					}	
					}else{
						filesToBeSent.add(file.getName());
					}
			i++;
		}
		System.out.println(filesToBeSent.toString());
		
		outToClient.writeInt(filesToBeSent.size());
		for (String strFile : filesToBeSent)
		{
			outToClient.writeUTF(strFile);
			outToClient.writeLong(new File("C:\\Users\\Akash\\Desktop\\ACNDemo\\"+strFile).length());
		}
		
		
	    for(String f : filesToBeSent){	 
		    System.out.println("Connected");
		    String dynamicPath = "C:\\Users\\Akash\\Desktop\\ACNDemo\\"+f; 
		    System.out.println(dynamicPath);
		    File myFile = new File(dynamicPath);
		    long flength = myFile.length();
		    
		    FileInputStream fis;
		    BufferedInputStream bis;
		    byte[] mybytearray = new byte[8192];
		    fis = new FileInputStream(myFile);
		    bis = new BufferedInputStream(fis);
		    int theByte = 0;
		    System.out.println("Sending " + myFile.getAbsolutePath() + "(" + myFile.length() + " bytes)");
		    
		    while ((theByte = bis.read()) != -1) {
		        outToClient.write(theByte);
		        System.out.println(theByte);
		        }
	    }
	    sock.close();

	
	}catch (SocketException se) {
		se.printStackTrace();
	} 
		 }catch (IOException e) {
	    e.printStackTrace();
	}	 
}


}
