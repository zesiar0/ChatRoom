package DALayer;

import java.io.*;
import java.util.Objects;


import BLLayer.DBConnect;
import BLLayer.UsersEntity;

public class UsersDA {

	public UsersEntity findByNamePassword(String name, String password) throws Exception {
        InputStream in = DBConnect.getInputStream(name, password);
        ObjectInputStream ois = new ObjectInputStream(in);

        UsersEntity user = (UsersEntity) ois.readObject();
        ois.close();
        return user;

	}
    public void insert(UsersEntity user) throws IOException {
        OutputStream out = DBConnect.getOutputStream(user.getName(), user.getPassword());
        ObjectOutputStream oos = new ObjectOutputStream(out);

        oos.writeObject(user);
    }
    public UsersEntity findByName(String name) throws IOException, ClassNotFoundException {
        UsersEntity user = null;

        File conn = DBConnect.getConnection();
        for (File f : Objects.requireNonNull(conn.listFiles())) {
            String filename = f.getName();
            if (filename.startsWith(name)) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                user = (UsersEntity) in.readObject();
                break;
            }
        }

        return user;
    }

    public void offline(int id, String user) throws IOException, ClassNotFoundException {
        update(id, 0, user);
    }

	public void online(int id, String user) throws IOException, ClassNotFoundException {
        update(id, 1, user);
    }

    public void hide(int id, String user) throws IOException, ClassNotFoundException {
        update(id, 2, user);
    }

    public void update(int newId, int oldId, String username) throws IOException, ClassNotFoundException {
        File f = DBConnect.getConnection();
        for (File file : Objects.requireNonNull(f.listFiles())) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            UsersEntity user = (UsersEntity) in.readObject();

            if (user.getID() == oldId && Objects.equals(user.getName(), username)) {
                user.setID(newId);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(user);
            }
            break;
        }
    }
	
}
