package com.thevoxelbox.voxelsniper.brush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.bukkit.util.noise.PerlinNoiseGenerator;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Heat_Ray
 *
 * @author Gavjenks
 */
public class HeatRayBrush extends AbstractBrush {

	/**
	 * @author MikeMatrix
	 */
	private enum FlameableBlock {
		WOOD(Material.LEGACY_LEGACY_WOOD),
		SAPLING(Material.LEGACY_LEGACY_SAPLING),
		LOG(Material.LEGACY_LEGACY_LOG),
		LEAVES(Material.LEGACY_LEGACY_LEAVES),
		SPONGE(Material.LEGACY_LEGACY_SPONGE),
		WEB(Material.LEGACY_LEGACY_WEB),
		LONG_GRASS(Material.LEGACY_LEGACY_LONG_GRASS),
		DEAD_BUSH(Material.LEGACY_LEGACY_DEAD_BUSH),
		WOOL(Material.LEGACY_LEGACY_WOOL),
		YELLOW_FLOWER(Material.LEGACY_LEGACY_YELLOW_FLOWER),
		RED_ROSE(Material.LEGACY_LEGACY_RED_ROSE),
		TORCH(Material.LEGACY_LEGACY_TORCH),
		FIRE(Material.LEGACY_LEGACY_FIRE),
		WOOD_STAIRS(Material.LEGACY_LEGACY_WOOD_STAIRS),
		CROPS(Material.LEGACY_LEGACY_CROPS),
		SIGN_POST(Material.LEGACY_LEGACY_SIGN_POST),
		WOODEN_DOOR(Material.LEGACY_LEGACY_WOODEN_DOOR),
		LADDER(Material.LEGACY_LEGACY_LADDER),
		WALL_SIGN(Material.LEGACY_LEGACY_WALL_SIGN),
		WOOD_PLATE(Material.LEGACY_LEGACY_WOOD_PLATE),
		SNOW(Material.LEGACY_LEGACY_SNOW),
		ICE(Material.LEGACY_LEGACY_ICE),
		SUGAR_CANE_BLOCK(Material.LEGACY_LEGACY_SUGAR_CANE_BLOCK),
		FENCE(Material.LEGACY_LEGACY_FENCE),
		TRAP_DOOR(Material.LEGACY_LEGACY_TRAP_DOOR),
		VINE(Material.LEGACY_LEGACY_VINE),
		FENCE_GATE(Material.LEGACY_LEGACY_FENCE_GATE),
		WATER_LILLY(Material.LEGACY_LEGACY_WATER_LILY);

		private Material material;

		FlameableBlock(Material material) {
			this.material = material;
		}
	}

	private static final double REQUIRED_OBSIDIAN_DENSITY = 0.6;
	private static final double REQUIRED_COBBLE_DENSITY = 0.5;
	private static final double REQUIRED_FIRE_DENSITY = -0.25;
	private static final double REQUIRED_AIR_DENSITY = 0;

	private static final List<Material> FLAMABLE_BLOCKS = new ArrayList<>();

	private int octaves = 5;
	private double frequency = 1;

	private double amplitude = 0.3;

	static {
		for (FlameableBlock flameableBlock : FlameableBlock.values()) {
			FLAMABLE_BLOCKS.add(flameableBlock.material);
		}
	}

	/**
	 * Default Constructor.
	 */
	public HeatRayBrush() {
		this.setName("Heat Ray");
	}

	/**
	 * Heat Ray executer.
	 */
	public final void heatRay(SnipeData v) {
		PerlinNoiseGenerator generator = new PerlinNoiseGenerator(new Random());
		Vector targetLocation = this.getTargetBlock()
			.getLocation()
			.toVector();
		Location currentLocation = new Location(this.getTargetBlock()
			.getWorld(), 0, 0, 0);
		Undo undo = new Undo();
		for (int z = v.getBrushSize(); z >= -v.getBrushSize(); z--) {
			for (int x = v.getBrushSize(); x >= -v.getBrushSize(); x--) {
				for (int y = v.getBrushSize(); y >= -v.getBrushSize(); y--) {
					currentLocation.setX(this.getTargetBlock()
						.getX() + x);
					currentLocation.setY(this.getTargetBlock()
						.getY() + y);
					currentLocation.setZ(this.getTargetBlock()
						.getZ() + z);
					if (currentLocation.toVector()
						.isInSphere(targetLocation, v.getBrushSize())) {
						Block currentBlock = currentLocation.getBlock();
						if (currentBlock == null || currentBlock.getType() == Material.LEGACY_CHEST) {
							continue;
						}
						if (currentBlock.isLiquid()) {
							undo.put(currentBlock);
							currentBlock.setType(Material.LEGACY_AIR);
							continue;
						}
						if (FLAMABLE_BLOCKS.contains(currentBlock.getType())) {
							undo.put(currentBlock);
							currentBlock.setType(Material.LEGACY_FIRE);
							continue;
						}
						if (!currentBlock.getType()
							.equals(Material.LEGACY_AIR)) {
							double airDensity = generator.noise(currentLocation.getX(), currentLocation.getY(), currentLocation.getZ(), this.octaves, this.frequency, this.amplitude);
							double fireDensity = generator.noise(currentLocation.getX(), currentLocation.getY(), currentLocation.getZ(), this.octaves, this.frequency, this.amplitude);
							double cobbleDensity = generator.noise(currentLocation.getX(), currentLocation.getY(), currentLocation.getZ(), this.octaves, this.frequency, this.amplitude);
							double obsidianDensity = generator.noise(currentLocation.getX(), currentLocation.getY(), currentLocation.getZ(), this.octaves, this.frequency, this.amplitude);
							if (obsidianDensity >= REQUIRED_OBSIDIAN_DENSITY) {
								undo.put(currentBlock);
								if (currentBlock.getType() != Material.LEGACY_OBSIDIAN) {
									currentBlock.setType(Material.LEGACY_OBSIDIAN);
								}
							} else if (cobbleDensity >= REQUIRED_COBBLE_DENSITY) {
								undo.put(currentBlock);
								if (currentBlock.getType() != Material.LEGACY_COBBLESTONE) {
									currentBlock.setType(Material.LEGACY_COBBLESTONE);
								}
							} else if (fireDensity >= REQUIRED_FIRE_DENSITY) {
								undo.put(currentBlock);
								if (currentBlock.getType() != Material.LEGACY_FIRE) {
									currentBlock.setType(Material.LEGACY_FIRE);
								}
							} else if (airDensity >= REQUIRED_AIR_DENSITY) {
								undo.put(currentBlock);
								if (currentBlock.getType() != Material.LEGACY_AIR) {
									currentBlock.setType(Material.LEGACY_AIR);
								}
							}
						}
					}
				}
			}
		}
		v.getOwner()
			.storeUndo(undo);
	}

	@Override
	protected final void arrow(SnipeData v) {
		this.heatRay(v);
	}

	@Override
	protected final void powder(SnipeData v) {
		this.heatRay(v);
	}

	@Override
	public final void info(Message message) {
		message.brushName(this.getName());
		message.custom(ChatColor.GREEN + "Octaves: " + this.octaves);
		message.custom(ChatColor.GREEN + "Amplitude: " + this.amplitude);
		message.custom(ChatColor.GREEN + "Frequency: " + this.frequency);
		message.size();
	}

	@Override
	public final void parameters(String[] parameters, SnipeData snipeData) {
		for (int i = 1; i < parameters.length; i++) {
			String parameter = parameters[i].toLowerCase();
			if (parameter.equalsIgnoreCase("info")) {
				snipeData.sendMessage(ChatColor.GOLD + "Heat Ray brush Parameters:");
				snipeData.sendMessage(ChatColor.AQUA + "/b hr oct[int] -- Octaves parameter for the noise generator.");
				snipeData.sendMessage(ChatColor.AQUA + "/b hr amp[float] -- Amplitude parameter for the noise generator.");
				snipeData.sendMessage(ChatColor.AQUA + "/b hr freq[float] -- Frequency parameter for the noise generator.");
			}
			if (parameter.startsWith("oct")) {
				this.octaves = Integer.valueOf(parameter.replace("oct", ""));
				snipeData.getMessage()
					.custom(ChatColor.GREEN + "Octaves: " + this.octaves);
			} else if (parameter.startsWith("amp")) {
				this.amplitude = Double.valueOf(parameter.replace("amp", ""));
				snipeData.getMessage()
					.custom(ChatColor.GREEN + "Amplitude: " + this.amplitude);
			} else if (parameter.startsWith("freq")) {
				this.frequency = Double.valueOf(parameter.replace("freq", ""));
				snipeData.getMessage()
					.custom(ChatColor.GREEN + "Frequency: " + this.frequency);
			}
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.heatray";
	}
}
