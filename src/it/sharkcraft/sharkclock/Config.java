package it.sharkcraft.sharkclock;

import static it.sharkcraft.sharkclock.SharkClock.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Config {

	//public static FileConfiguration config;
	
	private static Location[][] BLOCKS;
	
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
		
		System.out.println("Checking for Active section");
		
		if (!plugin.getConfig().contains("Active")) {
			
			System.out.println("Creating section: " + "Active");
			plugin.getConfig().createSection("Active");
			plugin.getConfig().set("Active", false);
		}
		
		// create position section
		
		if (!plugin.getConfig().contains("Position")) {
			
			System.out.println("Creating section: " + "Position");
			plugin.getConfig().createSection("Position");
		}
		
		if (!plugin.getConfig().contains("Position.World")) {
			
			System.out.println("Creating section: " + "Position.World");
			plugin.getConfig().createSection("Position.World");
			setWorld(Bukkit.getWorlds().get(0));	// get default world
		}
		
		for (String i : position) {
		
			if (!plugin.getConfig().contains("Position." + i)) {
				
				System.out.println("Creating section: " + "Position." + i);
				plugin.getConfig().createSection("Position." + i);
			}
				
			for (String w : digits) {
				
				if (!plugin.getConfig().contains("Position." + i + "." + w)) {
					
					System.out.println("Creating section: " + "Position." + i + "." + w);
					plugin.getConfig().createSection("Position." + i + "." + w);
				}
				
				if (!plugin.getConfig().contains("Position." + i + "." + w + ".Location")) {
					
					System.out.println("Creating section: " + "Position." + i + "." + w + ".Location");
					plugin.getConfig().createSection("Position." + i + "." + w + ".Location");
					plugin.getConfig().set("Position." + i + "." + w + ".Location", new Vector(0, 0, 0));
				}
			}
		}
		
		// create section Blocks
		
		if (!plugin.getConfig().contains("Blocks")) {
			
			System.out.println("Creating section: " + "Blocks");
			plugin.getConfig().createSection("Blocks");
		}
		
		for (int i = 0; i < 10; i++) {
			
			if (!plugin.getConfig().contains("Blocks." + i)) {
				
				System.out.println("Creating section: " + "Blocks." + i);
				
				plugin.getConfig().createSection("Blocks." + i);
			}
			
			if (!plugin.getConfig().contains("Blocks." + i + ".Size")) {
				
				System.out.println("Creating section: " + "Blocks." + i + ".Size");
				
				plugin.getConfig().createSection("Blocks." + i + ".Size");
				plugin.getConfig().set("Blocks." + i + ".Size", 0);
			}
			
			/*if (!plugin.getConfig().contains("Blocks." + i + ".0")) {
				
				System.out.println("Creating section: " + "Blocks." + i + ".0");
				System.out.println("Creating section: " + "Blocks." + i + ".0.Material");
				
				plugin.getConfig().createSection("Blocks." + i + ".0");
				plugin.getConfig().createSection("Blocks." + i + ".0.Material");
				plugin.getConfig().set("Blocks." + i + ".0.Material", Material.STONE.toString());
			}
				
			if (!plugin.getConfig().contains("Blocks." + i + ".0.Location")) {
				
				System.out.println("Creating section: " + "Blocks." + i + ".0.Location");
				
				plugin.getConfig().createSection("Blocks." + i + ".0.Location");
				plugin.getConfig().set("Blocks." + i + ".0.Location", new Vector(0, 0, 0));
				plugin.getConfig().set("Blocks." + i + ".Size", 1);
			}*/
		}
		
		// create section Messages
		
		if (!plugin.getConfig().contains("Messages"))
			plugin.getConfig().createSection("Messages");
		
		for (String[] i : messages) {
		
			if (!plugin.getConfig().contains("Messages." + i[0])) {
			
				plugin.getConfig().createSection("Messages." + i[0]);
				plugin.getConfig().set("Messages." + i[0], i[1]);
			}
		}
	}
	
	public static Location position(String pos, String digit) {
		
		Vector x = plugin.getConfig().getVector("Position." + pos + "." + digit + ".Location");
		
		return new Location(world(), x.getX(), x.getY(), x.getZ());
	}
	
	public static void setPosition(Location loc, String pos, String digit) {
		
		Vector v = loc.toVector();
		
		System.out.println("Setting position: " + v.getX() + " " + v.getY() + " " + v.getZ());
		System.out.println("Setting world: " + loc.getWorld().getName());
		
		plugin.getConfig().set("Position." + pos + "." + digit + ".Location", v);
		plugin.getConfig().set("Position.World", loc.getWorld().getName());
		
		Config.save();
		Config.reload();
	}
	
	public static int sizeofDigit(int number) {
		
		if (number < 0 || number > 9)
			throw new IllegalArgumentException("From 0 to 9 permitted");
		
		return plugin.getConfig().getInt("Blocks." + number + ".Size");
	}
	
	public static Location[] blocks(int number) {
		
		if (number < 0 || number > 9)
			throw new IllegalArgumentException("From 0 to 9 permitted");
		
		return BLOCKS[number];
	}
	
	public static void loadblocks() {
		
		BLOCKS = new Location[10][];
		
		for (int number = 0; number < 10; number++) {
		
			loadblocks(number);
		}
	}
	
	public static void loadblocks(int digit) {
		
		int size = sizeofDigit(digit);
		
		BLOCKS[digit] = new Location[size];
	
		for (int i = 0; i < size; i++) {
		
			BLOCKS[digit][i] = plugin.getConfig().getVector("Blocks." + digit + "." + i + ".Location").
					toLocation(world());
		}
	}
	
	public static void setWorld(World w) {
		
		plugin.getConfig().set("Position.World", w.getName());
		
		Config.save();
		Config.reload();
	}
	
	public static World world() {
		
		return Bukkit.getServer().getWorld(plugin.getConfig().getString("Position.World"));
	}
	
	public static void setBlock(Block block, int digit, int index) {	// !! reference of position hours first !!
		
		if (!plugin.getConfig().contains("Blocks." + digit + "." + index)) {
			
			System.out.println("Creating section: " + "Blocks." + digit + "." + index);
			System.out.println("Creating section: " + "Blocks." + digit + "." + index + ".Location");
			System.out.println("Add new block to: " + digit + " " + index);
			
			plugin.getConfig().createSection("Blocks." + digit + "." + index);
			plugin.getConfig().createSection("Blocks." + digit + "." + index + ".Location");
			plugin.getConfig().set("Blocks." + digit + ".Size", sizeofDigit(digit) + 1);
		}
		
		
		Vector v = block.getLocation().subtract(position(POS_HOURS, POS_FIRST)).toVector();
		
		System.out.println("Setting block: " + v.getX() + " " + v.getY() + " " + v.getZ());
		
		plugin.getConfig().set("Blocks." + digit + "." + index + ".Location", v);
		
		if (!plugin.getConfig().contains("Blocks." + digit + "." + index + ".Material")) {
			
			System.out.println("Creating section: " + "Blocks." + digit + "." + index + ".Material");
			plugin.getConfig().createSection("Blocks." + digit + "." + index + ".Material");
		}
		
		System.out.println("Setting material: " + block.getType().toString());
		
		plugin.getConfig().set("Blocks." + digit + "." + index + ".Material", block.getType().toString());
		
		Config.loadblocks(digit);
		Config.save();
		Config.reload();
	}
	
	public static void clearDigit(int digit) {
		
		BLOCKS[digit] = null;
		
		plugin.getConfig().set("Blocks." + digit, null);
		
		if (!plugin.getConfig().contains("Blocks." + digit))
			plugin.getConfig().createSection("Blocks." + digit);
		
		if (!plugin.getConfig().contains("Blocks." + digit + ".Size"))
			plugin.getConfig().createSection("Blocks." + digit + ".Size");
		
		plugin.getConfig().set("Blocks." + digit + ".Size", 0);
	}
	
	public static void setDigitByVolume(Block b1, Block b2, int digit) {
		
		Location oldpos = position(POS_HOURS, POS_FIRST);
		
		World wor = b1.getWorld();
		setWorld(wor);
		
		clearDigit(digit);
		
		Location l1 = b1.getLocation();
		Location l2 = b2.getLocation();
		
		int size = 0;
		int[] inc = new int[3];
		
		inc[0] = (l2.getX() - l1.getX() < 0) ? -1 : 1;
		inc[1] = (l2.getY() - l1.getY() < 0) ? -1 : 1;
		inc[2] = (l2.getZ() - l1.getZ() < 0) ? -1 : 1;
		
		setPosition(l1.clone().add(new Vector(inc[0], inc[1], inc[2])), POS_HOURS, POS_FIRST);
		
		for (int i = (int)l1.getX() + inc[0]; i != (int)l2.getX(); i += inc[0]) {
			
			for (int j = (int)l1.getY() + inc[1]; j != (int)l2.getY(); j += inc[1]) {
				
				for (int w = (int)l1.getZ() + inc[2]; w != (int)l2.getZ(); w += inc[2]) {
					
					//System.out.println("V")
					
					Block bk = wor.getBlockAt(new Location(wor, i, j, w));
					
					if (bk.getType().toString().equals(Material.AIR.toString()) || bk.toString().equals(b1.toString()) || bk.toString().equals(b2.toString())) {
						
						continue;
					}
					
					setBlock(bk, digit, size);
					size++;
				}
			}
		}
		
		setPosition(oldpos, POS_HOURS, POS_FIRST);
		loadblocks(digit);
	}
	
	public static Material material(int digit, int index) {
		
		return Material.getMaterial(plugin.getConfig().getString("Blocks." + digit + "." + index + ".Material"));
	}
	
	private static void decreaseBlockIndex(int digit, int index) {
		
		if (plugin.getConfig().contains("Blocks." + digit + "." + (index - 1)))
			plugin.getConfig().set("Blocks." + digit + "." + (index - 1), null);	// delete destination section
		
		Vector coords = plugin.getConfig().getVector("Blocks." + digit + "." + index + ".Location");
		String mat = plugin.getConfig().getString("Blocks." + digit + "." + index + ".Material");
		
		plugin.getConfig().set("Blocks." + digit + "." + index, null);
		
		plugin.getConfig().createSection("Blocks." + digit + "." + (index - 1));
		plugin.getConfig().createSection("Blocks." + digit + "." + (index - 1) + ".Location");
		plugin.getConfig().createSection("Blocks." + digit + "." + (index - 1) + ".Material");
		
		plugin.getConfig().set("Blocks." + digit + "." + (index - 1) + ".Location", coords);
		plugin.getConfig().set("Blocks." + digit + "." + (index - 1) + ".Material", mat);
	}
	
	public static void removeBlock(int digit, int index) {
		
		if (!plugin.getConfig().contains("Blocks." + digit + "." + index))
			return;
		
		plugin.getConfig().set("Blocks." + digit + "." + index, null);
		
		for (int i = index + 1; i < Config.sizeofDigit(digit); i++)
			decreaseBlockIndex(digit, i);	// rename blocks
		
		plugin.getConfig().set("Block." + digit + ".Size", sizeofDigit(digit) - 1);
		
		Config.loadblocks(digit);
		Config.save();
		Config.reload();
	}
	
	public static boolean active() {
		
		return plugin.getConfig().getBoolean("Active");
	}
	
	public static void setActive(boolean active) {
		
		plugin.getConfig().set("Active", active);
	}
	
	public static String timeMessage() {
		
		return plugin.getConfig().getString("Messages.Time");
	}
	
	public static String dateMessage() {
		
		return plugin.getConfig().getString("Messages.Date");
	}
	
	public static String infoMessage() {
		
		return plugin.getConfig().getString("Messages.ClockInfo");
	}
	
	public static String startMessage() {
		
		return plugin.getConfig().getString("Messages.ClockStart");
	}
	
	public static String stopMessage() {
		
		return plugin.getConfig().getString("Messages.ClockStop");
	}
	
	public static void save() {
		
		plugin.saveConfig();
	}
	
	public static void reload() {
		
		plugin.reloadConfig();
	}
}
