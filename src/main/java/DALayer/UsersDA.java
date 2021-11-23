package DALayer;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

//    public UsersEntity findById(int id) throws SQLException {
//        Connection conn = DBConnect.getConnection();
//        Statement state = conn.createStatement();
//        ResultSet rs = state.executeQuery(
//                "SELECT id,name,password,sex,status FROM users u WHERE u.id="+id);
//        UsersEntity user = null;
//        if (rs.next()) {
//            user = new UsersEntity();
//            user.setID(rs.getInt("id"));
//            user.setName(rs.getString("name"));
//            user.setPassword(rs.getString("password"));
//            user.setSex(rs.getString("sex"));
//            user.setStatus(rs.getInt("status"));
//        }
//        return user;
//    }

    public void offline(int id) throws IOException, ClassNotFoundException {
        File f = DBConnect.getConnection();
        for (File file : Objects.requireNonNull(f.listFiles())) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            UsersEntity user = (UsersEntity) in.readObject();

            if (user.getID() == 0) {
                user.setID(id);
            }
            break;
        }
    }

	public void online(int id) throws IOException, ClassNotFoundException {
        File f = DBConnect.getConnection();
        for (File file : Objects.requireNonNull(f.listFiles())) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            UsersEntity user = (UsersEntity) in.readObject();

            if (user.getID() == 1) {
                user.setID(id);
            }
            break;
        }
    }

    public void hide(int id) throws IOException, ClassNotFoundException {
        File f = DBConnect.getConnection();
        for (File file : Objects.requireNonNull(f.listFiles())) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            UsersEntity user = (UsersEntity) in.readObject();

            if (user.getID() == 2) {
                user.setID(id);
            }
            break;
        }
    }
	
	
}