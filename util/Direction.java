package util;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;

	/**
	 * Return the direction to the `d` of this one.
	 *
	 * @param d The direction to apply
	 * @return The composed direction
	 */

	public Direction compose(Direction d){
		return values()[(ordinal() + d.ordinal())%4];
	}
}
