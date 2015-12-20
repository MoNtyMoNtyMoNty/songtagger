package de.lischewski.songtagger.control;

import javax.swing.table.AbstractTableModel;

public class SongsTableModel extends AbstractTableModel {
	
	private ReTagger reTagger;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327365835596869376L;
	
	public SongsTableModel(ReTagger reTagger) {
		this.reTagger = reTagger;
	}
	
	public void set(ReTagger reTagger){
		this.reTagger = reTagger;
		this.fireTableDataChanged();
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
		return this.reTagger.getInfo(rowIndex, ReTagger.Info.values()[columnIndex]);
	}
	
	public String getColumnName(int column) {
		return ReTagger.Info.values()[column].toString();
    }

}
