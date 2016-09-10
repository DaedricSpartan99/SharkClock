package it.sharkcraft.sharkclock;

import org.bukkit.Location;
import org.bukkit.Material;
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
	
	private static void removeDigit(Location location, int digit) {
		
		for (Vector i : Config.blocks(digit)) {
			
			location.add(i).getBlock().setType(Material.AIR);
		}
	}
	
	private static void placeDigit(Location location, int digit) {
		
		for (Vector i : Config.blocks(digit)) {
		
			location.add(i).getBlock().setType(Material.STONE);
		}
	}
	
	public static void clockUpdate() {
		
		int old, newone;
		
		if ((old = Time.current.hour) != (newone = Time.hours())) {
			
			char[] _old = String.valueOf(old).toCharArray();
			char[] _newone = String.valueOf(newone).toCharArray();
			
			removeDigit(Config.position(Config.POS_HOURS, Config.POS_FIRST), (int)(_old[0] - '0'));
			placeDigit(Config.position(Config.POS_HOURS, Config.POS_FIRST), (int)(_newone[0] - '0'));
			
			removeDigit(Config.position(Config.POS_HOURS, Config.POS_SECOND), (int)(_old[1] - '0'));
			placeDigit(Config.position(Config.POS_HOURS, Config.POS_SECOND), (int)(_newone[1] - '0'));
		}
		
		if ((old = Time.current.min) != (newone = Time.minutes())) {
			
			char[] _old = String.valueOf(old).toCharArray();
			char[] _newone = String.valueOf(newone).toCharArray();
			
			removeDigit(Config.position(Config.POS_MINUTES, Config.POS_FIRST), (int)(_old[0] - '0'));
			placeDigit(Config.position(Config.POS_MINUTES, Config.POS_FIRST), (int)(_newone[0] - '0'));
			
			removeDigit(Config.position(Config.POS_MINUTES, Config.POS_SECOND), (int)(_old[1] - '0'));
			placeDigit(Config.position(Config.POS_MINUTES, Config.POS_SECOND), (int)(_newone[1] - '0'));
		}
		
		if ((old = Time.current.sec) != (newone = Time.seconds())) {
			
			char[] _old = String.valueOf(old).toCharArray();
			char[] _newone = String.valueOf(newone).toCharArray();
			
			removeDigit(Config.position(Config.POS_SECONDS, Config.POS_FIRST), (int)(_old[0] - '0'));
			placeDigit(Config.position(Config.POS_SECONDS, Config.POS_FIRST), (int)(_newone[0] - '0'));
			
			removeDigit(Config.position(Config.POS_SECONDS, Config.POS_SECOND), (int)(_old[1] - '0'));
			placeDigit(Config.position(Config.POS_SECONDS, Config.POS_SECOND), (int)(_newone[1] - '0'));
		}
	}
	
	public static void clockInfo(Player sender) {
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.infoMessage().
				replaceAll("%hours%", String.valueOf(Time.current.hour)).
				replaceAll("%minutes%", String.valueOf(Time.current.min)).
				replaceAll("%seconds%", String.valueOf(Time.current.sec)).
				replaceAll("%running%", String.valueOf(Update.active)));
	}
	
	public static void clockStart(Player sender) {
		
		Update.active = true;
		Config.setActive(true);
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.startMessage());
	}
	
	public static void clockStop(Player sender) {
		
		Update.active = false;
		Config.setActive(false);
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.stopMessage());
	}
	
	public static void clockAdd(Player sender, String _digit) {
		
		int digit;
		
		try {
			
			digit = Integer.parseInt(_digit);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit must be a number");
			return;
		}
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return;
		}
		
		if (digit < 0 || digit > 9) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit argument to high or too low, 0 - 9 permitted");
			return;
		}
		
		TouchListener.digit = digit;
		
		TouchListener.index = Config.sizeofDigit(digit);
		
		TouchListener.signal = TouchListener.SIGNAL_BLOCK;
		TouchListener.active = true;
	}
	
	public static void clockSet(Player sender, String _digit, String _index) {
		
		int digit, index;
		
		try {
			
			digit = Integer.parseInt(_digit);
			index = Integer.parseInt(_index);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit and index must be numbers");
			return;
		}
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return;
		}
		
		if (digit < 0 || digit > 9) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit argument to high or too low, 0 - 9 permitted");
			return;
		}
		
		TouchListener.digit = digit;
		
		if (index >= Config.sizeofDigit(digit)) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Index argument must exist");
			return;
		}
		
		TouchListener.index = index;
		
		TouchListener.signal = TouchListener.SIGNAL_BLOCK;
		TouchListener.active = true;
	}
	
	public static void clockPosition(Player sender, String pos, String digit) {
		
		if (pos.equalsIgnoreCase("hours") || pos.equalsIgnoreCase("h")) {
			
			TouchListener.pos_arg = Config.POS_HOURS;
			
		} else if (pos.equalsIgnoreCase("minutes") || pos.equalsIgnoreCase("min")) {
			
			TouchListener.pos_arg = Config.POS_MINUTES;
			
		} else if (pos.equalsIgnoreCase("seconds") || pos.equalsIgnoreCase("sec")) {
			
			TouchListener.pos_arg = Config.POS_SECONDS;
			
		} else {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Wrong argument: first argument -> \"hours, minutes, seconds\" only permitted");
			return;
		}
		
		if (digit.equalsIgnoreCase("first") || digit.equalsIgnoreCase("f") || digit.equals("1")) {
			
			TouchListener.pos_digit = Config.POS_FIRST;
			
		} else if (digit.equalsIgnoreCase("second") || digit.equalsIgnoreCase("s") || digit.equals("2")) {
			
			TouchListener.pos_digit = Config.POS_SECOND;
			
		} else {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Wrong argument: second argument -> \"first, second\" only permitted");
			return;
		}
		
		TouchListener.signal = TouchListener.SIGNAL_POSITION;
		TouchListener.active = true;
	}
	
	public static void clockRemove(Player sender, String _digit, String _index) {
		
		int digit, index;
		
		try {
			
			digit = Integer.parseInt(_digit);
			index = Integer.parseInt(_index);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit and index must be numbers");
			return;
		}
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return;
		}
		
		if (digit < 0 || digit > 9) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit argument to high or too low, 0 - 9 permitted");
			return;
		}
		
		if (index >= Config.sizeofDigit(digit)) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Index argument must exist");
			return;
		}
	}
}
