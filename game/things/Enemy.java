package game.things;

import game.*;
import game.things.EquipmentGameThing.Slot;

import java.util.*;
import util.*;

import serialization.*;

/**
 * 
 * @author wheelemaxw & zerzoumax
 *Enemy is a type of character, which has the option of being aggressive
 *or not. Either way wanders around the mape, and if aggressive attacks anything
 *within the defined radius
 *
 *Has an inventory for potentially dropped items
 *
 */
public class Enemy extends Character implements Namable {
	
	/**
	 * Custom serializers for Enemy
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Enemy? "enemy" : null;
			}
		});

		union.addSerializer("enemy", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Enemy in = (Enemy)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.type())));
				out.add(new Tree.Entry("name", new Tree(in.name)));
				out.add(new Tree.Entry("start", LocationS.s(world).write(in.start)));
				out.add(new Tree.Entry("wander", Serializers.Serializer_Integer.write(in.wanderdist)));
				out.add(new Tree.Entry("inventory", Container.serializer(union.serializer(), world).write(in.inventory)));
				out.add(new Tree.Entry("aggressive", new Tree(Boolean.toString(in.aggressive))));
				out.add(new Tree.Entry("radius", Serializers.Serializer_Integer.write(in.radius)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Enemy(world,
					in.find("type").value(),
					in.find("name").value(),
					LocationS.s(world).read(in.find("start")),
					Serializers.Serializer_Integer.read(in.find("wander")),
					Container.serializer(union.serializer(), world).read(in.find("inventory")),
					Boolean.parseBoolean(in.find("aggresive").value()),
					Serializers.Serializer_Integer.read(in.find("radius")));
			}
		});
	}

	private String name;
	private final Location start;
	private int wanderdist;
	private int radius;
	private boolean aggressive;
	private final Container inventory;


	public Enemy(GameWorld world, String t, String n, Location sl, int wd, Container inv, boolean agr,int rad){
		super(world, t);
		name = n;
		aggressive = agr;
		radius = rad;
		start = sl;
		wanderdist = wd;
		if(inv == null)
			inventory = new Container(world);
		else
			inventory = inv;
		health(1000);
		setStats(12,12,12,12);
		update();
		new Runnable(){
			public void run(){
				Location l = start, ml = location();
				Player p = null;

				if(aggressive && ml instanceof Level.Location){
					Level.Location ll = (Level.Location)ml;
					Iterable<GameThing> box = ll.level().portion(new Position(ll.position().x()-radius,ll.position().y()-radius),new Position(ll.position().x()+radius,ll.position().y()+radius));
					for(GameThing g : box){
						if(g instanceof Player)
							p = (Player)g;
					}
					if(p!=null)
						attack(p);
					else if(l instanceof Level.Location && ml instanceof Level.Location && (!busy() || ((Level.Location)l).dist((Level.Location)ml) > 2*wanderdist))
						moveTo(((Level.Location)l).next(Direction.SOUTH, (int)(Math.random()*wanderdist*2 - wanderdist)).next(Direction.EAST, (int)(Math.random()*wanderdist*2 - wanderdist)));
				}
				else{
					if(l instanceof Level.Location && ml instanceof Level.Location && (!busy() || ((Level.Location)l).dist((Level.Location)ml) > 2*wanderdist))
						moveTo(((Level.Location)l).next(Direction.SOUTH, (int)(Math.random()*wanderdist*2 - wanderdist)).next(Direction.EAST, (int)(Math.random()*wanderdist*2 - wanderdist)));
				}
				if(!forgotten())
					world().schedule(this, 3000);
			}
		}.run();
	}

	public String name(){
		return name;
	}

	public String name(String s){
		return name = s;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("follow");
		out.add("attack");
		out.add("examine");
		return out;
	}

	public void interact(String name, Player who){
		if(name.equals("follow"))
			who.follow(this);
		else if(name.equals("attack"))
			who.attack(this);
		else super.interact(name, who);
	}
	

	public Container inventory(){
		return inventory;
	}

	public void damage(int amt, Character from){
		if(!dying()){
			super.damage(amt, from);
			world().emitSay(this, from, from.name() + " hurts " + name() + " and their health is now " + health());
			attack(from);
		}
		if(health() <= 0 && dying()){
			final Enemy g = this;
			world().schedule(new Runnable(){
				public void run(){
				final game.things.Corpse cp = new Corpse(g.world(),"corpse_1", g.inventory());
				cp.location(g.location());
				LocationS.NOWHERE.put(g);
				world().forget(g);
				}
			}, 1500);
		}
	}
	
	public int walkdistance() {
		return wanderdist;
	}
	
	public int walkdistance(int d) {
		return wanderdist = d;
	}
	
	public boolean aggressive(){
		return aggressive;
	}
	
	public void aggressive(boolean b){
		aggressive = b;
	}
	
	public int radius(){
		return radius;
	}
	
	public void radius(int i){
		radius = i;
	}
	

}
