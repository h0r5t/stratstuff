package stratstuff;

import java.util.ArrayList;

public class BiomeGenerator {

	private ArrayList<Biome> biomes;

	public BiomeGenerator() {
		biomes = new ArrayList<Biome>();
	}

	public World generateAndModify(World world) {
		generateBiomes(world);
		activateBiomes(world);
		return world;
	}

	private void generateBiomes(World world) {
		Area3D wholeWorld = new Area3D(0, 0, 0, GameSettings.WORLD_WIDTH,
				GameSettings.WORLD_HEIGHT, GameSettings.WORLD_DEPTH);
		Area3D surface = new Area3D(0, 0, 0, GameSettings.WORLD_WIDTH,
				GameSettings.WORLD_HEIGHT, 1);

		biomes.add(new BiomeWoods(surface));
	}

	private void activateBiomes(World world) {
		for (Biome b : biomes) {
			b.modify(world);
		}
	}

}
