package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Volt-Meter_Brush
 *
 * @author Gavjenks
 */
public class VoltMeterBrush extends AbstractBrush {

	/**
	 *
	 */
	public VoltMeterBrush() {
		this.setName("VoltMeter");
	}

	@SuppressWarnings("deprecation")
	private void data(SnipeData v) {
		Block block = this.clampY(this.getTargetBlock()
			.getX(), this.getTargetBlock()
			.getY(), this.getTargetBlock()
			.getZ());
		byte data = block.getData();
		v.sendMessage(ChatColor.AQUA + "Blocks until repeater needed: " + data);
	}

	private void volt(SnipeData v) {
		Block block = this.clampY(this.getTargetBlock()
			.getX(), this.getTargetBlock()
			.getY(), this.getTargetBlock()
			.getZ());
		boolean indirect = block.isBlockIndirectlyPowered();
		boolean direct = block.isBlockPowered();
		v.sendMessage(ChatColor.AQUA + "Direct Power? " + direct + " Indirect Power? " + indirect);
		v.sendMessage(ChatColor.BLUE + "Top Direct? " + block.isBlockFacePowered(BlockFace.UP) + " Top Indirect? " + block.isBlockFaceIndirectlyPowered(BlockFace.UP));
		v.sendMessage(ChatColor.BLUE + "Bottom Direct? " + block.isBlockFacePowered(BlockFace.DOWN) + " Bottom Indirect? " + block.isBlockFaceIndirectlyPowered(BlockFace.DOWN));
		v.sendMessage(ChatColor.BLUE + "East Direct? " + block.isBlockFacePowered(BlockFace.EAST) + " East Indirect? " + block.isBlockFaceIndirectlyPowered(BlockFace.EAST));
		v.sendMessage(ChatColor.BLUE + "West Direct? " + block.isBlockFacePowered(BlockFace.WEST) + " West Indirect? " + block.isBlockFaceIndirectlyPowered(BlockFace.WEST));
		v.sendMessage(ChatColor.BLUE + "North Direct? " + block.isBlockFacePowered(BlockFace.NORTH) + " North Indirect? " + block.isBlockFaceIndirectlyPowered(BlockFace.NORTH));
		v.sendMessage(ChatColor.BLUE + "South Direct? " + block.isBlockFacePowered(BlockFace.SOUTH) + " South Indirect? " + block.isBlockFaceIndirectlyPowered(BlockFace.SOUTH));
	}

	@Override
	protected final void arrow(SnipeData v) {
		this.volt(v);
	}

	@Override
	protected final void powder(SnipeData v) {
		this.data(v);
	}

	@Override
	public final void info(Message message) {
		message.brushName(this.getName());
		message.brushMessage("Right click with arrow to see if blocks/faces are powered. Powder measures wire current.");
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.voltmeter";
	}
}
