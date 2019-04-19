package com.thevoxelbox.voxelsniper.brush;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Entity_Brush
 *
 * @author Piotr
 */
public class EntityBrush extends AbstractBrush {

	private EntityType entityType = EntityType.ZOMBIE;

	/**
	 *
	 */
	public EntityBrush() {
		this.setName("Entity");
	}

	private void spawn(SnipeData v) {
		for (int x = 0; x < v.getBrushSize(); x++) {
			try {
				this.getWorld()
					.spawn(this.getLastBlock()
						.getLocation(), this.entityType.getEntityClass());
			} catch (IllegalArgumentException exception) {
				v.sendMessage(ChatColor.RED + "Cannot spawn entity!");
			}
		}
	}

	@Override
	protected final void arrow(SnipeData v) {
		this.spawn(v);
	}

	@Override
	protected final void powder(SnipeData v) {
		this.spawn(v);
	}

	@SuppressWarnings("deprecation")
	@Override
	public final void info(Message message) {
		message.brushMessage(ChatColor.LIGHT_PURPLE + "Entity brush" + " (" + this.entityType.getName() + ")");
		message.size();
	}

	@SuppressWarnings("deprecation")
	@Override
	public final void parameters(String[] parameters, SnipeData snipeData) {
		if (parameters[1].equalsIgnoreCase("info")) {
			snipeData.sendMessage(ChatColor.BLUE + "The available entity types are as follows:");
			String names = Arrays.stream(EntityType.values())
				.map(currentEntity -> ChatColor.AQUA + " | " + ChatColor.DARK_GREEN + currentEntity.getName())
				.collect(Collectors.joining("", "", ChatColor.AQUA + " |"));
			snipeData.sendMessage(names);
		} else {
			EntityType currentEntity = EntityType.fromName(parameters[1]);
			if (currentEntity != null) {
				this.entityType = currentEntity;
				snipeData.sendMessage(ChatColor.GREEN + "Entity type set to " + this.entityType.getName());
			} else {
				snipeData.sendMessage(ChatColor.RED + "This is not a valid entity!");
			}
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.entity";
	}
}
