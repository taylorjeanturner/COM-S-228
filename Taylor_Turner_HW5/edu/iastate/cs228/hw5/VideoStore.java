package edu.iastate.cs228.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 
 * @author Taylor Turner
 *
 */

public class VideoStore {
	protected SplayTree<Video> inventory; // all the videos at the store

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor sets inventory to an empty tree.
	 */
	public VideoStore() {
		// no need to implement.
	}

	/**
	 * Constructor accepts a video file to create its inventory. Refer to Section
	 * 3.2 of the project description for details regarding the format of a video
	 * file.
	 * 
	 * Calls setUpInventory().
	 * 
	 * @param videoFile no format checking on the file
	 * @throws FileNotFoundException
	 */
	public VideoStore(String videoFile) throws FileNotFoundException {
		inventory = new SplayTree<Video>();
		setUpInventory(videoFile);
	}

	/**
	 * Accepts a video file to initialize the splay tree inventory. To be efficient,
	 * add videos to the inventory by calling the addBST() method, which does not
	 * splay.
	 * 
	 * Refer to Section 3.2 for the format of video file.
	 * 
	 * @param videoFile correctly formated if exists
	 * @throws FileNotFoundException
	 */
	public void setUpInventory(String videoFile) throws FileNotFoundException {
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String film = scan.nextLine();
			String title = parseFilmName(film);
			int quantity = parseNumCopies(film);
			Video video = new Video(title, quantity);
			inventory.addBST(video);
		}
		scan.close();
	}

	// ------------------
	// Inventory Addition
	// ------------------

	/**
	 * Find a Video object by film title.
	 * 
	 * @param film
	 * @return
	 */
	public Video findVideo(String film) {
		Video video = inventory.findEntry(new Video(film)).data;
		if (video.getFilm().contentEquals(film)) {
			return video;
		}
		return null;
	}

	/**
	 * Updates the splay tree inventory by adding a number of video copies of the
	 * film. (Splaying is justified as new videos are more likely to be rented.)
	 * 
	 * Calls the add() method of SplayTree to add the video object.
	 * 
	 * a) If true is returned, the film was not on the inventory before, and has
	 * been added. b) If false is returned, the film is already on the inventory.
	 * 
	 * The root of the splay tree must store the corresponding Video object for the
	 * film. Update the number of copies for the film.
	 * 
	 * @param film title of the film
	 * @param n    number of video copies
	 */
	public void addVideo(String film, int n) {
		Video video = new Video(film, n);
		if (inventory.add(video)) {
			// add() sets the root as the added video
		} else if (!inventory.add(video)) {
			Video existing = findVideo(film);
			existing.addNumCopies(n);
		}
	}

	/**
	 * Add one video copy of the film.
	 * 
	 * @param film title of the film
	 */
	public void addVideo(String film) {
		addVideo(film, 1);
	}

	/**
	 * Update the splay trees inventory by adding videos. Perform binary search
	 * additions by calling addBST() without splaying.
	 * 
	 * The videoFile format is given in Section 3.2 of the project description.
	 * 
	 * @param videoFile correctly formated if exists
	 * @throws FileNotFoundException
	 */
	public void bulkImport(String videoFile) throws FileNotFoundException {
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String film = scan.nextLine();
			String title = parseFilmName(film);
			int quantity = parseNumCopies(film);
			Video video = new Video(title, quantity);
			if (inventory.addBST(video)) {
				// adds video
			} else if (!inventory.addBST(video)) {
				Video existing = findVideo(video.getFilm());
				existing.addNumCopies(quantity);
			}
		}
		scan.close();
	}

	// ----------------------------
	// Video Query, Rental & Return
	// ----------------------------

	/**
	 * Search the splay tree inventory to determine if a video is available.
	 * 
	 * @param film
	 * @return true if available
	 */
	public boolean available(String film) {
		Video existing = findVideo(film);
		// video is not in inventory
		if (existing == null) {
			return false;
		}
		if (existing.getNumAvailableCopies() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Update inventory.
	 * 
	 * Search if the film is in inventory by calling findElement(new Video(film,
	 * 1)).
	 * 
	 * If the film is not in inventory, prints the message "Film <film> is not in
	 * inventory", where <film> shall be replaced with the string that is the value
	 * of the parameter film. If the film is in inventory with no copy left, prints
	 * the message "Film <film> has been rented out".
	 * 
	 * If there is at least one available copy but n is greater than the number of
	 * such copies, rent all available copies. In this case, no
	 * AllCopiesRentedOutException is thrown.
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException    if n <= 0 or film == null or
	 *                                     film.isEmpty()
	 * @throws FilmNotInInventoryException if film is not in the inventory
	 * @throws AllCopiesRentedOutException if there is zero available copy for the
	 *                                     film.
	 */
	public void videoRent(String film, int n)
			throws IllegalArgumentException, FilmNotInInventoryException, AllCopiesRentedOutException {
		if (n <= 0 || film == null || film.isEmpty()) {
			throw new IllegalArgumentException();
		} else if (findVideo(film) == null) {
			throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
		} else if (!available(film)) {
			throw new AllCopiesRentedOutException("Film " + film + " has been rented out");
		}
		Video video = findVideo(film);
		video.rentCopies(n);
	}

	/**
	 * Update inventory.
	 * 
	 * 1. Calls videoRent() repeatedly for every video listed in the file. 2. For
	 * each requested video, do the following: a) If it is not in inventory or is
	 * rented out, an exception will be thrown from videoRent(). Based on the
	 * exception, prints out the following message: "Film <film> is not in
	 * inventory" or "Film <film> has been rented out." In the message, <film> shall
	 * be replaced with the name of the video. b) Otherwise, update the video record
	 * in the inventory.
	 * 
	 * For details on handling of multiple exceptions and message printing, please
	 * read Section 3.4 of the project description.
	 * 
	 * @param videoFile correctly formatted if exists
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException    if the number of copies of any film is <=
	 *                                     0
	 * @throws FilmNotInInventoryException if any film from the videoFile is not in
	 *                                     the inventory
	 * @throws AllCopiesRentedOutException if there is zero available copy for some
	 *                                     film in videoFile
	 */
	public void bulkRent(String videoFile) throws FileNotFoundException, IllegalArgumentException,
			FilmNotInInventoryException, AllCopiesRentedOutException {
		ArrayList<Exception> exceptions = new ArrayList<Exception>();
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String film = scan.nextLine();
			String title = parseFilmName(film);
			int quantity = parseNumCopies(film);
			try {
				videoRent(title, quantity);
			} catch (IllegalArgumentException e) {
				exceptions.add(new IllegalArgumentException("Film " + title + " has an invalid request"));
			} catch (FilmNotInInventoryException e) {
				exceptions.add(new FilmNotInInventoryException("Film " + title + " is not in inventory"));
			} catch (AllCopiesRentedOutException e) {
				exceptions.add(new AllCopiesRentedOutException("Film " + title + " has been rented out"));
			}
		}
		scan.close();
		String message = "";
		// Concatenate the messages of all the exceptions into one string
		for (Exception e : exceptions) {
			message += e.getMessage() + "\n";
		}
		message = message.trim();
		// find highest ranking exception
		for (Exception e : exceptions) {
			if (e instanceof IllegalArgumentException) {
				throw new IllegalArgumentException(message);
			}
		}
		// if highest ranking exception is not found, search for next highest ranking
		for (Exception e : exceptions) {
			if (e instanceof FilmNotInInventoryException) {
				throw new FilmNotInInventoryException(message);
			}
		}
		// if next highest ranking exception is not found, search for next highest
		// ranking
		for (Exception e : exceptions) {
			if (e instanceof AllCopiesRentedOutException) {
				throw new AllCopiesRentedOutException(message);
			}
		}
	}

	/**
	 * Update inventory.
	 * 
	 * If n exceeds the number of rented video copies, accepts up to that number of
	 * rented copies while ignoring the extra copies.
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException    if n <= 0 or film == null or
	 *                                     film.isEmpty()
	 * @throws FilmNotInInventoryException if film is not in the inventory
	 */
	public void videoReturn(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException {
		if (n <= 0 || film == null || film.isEmpty()) {
			throw new IllegalArgumentException();
		} else if (findVideo(film) == null) {
			throw new FilmNotInInventoryException();
		}
		Video video = findVideo(film);
		video.returnCopies(n);
	}

	/**
	 * Update inventory.
	 * 
	 * Handles excessive returned copies of a film in the same way as videoReturn()
	 * does. See Section 3.4 of the project description on how to handle multiple
	 * exceptions.
	 * 
	 * @param videoFile
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException    if the number of return copies of any
	 *                                     film is <= 0
	 * @throws FilmNotInInventoryException if a film from videoFile is not in
	 *                                     inventory
	 */
	public void bulkReturn(String videoFile)
			throws FileNotFoundException, IllegalArgumentException, FilmNotInInventoryException {
		ArrayList<Exception> exceptions = new ArrayList<Exception>();
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String film = scan.nextLine();
			String title = parseFilmName(film);
			int quantity = parseNumCopies(film);
			try {
				videoReturn(title, quantity);
			} catch (IllegalArgumentException e) {
				exceptions.add(new IllegalArgumentException("Film " + title + " has an invalid request"));
			} catch (FilmNotInInventoryException e) {
				exceptions.add(new FilmNotInInventoryException("Film " + title + " is not in inventory"));
			}
		}
		scan.close();
		String message = "";
		// Concatenate the messages of all the exceptions into one string
		for (Exception e : exceptions) {
			message += e.getMessage() + "\n";
		}
		message = message.trim();
		// find highest ranking exception
		for (Exception e : exceptions) {
			if (e instanceof IllegalArgumentException) {
				throw new IllegalArgumentException(message);
			}
		}
		// if highest ranking exception is not found, search for next highest ranking
		for (Exception e : exceptions) {
			if (e instanceof FilmNotInInventoryException) {
				throw new FilmNotInInventoryException(message);
			}
		}
	}

	// ------------------------
	// Methods without Splaying
	// ------------------------

	/**
	 * Performs inorder traversal on the splay tree inventory to list all the videos
	 * by film title, whether rented or not. Below is a sample string if printed
	 * out:
	 * 
	 * 
	 * Films in inventory:
	 * 
	 * A Streetcar Named Desire (1) Brokeback Mountain (1) Forrest Gump (1) Psycho
	 * (1) Singin' in the Rain (2) Slumdog Millionaire (5) Taxi Driver (1) The
	 * Godfather (1)
	 * 
	 * 
	 * @return
	 */
	public String inventoryList() {
		Iterator<Video> iter = inventory.iterator();
		String result = "Films in inventory: \n";
		while (iter.hasNext()) {
			Video video = iter.next();
			result += "\n" + video.getFilm() + " (" + video.getNumCopies() + ")";
		}
		return result;
	}

	/**
	 * Calls rentedVideosList() and unrentedVideosList() sequentially. For the
	 * string format, see Transaction 5 in the sample simulation in Section 4 of the
	 * project description.
	 * 
	 * @return
	 */
	public String transactionsSummary() {
		return rentedVideosList() + "\n \n" + unrentedVideosList();
	}

	/**
	 * Performs inorder traversal on the splay tree inventory. Use a splay tree
	 * iterator.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * Rented films:
	 * 
	 * Brokeback Mountain (1) Forrest Gump (1) Singin' in the Rain (2) The Godfather
	 * (1)
	 * 
	 * 
	 * @return
	 */
	private String rentedVideosList() {
		Iterator<Video> iter = inventory.iterator();
		String result = "Rented films: \n";
		while (iter.hasNext()) {
			Video video = iter.next();
			if (video.getNumRentedCopies() > 0) {
				result += "\n" + video.getFilm() + " (" + video.getNumRentedCopies() + ")";
			}
		}
		return result;
	}

	/**
	 * Performs inorder traversal on the splay tree inventory. Use a splay tree
	 * iterator. Prints only the films that have unrented copies.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * 
	 * Films remaining in inventory:
	 * 
	 * A Streetcar Named Desire (1) Forrest Gump (1) Psycho (1) Slumdog Millionaire
	 * (4) Taxi Driver (1)
	 * 
	 * 
	 * @return
	 */
	private String unrentedVideosList() {
		Iterator<Video> iter = inventory.iterator();
		String result = "Films remaining in inventory: \n";
		while (iter.hasNext()) {
			Video video = iter.next();
			if (video.getNumAvailableCopies() > 0) {
				result += "\n" + video.getFilm() + " (" + video.getNumAvailableCopies() + ")";
			}
		}
		return result;
	}

	/**
	 * Parse the film name from an input line.
	 * 
	 * @param line
	 * @return
	 */
	public static String parseFilmName(String line) {
		String title = "";
		if (line.contains("(")) {
			String[] movieData = line.split("\\(");
			title = movieData[0].trim();
		} else {
			title = line.trim();
		}
		return title.trim();
	}

	/**
	 * Parse the number of copies from an input line.
	 * 
	 * @param line
	 * @return
	 */
	public static int parseNumCopies(String line) {
		int quantity = 1;
		if (line.contains("(")) {
			String[] movieData = line.split("\\(");
			quantity = Integer.parseInt(movieData[1].substring(0, movieData[1].length() - 1));
			if (quantity < 0) {
				quantity = 0; // A negative number following a film title is treated as zero
			}
		}
		return quantity;
	}

}
