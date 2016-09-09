package it.sharkcraft.sharkclock;

import it.sharkcraft.sharkclock.Config.Vector3i;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Commands {
	
	public static void shtime(Player sender) {
		
		String h = String.valueOf(Time.hours());
		String m = String.valueOf(Time.minutes());
		String s = String.valueOf(Time.seconds());
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.timeMessage()
				.replaceAll("%hours%", h).replaceAll("%minutes%", m)
				.replaceAll("%seconds%", s));
	}
	
	public static void shdate(Player sender) {
		
		String y = String.valueOf(Time.year());
		String m = String.valueOf(Time.month());
		String d = String.valueOf(Time.day());
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.timeMessage()
				.replaceAll("%year%", y).replaceAll("%month%", m)
				.replaceAll("%day%", d));
	}
	
	private static void removeDigit(World world, Vector3i location, int digit) {
		
		for (Vector3i i : Config.blocks(digit)) {
			
			Location loc = new Location(world, location.x + i.x, location.y + i.y, location.z + i.z);
			loc.getBlock().setType(Material.AIR);
		}
	}
	
	private static void placeDigit(World world, Vector3i location, int digit) {
		
		byte blockdata = 0x0;
		
		for (Vector3i i : Config.blocks(digit)) {
		
			Location loc = new Location(world, location.x + i.x, location.y + i.y, location.z + i.z);
			@SuppressWarnings("deprecation")
			FallingBlock block = world.spawnFallingBlock(loc, Material.COBBLESTONE, blockdata);
			block.setVelocity(new Vector(0, 0, 0));
		}
	}
	
	public static void clockUpdate() {
		
		World world = Config.world();
		
		int old, newone;
		
		if ((old = Time.current.hour) != (newone = Time.hours())) {
			
			char[] _old = String.valueOf(old).toCharArray();
			char[] _newone = String.valueOf(newone).toCharArray();
			
			removeDigit(world, Config.position(Config.POS_HOURS, Config.POS_FIRST), (int)(_old[0] - '0'));
			placeDigit(world, Config.position(Config.POS_HOURS, Config.POS_FIRST), (int)(_newone[0] - '0'));
			
			removeDigit(world, Config.position(Config.POS_HOURS, Config.POS_SECOND), (int)(_old[1] - '0'));
			placeDigit(world, Config.position(Config.POS_HOURS, Config.POS_SECOND), (int)(_newone[1] - '0'));
		}
		
		if ((old = Time.current.min) != (newone = Time.minutes())) {
			
			char[] _old = String.valueOf(old).toCharArray();
			char[] _newone = String.valueOf(newone).toCharArray();
			
			removeDigit(world, Config.position(Config.POS_MINUTES, Config.POS_FIRST), (int)(_old[0] - '0'));
			placeDigit(world, Config.position(Config.POS_MINUTES, Config.POS_FIRST), (int)(_newone[0] - '0'));
			
			removeDigit(world, Config.position(Config.POS_MINUTES, Config.POS_SECOND), (int)(_old[1] - '0'));
			placeDigit(world, Config.position(Config.POS_MINUTES, Config.POS_SECOND), (int)(_newone[1] - '0'));
		}
		
		if ((old = Time.current.sec) != (newone = Time.seconds())) {
			
			char[] _old = String.valueOf(old).toCharArray();
			char[] _newone = String.valueOf(newone).toCharArray();
			
			removeDigit(world, Config.position(Config.POS_SECONDS, Config.POS_FIRST), (int)(_old[0] - '0'));
			placeDigit(world, Config.position(Config.POS_SECONDS, Config.POS_FIRST), (int)(_newone[0] - '0'));
			
			removeDigit(world, Config.position(Config.POS_SECONDS, Config.POS_SECOND), (int)(_old[1] - '0'));
			placeDigit(world, Config.position(Config.POS_SECONDS, Config.POS_SECOND), (int)(_newone[1] - '0'));
		}
	}
	
	public static void clockInfo(Player sender) {
		
		
	}
	
	public static void clockStart(Player sender) {
		
		
	}
	
	public static void clockStop(Player sender) {
		
		
	}
}
