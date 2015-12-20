package de.lischewski.songtagger.view.commandline;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import de.lischewski.songtagger.control.ReTagger;

public class CommandLineCommunicator {

	public void sayHello() {
		System.out.println("Hello and Welcome!");
		System.out.println("This songtagger ist made by Martin Lischewski!");
	}

	public File chooseDirectory() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.exists()) {
				System.out.println("Choosen Directory does not exist!");
				return null;
			} else if (!file.isDirectory()) {
				System.out.println("Choosen File not Directory!");
				return null;
			}
			System.out.println("Choosen Directory: " + file.getAbsolutePath());
			return file;
		}
		return null;
	}

	public ReTagger.Info chooseInfo() {
		String question = "Please choose the number:";
		ReTagger.Info[] infos = ReTagger.Info.values();
		for (int i = 0; i < infos.length; ++i) {
			question += "\n" + (i + 1) + ".\t" + infos[i];
		}
		Scanner scanner = new Scanner(System.in);
		System.out.println(question);
		System.out.println("Choose number:");
		try {
			int choose = scanner.nextInt() - 1;
			if (choose >= 0 && choose < infos.length) {
				scanner.close();
				System.out.println("Choosen: " + infos[choose]);
				return infos[choose];
			}
		} catch (NoSuchElementException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Wrong Number! Try again!");
		return chooseInfo();
	}

}
