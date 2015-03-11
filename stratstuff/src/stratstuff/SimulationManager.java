package stratstuff;

import java.util.ArrayList;

public class SimulationManager implements Updatable {

	private ArrayList<WorldSimulator> worldSimulators;
	private ArrayList<GalaxySimulator> galaxySimulators;

	public SimulationManager() {
		worldSimulators = new ArrayList<WorldSimulator>();
		galaxySimulators = new ArrayList<GalaxySimulator>();
	}

	public World getWorldWithName(String name) {
		for (WorldSimulator simulator : worldSimulators) {
			if (simulator.getWorld().getName().equals(name)) {
				return simulator.getWorld();
			}
		}

		System.out.println("world not found: " + name);
		return null;
	}

	public Galaxy getGalaxyWithName(String name) {
		for (GalaxySimulator simulator : galaxySimulators) {
			if (simulator.getGalaxy().getName().equals(name)) {
				return simulator.getGalaxy();
			}
		}

		System.out.println("galaxy not found: " + name);
		return null;
	}

	public void addSimulator(Updatable simulator) {
		if (simulator instanceof WorldSimulator)
			worldSimulators.add((WorldSimulator) simulator);

		if (simulator instanceof GalaxySimulator)
			galaxySimulators.add((GalaxySimulator) simulator);
	}

	@Override
	public void update() {
		for (WorldSimulator sim : worldSimulators) {
			sim.update();
		}
		for (GalaxySimulator sim : galaxySimulators) {
			sim.update();
		}
	}

	public void initialCreationOfEdges() {
		for (WorldSimulator s : worldSimulators) {
			s.getWorld().initialCreationOfEdges();
		}
	}

	public void saveWorlds(Core core) {
		for (WorldSimulator s : worldSimulators) {
			PersistanceManager.saveWorld(core, s.getWorld(), s.getWorld()
					.getName());
		}
	}

	public void initLights() {
		for (WorldSimulator s : worldSimulators) {
			s.getLightManager().initLights();
		}

	}

	public void saveGalaxies() {
		for (GalaxySimulator sim : galaxySimulators) {
			PersistanceManager.saveGalaxy(sim.getGalaxy());
		}
	}
}
