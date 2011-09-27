package util;

import serialization.*;

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

	public static Serializer<Direction> SERIALIZER = new Serializer<Direction>(){
		public Tree write(Direction in){
			return new Tree(in.toString());
		}

		public Direction read(Tree in){
			return valueOf(in.value());
		}
	};
}
