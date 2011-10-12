package game.tests;
import static org.junit.Assert.*;
import game.*;
import game.things.*;
import util.*;
import serialization.*;

import org.junit.*;

public class GameTests {
	public GameWorld random(GameWorld gw){
		int n = 10000 + (int)(Math.random()*40000);
		for(int x = 0; x < n; x++){
			GameThing g;
			switch((int)(Math.random()*5)){
				case 0:
					g = new Pouch(gw);
					break;
				case 1:
					g = new Wall(gw, "ruby");
					break;
				case 2:
					g = new Door(gw, "wall_grey_1_door_open", "wall_grey_1_door_closed", Math.random() > 0.5, "hello" + Math.random());
					break;
				case 3:
					g = new Coins(gw, (int)(Math.random()*50000));
					break;
				default:
					g = new Stairs(gw, "armour_tunic", (int)(Math.random()*10 - 5), (int)(Math.random()*10 - 5), Direction.values()[(int)(Math.random()*4)]);
					break;
			}
			gw.level((int)(Math.random()*50 - 25)).location(new Position((int)(Math.random()*100 - 50), (int)(Math.random()*100 - 50)), Direction.values()[(int)(Math.random()*4)]).put(g);
		}
		for(int x = 0; x < 50; x++)
			gw.level((int)(Math.random()*50 - 25)).luminance((int)(Math.random()*1000 - 500));
		return gw;
	}
	
	public GameWorld random(){
		GameWorld r = new GameWorld();
		random(r);
		return r;
	}
	
	public void isSuper(GameWorld a, GameWorld b){
		for(int l : b.levels())
			gtloop: for(GameThing gt : b.level(l)){
				for(GameThing gts : a.level(l).location(((Level.Location)gt.location()).position(), ((Level.Location)gt.location()).direction()).contents()){
					if(gts instanceof Pouch && gt instanceof Pouch)
						continue gtloop;
					if(gts instanceof Wall && gt instanceof Wall)
						continue gtloop;
					if(gts instanceof Door && gt instanceof Door){
						Door da = (Door)gts, db = (Door)gt;
						if(da.renderer().equals(db.renderer()) && ((Level.Location)da.location()).direction().equals(((Level.Location)db.location()).direction()))
							continue gtloop;
					}
					if(gts instanceof Coins && gt instanceof Coins){
						Coins da = (Coins)gts, db = (Coins)gt;
						if(da.info().equals(db.info()))
							continue gtloop;
					}
					if(gts instanceof Stairs && gt instanceof Stairs){
						Stairs da = (Stairs)gts, db = (Stairs)gt;
						if(da.up() == db.up() && da.down() == db.down())
							continue gtloop;
					}
					if(gts instanceof DumbGameThing && gt instanceof DumbGameThing){
						if(gts.renderer().equals(gt.renderer()) && gts.info().equals(gt.info()) && gts.interactions().equals(gt.interactions()))
							continue gtloop;
					}
				}
				assertTrue("Not a super!", false);
			}
	}

	@Test public void testSerialize(){
		try{
        		GameWorld first = random();
        		Tree tree = first.toTree();
        		GameWorld second = new GameWorld();
        		second.fromTree(tree);
        		isSuper(second, first);
        		isSuper(first, second);
		}catch(Exception e){
			assertTrue(e.toString(), false);
		}
	}
	
	@Test public void testDeltas(){
		GameWorld server = random();
		GameWorld client1 = new GameWorld();
		server.allDeltas(new DeltaWatcher(){
			public void delta(WorldDelta wd){
				wd.apply(client1);
			}
		});
	}
}
