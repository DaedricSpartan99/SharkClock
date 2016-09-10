package it.sharkcraft.sharkclock;

import static it.sharkcraft.sharkclock.SharkClock.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
		
		String[] position = {"Hours", "Minutes", "Seconds" };
		String[] digits = {"First", "Second" };
		String[][] messages = {{"Time", "Tempo attuale %hours% : %minutes% : %seconds%"},
			{"Date", "Data attuale %day%.%month%.%year%"},
			{"ClockInfo", "Ultimo aggiornamento: %hours% : %minutes% : %seconds%  active: %running%"},
			{"ClockStart", "Orologio digitale inizializzato"}, 
			{"ClockStop", "Orologo digitale interrotto"} };
		
		// create active section
		
		if (!config.contains("Active")) {
			
			config.createSection("Active");
			config.set("Active", false);
		}
		
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
				
				if (!config.contains("Position." + i + "." + w + ".Location")) {
						
					config.createSection("Position." + i + "." + w + ".Location");
					config.set("Position." + i + "." + w + ".Location", new Vector(0, 0, 0));
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
				config.createSection("Blocks." + i + ".0.Material");
				config.set("Blocks." + i + ".0.Material", Material.STONE);
			}
				
			if (!config.contains("Blocks." + i + ".0.Location")) {
					
				config.createSection("Blocks." + i + ".0.Location");
				config.set("Blocks." + i + ".0.Location", new Vector(0, 0, 0));
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
		
		Vector x = config.getVector("Position." + pos + "." + digit + ".Location");
		
		return new Location(world(), x.getX(), x.getY(), x.getZ());
	}
	
	public static void setPosition(Location loc, String pos, String digit) {
		
		config.set("Position." + pos + "." + digit + ".Location", loc.toVector());
	}
	
	public static int sizeofDigit(int number) {
		
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
		
			loadblocks(number);
		}
	}
	
	public static void loadblocks(int digit) {
		
		int size = sizeofDigit(digit);
		
		BLOCKS[digit] = new Vector[size];
	
		for (int i = 0; i < size; i++) {
		
			BLOCKS[digit][i] = config.getVector("Blocks." + digit + "." + i + ".Location");
		}
	}
	
	public static void setWorld(World w) {
		
		config.set("Position.World", w.getName());
	}
	
	public static World world() {
		
		return Bukkit.getServer().getWorld(config.getString("Position.World"));
	}
	
	public static void setBlock(Block block, int digit, int index) {	// !! reference of position hours first !!
		
		if (!config.contains("Blocks." + digit + "." + index)) {
			
			config.createSection("Blocks." + digit + "." + index);
			config.createSection("Blocks." + digit + "." + index + ".Location");
			config.set("Blocks." + digit + ".Size", sizeofDigit(digit) + 1);
		}
		
		config.set("Blocks." + digit + "." + index + ".x", block.getLocation().toVector().subtract(position(POS_HOURS, POS_FIRST).toVector()));
		
		if (!config.contains("Blocks." + digit + "." + index + ".Material")) {
			
			config.createSection("Blocks." + digit + "." + index + ".Material");
		}
		
		config.set("Blocks." + digit + "." + index + ".Material", block.getType());
		
		Config.save();
		Config.reload();
	}
	
	private static void decreaseBlockIndex(int digit, int index) {
		
		if (config.contains("Blocks." + digit + "." + (index - 1)))
			config.set("Blocks." + digit + "." + (index - 1), null);	// delete destination section
		
		Vector coords = config.getVector("Blocks." + digit + "." + index + ".Location");
		String mat = config.getString("Blocks." + digit + "." + index + ".Material");
		
		config.set("Blocks." + digit + "." + index, null);
		
		config.createSection("Blocks." + digit + "." + (index - 1));
		config.createSection("Blocks." + digit + "." + (index - 1) + ".Location");
		config.createSection("Blocks." + digit + "." + (index - 1) + ".Material");
		
		config.set("Blocks." + digit + "." + (index - 1) + ".Location", coords);
		config.set("Blocks." + digit + "." + (index - 1) + ".Material", mat);
	}
	
	public static void removeBlock(int digit, int index) {
		
		if (!config.contains("Blocks." + digit + "." + index))
			return;
		
		config.set("Blocks." + digit + "." + index, null);
		
		for (int i = index + 1; i < Config.sizeofDigit(digit); i++)
			decreaseBlockIndex(digit, i);	// rename blocks
		
		config.set("Block." + digit + ".Size", sizeofDigit(digit) - 1);
		
		Config.save();
		Config.reload();
	}
	
	public static boolean active() {
		
		return config.getBoolean("Active");
	}
	
	public static void setActive(boolean active) {
		
		config.set("Active", active);
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
