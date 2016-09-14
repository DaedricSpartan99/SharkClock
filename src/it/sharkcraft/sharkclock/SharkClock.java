package it.sharkcraft.sharkclock;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharkClock extends JavaPlugin {
	
	public static SharkClock plugin;

	public void onEnable() {
		
		System.out.println("Starting SharkClock");
		
		SharkClock.plugin = this;
		
		Config.init();
		Config.loadblocks();
		
		@SuppressWarnings("unused")
		Update up = new Update();
		
		Bukkit.getServer().getPluginManager().registerEvents(new TouchListener(), this);
		
		Update.active = Config.active();
		
		Config.save();
		Config.reload();
	}
	
	public void onDisable() {
		
		System.out.println("Closing SharkClock");
		Config.save();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			sender.sendMessage("You are not a Player!!");
			return true;
		}
		
		Player p = (Player) sender;
        
		if (cmd.getName().equalsIgnoreCase("shtime")){
        	
        	return Commands.shtime(p);
        	
        } else if (cmd.getName().equalsIgnoreCase("shdate")){
        	
        	return Commands.shdate(p);
        	
        } else if (cmd.getName().equalsIgnoreCase("clock")) {
        	
        	if (args.length == 0) {
        		
        		return Commands.clockInfo(p);
        	}
        	
        	if (args[0].equalsIgnoreCase("info")) {
        		
        		return Commands.clockInfo(p);
        		
        	} else if (args[0].equalsIgnoreCase("update")) {
        		
        		return Commands.clockUpdate();
        		
        	} else if (args[0].equalsIgnoreCase("start")) {
        		
        		return Commands.clockStart(p);
        		
        	} else if (args[0].equalsIgnoreCase("stop")) {
        		
        		return Commands.clockStop(p);
        		
        	} else if (args[0].equalsIgnoreCase("add")) {
        		
        		if (args.length < 2) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockAdd(p, args[1]);
        		
        	} else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")) {
        		
        		if (args.length < 3) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockRemove(p, args[1], args[2]);
        		
        	} else if (args[0].equalsIgnoreCase("position") || args[0].equalsIgnoreCase("pos")) {
        		
        		if (args.length < 3) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockPosition(p, args[1], args[2]);
        		
        	} else if (args[0].equalsIgnoreCase("set")) {
        		
        		if (args.length < 3) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockSet(p, args[1], args[2]);
        		
        	} else if (args[0].equalsIgnoreCase("save")) {
        		
        		Config.init();
        		Config.save();
        		Config.reload();
        		Config.loadblocks();
        		return true;
        		
        	} else if (args[0].equalsIgnoreCase("shpos")) {
        		
        		if (args.length < 3) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockShPosition(p, args[1], args[2]);
        		
        	} else if (args[0].equalsIgnoreCase("shdigit")) {
        		
        		if (args.length < 2) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockShDigit(p, args[1]);
        		
        	} else if (args[0].equalsIgnoreCase("setvolume")) {
        		
        		if (args.length < 2) {
        			
        			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments");
        			return false;
        		}
        		
        		return Commands.clockSetVolume(p, args[1]);
        		
        	}
        }  
        
		return false;
	}
}
