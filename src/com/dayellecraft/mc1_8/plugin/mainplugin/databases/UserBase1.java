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
//UUID -> Username Base
	private static UserDatabase ub1;
	@Deprecated public static UserDatabase getUserBase(){return ub1;} //Only Use For Direct Testing! Not Thread Safe!
	@Deprecated public static void setUserBase1(UserDatabase ub1){UserBase1.ub1 = ub1;} //Only Use For Direct Testing! Not Thread Safe!
	
	public static void Create(File f) throws ClassNotFoundException, SQLException {
		ub1 = new UserDatabase(f);
	}
	
	private UserBase1(File f){
		
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
			statement.executeUpdate("create table if not exists Users (UNIQUE uuid string, jsonstore string)");
			prepareStatements();
		}
		
		public void prepareStatements() throws SQLException {
			getAll = connection.prepareStatement("select * from uuid");
			delete = connection.prepareStatement("delete from Users WHERE ? = ?");
			contains = connection.prepareStatement("select * from uuid WHERE jsonstore LIKE %?%");
			select1 = connection.prepareStatement("select * from Users WHERE uuid = ?");
			updateJson = connection.prepareStatement("UPDATE Users SET jsonstore = ? WHERE uuid = ?");
		}
		
		private PreparedStatement getAll; //Gets Every User
		private PreparedStatement delete; //Deletes A User, MultiType Checker
		//private PreparedStatement deleteWhereContains; //delete * from uuid WHERE ? LIKE %?%
		private PreparedStatement contains; //For 
		private PreparedStatement select1;
		private PreparedStatement updateJson;
		
		/*
		public void prepareStatements() throws SQLException {
			getAll = connection.prepareStatement("select * from uuid");
			delete = connection.prepareStatement("delete from Users WHERE ? = ?");
			insert = connection.prepareStatement("insert into Users values(?,?,?,?)");
			select1 = connection.prepareStatement("select * from Users WHERE username = ?");
			updatePassword = connection.prepareStatement("UPDATE Users SET password = ? WHERE username = ?");
			updateUsername = connection.prepareStatement("UPDATE Users SET username = ? WHERE username = ?");
			updateDisplayName = connection.prepareStatement("UPDATE Users SET displayname = ? WHERE username = ?");
			updatePermissions = connection.prepareStatement("UPDATE Users SET permissions = ? WHERE username = ?");
		}
		private PreparedStatement getAll;
		private PreparedStatement delete;
		private PreparedStatement insert;
		private PreparedStatement select1;
		private PreparedStatement updatePassword;
		private PreparedStatement updatePermissions;
		private PreparedStatement updateUsername;
		private PreparedStatement updateDisplayName;
		public boolean deleteUser(String where, String what) {
			try{
				if(where.length() > 40 || what.length() > 40)return false;
				delete.setString(1, where);
				delete.setString(2, what);
				delete.executeUpdate();
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}		
		boolean createUser(String username, String password,String displayname, PermissionsList list){
			try{
				if(username.length() > 40)return false;
				if(password.length() > 40)return false;
				if(displayname.length() > 40) return false;
				if(list.permsList.size() > 40) return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				if(rs.next()){return false;}

				insert.setString(1, username);
				insert.setString(2, password);
				insert.setString(3, username);

				insert.setObject(4, list.toByteArray());
				insert.executeUpdate();
				rs = null;
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		public User validateUser(String username, String password){
			try{
				if(username.length() > 40)return null;
				if(password.length() > 40)return null;
				//ResultSet rs = sqld.getStatement().executeQuery("select * from Users WHERE username = '"+username+"'");
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("password").equals(password)){
						return new User(
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("displayname"),
								PermissionsList.fromByteArray(rs.getBytes("permissions"))
								);
					}
				}
				rs = null;
			}catch(Exception e){e.printStackTrace();}
			return null;
		}
		public List<User> getUsers(){
			List<User> list = new ArrayList<User>();
			try{
				ResultSet rs = getAll.executeQuery();
				while(rs.next()){list.add(
						new User(
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("displayname"),
								PermissionsList.fromByteArray(rs.getBytes("permissions"))
								)
						);
				}
				rs = null;
				return list;
			}catch(Exception e){}
			return null;
		}
		public boolean changePassword(String username, String oldPassword, String newPassword){
			try{
				if(username.length() > 40)return false;
				if(newPassword.length() > 40)return false;
				if(oldPassword.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)&&rs.getString("password").equals(oldPassword)){
						updatePassword.setString(1, newPassword);
						updatePassword.setString(2, username);
						updatePassword.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		public boolean changePassword(String username, String newPassword){
			try{
				if(username.length() > 40)return false;
				if(newPassword.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updatePassword.setString(1, newPassword);
						updatePassword.setString(2, username);
						updatePassword.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		public boolean changePermissions(String username, PermissionsList list){
			try{
				if(username.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updatePermissions.setBytes(1, list.toByteArray());
						updatePermissions.setString(2, username);
						updatePermissions.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		public boolean changeDisplayName(String username, String displayname){
			try{
				if(username.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updateDisplayName.setString(1, displayname);
						updateDisplayName.setString(2, username);
						updateDisplayName.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		public boolean changeUsername(String username, String newUsername){
			try{
				if(newUsername.length() > 40)return false;
				if(username.length() > 40) return false;
				select1.setString(1, newUsername);
				ResultSet rs = select1.executeQuery();
				if(rs.next())return false;
				updateUsername.setString(1, newUsername);
				updateUsername.setString(2, username);
				updateUsername.executeUpdate();
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		//*/
		//	PreparedStatement ps2 = prepareStatement("delete from Users WHERE username = ?");
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
