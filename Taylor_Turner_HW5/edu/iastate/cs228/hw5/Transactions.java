package edu.iastate.cs228.hw5;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  
 * @author Taylor Turner
 *
 */

/**
 * 
 * The Transactions class simulates video transactions at a video store.
 *
 */
public class Transactions {

	/**
	 * The main method generates a simulation of rental and return activities.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws AllCopiesRentedOutException
	 * @throws FilmNotInInventoryException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException,
			FilmNotInInventoryException, AllCopiesRentedOutException {
		// TODO
		//
		// 1. Construct a VideoStore object.
		// 2. Simulate transactions as in the example given in Section 4 of the
		// the project description.
		int key = 0;
		VideoStore store = new VideoStore("videoList1.txt");
		Scanner scan = new Scanner(System.in);
		System.out.println("Transactions at a Video Store\r\n" + " keys: 1 (rent) 2 (bulk rent)\r\n"
				+ " 3 (return) 4 (bulk return)\r\n" + " 5 (summary) 6 (exit)");
		while (key != 6) {
			System.out.print("\n");
			System.out.print("Transaction: ");
			key = scan.nextInt();
			// rent
			if (key == 1) {
				try {
					System.out.print("Film to rent: ");
					String line = scan.next();
					line += scan.nextLine();
					String title = VideoStore.parseFilmName(line);
					int quantity = VideoStore.parseNumCopies(line);
					store.videoRent(title, quantity);
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				} catch (AllCopiesRentedOutException e) {
					System.out.println(e.getMessage());
				}
			}
			// bulk rent
			else if (key == 2) {
				try {
					System.out.print("Video file (rent): ");
					String videoFile = scan.next();
					store.bulkRent(videoFile);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				} catch (AllCopiesRentedOutException e) {
					System.out.println(e.getMessage());
				}
			}
			// return
			else if (key == 3) {
				try {
					System.out.print("Film to return: ");
					String line = scan.next();
					line += scan.nextLine();
					String title = VideoStore.parseFilmName(line);
					int quantity = VideoStore.parseNumCopies(line);
					store.videoReturn(title, quantity);
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				}
			}
			// bulk return
			else if (key == 4) {
				try {
					System.out.print("Video file (return): ");
					String videoFile = scan.next();
					store.bulkReturn(videoFile);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				}
			}
			// summary
			else if (key == 5) {
				System.out.println(store.transactionsSummary());
			}
			// exit
			else if (key == 6) {
				break;
			} else {
				System.out.println("Please enter a valid key.");
			}
		}
		scan.close();
	}
}
