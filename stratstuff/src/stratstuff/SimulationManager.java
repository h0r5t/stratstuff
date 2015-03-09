package stratstuff;

import java.util.ArrayList;

public class SimulationManager implements Updatable {

	ArrayList<WorldSimulator> worldSimulators;

	public SimulationManager() {
		worldSimulators = new ArrayList<WorldSimulator>();
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

	public void addSimulator(WorldSimulator w) {
		worldSimulators.add(w);
	}

	@Override
	public void update() {
		for (WorldSimulator sim : worldSimulators) {
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
			PersistanceManager.save(core, s.getWorld(), s.getWorld().getName());
		}
	}

	public void initLights() {
		for (WorldSimulator s : worldSimulators) {
			s.getLightManager().initLights();
		}

	}
}
