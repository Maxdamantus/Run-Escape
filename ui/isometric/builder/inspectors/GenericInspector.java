package ui.isometric.builder.inspectors;

import ui.isometric.builder.InspectorPanel;
import game.GameThing;

/**
 * A generic GameThing inspector
 * 
 * @author melby
 *
 */
public class GenericInspector extends GameThingInspector<GameThing> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a GenericInspector for a given GameThing
	 * @param t
	 * @param inspectorPanel
	 */
	public GenericInspector(GameThing t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
	}
}
