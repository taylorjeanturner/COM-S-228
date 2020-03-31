package edu.iastate.cs228.hw1;

/**
 *  
 * @author Taylor Turner
 *
 */

/**
 * 
 * Living refers to the life form occupying a square in a plain grid. It is a
 * superclass of Empty, Grass, and Animal, the latter of which is in turn a
 * superclass of Badger, Fox, and Rabbit. Living has two abstract methods
 * awaiting implementation.
 *
 */
public abstract class Living {
	protected Plain plain; // the plain in which the life form resides
	protected int row; // location of the square on which
	protected int column; // the life form resides

	// constants to be used as indices.
	protected static final int BADGER = 0;
	protected static final int EMPTY = 1;
	protected static final int FOX = 2;
	protected static final int GRASS = 3;
	protected static final int RABBIT = 4;

	public static final int NUM_LIFE_FORMS = 5;

	// life expectancies
	public static final int BADGER_MAX_AGE = 4;
	public static final int FOX_MAX_AGE = 6;
	public static final int RABBIT_MAX_AGE = 3;

	/**
	 * Censuses all life forms in the 3 X 3 neighborhood in a plain.
	 * 
	 * @param population counts of all life forms
	 */
	protected void census(int population[]) {
		// If grid is 1x1
		if (plain.grid.length == 1) {
			if (plain.grid[0][0].who() == State.BADGER) {
				population[BADGER]++;
			} else if (plain.grid[0][0].who() == State.EMPTY) {
				population[EMPTY]++;
			} else if (plain.grid[0][0].who() == State.FOX) {
				population[FOX]++;
			} else if (plain.grid[0][0].who() == State.GRASS) {
				population[GRASS]++;
			} else if (plain.grid[0][0].who() == State.RABBIT) {
				population[RABBIT]++;
			}
		}

		// If living object is located in the top left corner, creates a 2x2
		// neighborhood
		else if (row == 0 && column == 0) {
			for (int i = row; i <= row + 1; i++) {
				for (int j = column; j <= column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// // If living object is located in the top right corner, creates a 2x2
		// neighborhood
		else if (row == 0 && column == plain.getWidth() - 1) {
			for (int i = row; i <= row + 1; i++) {
				for (int j = column - 1; j < column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// If living object is located in the bottom left corner, creates a 2x2
		// neighborhood
		else if (row == plain.getWidth() - 1 && column == 0) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = column; j <= column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// If living object is located in the bottom right corner, creates a 2x2
		// neighborhood
		else if (row == plain.getWidth() - 1 && column == plain.getWidth() - 1) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = column - 1; j <= column; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// If living object is located in the top row, creates a 2x3 neighborhood
		else if (row == 0) {
			for (int i = row; i <= row + 1; i++) {
				for (int j = column - 1; j <= column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// If living object is located in the top row, creates a 2x3 neighborhood
		else if (row == plain.getWidth() - 1) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = column - 1; j <= column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// If living object is located in the left-most column, creates a 3x2
		// neighborhood
		else if (column == 0) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = column; j <= column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}

		// If living object is located in the right-most column, creates a 3x2
		// neighborhood
		else if (column == plain.getWidth() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = column - 1; j <= column; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		} else {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = column - 1; j <= column + 1; j++) {
					if (plain.grid[i][j].who() == State.BADGER) {
						population[BADGER]++;
					} else if (plain.grid[i][j].who() == State.EMPTY) {
						population[EMPTY]++;
					} else if (plain.grid[i][j].who() == State.FOX) {
						population[FOX]++;
					} else if (plain.grid[i][j].who() == State.GRASS) {
						population[GRASS]++;
					} else if (plain.grid[i][j].who() == State.RABBIT) {
						population[RABBIT]++;
					}
				}
			}
		}
	}

	/**
	 * Gets the identity of the life form on the square.
	 * 
	 * @return State
	 */
	public abstract State who();

	/**
	 * Determines the life form on the square in the next cycle.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living
	 */
	public abstract Living next(Plain pNew);

}
