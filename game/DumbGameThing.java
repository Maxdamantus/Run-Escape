package game;

import java.io.Serializable;
import java.util.*;

import serialization.*;

public class DumbGameThing extends AbstractGameThing.AbstractDumbGameThing {
	private List<String> interactions;
	private String defaultInteraction, renderer, name;
	private Map<String, Serializable> userArguments;

	public DumbGameThing(long g, List<String> i, String d, String r, String n, Map<String, Serializable> u){
		this(null, g, i, d, r, n, u);
	}

	public DumbGameThing(GameWorld w, long g, List<String> i, String d, String r, String n, Map<String, Serializable> u){
		super(w, g);
		interactions = i;
		defaultInteraction = d;
		renderer = r;
		name = n;
		userArguments = u;
		if(w != null)
			w.introduce(this, gid());
	}
	
	@Override
	public Map<String, Serializable> userArguments() { // Melby fixed
		return userArguments != null? userArguments : super.userArguments();
	}
	
	@Override
	public List<String> interactions() { // Melby fixed
		return interactions != null? interactions : super.interactions();
	}
	
	@Override
	public String defaultInteraction() { // Melby fixed
		return defaultInteraction != null? defaultInteraction : super.defaultInteraction();
	}

	@Override
	public String name(){
		return name != null? name : super.name();
	}

	public DumbGameThing(GameWorld w, long g){
		this(w, g, null, null, null, null, null);
	}

	public DumbGameThing(GameThing gt){
		this(gt.gid(), gt.interactions(), gt.defaultInteraction(), gt.renderer(), gt.name(), gt.userArguments());
	}

	public void update(DumbGameThing o){
		if(o.interactions != null)
			interactions = o.interactions;
		if(o.defaultInteraction != null)
			defaultInteraction = o.defaultInteraction;
		if(o.userArguments != null)
			userArguments = o.userArguments;
		if(o.renderer != null)
			renderer = o.renderer;
		if(o.name != null)
			name = o.name;
	}

	public String renderer() {
		return renderer != null? renderer : super.renderer();
	}

	public static Serializer<DumbGameThing> serializer(final GameWorld w){
		return new Serializer<DumbGameThing>(){
			private final Serializer<List<String>> interS = new Serializers.Nullable<List<String>>(new Serializers.List<String>(Serializers.Serializer_String));
			private final Serializer<String> nullStringS = new Serializers.Nullable<String>(Serializers.Serializer_String);
			// private static final Serializer<Map<String, Object>> = ???

			public Tree write(DumbGameThing in){
				Tree out = new Tree();
				out.add(new Tree.Entry("gid", Serializers.Serializer_Long.write(in.gid())));
				out.add(new Tree.Entry("interactions", interS.write(in.interactions)));
				out.add(new Tree.Entry("defaultinteraction", nullStringS.write(in.defaultInteraction)));
				out.add(new Tree.Entry("renderer", nullStringS.write(in.renderer)));
				out.add(new Tree.Entry("name", nullStringS.write(in.name)));
				out.add(new Tree.Entry("userArgs", new Serializers.Map<String, Serializable>(Serializers.Serializer_String, new Serializers.JSerializable<Serializable>()).write(in.userArguments)));
				return out;
			}

			public DumbGameThing read(Tree in){				
				return new DumbGameThing(
					Serializers.Serializer_Long.read(in.find("gid")),
					interS.read(in.find("interactions")),
					nullStringS.read(in.find("defaultinteraction")),
					nullStringS.read(in.find("renderer")),
					nullStringS.read(in.find("name")),
					new Serializers.Map<String, Serializable>(Serializers.Serializer_String, new Serializers.JSerializable<Serializable>()).read(in.find("userArgs")));
			}
		};
	}
}
