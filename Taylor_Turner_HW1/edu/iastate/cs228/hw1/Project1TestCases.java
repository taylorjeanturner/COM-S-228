package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class Project1TestCases {

	@Test
	public void testPlainWidthConstructor() // Tests Plain constructor assuming getWidth works
	{
		Plain p = new Plain(3);
		assertTrue(p.getWidth() == 3);
	}

	@Test
	public void testBadgerConstructor() // Tests the badger constructor
	{
		Plain p = new Plain(3);
		Badger b = new Badger(p, 1, 2, 3);
		assertEquals(b.row, 1);
		assertEquals(b.column, 2);
		assertEquals(b.plain, p);
		assertEquals(b.age, 3);
	}

	@Test
	public void badgerTest() // Tests the example given for Badger in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/BadgerExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Plain newPlain = new Plain (3);
		Living nextLiving = p.grid[1][1].next(newPlain);
		assertEquals(nextLiving.who(), State.BADGER);
		assertEquals(((Animal) nextLiving).myAge(), 3);
	}

	@Test
	public void foxTest() // Tests the example given for Fox in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/FoxExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Plain newPlain = new Plain(3);
		Living nextLiving = p.grid[1][1].next(newPlain);
		assertEquals(nextLiving.who(), State.EMPTY);
	}

	@Test
	public void rabbitTest() //// Tests the example given for Rabbit in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/RabbitExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Plain newPlain = new Plain(3);
		Living nextLiving = p.grid[1][1].next(newPlain);
		assertEquals(nextLiving.who(), State.EMPTY);
	}

	@Test
	public void grassTest() // Tests the example given for Grass in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/GrassExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Plain newPlain = new Plain(3);
		Living nextLiving = p.grid[1][1].next(newPlain);
		assertEquals(nextLiving.who(), State.GRASS);
	}

	@Test
	public void emptyTest() // Tests the example given for Empty in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/EmptyExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Plain newPlain = new Plain(3);
		Living nextLiving = p.grid[0][1].next(newPlain);
		assertEquals(nextLiving.who(), State.RABBIT);
		assertEquals(((Animal) nextLiving).myAge(), 0);
	}

	@Test
	public void testReadIn() // Tests the Plain constructor for input file argument
	{
		File testFile = null;
		FileWriter writer = null;
		try {
			testFile = new File("src/edu/iastate/cs228/hw1/testFile.txt");
			if (testFile.createNewFile()) {
				System.out.println("File created: " + testFile.getName());
			} else {
				System.out.println("File already exists");
			}
			writer = new FileWriter("src/edu/iastate/cs228/hw1/testFile.txt");
			writer.write("B0 R1 E  \nG  R2 E  \nF3 G  R1 ");
			writer.close();
			System.out.println("Wrote to file");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/testFile.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Living[][] correctGrid = new Living[3][3];
		correctGrid[0][0] = new Badger(p, 0, 0, 0);
		correctGrid[0][1] = new Rabbit(p, 0, 1, 1);
		correctGrid[0][2] = new Empty(p, 0, 2);
		correctGrid[1][0] = new Grass(p, 1, 0);
		correctGrid[1][1] = new Rabbit(p, 1, 1, 2);
		correctGrid[1][2] = new Empty(p, 1, 2);
		correctGrid[2][0] = new Fox(p, 2, 0, 3);
		correctGrid[2][1] = new Grass(p, 2, 1);
		correctGrid[2][2] = new Rabbit(p, 2, 2, 1);

		Living correctLiving;
		Living readInLiving;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				correctLiving = correctGrid[i][j];
				readInLiving = p.grid[i][j];
				assertEquals(correctLiving.who(), readInLiving.who());
				assertEquals(correctLiving.column, readInLiving.column);
				assertEquals(correctLiving.row, readInLiving.row);
				if (correctLiving instanceof Animal) {
					assertEquals(((Animal) correctLiving).myAge(), ((Animal) readInLiving).myAge());
				}
			}
		}
		if (testFile.delete()) {
			System.out.println("File Deleted: " + testFile.getName());
		}
	}

	@Test
	public void trial1() // Tests the first trial given in the project documentation
	{
		String inputString = "2 src/edu/iastate/cs228/hw1/SimulationOne.txt 1 3";
		InputStream targetStream = new ByteArrayInputStream(inputString.getBytes());
		OutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		System.setIn(targetStream);
		Plain finalPlain = null;
		try {
			Wildlife.main(null);
			finalPlain = new Plain("src/edu/iastate/cs228/hw1/SimulationOneFinal.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String output = outStream.toString();
		String strToFind = "Final plain:";
		String s = output.substring(output.indexOf(strToFind) + strToFind.length() + 1);
		s = s.trim();
		int indexEnd = finalPlain.toString().length();
		String finalPlainOutput = s.substring(0, indexEnd);
		assertTrue(finalPlainOutput.equals(finalPlain.toString()));
	}

	@Test
	public void trial2() // Tests the second trial given in the project documentation
	{
		
		String inputString = "2 src/edu/iastate/cs228/hw1/SimulationTwo.txt 8 3";
		InputStream targetStream = new ByteArrayInputStream(inputString.getBytes());
		OutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		System.setIn(targetStream);

		Plain finalPlain = null;
		try {
			Wildlife.main(null);
			finalPlain = new Plain("src/edu/iastate/cs228/hw1/SimulationTwoFinal.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String output = outStream.toString();
		String strToFind = "Final plain:";
		String s = output.substring(output.indexOf(strToFind) + strToFind.length() + 1);
		s = s.trim();
		int indexEnd = finalPlain.toString().length();
		String finalPlainOutput = s.substring(0, indexEnd);
		assertTrue(finalPlainOutput.equals(finalPlain.toString()));
	}

	@Test
	public void trial3() // Tests the third trial given in the project documentation
	{
		String inputString = "2 src/edu/iastate/cs228/hw1/SimulationThree.txt 6 3";
		InputStream targetStream = new ByteArrayInputStream(inputString.getBytes());
		OutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		System.setIn(targetStream);
		Plain finalPlain = null;
		try {
			Wildlife.main(null);
			finalPlain = new Plain("src/edu/iastate/cs228/hw1/SimulationThreeFinal.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String output = outStream.toString();
		String strToFind = "Final plain:";
		String s = output.substring(output.indexOf(strToFind) + strToFind.length() + 1);
		s = s.trim();
		int indexEnd = finalPlain.toString().length();
		String finalPlainOutput = s.substring(0, indexEnd);
		assertTrue(finalPlainOutput.equals(finalPlain.toString()));
	}
}
