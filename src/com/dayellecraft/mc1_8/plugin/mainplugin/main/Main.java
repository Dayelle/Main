package com.dayellecraft.mc1_8.plugin.mainplugin.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	private static Main instance;
	public static Main getInstance(){return instance;}
	
	public static void main(String[] args) {

	}

	@Override public void onLoad(){
		instance = this; //set Instance.
		
	}

	@Override public void onEnable(){

	}

	@Override public void onDisable(){

	}

}
