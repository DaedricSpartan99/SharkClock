package it.sharkcraft.sharkclock;

import org.bukkit.Bukkit;
import static it.sharkcraft.sharkclock.SharkClock.*;

public class Update implements Runnable {
	
	public static final int SEC = 20;
	public static volatile boolean active = false;	// !!! volatile makes it thread reachable !!!
	int iter;
	
	public Update() {
		
		iter = 0;
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, SEC);
	}

	@Override
	public void run() {
		
		if (iter == 30) {
			
			iter = 0;
			Config.save();
			Config.reload();
		}
		
		if (active) {
		
			Commands.clockUpdate();
		}
		
		iter++;
	}

}
