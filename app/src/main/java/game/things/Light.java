package game.things;

import game.*;

import java.util.*;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Serializers;
import serialization.Tree;

/**
 * 
 * @author zerzoumax
 *
 */
public class Light extends AbstractGameThing implements Luminant {
	/**
	 * Custom serializer for Light
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Light? "Light" : null;
			}
		});

		union.addSerializer("Light", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Light in = (Light)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("renderer", new Tree(in.renderer)));
				out.add(new Tree.Entry("luminance", Serializers.Serializer_Integer.write(in.luminance())));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Light(world,
					in.find("renderer").value(),
					Serializers.Serializer_Integer.read(in.find("luminance")));
			}
		});
	}
	
	
	private int luminance;
	private final String renderer;

	public Light(GameWorld w, String r, int l){
		super(w);
		renderer = r;
		luminance = l;
		update();
	}

	@Override
	public String name(){
		return "Light";
	}

	@Override
	public String renderer(){
		return renderer;
	}

	@Override
	public Map<String, String> info(){
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("luminance", String.valueOf(luminance));
		return out;
	}

	/**
	 * Getter
	 * @return luminance level of the light
	 */
	public int luminance(){
		return luminance;
	}

	/**
	 * Setter
	 * @return luminance level of the light
	 */
	public int luminance(int s){
		luminance = s;
		update();
		return s;
	}

	/**
	 * Returns the maximum render level
	 */
	public int renderLevel(){
		return 9000;
	}
}
