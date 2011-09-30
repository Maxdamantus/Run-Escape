package game;

import java.util.*;

import serialization.*;

public class DumbGameThing extends AbstractGameThing.AbstractDumbGameThing {
	private List<String> interactions;
	private String defaultInteraction, renderer;
	private Map<String, Object> userArguments;

	public DumbGameThing(int g, List<String> i, String d, String r, Map<String, Object> u){
		this(null, g, i, d, r, u);
	}

	public DumbGameThing(GameWorld w, int g, List<String> i, String d, String r, Map<String, Object> u){
		super(w, g);
		interactions = i;
		defaultInteraction = d;
		renderer = r;
		userArguments = u;
		if(w != null)
			w.introduce(this, gid());
	}

	public DumbGameThing(GameWorld w, int g){
		this(w, g, null, null, null, null);
	}

	public DumbGameThing(GameThing gt){
		this(gt.gid(), gt.interactions(), gt.defaultInteraction(), gt.renderer(), gt.userArguments());
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
	}

	public String renderer(){
		return renderer != null? renderer : super.renderer();
	}

	public static Serializer<DumbGameThing> serializer(final GameWorld w){
		return new Serializer<DumbGameThing>(){
			private final Serializer<List<String>> interS = new Serializers.Nullable<List<String>>(new Serializers.List<String>(Serializers.Serializer_String));
			private final Serializer<String> nullStringS = new Serializers.Nullable<String>(Serializers.Serializer_String);
			// private static final Serializer<Map<String, Object>> = ???

			public Tree write(DumbGameThing in){
				Tree out = new Tree();
				out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid())));
				out.add(new Tree.Entry("interactions", interS.write(in.interactions)));
				out.add(new Tree.Entry("defaultinteraction", nullStringS.write(in.defaultInteraction)));
				out.add(new Tree.Entry("renderer", nullStringS.write(in.renderer)));
				return out;
			}

			public DumbGameThing read(Tree in){
				in.print();
				
				return new DumbGameThing(
					Serializers.Serializer_Integer.read(in.find("gid")),
					interS.read(in.find("interactions")),
					nullStringS.read(in.find("defaultinteraction")),
					nullStringS.read(in.find("renderer")),
					new HashMap<String, Object>());
			}
		};
	}
}
