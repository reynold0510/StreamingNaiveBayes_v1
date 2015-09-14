package hw1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class NBTrain {
	
	public static void main(String[] args) throws IOException {
		int bufferSize = 10; // set up the buffer size
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in), bufferSize);
        BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(System.out));
        NBRead(bfr, bfw);
        bfr.close();
        bfw.close();
	}
	
	private static void NBRead(BufferedReader bfr, BufferedWriter bfw) throws IOException {
		int hashSize = 1000000; // set up the hash table limit
        String aLine = bfr.readLine();
        HashMap<String, Integer> hash = new HashMap<String, Integer>();
        while (aLine != null) {
            if (hash.size() > hashSize) {
            	for (String key : hash.keySet()) {
            		bfw.write(key + "\t" + hash.get(key) + "\n");
            	}
            	hash.clear();
            }
            String[] strLine = aLine.split("\t+");            
            if (strLine.length != 2) {
            	System.out.println("Label document split is wrong!");
            }
            Vector<String> labels = tokenizeLabel(strLine[0]);
            Vector<String> tokens = tokenizeDoc(strLine[1]);
        	NBCounter(labels, tokens, hash);
            aLine = bfr.readLine();
        }
        // iterate through the hash map last time
    	for (String key : hash.keySet()) {
    		bfw.write(key + "\t" + hash.get(key) + "\n");
    	}  	
	}
	
	private static Vector<String> tokenizeDoc(String cur_doc) {
		String[] words = cur_doc.split("\\s+");
		Vector<String> tokens = new Vector<String>();
		for (int i = 0; i < words.length; i++) {
		words[i] = words[i].replaceAll("\\W", "");
		    if (words[i].length() > 0) {
		    tokens.add(words[i]);
		    }
		}
		return tokens;
	}
	
	private static Vector<String> tokenizeLabel(String cur_doc) {
		String[] words = cur_doc.split(",");
		Vector<String> tokens = new Vector<String>();
		for (int i = 0; i < words.length; i++) {
		words[i] = words[i].replaceAll("\\W", "");
		    if (words[i].length() > 0) {
		    tokens.add(words[i]);
		    }
		}
		return tokens;
	}
	
	private static void NBCounter(Vector<String> labels, Vector<String> tokens, HashMap<String, Integer> hash) {
		for (int i = 0; i < labels.size(); i++) {
			if (hash.get("Y=*") == null) {
				hash.put("Y=*", 1);
			} else {
				hash.put("Y=*", hash.get("Y=*")+1);
			}
			String temp1 = "Y=" + labels.get(i);
			if (hash.get(temp1) == null) {
				hash.put(temp1, 1);
			} else {
				hash.put(temp1, hash.get(temp1)+1);
			}
			for (int j = 1; j < tokens.size(); j++) {
				String temp2 = "Y=" + labels.get(i) + "," + "X=*";
				if (hash.get(temp2) == null) {
					hash.put(temp2, 1);
				} else {
					hash.put(temp2, hash.get(temp2)+1);
				}	
				String temp3 = "Y=" + labels.get(i) + "," + "X=" + tokens.get(j);
				if (hash.get(temp3) == null) {
					hash.put(temp3, 1);
				} else {
					hash.put(temp3, hash.get(temp3)+1);
				}
			}
		}
	}
}
