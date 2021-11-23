package BLLayer;

import java.io.Serializable;

public class UsersEntity implements Serializable {
	private int id;
	private String name;
	private String password;
	private String sex;
	private int status;
	
	public UsersEntity(){
	}
	
	public UsersEntity(String name,String pw,String sex,int status){
		this.name = name;
		this.password = pw;
		this.sex = sex;
		this.status = status;
	}

	public int getID() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getPassword() {
		return this.password;
	}
	public String getSex() {
		return this.sex;
	}
	public int getStatus() {
		return this.status;
	}

	public void setID(int id) {
		this.id = id ;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String toString(){
		return "UsersEntity{"+
				"id = "+ id +
				",name = \'"+ name +"\'"+
				",password = \'"+ password +"\'"+
				",sex = \'"+ sex +"\'"+
				",status = " + status+
				"}"	;
	}
	
}
