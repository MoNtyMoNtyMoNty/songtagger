package de.lischewski.songtagger.control;

import java.io.IOException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import de.lischewski.songtagger.view.graphical.MainFrame;

public class Main {

	public static void main(String[] args) throws UnsupportedTagException, InvalidDataException, IOException {
		ReTagger reTagger = new ReTagger();
		SongsTableModel songsTableModel = new SongsTableModel(reTagger);
		MainFrame mainFrame = new MainFrame(songsTableModel);
		mainFrame.setVisible(true);
	}

}
