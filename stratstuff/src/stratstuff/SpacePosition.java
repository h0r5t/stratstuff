package stratstuff;

public class SpacePosition {

	private Sector sector;
	private double micX, micY;

	public SpacePosition(Sector sector, double microX, double microY) {
		this.sector = sector;
		micX = microX;
		micY = microY;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public double getMicX() {
		return micX;
	}

	public void setMicX(double micX) {
		this.micX = micX;
	}

	public double getMicY() {
		return micY;
	}

	public void setMicY(double micY) {
		this.micY = micY;
	}

	public double getMacX() {
		return sector.getXPos() * Sector.sectorWidth + micX;
	}

	public double getMacY() {
		return sector.getYPos() * Sector.sectorHeight + micY;
	}

	public boolean moveBy(double vectorX, double vectorY, Galaxy galaxy,
			FloatingObject objectMoved) {
		micX += vectorX;
		micY += vectorY;

		int xLimit = Sector.sectorWidth;
		int yLimit = Sector.sectorHeight;

		int xmoved = 0;
		int ymoved = 0;
		while (true) {
			if (micX > xLimit) {
				micX -= xLimit;
				xmoved = 1;
			} else if (micX < 0) {
				micX += xLimit;
				xmoved = -1;
			}
			if (micY > yLimit) {
				micY -= yLimit;
				ymoved = 1;
			} else if (micY < 0) {
				micY += yLimit;
				ymoved = -1;
			}

			if (xmoved != 0 || ymoved != 0) {
				sector.removeObject(objectMoved);

				int newXPos = sector.getXPos() + xmoved;
				int newYPos = sector.getYPos() + ymoved;
				if (!galaxy.isInBounds(newXPos, newYPos)) {
					return false;
				}
				Sector newSector = galaxy.getSector(newXPos, newYPos);
				this.sector = newSector;
				this.sector.addObject(objectMoved);
				xmoved = 0;
				ymoved = 0;
			} else {
				break;
			}
		}
		return true;
	}

	public double[] calculateVectorTo(double targetMacX, double targetMacY,
			int moveSpeed) {
		double[] results = new double[2];

		double distanceX = targetMacX - getMacX();
		double distanceY = targetMacY - getMacY();
		double distanceDirection = Math.sqrt(distanceX * distanceX + distanceY
				* distanceY);
		results[0] = distanceX * (moveSpeed / distanceDirection);
		results[1] = distanceY * (moveSpeed / distanceDirection);

		return results;
	}

	public boolean isNearTo(SpacePosition pos, int allowedDeviationRadius) {
		if (Math.abs(getMacX() - pos.getMacX()) <= allowedDeviationRadius) {
			if (Math.abs(getMacY() - pos.getMacY()) <= allowedDeviationRadius) {
				return true;
			}
		}

		return false;
	}
}
