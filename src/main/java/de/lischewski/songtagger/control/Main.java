package de.lischewski.songtagger.control;

import java.io.IOException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Main {

	public static void main(String[] args) throws UnsupportedTagException, InvalidDataException, IOException {
		for (ReTagger.Info info : ReTagger.Info.values()) {
			System.out.println("case "+info.toString()+": return ;");
		}
	}

}
