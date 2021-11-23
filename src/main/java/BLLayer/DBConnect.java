package BLLayer;

import java.io.*;

public class DBConnect {
	public static String URL = "./database";
	private static File conn;

	static {
			conn = new File(URL);
			if (!conn.exists()) {
				conn.delete();
				conn.mkdir();
			}

		System.out.println("File Load Success.");
	}

	public static File getConnection(){
		return conn;
	}

	public static InputStream getInputStream(String user, String pass) throws IOException {
		String filename = URL + "/" + user + pass;
		File f = new File(filename);

		if (!f.exists()) {
			f.createNewFile();
		}
		return new FileInputStream(f);
	}

	public static OutputStream getOutputStream(String user, String pass) throws IOException {
		String filename = URL + "/" + user + pass;
		File f = new File(filename);

		if (!f.exists()) {
			f.createNewFile();
		}
		return new FileOutputStream(f);
	}

}
