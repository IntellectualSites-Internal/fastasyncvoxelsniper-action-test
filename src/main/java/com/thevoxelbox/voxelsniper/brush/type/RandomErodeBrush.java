package com.thevoxelbox.voxelsniper.brush.type;

import java.util.Random;
import com.thevoxelbox.voxelsniper.Messages;
import com.thevoxelbox.voxelsniper.sniper.snipe.SnipeData;
import com.thevoxelbox.voxelsniper.sniper.Sniper;
import com.thevoxelbox.voxelsniper.sniper.Undo;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Random-Erode_Brush
 *
 * @author Piotr
 * @author Giltwist (Randomized blockPositionY)
 */
public class RandomErodeBrush extends AbstractBrush {

	private static final double TRUE_CIRCLE = 0.5;

	private BlockWrapper[][][] snap;
	private BlockWrapper[][][] firstSnap;
	private int brushSize;
	private int erodeFace;
	private int fillFace;
	private int erodeRecursion = 1;
	private int fillRecursion = 1;
	private Random generator = new Random();

	public RandomErodeBrush() {
		super("RandomErode");
	}

	private boolean erode(int x, int y, int z) {
		if (this.snap[x][y][z].isSolid()) {
			int i = 0;
			if (!this.snap[x + 1][y][z].isSolid()) {
				i++;
			}
			if (!this.snap[x - 1][y][z].isSolid()) {
				i++;
			}
			if (!this.snap[x][y + 1][z].isSolid()) {
				i++;
			}
			if (!this.snap[x][y - 1][z].isSolid()) {
				i++;
			}
			if (!this.snap[x][y][z + 1].isSolid()) {
				i++;
			}
			if (!this.snap[x][y][z - 1].isSolid()) {
				i++;
			}
			return (i >= this.erodeFace);
		} else {
			return false;
		}
	}

	private boolean fill(int x, int y, int z) {
		if (this.snap[x][y][z].isSolid()) {
			return false;
		} else {
			int d = 0;
			if (this.snap[x + 1][y][z].isSolid()) {
				Block block = this.snap[x + 1][y][z].getNativeBlock();
				this.snap[x][y][z].setType(block.getType());
				d++;
			}
			if (this.snap[x - 1][y][z].isSolid()) {
				Block block = this.snap[x - 1][y][z].getNativeBlock();
				this.snap[x][y][z].setType(block.getType());
				d++;
			}
			if (this.snap[x][y + 1][z].isSolid()) {
				Block block = this.snap[x][y + 1][z].getNativeBlock();
				this.snap[x][y][z].setType(block.getType());
				d++;
			}
			if (this.snap[x][y - 1][z].isSolid()) {
				Block block = this.snap[x][y - 1][z].getNativeBlock();
				this.snap[x][y][z].setType(block.getType());
				d++;
			}
			if (this.snap[x][y][z + 1].isSolid()) {
				Block block = this.snap[x][y][z + 1].getNativeBlock();
				this.snap[x][y][z].setType(block.getType());
				d++;
			}
			if (this.snap[x][y][z - 1].isSolid()) {
				Block block = this.snap[x][y][z - 1].getNativeBlock();
				this.snap[x][y][z].setType(block.getType());
				d++;
			}
			return (d >= this.fillFace);
		}
	}

	private void getMatrix() {
		int brushSize = (this.brushSize + 1) * 2 + 1;
		Block targetBlock = this.getTargetBlock();
		if (this.snap.length == 0) {
			this.snap = new BlockWrapper[brushSize][brushSize][brushSize];
			int sx = targetBlock.getX() - (this.brushSize + 1);
			int sy = targetBlock.getY() - (this.brushSize + 1);
			int sz = targetBlock.getZ() - (this.brushSize + 1);
			for (int x = 0; x < this.snap.length; x++) {
				sz = targetBlock.getZ() - (this.brushSize + 1);
				for (int z = 0; z < this.snap.length; z++) {
					sy = targetBlock.getY() - (this.brushSize + 1);
					for (int y = 0; y < this.snap.length; y++) {
						this.snap[x][y][z] = new BlockWrapper(this.clampY(sx, sy, sz));
						sy++;
					}
					sz++;
				}
				sx++;
			}
			this.firstSnap = this.snap.clone();
		} else {
			this.snap = new BlockWrapper[brushSize][brushSize][brushSize];
			int sx = targetBlock.getX() - (this.brushSize + 1);
			int sy = targetBlock.getY() - (this.brushSize + 1);
			int sz = targetBlock.getZ() - (this.brushSize + 1);
			for (int x = 0; x < this.snap.length; x++) {
				sz = targetBlock.getZ() - (this.brushSize + 1);
				for (int z = 0; z < this.snap.length; z++) {
					sy = targetBlock.getY() - (this.brushSize + 1);
					for (int y = 0; y < this.snap.length; y++) {
						this.snap[x][y][z] = new BlockWrapper(this.clampY(sx, sy, sz));
						sy++;
					}
					sz++;
				}
				sx++;
			}
		}
	}

	private void randomErosion(SnipeData snipeData) {
		Undo undo = new Undo();
		if (this.erodeFace >= 0 && this.erodeFace <= 6) {
			for (int currentErodeRecursion = 0; currentErodeRecursion < this.erodeRecursion; currentErodeRecursion++) {
				getMatrix();
				double brushSizeSquared = Math.pow(this.brushSize + TRUE_CIRCLE, 2);
				for (int z = 1; z < this.snap.length - 1; z++) {
					double zSquared = Math.pow(z - (this.brushSize + 1), 2);
					for (int x = 1; x < this.snap.length - 1; x++) {
						double xSquared = Math.pow(x - (this.brushSize + 1), 2);
						for (int y = 1; y < this.snap.length - 1; y++) {
							if (((xSquared + Math.pow(y - (this.brushSize + 1), 2) + zSquared) <= brushSizeSquared)) {
								if (this.erode(x, y, z)) {
									Block block = this.snap[x][y][z].getNativeBlock();
									block.setType(Material.AIR);
								}
							}
						}
					}
				}
			}
		}
		if (this.fillFace >= 0 && this.fillFace <= 6) {
			double brushSizeSquared = Math.pow(this.brushSize + 0.5, 2);
			for (int currentFillRecursion = 0; currentFillRecursion < this.fillRecursion; currentFillRecursion++) {
				this.getMatrix();
				for (int z = 1; z < this.snap.length - 1; z++) {
					double zSquared = Math.pow(z - (this.brushSize + 1), 2);
					for (int x = 1; x < this.snap.length - 1; x++) {
						double xSquared = Math.pow(x - (this.brushSize + 1), 2);
						for (int y = 1; y < this.snap.length - 1; y++) {
							if (((xSquared + Math.pow(y - (this.brushSize + 1), 2) + zSquared) <= brushSizeSquared)) {
								if (this.fill(x, y, z)) {
									Block block = this.snap[x][y][z].getNativeBlock();
									block.setType(this.snap[x][y][z].getType());
								}
							}
						}
					}
				}
			}
		}
		for (BlockWrapper[][] firstSnapSlice : this.firstSnap) {
			for (BlockWrapper[] firstSnapString : firstSnapSlice) {
				for (BlockWrapper block : firstSnapString) {
					Block nativeBlock = block.getNativeBlock();
					if (block.getNativeType() != nativeBlock.getType()) {
						undo.put(nativeBlock);
					}
				}
			}
		}
		Sniper owner = snipeData.getOwner();
		owner.storeUndo(undo);
	}

	private void randomFilling(SnipeData snipeData) {
		Undo undo = new Undo();
		if (this.fillFace >= 0 && this.fillFace <= 6) {
			double bSquared = Math.pow(this.brushSize + 0.5, 2);
			for (int currentFillRecursion = 0; currentFillRecursion < this.fillRecursion; currentFillRecursion++) {
				this.getMatrix();
				for (int z = 1; z < this.snap.length - 1; z++) {
					double zSquared = Math.pow(z - (this.brushSize + 1), 2);
					for (int x = 1; x < this.snap.length - 1; x++) {
						double xSquared = Math.pow(x - (this.brushSize + 1), 2);
						for (int y = 1; y < this.snap.length - 1; y++) {
							if (((xSquared + Math.pow(y - (this.brushSize + 1), 2) + zSquared) <= bSquared)) {
								if (this.fill(x, y, z)) {
									Block block = this.snap[x][y][z].getNativeBlock();
									block.setType(this.snap[x][y][z].getType());
								}
							}
						}
					}
				}
			}
		}
		if (this.erodeFace >= 0 && this.erodeFace <= 6) {
			double bSquared = Math.pow(this.brushSize + TRUE_CIRCLE, 2);
			for (int currentErodeRecursion = 0; currentErodeRecursion < this.erodeRecursion; currentErodeRecursion++) {
				this.getMatrix();
				for (int z = 1; z < this.snap.length - 1; z++) {
					double zSquared = Math.pow(z - (this.brushSize + 1), 2);
					for (int x = 1; x < this.snap.length - 1; x++) {
						double xSquared = Math.pow(x - (this.brushSize + 1), 2);
						for (int y = 1; y < this.snap.length - 1; y++) {
							if (((xSquared + Math.pow(y - (this.brushSize + 1), 2) + zSquared) <= bSquared)) {
								if (this.erode(x, y, z)) {
									Block block = this.snap[x][y][z].getNativeBlock();
									block.setType(Material.AIR);
								}
							}
						}
					}
				}
			}
		}
		for (BlockWrapper[][] firstSnapSlice : this.firstSnap) {
			for (BlockWrapper[] firstSnapString : firstSnapSlice) {
				for (BlockWrapper block : firstSnapString) {
					Block nativeBlock = block.getNativeBlock();
					if (block.getNativeType() != nativeBlock.getType()) {
						undo.put(nativeBlock);
					}
				}
			}
		}
		Sniper owner = snipeData.getOwner();
		owner.storeUndo(undo);
	}

	@Override
	public final void arrow(SnipeData snipeData) {
		this.brushSize = snipeData.getBrushSize();
		this.snap = new BlockWrapper[0][0][0];
		this.erodeFace = this.generator.nextInt(5) + 1;
		this.fillFace = this.generator.nextInt(3) + 3;
		this.erodeRecursion = this.generator.nextInt(3);
		this.fillRecursion = this.generator.nextInt(3);
		if (this.fillRecursion == 0 && this.erodeRecursion == 0) { // if they are both zero, it will lead to a null pointer exception. Still want to give them a
			// chance to be zero though, for more interestingness -Gav
			this.erodeRecursion = this.generator.nextInt(2) + 1;
			this.fillRecursion = this.generator.nextInt(2) + 1;
		}
		this.randomErosion(snipeData);
	}

	@Override
	public final void powder(SnipeData snipeData) {
		this.brushSize = snipeData.getBrushSize();
		this.snap = new BlockWrapper[0][0][0];
		this.erodeFace = this.generator.nextInt(3) + 3;
		this.fillFace = this.generator.nextInt(5) + 1;
		this.erodeRecursion = this.generator.nextInt(3);
		this.fillRecursion = this.generator.nextInt(3);
		if (this.fillRecursion == 0 && this.erodeRecursion == 0) { // if they are both zero, it will lead to a null pointer exception. Still want to give them a
			// chance to be zero though, for more interestingness -Gav
			this.erodeRecursion = this.generator.nextInt(2) + 1;
			this.fillRecursion = this.generator.nextInt(2) + 1;
		}
		this.randomFilling(snipeData);
	}

	@Override
	public final void info(Messages messages) {
		messages.brushName(this.getName());
		messages.size();
	}

	/**
	 * @author unknown
	 */
	private static final class BlockWrapper {

		private Block nativeBlock;
		private Material nativeType;
		private boolean solid;
		private Material type;

		private BlockWrapper(Block block) {
			this.nativeBlock = block;
			this.nativeType = block.getType();
			this.solid = !this.nativeType.isEmpty() && this.nativeType != Material.WATER && this.nativeType != Material.LAVA;
		}

		public Block getNativeBlock() {
			return this.nativeBlock;
		}

		public Material getNativeType() {
			return this.nativeType;
		}

		public boolean isSolid() {
			return this.solid;
		}

		public Material getType() {
			return this.type;
		}

		public void setType(Material type) {
			this.type = type;
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.randomerode";
	}
}
