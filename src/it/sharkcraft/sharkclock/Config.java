package it.sharkcraft.sharkclock;

import static it.sharkcraft.sharkclock.SharkClock.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class Config {

	public static FileConfiguration config;
	
	private static Vector[][] BLOCKS;
	
	public final static String POS_HOURS = "Hours";
	public final static String POS_MINUTES = "Minutes";
	public final static String POS_SECONDS = "Seconds";
	
	public final static String POS_FIRST = "First";
	public final static String POS_SECOND = "Second";
	
	public static void init() {
		
		String[] base = {"x", "y", "z"};
		String[] position = {"Hours", "Minutes", "Seconds" };
		String[] digits = {"First", "Second" };
		String[][] messages = {{"Time", "Tempo attuale %hours% : %minutes% : %seconds%"},
			{"Date", "Data attuale %day%.%month%.%year%"},
			{"ClockInfo", "Ultimo aggiornamento: %hours% : %minutes% : %seconds%  running: %running%"},
			{"ClockStart", "Orologio digitale inizializzato"}, 
			{"ClockStop", "Orologo digitale interrotto"} };
		
		// create position section
		
		if (!config.contains("Position"))
			config.createSection("Position");
		
		if (!config.contains("Position.World")) {
			
			config.createSection("Position.World");
			setWorld(Bukkit.getWorlds().get(0));	// get default world
		}
		
		for (String i : position) {
		
			if (!config.contains("Position." + i))
				config.createSection("Position." + i);
				
			for (String w : digits) {
				
				if (!config.contains("Position." + i + "." + w))
					config.createSection("Position." + i + "." + w);
			
				for (String j : base) {
				
					if (!config.contains("Position." + i + "." + w + "." + j)) {
						
						config.createSection("Position." + i + "." + w + "." + j);
						config.set("Position." + i + "." + w + "." + j, 0);
					}
				}
			}
		}
		
		// create section Blocks
		
		if (!config.contains("Blocks"))
			config.createSection("Blocks");
		
		for (int i = 0; i < 10; i++) {
			
			if (!config.contains("Blocks." + i))
				config.createSection("Blocks." + i);
			
			if (!config.contains("Blocks." + i + ".Size")) {
				
				config.createSection("Blocks." + i + ".Size");
				config.set("Blocks." + i + ".Size", 0);
			}
			
			if (!config.contains("Blocks." + i + ".0")) {
				
				config.createSection("Blocks." + i + ".0");
			}
			
			for (String j : base) {
				
				if (!config.contains("Blocks." + i + ".0." + j)) {
					
					config.createSection("Blocks." + i + ".0." + j);
					config.set("Blocks." + i + ".0." + j, 0);
				}
			}
		}
		
		// create section Messages
		
		if (!config.contains("Messages"))
			config.createSection("Messages");
		
		for (String[] i : messages) {
		
			if (!config.contains("Messages." + i[0])) {
			
				config.createSection("Messages." + i[0]);
				config.set("Messages." + i[0], i[1]);
			}
		}
	}
	
	public static Location position(String pos, String digit) {
		
		int x = config.getInt("Position." + pos + "." + digit + ".x");
		int y = config.getInt("Position." + pos + "." + digit + ".y");
		int z = config.getInt("Position." + pos + "." + digit + ".z");
		
		return new Location(world(), x, y, z);
	}
	
	public static int sizeofNumber(int number) {
		
		if (number < 0 || number > 9)
			throw new IllegalArgumentException("From 0 to 9 permitted");
		
		return config.getInt("Blocks." + number + ".size");
	}
	
	public static Vector[] blocks(int number) {
		
		if (number < 0 || number > 9)
			throw new IllegalArgumentException("From 0 to 9 permitted");
		
		return BLOCKS[number];
	}
	
	public static void loadblocks() {
		
		BLOCKS = new Vector[10][];
		
		for (int number = 0; number < 10; number++) {
		
			int size = sizeofNumber(number);
		
			BLOCKS[number] = new Vector[size];
		
			for (int i = 0; i < size; i++) {
			
				int x = config.getInt("Blocks." + number + "." + i + ".x");
				int y = config.getInt("Blocks." + number + "." + i + ".y");
				int z = config.getInt("Blocks." + number + "." + i + ".z");
			
				BLOCKS[number][i] = new Vector(x, y, z);
			}
		}
	}
	
	public static void setWorld(World w) {
		
		config.set("Position.World", w.getName());
	}
	
	public static World world() {
		
		return Bukkit.getServer().getWorld(config.getString("Position.World"));
	}
	
	public static String timeMessage() {
		
		return config.getString("Messages.Time");
	}
	
	public static String dateMessage() {
		
		return config.getString("Messages.Date");
	}
	
	public static String infoMessage() {
		
		return config.getString("Messages.ClockInfo");
	}
	
	public static String startMessage() {
		
		return config.getString("Messages.ClockStart");
	}
	
	public static String stopMessage() {
		
		return config.getString("Messages.ClockStop");
	}
	
	public static void save() {
		
		plugin.saveConfig();
	}
	
	public static void reload() {
		
		plugin.reloadConfig();
	}
}
