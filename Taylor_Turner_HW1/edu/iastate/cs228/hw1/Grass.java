package edu.iastate.cs228.hw1;

/**
 *  
 * @author Taylor Turner
 *
 */

/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is
 * eaten.
 *
 */
public class Grass extends Living {
	public Grass(Plain p, int r, int c) {
		plain = p;
		row = r;
		column = c;
	}

	public State who() {
		return State.GRASS;
	}

	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast
	 * enough to take over Grass.
	 */
	public Living next(Plain pNew) {
		int[] pop = new int[NUM_LIFE_FORMS];
		this.census(pop);
		if (pop[RABBIT] >= pop[GRASS] * 3) {
			return new Empty(pNew, this.row, this.column);
		} else if (pop[RABBIT] >= 3) {
			return new Rabbit(pNew, this.row, this.column, 0);
		} else {
			this.plain = pNew;
			return this;
		}
	}
}
