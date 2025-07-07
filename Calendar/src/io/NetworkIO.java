package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class for inputting and outputting the hostname and port number of the server
 * to a text file
 */
public class NetworkIO {

	/**
	 * Reads the NetworkIO file and sets the hostname and port number from file
	 * 
	 * @param file     the file to read
	 * @return hostport the hostname and the port
	 */
	public static String[] readNetworkIO(File file) {
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("@@");
			String[] hostport = new String[2];
			try {
				hostport[0] = scanner.next();
				hostport[1] = Integer.toString(scanner.nextInt());
			} catch (Exception e) {
				scanner.close();
				return null;
				// Nothing
			}
			scanner.close();
			return hostport;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
