package hw1;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;


public class NBTest {
    public static void main(String[] args) throws IOException{
		// Build Test Vocabulary 
    	int bufferSize = 10; // set up the buffer size
    	String testFile = args[0];
		BufferedReader bfr = new BufferedReader(new FileReader(testFile), bufferSize);
		HashSet<String> testVoc = new HashSet<String>();
		NBTestRead(bfr, testVoc);
		bfr.close();
		// Training Parameter Hash Table
		BufferedReader bfr2 = new BufferedReader(new InputStreamReader(System.in));
		HashMap<String, Integer> trainHash = new HashMap<String, Integer>();
		Vector<String> labels = new Vector<String>();
		int LP = NBTrainHash(testVoc, bfr2, trainHash, labels);
		bfr2.close();
		// Predict
		BufferedReader bfr3 = new BufferedReader(new FileReader(testFile), bufferSize);
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(System.out));
		NBPredict(bfr3, trainHash, bfw, labels, LP);
		bfr3.close();
		bfw.close();
    }
    
    private static void NBTestRead(BufferedReader bfr, HashSet<String> testVoc) throws IOException {
    	String aLine = bfr.readLine();
		while (aLine != null){	
			String[] strLine = aLine.split("\t+");
			Vector<String> tokens = tokenizeDoc(strLine[1]);
			for (int i = 0; i< tokens.size(); i++){
				if (!testVoc.contains(tokens.get(i))){
					testVoc.add(tokens.get(i));
				} 
			}
			aLine = bfr.readLine();
		}        
    }

    private static int NBTrainHash(HashSet<String> testVoc, BufferedReader bfr2, HashMap<String, Integer> trainHash
    		                        , Vector<String> labels) throws IOException {
    	int LP = 0;
    	int numYX = 0;
    	int numYStar = 0;
    	String aLine = bfr2.readLine();		
		while (aLine != null){
			String keyWord = aLine.split("\t")[0];
			int trainCount = Integer.parseInt(aLine.split("\t")[1]);			
			if(keyWord.indexOf(",") >= 0){
				numYX++;
			    if(testVoc.contains(keyWord.split("=")[2])) {
				    trainHash.put(keyWord, trainCount);				
			    } else if (keyWord.split("=")[2].equals("*")) {
			    	trainHash.put(keyWord, trainCount);
			    }
			} else {
				trainHash.put(keyWord, trainCount);
				if (!(keyWord.split("=")[1].equals("*"))) {
					numYStar++;
				    labels.add(keyWord);
				}
			}
			aLine = bfr2.readLine();
		}
		LP = numYX - numYStar;
		return LP;
    }		

	private static void NBPredict(BufferedReader bfr3, HashMap<String, Integer> trainHash
			                      , BufferedWriter bfw, Vector<String> labels, int LP) throws IOException {	
		String aLine = bfr3.readLine();
		while(aLine !=null){
			String aDoc = aLine.split("\t")[1];
			Vector<String> tokens = tokenizeDoc(aDoc);
			double threshold = -0.9 * Double.MAX_VALUE;
			double score = -0.9 * Double.MAX_VALUE;
			String predLabel = null;
			int docCount = trainHash.get("Y=*");
			int yLabelCount = 0;
			int yWordCount = 0;
			for (int i = 0; i < labels.size(); i++){
				yLabelCount = trainHash.get(labels.get(i));
				yWordCount = trainHash.get(labels.get(i) + ",X=*");
				score = Math.log((yLabelCount + 1.0) / (docCount + labels.size()));
				for (int j = 0; j < tokens.size(); j++){
					String key = labels.get(i) + ",X=" + tokens.get(j);
					if (trainHash.get(key) != null){
						score = score + Math.log((trainHash.get(key) + 1.0) / (yWordCount + LP));
					}else{
						score = score + Math.log(1.0 / (yWordCount + LP));
					}
				}
				if (score > threshold) {
					threshold = score;
					predLabel = labels.get(i).split("=")[1];
				}
			}
			String result = predLabel + "\t" + String.format("%.4f", score) + "\n";			
			bfw.write(result);
			aLine = bfr3.readLine();
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
}
	
			

