package onishinji.copyHouse.Listener;

import onishinji.copyHouse.CopyHouse;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class HelloEntityListener extends EntityListener {

	public HelloEntityListener(CopyHouse hello) {
		// TODO Auto-generated constructor stub
	}

	public void onEntityDamage(EntityDamageEvent event)  
	{
		if(event.getEntity() instanceof Player)
		{
			//event.setCancelled(true);
		}
	}
}
