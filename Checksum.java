
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author akash
 */
public class Checksum {

    /**
     * @param args the command line arguments
     * @return 
     */
    public StringBuffer calculateChecksum(String datafile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        // TODO code application logic here
        
       // String datafile = "C:\\Users\\akash\\Desktop\\rucha.txt";

    MessageDigest md = MessageDigest.getInstance("SHA1");
    FileInputStream fis = new FileInputStream(datafile);
    byte[] dataBytes = new byte[1024];
    
    int nread = 0; 
    
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    };

    byte[] mdbytes = md.digest();
   
    //convert the byte to hex format
    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < mdbytes.length; i++) {
    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    
    //System.out.println("Digest(in hex format):: " + sb.toString());
    return sb;
    
  }
}
    

