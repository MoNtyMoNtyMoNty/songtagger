package de.lischewski.songtagger.control;

import java.io.File;
import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import com.mpatric.mp3agic.NotSupportedException;

public class SongsTableModel extends AbstractTableModel {
	
	private ReTagger reTagger;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327365835596869376L;
	
	public SongsTableModel(ReTagger reTagger) {
		this.reTagger = reTagger;
	}
	
	public void setDestination(File directory){
		this.reTagger.setDirectory(directory);
		this.fireTableDataChanged();
	}
	
	public void setInfo(ReTagger.Info sourceInfo, ReTagger.Info destinationInfo){
		this.reTagger.setSourceInfo(sourceInfo);
		this.reTagger.setDestinationInfo(destinationInfo);
		this.fireTableDataChanged();
	}
	
	public int getNumberOfMaxNumbers(){
		return this.reTagger.getNumberOfMaxNumbers();
	}
	
	public void setNumber(int number){
		if(this.reTagger.getNumber()!=number){
			this.reTagger.setNumber(number);
			this.fireTableDataChanged();
		}
	}

	@Override
	public int getRowCount() {
		return this.reTagger.getNumberOfSongs();
	}

	@Override
	public int getColumnCount() {
		return ReTagger.Info.values().length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.reTagger.getHTMLInfo(rowIndex, ReTagger.Info.values()[columnIndex]);
	}
	
	public String getColumnName(int column) {
		return ReTagger.Info.values()[column].toString();
    }
	
	public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

	public int getMaxNumberLength() {
		return this.reTagger.getMaxNumberLength();
	}

	public void makeChanges() throws NotSupportedException, IOException {
		this.reTagger.makeChanges();
	}

}
