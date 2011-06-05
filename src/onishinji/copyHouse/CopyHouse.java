package onishinji.copyHouse;

import onishinji.copyHouse.Listener.HelloBlockListener;
import onishinji.copyHouse.Listener.HelloEntityListener;
import onishinji.copyHouse.Listener.HelloPlayerListener;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class CopyHouse extends JavaPlugin {

	private final HelloPlayerListener playerListener = new HelloPlayerListener(this);
	private final HelloBlockListener blockListener = new HelloBlockListener(this);
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

	private final HelloEntityListener entityListener = new HelloEntityListener(this);

	public Collection<Block> blocksCopied;
	public boolean canCopie;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener,
				Priority.Normal, this); 

		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener,
				Priority.Normal, this); 

		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener,
				Priority.Normal, this);

		// Register our commands
		getCommand("copy-list").setExecutor( new ListCommand(this)); 
		getCommand("copy-undo").setExecutor( new UndoCommand(this)); 
		
		// crée le répertoire des structures
		new File("plugins/copyHouse").mkdir(); 
		
		getCommand("plane").setExecutor(new DebugCommand(this, "plane"));

		// EXAMPLE: Custom code, here we just output some info so we can check
		// all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");

		this.canCopie = false;
	}

}
