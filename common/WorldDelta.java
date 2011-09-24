package common;

import serialization.*;
import serialization.util.Serializers;

import client.model.*;

public class WorldDelta {
	public static interface Action {
		public execute(GameModel world);
		public Serializer<? extends Action> serializer();
	}

	public static class Put implements Action {
		private final int gid;
		private final Location loc;

		public execute(GameModel world){
			if(loc instanceof LevelLocation){
			}
		}
	}

	private final Action action;

	public WorldDelta(Action a){
		action = a;
	}

	public final static Serializer<WorldDelta> serializer = new Serializer<WorldDelta>(){
		public Tree write(WorldDelta d){
			aoe
		}
	}
}
