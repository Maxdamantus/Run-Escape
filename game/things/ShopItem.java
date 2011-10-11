package game.things;

import game.*;

import java.util.*;

import serialization.*;

public class ShopItem extends Stackable {
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof ShopItem? "shopitem" : null;
			}
		});

		union.addSerializer("shopitem", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				ShopItem in = (ShopItem)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("amount", new Tree(String.valueOf(in.amount()))));
				out.add(new Tree.Entry("cost", new Tree(String.valueOf(in.cost))));
				out.add(new Tree.Entry("item", in.tree));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new ShopItem(world, union.serializer().read(in.find("item")), Serializers.Serializer_Integer.read(in.find("amount")), Serializers.Serializer_Integer.read(in.find("cost")));
			}
		});
	}

	private final Serializer<GameThing> reader;
	private final Tree tree;
	private final DumbGameThing prototype;
	private int cost;

	public ShopItem(GameWorld w, GameThing g, int a, int c){
		super(w, a);
		reader = ThingsS.makeSerializer(w);
		tree = reader.write(g);
		cost = c;
		prototype = new DumbGameThing(g);
	}

	public int cost(){
		return cost;
	}

	public int cost(int s){
		return cost = s;
	}

	private final Object type = new Object();
	public Object type(){
		return type;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		out.add("buy");
		out.add("value");
		return out;
	}

	public void interact(String name, Player who){
		if(name.equals("value"))
		world().emitSay(this, who, prototype.name() + " costs " + cost + " gold");
		else if(name.equals("buy")){
			if(amount() <= 0){
				world().emitSay(this, who, "Out of stock.");
				return;
			}
			boolean okay = cost == 0;
			if(!okay)
				for(GameThing gt : who.inventory())
					if(gt instanceof Coins && ((Coins)gt).amount() > cost){
						((Coins)gt).subtract(cost);
						okay = true;
						break;
					}else if(gt instanceof Coins && ((Coins)gt).amount() == cost){
						LocationS.NOWHERE.put(gt);
						world().forget(gt);
						okay = true;
						break;
					}
			if(okay){
				try{
					who.inventory().put(reader.read(tree));
					subtract(1);
				}catch(ParseException e){
					throw new RuntimeException("wtf");
				}
			}else
				world().emitSay(this, who, "Can't afford that!");
		}
	}

	public String name(){
		return prototype.name();
	}

	public String renderer(){
		return prototype.renderer();
	}
}
