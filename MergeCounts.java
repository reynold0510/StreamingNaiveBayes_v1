package hw1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MergeCounts {

	public static void main(String[] args) throws IOException {
		int bufferSize = 10; // set up the buffer size
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in), bufferSize);
        BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(System.out));
        NBMerge(bfr, bfw);
        bfr.close();
        bfw.close();
	}
	
	private static void NBMerge(BufferedReader bfr, BufferedWriter bfw) throws IOException {
        String aLine = bfr.readLine();
        if (aLine == null) {
        	return;
        }
        String[] strLine = aLine.split("\t+");
        String previousKey = strLine[0];
        int sumForPreviousKey = Integer.parseInt(strLine[1]);
        aLine = bfr.readLine();
        while (aLine != null) {
            strLine = aLine.split("\t+");            
            if (strLine[0] == previousKey) {
            	sumForPreviousKey += Integer.parseInt(strLine[1]);
            } else {
            	bfw.write(previousKey + "\t" + sumForPreviousKey + "\n");
            	previousKey = strLine[0];
            	sumForPreviousKey = Integer.parseInt(strLine[1]);
            }
            aLine = bfr.readLine();
        }
    	bfw.write(previousKey + "\t" + sumForPreviousKey + "\n");  	
	}
}
