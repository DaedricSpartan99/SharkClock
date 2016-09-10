package it.sharkcraft.sharkclock;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharkClock extends JavaPlugin {
	
	public static SharkClock plugin;

	@Override
	public void onEnable() {
		
		SharkClock.plugin = this;
		Config.config = getConfig();
		
		Config.init();
		Config.loadblocks();
		
		@SuppressWarnings("unused")
		Update up = new Update();
		
		Bukkit.getServer().getPluginManager().registerEvents(new TouchListener(), this);
		
		Update.active = Config.active();
	}
	
	@Override
	public void onDisable() {
		
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
        	
        	Commands.shtime(p);
        	
        } else if (cmd.getName().equalsIgnoreCase("shdate")){
        	
        	Commands.shdate(p);
        	
        } else if (cmd.getName().equalsIgnoreCase("clock")) {
        		
        	Commands.clockInfo(p);
        	
        } else if (args[0].equalsIgnoreCase("clock info")) {
    		
    		Commands.clockInfo(p);
    		
    	} else if (args[0].equalsIgnoreCase("clock update")) {
    		
    		Commands.clockUpdate();
    		
    	} else if (args[0].equalsIgnoreCase("clock start")) {
    		
    		Commands.clockStart(p);
    		
    	} else if (args[0].equalsIgnoreCase("clock stop")) {
    		
    		Commands.clockStop(p);
    		
    	} else if (args[0].equalsIgnoreCase("clock add")) {
    		
    		if (args.length < 1) {
    			
    			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments " + cmd.getUsage());
    			return false;
    		}
    		
    		Commands.clockAdd(p, args[0]);
    		
    	} else if (args[0].equalsIgnoreCase("clock remove") || args[0].equalsIgnoreCase("clock rm")) {
    		
    		if (args.length < 2) {
    			
    			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments " + cmd.getUsage());
    			return false;
    		}
    		
    		Commands.clockRemove(p, args[0], args[1]);
    		
    	} else if (args[0].equalsIgnoreCase("clock position") || args[0].equalsIgnoreCase("clock pos")) {
    		
    		if (args.length < 2) {
    			
    			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments " + cmd.getUsage());
    			return false;
    		}
    		
    		Commands.clockPosition(p, args[0], args[1]);
    		
    	} else if (args[0].equalsIgnoreCase("clock set")) {
    		
    		if (args.length < 2) {
    			
    			p.sendMessage("§8[§c§l!§8] §9SharkClock> Too few arguments " + cmd.getUsage());
    			return false;
    		}
    		
    		Commands.clockSet(p, args[0], args[1]);
    	}
        
		return false;
	}
}
