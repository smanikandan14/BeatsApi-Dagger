package mani.beatsalbumsearch.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	public String readFromAssetsFolder(String fileName) {
	    StringBuilder returnString = new StringBuilder();
	    InputStream fileStream = null;
	    InputStreamReader isr = null;
	    BufferedReader input = null;
	    try {
            fileStream = this.getClass().getClassLoader()
                    .getResourceAsStream("assets/" + fileName);
	        isr = new InputStreamReader(fileStream);
	        input = new BufferedReader(isr);
	        String line = "";
	        while ((line = input.readLine()) != null) {
	            returnString.append(line);
	        }
	    } catch (Exception e) {
	        e.getMessage();
	        e.printStackTrace();
	    } finally {
	        try {
	            if (isr != null)
	                isr.close();
	            if (fileStream != null)
                    fileStream.close();
	            if (input != null)
	                input.close();
	        } catch (Exception e2) {
	            e2.getMessage();
	        }
	    }
	    return returnString.toString();
	}
}
