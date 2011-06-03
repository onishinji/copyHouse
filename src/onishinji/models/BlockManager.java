package onishinji.models;

import java.io.Serializable;

import onishinji.hello.CopyHouseManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

public class BlockManager  implements Serializable{
  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Class<? extends Block> classname;
	private byte data;
	private Material type;

	private MyLocation location;
	private float direction;

	
	
	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public Location getLocation(World world) {
		return new Location(world, location.getX(), location.getY(), location.getZ()); 
	}

	public void setLocation(MyLocation location) {
		this.location = location;
	} 
	
	public void setDirection(float direction) {
		this.direction = direction;
	}

	public float getDirection() {
		return direction;
	}   
	
	public void setData(byte data) {
		this.data = data;
	}

	public byte getData() {
		return data;
	}
	
	public BlockManager()
	{
		
	}
	
	public BlockManager convertBlock(Block block)
	{ 
		this.classname = block.getClass();
		this.setData(block.getData());
		this.type = block.getType();				
		this.setDirection(block.getLocation().getPitch());		
		this.location = new MyLocation(block.getLocation());
		 
		return this;
	}

}
