package com.dayellecraft.mc1_8.plugin.mainplugin.databases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserBase1 {

	private static UserDatabase ub1;
	@Deprecated public static UserDatabase getUserBase(){return ub1;} //Only Use For Direct Testing! Not Thread Safe!
	@Deprecated public static void setUserBase1(UserDatabase ub1){UserBase1.ub1 = ub1;} //Only Use For Direct Testing! Not Thread Safe!

	private UserBase1(){}

	public static void connect(File f) throws ClassNotFoundException, SQLException {
		ub1 = new UserDatabase(f);
	}

	/*Get Users*/public List<User> getUsers(){
		synchronized(ub1){
			return ub1.getUsers();
		}
	}
	/*Get User*/public User getUser(UUID uuid){
		synchronized(ub1){
			return ub1.getUser(uuid);
		}
	}
	/*Get User*/public User getUser(String uuid){
		synchronized(ub1){
			return ub1.getUser(uuid);
		}
	}
	/*DeleteUser*/public boolean deleteUser(UUID uuid){
		synchronized(ub1){
			return ub1.deleteUser(uuid);
		}
	}
	/*Delete User*/public boolean deleteUser(String uuid) {
		synchronized(ub1){
			return ub1.deleteUser(uuid);
		}
	}		
	/*Contains*/public List<User> contains(String compare){
		synchronized(ub1){
			return ub1.contains(compare);
		}
	}
	/*Create User*/public boolean createUser(UUID uuid, String jsonStore){
		synchronized(ub1){
			return ub1.createUser(uuid, jsonStore);
		}
	}
	/*Create User*/public boolean createUser(String uuid, String jsonStore){
		synchronized(ub1){
			return ub1.createUser(uuid, jsonStore);
		}
	}
	/*Update JSON*/public boolean updateJson(UUID uuid, String newJson){
		synchronized(ub1){
			return ub1.updateJson(uuid, newJson);
		}
	}
	/*Update JSON*/public boolean updateJson(String uuid, String newJson){
		synchronized(ub1){
			return ub1.updateJson(uuid, newJson);
		}
	}

	public static class UserDatabase {

		private Statement statement;
		public Statement getStatement(){return statement;}

		private Connection connection;
		public Connection getConnection(){return connection;}

		private void Load(File f) throws ClassNotFoundException, SQLException {
			Class.forName("org.sqlite.JDBC");

			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+f.getAbsolutePath());
			statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			statement.executeUpdate("create table if not exists Users (uuid string, jsonstore string)");
			prepareStatements();
		}

		public void prepareStatements() throws SQLException {
			getAll = connection.prepareStatement("select * from Users");
			delete = connection.prepareStatement("delete from Users WHERE uuid = ?");
			contains = connection.prepareStatement("select * from Users WHERE jsonstore LIKE ?");
			select1 = connection.prepareStatement("select * from Users WHERE uuid = ?");
			updateJson = connection.prepareStatement("UPDATE Users SET jsonstore = ? WHERE uuid = ?");
			insert = connection.prepareStatement("insert into Users values(?,?)");
		}

		private PreparedStatement getAll; //Gets Every User
		private PreparedStatement delete; //Deletes A User, MultiType Checker
		//private PreparedStatement deleteWhereContains; //delete * from users WHERE jsonstore LIKE %?%
		private PreparedStatement contains; //For 
		private PreparedStatement select1;
		private PreparedStatement updateJson;
		private PreparedStatement insert;

		/*Get Users*/public List<User> getUsers(){
			List<User> list = new ArrayList<User>();
			try{
				ResultSet rs = getAll.executeQuery();
				while(rs.next()){
					list.add(new User(UUID.fromString(rs.getString("uuid")),rs.getString("jsonstore")));
				}
				rs = null;
				return list;
			}catch(Exception e){e.printStackTrace();}
			return null;
		}
		/*Get User*/public User getUser(UUID uuid){
			return getUser(uuid.toString());
		}
		/*Get User*/public User getUser(String uuid){
			ResultSet rs;
			try{
				select1.setString(1, uuid);
				rs = select1.executeQuery();
				while(rs.next()){
					return new User(UUID.fromString(rs.getString("uuid")),rs.getString("jsonstore"));
				}
			}catch(Exception e){e.printStackTrace();}
			finally{rs = null;}
			return null;
		}
		/*DeleteUser*/public boolean deleteUser(UUID uuid){
			return deleteUser(uuid.toString());
		}
		/*Delete User*/public boolean deleteUser(String uuid) {
			try{
				delete.setString(1, uuid);
				delete.executeUpdate();
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}		
		/*Contains*/public List<User> contains(String compare){
			List<User> list = new ArrayList<User>();
			try{
				contains.setString(1, "%"+compare+"%");
				ResultSet rs = contains.executeQuery();
				while(rs.next()){
					list.add(new User(UUID.fromString(rs.getString("uuid")),rs.getString("jsonstore")));
				}
				rs = null;
				return list;
			}catch(Exception e){e.printStackTrace();}
			return null;
		}
		/*Create User*/public boolean createUser(UUID uuid, String jsonStore){
			return createUser(uuid.toString(),jsonStore);
		}
		/*Create User*/public boolean createUser(String uuid, String jsonStore){
			try{
				select1.setString(1, uuid);

				ResultSet rs = select1.executeQuery();
				if(rs.next()){return false;}

				insert.setString(1, uuid);
				insert.setString(2, jsonStore);
				insert.executeUpdate();
				rs = null;
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Update JSON*/public boolean updateJson(UUID uuid, String newJson){
			return updateJson(uuid.toString(),newJson);
		}
		/*Update JSON*/public boolean updateJson(String uuid, String newJson){
			try{
				select1.setString(1, uuid);
				ResultSet rs = select1.executeQuery();
				if(!rs.next()){return false;}
				try{
					updateJson.setString(1, newJson);
					updateJson.setString(2, uuid);
					updateJson.executeUpdate();
					return true;
				}catch(Exception e){}
			}catch(Exception e){e.printStackTrace();}
			return false;

		}

		private UserDatabase(File f) throws ClassNotFoundException, SQLException {
			Load(f); //setup 
		}

	}

	public static class User{	
		
		private String dataStore= null;
		public String getDataStore(){return dataStore;}
		public void setDataStore(String o){dataStore = o;}

		private UUID usersID;
		public UUID getID(){return usersID;}

		public boolean save(){
			//TODO-Add A Save Functionality.
			return false;
		}

		public User(UUID id, String data){
			dataStore = data;
			usersID = id;
		}
		
	}

}
