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
		
		Update.running = true;	// Make the clock work
		
		Bukkit.getServer().getPluginManager().registerEvents(new TouchListener(), this);
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
        	
        	if (args.length == 0) {
        		
        		Commands.clockInfo(p);
        		return false;
        	}
        	
        	if (args[0].equalsIgnoreCase("info")) {
        		
        		Commands.clockInfo(p);
        		
        	} else if (args[0].equalsIgnoreCase("update")) {
        		
        		Commands.clockUpdate();
        		
        	} else if (args[0].equalsIgnoreCase("start")) {
        		
        		Commands.clockStart(p);
        		
        	} else if (args[0].equalsIgnoreCase("stop")) {
        		
        		Commands.clockStop(p);
        	}
        }
        
		return false;
	}
}
