package it.sharkcraft.sharkclock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Commands {
	
	public static boolean shtime(Player sender) {
		
		String h = String.valueOf(Time.hours());
		String m = String.valueOf(Time.minutes());
		String s = String.valueOf(Time.seconds());
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.timeMessage()
				.replaceAll("%hours%", h).replaceAll("%minutes%", m)
				.replaceAll("%seconds%", s));
		
		return true;
	}
	
	public static boolean shdate(Player sender) {
		
		String y = String.valueOf(Time.year());
		String m = String.valueOf(Time.month());
		String d = String.valueOf(Time.day());
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.dateMessage()
				.replaceAll("%year%", y).replaceAll("%month%", m)
				.replaceAll("%day%", d));
		
		return true;
	}
	
	private static void removeDigit(Location location, int digit) {
		
		for (Location i : Config.blocks(digit)) {
			
			Location abs = location.clone();
			abs.add(i);
			//System.out.println("Removing " + abs.toString());
			abs.getBlock().setType(Material.AIR);
		}
	}
	
	private static void placeDigit(Location location, int digit) {
		
		Location[] locs = Config.blocks(digit);
		
		for (int i = 0; i < locs.length; i++) {
		
			Location abs = location.clone();
			abs.add(locs[i]);
			/*System.out.println("Position " + location.toString());
			System.out.println("Relative " + locs[i].toString());
			System.out.println("Absolute " + abs.toString());*/
			abs.getBlock().setType(Config.material(digit, i));
		}
	}
	
	private static int[] digitsOf(int number) {
		
		int[] out = { number / 10, number % 10 };
		
		return out;
	}
	
	public static boolean clockUpdate() {
		
		int old, newone;
		
		if ((old = Time.current.hour) != (newone = Time.hours())) {
			
			int[] _old = digitsOf(old);
			int[] _newone = digitsOf(newone);
			
			//System.out.println("Hours: " + _old[0] + _old[1] + "  " + _newone[0] + _newone[1]);
			
			removeDigit(Config.position(Config.POS_HOURS, Config.POS_FIRST), _old[0]);
			placeDigit(Config.position(Config.POS_HOURS, Config.POS_FIRST), _newone[0]);
			
			removeDigit(Config.position(Config.POS_HOURS, Config.POS_SECOND), _old[1]);
			placeDigit(Config.position(Config.POS_HOURS, Config.POS_SECOND), _newone[1]);
		}
		
		if ((old = Time.current.min) != (newone = Time.minutes())) {
			
			int[] _old = digitsOf(old);
			int[] _newone = digitsOf(newone);
			
			//System.out.println("Minutes: " + _old[0] + _old[1] + "  " + _newone[0] + _newone[1]);
			
			removeDigit(Config.position(Config.POS_MINUTES, Config.POS_FIRST), _old[0]);
			placeDigit(Config.position(Config.POS_MINUTES, Config.POS_FIRST), _newone[0]);
			
			removeDigit(Config.position(Config.POS_MINUTES, Config.POS_SECOND), _old[1]);
			placeDigit(Config.position(Config.POS_MINUTES, Config.POS_SECOND), _newone[1]);
		}
		
		if ((old = Time.current.sec) != (newone = Time.seconds())) {
			
			int[] _old = digitsOf(old);
			int[] _newone = digitsOf(newone);
			
			//System.out.println("Seconds: " + _old[0] + _old[1] + "  " + _newone[0] + _newone[1]);
			
			removeDigit(Config.position(Config.POS_SECONDS, Config.POS_FIRST), _old[0]);
			placeDigit(Config.position(Config.POS_SECONDS, Config.POS_FIRST), _newone[0]);
			
			removeDigit(Config.position(Config.POS_SECONDS, Config.POS_SECOND), _old[1]);
			placeDigit(Config.position(Config.POS_SECONDS, Config.POS_SECOND), _newone[1]);
		}
		
		return true;
	}
	
	public static boolean clockInfo(Player sender) {
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.infoMessage().
				replaceAll("%hours%", String.valueOf(Time.current.hour)).
				replaceAll("%minutes%", String.valueOf(Time.current.min)).
				replaceAll("%seconds%", String.valueOf(Time.current.sec)).
				replaceAll("%running%", String.valueOf(Update.active)));
		
		return true;
	}
	
	public static boolean clockStart(Player sender) {
		
		Update.active = true;
		Config.setActive(true);
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.startMessage());
		
		return true;
	}
	
	public static boolean clockStop(Player sender) {
		
		Update.active = false;
		Config.setActive(false);
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + Config.stopMessage());
		
		return true;
	}
	
	public static boolean clockAdd(Player sender, String _digit) {
		
		int digit;
		
		try {
			
			digit = Integer.parseInt(_digit);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit must be a number");
			return false;
		}
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return false;
		}
		
		if (digit < 0 || digit > 9) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit argument to high or too low, 0 - 9 permitted");
			return false;
		}
		
		TouchListener.digit = digit;
		
		TouchListener.index = Config.sizeofDigit(digit);
		
		TouchListener.signal = TouchListener.SIGNAL_BLOCK;
		TouchListener.active = true;
		
		return true;
	}
	
	public static boolean clockSet(Player sender, String _digit, String _index) {
		
		int digit, index;
		
		try {
			
			digit = Integer.parseInt(_digit);
			index = Integer.parseInt(_index);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit and index must be numbers");
			return false;
		}
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return false;
		}
		
		if (digit < 0 || digit > 9) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit argument to high or too low, 0 - 9 permitted");
			return false;
		}
		
		TouchListener.digit = digit;
		
		if (index >= Config.sizeofDigit(digit)) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Index argument must exist");
			return false;
		}
		
		TouchListener.index = index;
		
		TouchListener.signal = TouchListener.SIGNAL_BLOCK;
		TouchListener.active = true;
		
		return true;
	}
	
	public static boolean clockPosition(Player sender, String pos, String digit) {
		
		if (pos.equalsIgnoreCase("hours") || pos.equalsIgnoreCase("h")) {
			
			TouchListener.pos_arg = Config.POS_HOURS;
			
		} else if (pos.equalsIgnoreCase("minutes") || pos.equalsIgnoreCase("min")) {
			
			TouchListener.pos_arg = Config.POS_MINUTES;
			
		} else if (pos.equalsIgnoreCase("seconds") || pos.equalsIgnoreCase("sec")) {
			
			TouchListener.pos_arg = Config.POS_SECONDS;
			
		} else {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Wrong argument: first argument -> \"hours, minutes, seconds\" only permitted");
			return false;
		}
		
		if (digit.equalsIgnoreCase("first") || digit.equalsIgnoreCase("f") || digit.equals("1")) {
			
			TouchListener.pos_digit = Config.POS_FIRST;
			
		} else if (digit.equalsIgnoreCase("second") || digit.equalsIgnoreCase("s") || digit.equals("2")) {
			
			TouchListener.pos_digit = Config.POS_SECOND;
			
		} else {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Wrong argument: second argument -> \"first, second\" only permitted");
			return false;
		}
		
		TouchListener.signal = TouchListener.SIGNAL_POSITION;
		TouchListener.active = true;
		
		return true;
	}
	
	public static boolean clockRemove(Player sender, String _digit, String _index) {
		
		int digit, index;
		
		try {
			
			digit = Integer.parseInt(_digit);
			index = Integer.parseInt(_index);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit and index must be numbers");
			return false;
		}
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return false;
		}
		
		if (digit < 0 || digit > 9) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit argument to high or too low, 0 - 9 permitted");
			return false;
		}
		
		if (index >= Config.sizeofDigit(digit)) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Index argument must exist");
			return false;
		}
		
		Config.removeBlock(digit, index);
		
		return true;
	}
	
	public static boolean clockSetVolume(Player sender, String _digit) {
		
		if (TouchListener.active) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> An event is already active, right click to a block to finish it");
			return false;
		}
		
		int digit;
		
		try {
			
			digit = Integer.parseInt(_digit);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit must be a number");
			return false;
		}
		
		TouchListener.signal = TouchListener.SIGNAL_VOLUME;
		TouchListener.volume_prompt = TouchListener.VOLUME_FIRST;
		TouchListener.digit = digit;
		TouchListener.active = true;
		
		return true;
	}
	
	public static boolean clockShDigit(Player sender, String _digit) {
		
		int digit;
		
		try {
			
			digit = Integer.parseInt(_digit);
			
		} catch (NumberFormatException e) {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Digit must be a number");
			return false;
		}
		
		Location[] bl = Config.blocks(digit);
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> Size = " + bl.length);
		
		for (int i = 0; i < bl.length; i++)
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Index: " + i + " Location: " + 
					bl[i].toVector().toString() + " Material: " + Config.material(digit, i));
		
		return true;
	}
	
	public static boolean clockShPosition(Player sender, String pos, String digit) {
		
		String pos_arg, pos_digit;
		
		if (pos.equalsIgnoreCase("hours") || pos.equalsIgnoreCase("h")) {
			
			pos_arg = Config.POS_HOURS;
			
		} else if (pos.equalsIgnoreCase("minutes") || pos.equalsIgnoreCase("min")) {
			
			pos_arg = Config.POS_MINUTES;
			
		} else if (pos.equalsIgnoreCase("seconds") || pos.equalsIgnoreCase("sec")) {
			
			pos_arg = Config.POS_SECONDS;
			
		} else {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Wrong argument: first argument -> \"hours, minutes, seconds\" only permitted");
			return false;
		}
		
		if (digit.equalsIgnoreCase("first") || digit.equalsIgnoreCase("f") || digit.equals("1")) {
			
			pos_digit = Config.POS_FIRST;
			
		} else if (digit.equalsIgnoreCase("second") || digit.equalsIgnoreCase("s") || digit.equals("2")) {
			
			pos_digit = Config.POS_SECOND;
			
		} else {
			
			sender.sendMessage("§8[§c§l!§8] §9SharkClock> Wrong argument: second argument -> \"first, second\" only permitted");
			return false;
		}
		
		Location p = Config.position(pos_arg, pos_digit);
		
		sender.sendMessage("§8[§c§l!§8] §9SharkClock> " + p.toVector().toString());
		
		return true;
	}
}
