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
	 
	    ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
	 	ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
	 	
	 	//code to get the list of files and their checksum from server
	 	
	 	File cFiles = new File("C:\\Users\\Rucha\\Desktop\\ACNDemo\\"); // path of the folder at desktop client 
	 	//File sFiles = new File("C:\\Users\\Administrator\\Desktop\\ACN\\");
		File[] cFileList = cFiles.listFiles();		
		System.out.println(cFileList.length);
		
		Checksum checksum = new Checksum();
		List<String> filesToBeSent= new ArrayList<String>();
		//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Map <String,Date> cDetails = new HashMap<String,Date>();
		cDetails =(Map<String, Date>) ois.readObject();//new HashMap<String,Map<Long,StringBuffer> >();
		System.out.println("got details");
		List<StringBuffer> cDetails1 = new ArrayList<StringBuffer>();
		cDetails1 = (List<StringBuffer>) ois.readObject();
		System.out.println("got details2");
		//cDetails o = ois.readObject(); 
		StringBuffer s = new StringBuffer();
		Set<String> keyMap = new HashSet<String>();
		keyMap=cDetails.keySet();
		int i=0;
		for(File file : cFileList){
			//if(file.length() != 0)
			{
				//s=;// keyMap contains all the filenames from server
				
				System.out.println("inside for");
				if(keyMap.contains(file.getName())){
					//find if checksum is same or the client file is edited after sent to server
					System.out.println(keyMap.toString());
					System.out.println(cDetails.toString());
					System.out.println(cDetails1.toString());
					
					Date d = new Date(file.lastModified());
					if(checksum.calculateChecksum(file.getPath()) != cDetails1.get(i)){
						filesToBeSent.add(file.getName());	
					}else if ( d.compareTo((Date)cDetails.get(file.getPath()))> 0){
						filesToBeSent.add(file.getName());
					}	
					}else{
						filesToBeSent.add(file.getName());
					}
			}
			i++;
		}
		System.out.println(filesToBeSent.toString());
		oos.writeObject(filesToBeSent);
		oos.flush();
	    for(String f : filesToBeSent){	 
		    DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
		    System.out.println("Connected");
		    //dynamic path
		    String dynamicPath = "C:\\Users\\Rucha\\Desktop\\ACNDemo\\"+f;
		    System.out.println(dynamicPath);
		    File myFile = new File(dynamicPath);
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
		    }
	    bis.close();
	    }
	    sock.close();

	
	}catch (SocketException se) {

	    System.exit(0);
	} 
		 }catch (IOException e) {
	    e.printStackTrace();
	}	 
}


}
