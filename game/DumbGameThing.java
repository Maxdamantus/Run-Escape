package game;

import java.util.*;

public class DumbGameThing extends AbstractGameThing.AbstractDumbGameThing {
	private List<String> interactions;
	private String defaultInteraction;
	private Map<String, Object> userArguments;

	public DumbGameThing(int g, List<String> i, String d, Map<String, Object> u){
		super(g);
		interactions = i;
		defaultInteraction = d;
		userArguments = u;
	}

	public DumbGameThing(GameWorld w, int g, List<String> i, String d, Map<String, Object> u){
		this(g, i, d, u);
		w.introduce(this, gid());
	}

	public DumbGameThing(GameWorld w, int g){
		this(w, g, null, null, null);
	}

	public void update(DumbGameThing o){
		if(o.interactions != null)
			interactions = o.interactions;
		if(o.defaultInteraction != null)
			defaultInteraction = o.defaultInteraction;
		if(o.userArguments != null)
			userArguments = o.userArguments;
	}
}
