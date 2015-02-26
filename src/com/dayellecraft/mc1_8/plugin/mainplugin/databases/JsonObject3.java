package com.dayellecraft.mc1_8.plugin.mainplugin.databases;

import java.util.ArrayList;
import java.util.List;

@Deprecated public class JsonObject3 {

	//Json is too heavy for this.
	public List<UserEntry> u = new ArrayList<UserEntry>();
	
	public static class UserEntry {
		public String us;
		public long d;
		
		public UserEntry(String u, long d){
			us = u;
			this.d = d;
		}
	}
}
