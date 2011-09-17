// This file is part of the Multi-player Pacman Game.
//
// Pacman is free software; you can redistribute it and/or modify 
// it under the terms of the GNU General Public License as published 
// by the Free Software Foundation; either version 3 of the License, 
// or (at your option) any later version.
//
// Pacman is distributed in the hope that it will be useful, but 
// WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
// the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public 
// License along with Pacman. If not, see <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce. 

package control;

import Game.game.*;

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is
 * used to update the game state, and refresh the display. Setting the pulse
 * rate too high may cause problems, when the point is reached at which the work
 * done to service a given pulse exceeds the time between pulses.
 * 
 * @author djp
 * 
 */
public class ClockThread extends Thread {
	private final int delay; // delay between pulses in us
	private final Board game;
	private final BoardFrame display;
	
	public ClockThread(int delay, Board game, BoardFrame display) {
		this.delay = delay;
		this.game = game;
		this.display = display;
	}
	
	public void run() {
		while(1 == 1) {
			// Loop forever			
			try {
				Thread.sleep(delay);
				game.clockTick();
				if(display != null) {
					display.repaint();
				}
			} catch(InterruptedException e) {
				// should never happen
			}			
		}
	}
}
