package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_CANYONATOR
 *
 * @author Voxel
 */
public class CanyonBrush extends AbstractBrush {

	private static final int SHIFT_LEVEL_MIN = 10;
	private static final int SHIFT_LEVEL_MAX = 60;
	private int yLevel = 10;

	/**
	 *
	 */
	public CanyonBrush() {
		this.setName("Canyon");
	}

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected final void canyon(Chunk chunk, Undo undo) {
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {
				int currentYLevel = this.yLevel;
				for (int y = 63; y < this.getWorld()
					.getMaxHeight(); y++) {
					Block block = chunk.getBlock(x, y, z);
					Block currentYLevelBlock = chunk.getBlock(x, currentYLevel, z);
					undo.put(block);
					undo.put(currentYLevelBlock);
					currentYLevelBlock.setTypeId(block.getTypeId(), false);
					block.setType(Material.AIR);
					currentYLevel++;
				}
				Block block = chunk.getBlock(x, 0, z);
				undo.put(block);
				block.setTypeId(Material.BEDROCK.getId());
				for (int y = 1; y < SHIFT_LEVEL_MIN; y++) {
					Block currentBlock = chunk.getBlock(x, y, z);
					undo.put(currentBlock);
					currentBlock.setType(Material.STONE);
				}
			}
		}
	}

	@Override
	protected void arrow(SnipeData v) {
		Undo undo = new Undo();
		canyon(getTargetBlock().getChunk(), undo);
		v.owner()
			.storeUndo(undo);
	}

	@Override
	protected void powder(SnipeData v) {
		Undo undo = new Undo();
		Chunk targetChunk = getTargetBlock().getChunk();
		for (int x = targetChunk.getX() - 1; x <= targetChunk.getX() + 1; x++) {
			for (int z = targetChunk.getX() - 1; z <= targetChunk.getX() + 1; z++) {
				canyon(getWorld().getChunkAt(x, z), undo);
			}
		}
		v.owner()
			.storeUndo(undo);
	}

	@Override
	public void info(Message message) {
		message.brushName(this.getName());
		message.custom(ChatColor.GREEN + "Shift Level set to " + this.yLevel);
	}

	@Override
	public final void parameters(String[] parameters, SnipeData snipeData) {
		if (parameters[1].equalsIgnoreCase("info")) {
			snipeData.sendMessage(ChatColor.GREEN + "y[number] to set the Level to which the land will be shifted down");
		}
		if (parameters[1].startsWith("y")) {
			int _i = Integer.parseInt(parameters[1].replace("y", ""));
			if (_i < SHIFT_LEVEL_MIN) {
				_i = SHIFT_LEVEL_MIN;
			} else if (_i > SHIFT_LEVEL_MAX) {
				_i = SHIFT_LEVEL_MAX;
			}
			this.yLevel = _i;
			snipeData.sendMessage(ChatColor.GREEN + "Shift Level set to " + this.yLevel);
		}
	}

	protected final int getYLevel() {
		return this.yLevel;
	}

	protected final void setYLevel(int yLevel) {
		this.yLevel = yLevel;
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.canyon";
	}
}
