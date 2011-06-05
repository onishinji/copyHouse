package onishinji.copyHouse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UndoCommand implements CommandExecutor {

	private CopyHouse plugin;
	public UndoCommand(CopyHouse copyHouse) {
		this.plugin = copyHouse;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		CopyHouseManager.getInstance().undoLastPaste((Player)sender);
		return false;
	}

}
