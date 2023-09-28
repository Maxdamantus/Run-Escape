package game.things;

import game.*;

import java.util.*;
import util.*;

import serialization.*;

public class Stairs extends AbstractGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Stairs? "stairs" : null;
			}
		});

		union.addSerializer("stairs", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Stairs in = (Stairs)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("renderer", new Tree(in.renderer)));
				out.add(new Tree.Entry("up", Serializers.Serializer_Integer.write(in.up)));
				out.add(new Tree.Entry("down", Serializers.Serializer_Integer.write(in.down)));
				out.add(new Tree.Entry("allow", Direction.SERIALIZER.write(in.allow)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Stairs(world, in.find("renderer").value(),
						Serializers.Serializer_Integer.read(in.find("up")),
						Serializers.Serializer_Integer.read(in.find("down")),
						Direction.SERIALIZER.read(in.find("allow")));
			}
		});
	}

	private int up, down;
	private final String renderer;
	private final Direction allow;

	public Stairs(GameWorld w, String r, int u, int d, Direction a){
		super(w);
		up = u;
		down = d;
		renderer = r;
		allow = a;
		update();
	}

	public String renderer(){
		return renderer;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		if(up > 0)
			out.add("go up");
		if(down > 0)
			out.add("go down");
		out.addAll(super.interactions());
		return out;
	}

	public void walkAndGo(final boolean goup, final Player p){
		final Location l = location();
		if(l instanceof Level.Location)
			p.moveTo(((Level.Location)l).rotate(allow).next(), 0, new Runnable(){
				public void run(){
					Location pl = p.location();
					if(pl instanceof Level.Location){
						if(goup)
							((Level.Location)l).rotate(allow.compose(Direction.SOUTH)).next().above(up).put(p);
						else
							((Level.Location)l).rotate(allow.compose(Direction.SOUTH)).next().below(down).put(p);
					}
				}
			});
	}

	public void interact(String name, Player who){
		if(name.equals("go up") && up > 0)
			walkAndGo(true, who);
		else if(name.equals("go down") && down > 0)
			walkAndGo(false, who);
		else
			super.interact(name, who);
	}

	public String name(){
		return "Stairs";
	}

	public boolean canWalkInto(Direction d, Character c){
		return false;
	}

	public int up(){ return up; }
	public int up(int s){ return up = s; }
	public int down(){ return down; }
	public int down(int s){ return down = s; }

	public int renderLevel(){
		return ui.isometric.abstractions.IsoSquare.WALL;
	}
}
