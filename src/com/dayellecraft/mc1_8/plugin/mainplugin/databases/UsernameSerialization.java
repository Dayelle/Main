package com.dayellecraft.mc1_8.plugin.mainplugin.databases;

public class UsernameSerialization {

	/*
	 <username>:<date>\n
	       ^   ^   ^   ^ <- This the indexer
	       ^   ^   ^ <- This is System.currentTimeMillis()
	       ^   ^ <- This is the split.
	       ^ <- this is Player.getUsername()
	 */
	
}
