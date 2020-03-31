package edu.iastate.cs228.hw1;

/**
 *  
 * @author Taylor Turner
 *
 */

/**
 * A badger eats a rabbit and competes against a fox.
 */
public class Badger extends Animal {
	/**
	 * Constructor
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 * @param a: age
	 */
	public Badger(Plain p, int r, int c, int a) {
		plain = p;
		row = r;
		column = c;
		age = a;
	}

	/**
	 * A badger occupies the square.
	 */
	public State who() {
		return State.BADGER;
	}

	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group
	 * of foxes.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living life form occupying the square in the next cycle.
	 */
	public Living next(Plain pNew) {
		int[] pop = new int[NUM_LIFE_FORMS];
		this.census(pop);
		if (this.age >= BADGER_MAX_AGE) {
			return new Empty(pNew, this.row, this.column);
		} else if (pop[BADGER] == 1 && pop[FOX] > 1) {
			return new Fox(pNew, this.row, this.column, 0);
		} else if (pop[BADGER] + pop[FOX] > pop[RABBIT]) {
			return new Empty(pNew, this.row, this.column);
		} else {
			this.plain = pNew;
			this.age++;
			return this;
		}
	}
}
