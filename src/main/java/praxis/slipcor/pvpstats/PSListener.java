package praxis.slipcor.pvpstats;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener class
 * 
 * @author slipcor
 *
 */

public class PSListener implements Listener {
	private final PVPStats plugin;
	
	private final Map<String, String> lastKill = new HashMap<String, String>();

	public PSListener(final PVPStats instance) {
		this.plugin = instance;
        }
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDeath(final PlayerDeathEvent event) {
		
		if (event.getEntity() == null || event.getEntity().getKiller() == null ||
				plugin.ignoresWorld(event.getEntity().getWorld().getName())) {
			return;
		}

		final Player attacker = event.getEntity().getKiller();
		final Player player = event.getEntity();
		
		if (plugin.getConfig().getBoolean("checkabuse")) {
			
			if (lastKill.containsKey(attacker.getName()) && lastKill.get(attacker.getName()).equals(player.getName())) {
				return; // no logging!
			}
			
			lastKill.put(attacker.getName(), player.getName());
		}
		// here we go, PVP!
		PSMySQL.incDeath(player);
		PSMySQL.incKill(attacker);
	}
}