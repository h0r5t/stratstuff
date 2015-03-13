package stratstuff;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DynamicTexture {

	private static final int rotationGoalUp = 0;
	private static final int rotationGoalDown = 180;
	private static final int rotationGoalLeft = 270;
	private static final int rotationGoalRight = 90;

	private static final int turnDirectionClockWise = 0;
	private static final int turnDirectionCounterClockWise = 1;

	private BufferedImage image;
	private int imageRotation = 0;
	private int rotationGoal = 0;
	private int turnDirection = 0;
	private boolean hasTurned = false;
	private boolean allFinished = false;
	private boolean turningToFaceWP = false;
	private int eventID;

	private int currentTurnRate;
	private int turnRate;
	private int moveSpeed;

	private int countdown;

	public DynamicTexture(BufferedImage image, int turnRate, int moveSpeed) {
		this.image = image;
		this.turnRate = turnRate;
		this.moveSpeed = moveSpeed;
		this.countdown = moveSpeed;
		currentTurnRate = turnRate;
	}

	public void updateRotationDirection(WorldPoint currentWP, WorldPoint nextWP) {
		if (currentWP == null || nextWP == null) {
			return;
		}
		if (currentWP.getX() < nextWP.getX()) {
			rotationGoal = rotationGoalRight;
			if (imageRotation > 90 && imageRotation <= 270) {
				turnDirection = turnDirectionCounterClockWise;
			} else {
				turnDirection = turnDirectionClockWise;
			}
		} else if (currentWP.getX() > nextWP.getX()) {
			rotationGoal = rotationGoalLeft;
			if (imageRotation > 90 && imageRotation <= 270) {
				turnDirection = turnDirectionClockWise;
			} else {
				turnDirection = turnDirectionCounterClockWise;
			}
		} else if (currentWP.getY() > nextWP.getY()) {
			rotationGoal = rotationGoalUp;
			if (imageRotation > 0 && imageRotation <= 180) {
				turnDirection = turnDirectionCounterClockWise;
			} else {
				turnDirection = turnDirectionClockWise;
			}
		} else if (currentWP.getY() < nextWP.getY()) {
			rotationGoal = rotationGoalDown;
			if (imageRotation > 0 && imageRotation <= 180) {
				turnDirection = turnDirectionClockWise;
			} else {
				turnDirection = turnDirectionCounterClockWise;
			}
		}
	}

	public void turnDirectionVectorSpace(SpacePosition currentPos,
			SpacePosition posToFace) {
		if (currentPos == null || posToFace == null) {
			return;
		}

		turningToFaceWP = false;

		rotationGoal = getAngle2D(currentPos, posToFace);
		hasTurned = false;

		if (rotationGoal > imageRotation) {
			if (Math.abs(rotationGoal - imageRotation) > 180) {
				turnDirection = turnDirectionCounterClockWise;
			} else {
				turnDirection = turnDirectionClockWise;
			}
		} else {
			if (Math.abs(rotationGoal - imageRotation) > 180) {
				turnDirection = turnDirectionClockWise;
			} else {
				turnDirection = turnDirectionCounterClockWise;
			}
		}
	}

	private int getAngle2D(SpacePosition p1, SpacePosition p2) {
		double angle = Math.toDegrees(Math.atan2(p2.getMacX() - p1.getMacX(),
				p1.getMacY() - p2.getMacY()));

		if (angle < 0)
			angle += 360;

		return (int) angle;
	}

	public void turnDirectionVector(WorldPoint currentWP, WorldPoint wpToFace,
			int eventID) {
		if (currentWP == null || wpToFace == null) {
			return;
		}

		turningToFaceWP = true;
		this.eventID = eventID;

		rotationGoal = getAngle2D(currentWP, wpToFace);
		hasTurned = false;

		if (rotationGoal > imageRotation) {
			if (Math.abs(rotationGoal - imageRotation) > 180) {
				turnDirection = turnDirectionCounterClockWise;
			} else {
				turnDirection = turnDirectionClockWise;
			}
		} else {
			if (Math.abs(rotationGoal - imageRotation) > 180) {
				turnDirection = turnDirectionClockWise;
			} else {
				turnDirection = turnDirectionCounterClockWise;
			}
		}

	}

	private int getAngle2D(WorldPoint p1, WorldPoint p2) {
		double angle = Math.toDegrees(Math.atan2(p2.getX() - p1.getX(),
				p1.getY() - p2.getY()));

		if (angle < 0)
			angle += 360;

		return (int) angle;
	}

	public void update() {
		countdown--;
		if (imageRotation >= 360) {
			imageRotation -= 360;
		} else if (imageRotation < 0) {
			imageRotation += 360;
		}
		if (!hasTurned && Math.abs(imageRotation - rotationGoal) < turnRate) {
			currentTurnRate = 1;
			if (rotationGoal > imageRotation) {
				if (Math.abs(rotationGoal - imageRotation) > 180) {
					turnDirection = turnDirectionCounterClockWise;
				} else {
					turnDirection = turnDirectionClockWise;
				}
			} else {
				if (Math.abs(rotationGoal - imageRotation) > 180) {
					turnDirection = turnDirectionClockWise;
				} else {
					turnDirection = turnDirectionCounterClockWise;
				}
			}
		}
		if (!hasTurned && Math.abs(imageRotation - rotationGoal) <= 2) {
			hasTurned = true;
			currentTurnRate = turnRate;
			imageRotation = rotationGoal;
		}
		if (!hasTurned) {
			if (turnDirection == turnDirectionClockWise) {
				imageRotation += currentTurnRate;
			} else {
				imageRotation -= currentTurnRate;
			}
		}
		if (hasTurned && countdown <= 0) {
			allFinished = true;
			if (turningToFaceWP == true) {
				Core.tellFrontend(FrontendMessaging.eventOccurred(eventID,
						"true"));
				turningToFaceWP = false;
			}
		}
	}

	public BufferedImage getImage() {
		BufferedImage rotatedImage = rotateImage(image, imageRotation);
		return rotatedImage;
	}

	private static BufferedImage rotateImage(BufferedImage src, double degrees) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(
				Math.toRadians(degrees), src.getWidth() / 2,
				src.getHeight() / 2);
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(), src.getType());
		Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
		g.setTransform(affineTransform);
		g.drawImage(src, 0, 0, null);
		return rotatedImage;
	}

	public boolean hasTurned() {
		if (allFinished) {
			hasTurned = false;
			allFinished = false;
			countdown = moveSpeed;
			return true;
		}
		return false;
	}

	public int getCurrentAngle() {
		return imageRotation;
	}
}
