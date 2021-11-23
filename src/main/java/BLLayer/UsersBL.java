package BLLayer;

import java.io.IOException;
import java.sql.SQLException;

import DALayer.UsersDA;


public class UsersBL {
	public UsersBL() {}
	public UsersEntity loginCheckOut(String name,String password)  {
		UsersEntity user = null;
		try {
			user = new UsersDA().findByNamePassword(name,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public int signUp(UsersEntity user) {
		int isSuccess = 0;
		UsersDA usersDA = new UsersDA();
		UsersEntity tuser = null;
		try {
			tuser = usersDA.findByName(user.getName());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(tuser != null){
			isSuccess = 1;			 
		}
		else {
			try {
				usersDA.insert(user);
				isSuccess = 2; 
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
	public void setOffline(int id) {
		try {
			new UsersDA().offline(id);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void setOnline(int id) {
		try {
			new UsersDA().online(id);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void setHide(int id) {
		try {
			new UsersDA().hide(id);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
