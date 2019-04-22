package com.thevoxelbox.voxelsniper.brush.type;

import com.thevoxelbox.voxelsniper.Messages;
import com.thevoxelbox.voxelsniper.sniper.snipe.SnipeData;
import com.thevoxelbox.voxelsniper.sniper.Sniper;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author MikeMatrix
 */
public class WarpBrush extends AbstractBrush {

	public WarpBrush() {
		super("Warp");
	}

	@Override
	public final void info(Messages messages) {
		messages.brushName(this.getName());
	}

	@Override
	public final void arrow(SnipeData snipeData) {
		Sniper owner = snipeData.getOwner();
		Player player = owner.getPlayer();
		if (player == null) {
			return;
		}
		Block lastBlock = this.getLastBlock();
		if (lastBlock == null) {
			return;
		}
		Location location = lastBlock.getLocation();
		Location playerLocation = player.getLocation();
		location.setPitch(playerLocation.getPitch());
		location.setYaw(playerLocation.getYaw());
		player.teleport(location);
	}

	@Override
	public final void powder(SnipeData snipeData) {
		Sniper owner = snipeData.getOwner();
		Player player = owner.getPlayer();
		if (player == null) {
			return;
		}
		Block lastBlock = this.getLastBlock();
		if (lastBlock == null) {
			return;
		}
		Location location = lastBlock.getLocation();
		Location playerLocation = player.getLocation();
		location.setPitch(playerLocation.getPitch());
		location.setYaw(playerLocation.getYaw());
		getWorld().strikeLightning(location);
		player.teleport(location);
		getWorld().strikeLightning(location);
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.warp";
	}
}
