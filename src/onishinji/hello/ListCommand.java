package onishinji.hello;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {
 
	private CopyHouse plugin;
	
	public ListCommand(CopyHouse hello) {
		this.plugin = hello;
	} 

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		  
		String liste = "Liste des structures: ";
		for (String mapKey : CopyHouseManager.getInstance().motifs.keySet()) {
			// utilise ici hashMap.get(mapKey) pour accéder aux valeurs
			liste = liste + mapKey +", ";
		}
		
		Player player = (Player)sender;
		player.sendMessage(liste);
		
		 
		return false;
	}
 

}
