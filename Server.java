import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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
import java.util.List;
import java.util.Map;

import org.omg.CORBA.portable.OutputStream;


public class ACNServer implements Runnable {
	 
	 private ServerSocket serverSocket = null;
	 private Socket socket = null;	 
	 private ObjectInputStream inStream = null;
	 
	 public ACNServer() {

	 }


	public void run() {
		
		try {
			serverSocket = new ServerSocket(4444); // create socket
			socket = serverSocket.accept(); // accept request from client
			System.out.println("Connecting..");
			try(DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream())) {
				try (DataInputStream dIn = new DataInputStream(socket.getInputStream())) {
		            long fileLen, downData;
		            int bufferSize = socket.getReceiveBufferSize();

		           //code to send the list of files
		            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		    	 	ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		    	 	StringBuffer s = new StringBuffer();// for checksum
		    	 	Checksum checksum=new Checksum();
		    	 	
		    	 	File sFiles = new File("C:\\Users\\Administrator\\Desktop\\ACN1\\");
		    		File[] sFileList = sFiles.listFiles();
		    		System.out.println("creating map..");
		    		Map <String,Date> fDetails = new HashMap<String, Date>();
		    		List<StringBuffer> fDetails1 = new ArrayList<StringBuffer>();		    		
		    		for(File file : sFileList)
		    		{
		    			s = checksum.calculateChecksum(file.getPath());
		    			System.out.println(file.getName());
		    			System.out.println("inside for loop..");
		    			if(file.isFile())
		    			{
		    				fDetails.put(file.getName(),new Date(file.lastModified()));
		    				fDetails1.add(s);
		    			}
		    		}
		    		oos.writeObject(fDetails); //sending details to client
		    		oos.flush(); 
		    		System.out.println("details sent");
		    		oos.writeObject(fDetails1);
		    		oos.flush(); 
		    		System.out.println("details sent2");
		    		//read the list of files
		    		
		    		List<String> fileList= new ArrayList<String>();
		    		List<Long> fileSize= new ArrayList<Long>();
		    		//get the number of files
		    		int noFiles = dIn.readInt();
		    		for(int i=0;i<noFiles;i++)
		    		{
		    			fileList.add(dIn.readUTF());
		    			fileSize.add(dIn.readLong());
		    		}
		    		

		            System.out.println(fileList.toString());
		            //done
		            for(int i = 0;i<fileList.size();i++){
		            
		            String path = "C:\\Users\\Administrator\\Desktop\\ACN1\\"+ fileList.get(i);
		            long starttime = System.currentTimeMillis();
		            System.out.println(path);
		            File myFIle = new File(path);
		            try (FileOutputStream fos = new FileOutputStream(myFIle); 
		            	BufferedOutputStream bos = new BufferedOutputStream(fos)) {
			            	fileLen = fileSize.get(i);
		                System.out.println(fileSize.get(i));
		                
		               downData = fileLen;
		                int n = 0;
		                byte[] buf = new byte[8192];
		               while (fileLen > 0 && ((n = dIn.read(buf, 0, buf.length)) != -1)) {
		                	 bos.write(buf, 0, n);//bos.write(buf);
		                 fileLen -= n;
		                  System.out.println("Remaining "+fileLen);
		                 }
	
		                bos.flush();
		                long endtime = System.currentTimeMillis();
		                System.out.println("File " + myFIle.getAbsolutePath()
		                        + " downloaded (" + downData + " bytes read) in " + (endtime - starttime) + " ms");
		            }

		            }
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch(EOFException e)
				{
					System.out.println("Not valid file");
				}catch(SocketException se){
					se.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String s[]){
		Thread t = new Thread(new ACNServer());
	     t.start();
	}
}

