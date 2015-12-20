package de.lischewski.songtagger.view.graphical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import de.lischewski.songtagger.control.ReTagger;
import de.lischewski.songtagger.control.ReTagger.Info;
import de.lischewski.songtagger.control.SongsTableModel;

public class MainFrame extends JFrame implements ActionListener, ItemListener {

	private final static String authorname = "Martin Lischewski";
	private final static String choose = "choose";
	private final static String inputDirectory = "Input directory:";
	private final static String sourceTag = "Source Tag:";
	private final static String destinationTag = "Destination Tag:";
	
	private final File file = new File("D:/Hörbücher/Elizabeth George - Bedenke, was du tust/");

	/**
	 * 
	 */
	private static final long serialVersionUID = -5008234300602135019L;
	
	private JFileChooser fileChooser;

	private JPanel total;
	private JPanel center;
	private JPanel north;
	private JPanel west;
	private JPanel east;
	private JPanel south;

	private JComboBox<ReTagger.Info> sourceInfo;
	private JComboBox<ReTagger.Info> destinationInfo;

	private ReTagger.Info sourceInfoSelected = ReTagger.Info.FileName;
	private ReTagger.Info destinationInfoSelected = ReTagger.Info.Title;

	private JButton chooseDirectory;

	private JTextField inputDirectoryTextField;
	
	private JTable songsTable;
	
	private SongsTableModel songsTableModel;
	
	private JScrollPane scrollPane;

	private JLabel copyright;
	private JLabel inputDirectoryJLabel;
	private JLabel sourceTagLabel;
	private JLabel destinationTagLabel;

	public MainFrame(SongsTableModel songsTableModel) {
		this.songsTableModel = songsTableModel;
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.fileChooser.setSelectedFile(file);
		
		this.total = new JPanel(new BorderLayout(0, 0));
		this.north = new JPanel(new BorderLayout(2, 0));
		this.west = new JPanel(new BorderLayout());
		this.east = new JPanel();
		this.center = new JPanel(new BorderLayout(5,5));
		this.south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		this.inputDirectoryJLabel = new JLabel(inputDirectory);
		this.inputDirectoryTextField = new JTextField();
		this.inputDirectoryTextField.setEnabled(false);
		this.inputDirectoryTextField.setDisabledTextColor(Color.BLACK);
		this.chooseDirectory = new JButton(choose);
		this.chooseDirectory.addActionListener(this);
		this.north.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.north.add(this.inputDirectoryJLabel, BorderLayout.NORTH);
		this.north.add(this.inputDirectoryTextField, BorderLayout.CENTER);
		this.north.add(this.chooseDirectory, BorderLayout.EAST);

		this.sourceInfo = new JComboBox<ReTagger.Info>();
		this.destinationInfo = new JComboBox<ReTagger.Info>();
		this.updateDestinationInfo();
		this.updateSourcesInfo();
		this.sourceInfo.addItemListener(this);
		this.destinationInfo.addItemListener(this);
		this.sourceTagLabel = new JLabel(sourceTag);
		this.destinationTagLabel = new JLabel(destinationTag);
		JPanel left = new JPanel(new BorderLayout());
		left.add(this.sourceTagLabel, BorderLayout.NORTH);
		left.add(this.sourceInfo, BorderLayout.CENTER);
		JPanel right = new JPanel(new BorderLayout());
		right.add(this.destinationTagLabel, BorderLayout.NORTH);
		right.add(this.destinationInfo, BorderLayout.CENTER);
		JPanel help = new JPanel(new GridLayout(1, 2, 10, 0));
		help.add(left);
		help.add(right);
		help.setBorder(new EmptyBorder(0, 5, 0, 5));
		this.center.add(help, BorderLayout.NORTH);
		
		this.songsTable = new JTable(songsTableModel);
		this.scrollPane = new JScrollPane(songsTable);
		this.scrollPane.setBorder(new EmptyBorder(0, 5, 0, 5));
		this.center.add(this.scrollPane, BorderLayout.CENTER);
		this.center.setBorder(new EmptyBorder(0, 2, 0, 2));

		this.copyright = new JLabel(authorname);
		help = new JPanel(new BorderLayout());
		help.add(copyright, BorderLayout.CENTER);
		help.setBorder(new EmptyBorder(0, 2, 2, 2));
		this.south.add(help);
		this.south.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

		this.total.add(north, BorderLayout.NORTH);
		this.total.add(west, BorderLayout.WEST);
		this.total.add(east, BorderLayout.EAST);
		this.total.add(south, BorderLayout.SOUTH);
		this.total.add(center, BorderLayout.CENTER);

		this.add(this.total);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setName("songtagger");
		this.setTitle("songtagger");
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		Object object = paramActionEvent.getSource();
		if (this.chooseDirectory == object) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (!file.exists()) {
					JOptionPane.showMessageDialog(this, "File does not exist!", "Insane error", JOptionPane.ERROR_MESSAGE);
				} else if (!file.isDirectory()) {
					JOptionPane.showMessageDialog(this, "Wie hast du das geschafft?", "Insane error", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println("Choosen Directory: " + file.getAbsolutePath());
				this.inputDirectoryTextField.setText(file.getAbsolutePath());
				this.songsTableModel.set(new ReTagger(file));
			}
		}
	}

	private boolean waitUpdateSourcesInfo = false;

	private void updateSourcesInfo() {
		waitUpdateSourcesInfo = true;
		ArrayList<ReTagger.Info> sources = new ArrayList<ReTagger.Info>();

		for (ReTagger.Info info : ReTagger.Info.values()) {
			if (!(info == this.destinationInfoSelected)) {
				sources.add(info);
			}
		}

		Collections.sort(sources);

		this.sourceInfo.removeAllItems();
		for (ReTagger.Info info : sources) {
			this.sourceInfo.addItem(info);
		}
		this.sourceInfo.setSelectedItem(this.sourceInfoSelected);
		waitUpdateSourcesInfo = false;
	}

	private boolean waitUpdateDestinationInfo = false;

	private void updateDestinationInfo() {
		waitUpdateDestinationInfo = true;
		ArrayList<ReTagger.Info> destinations = new ArrayList<ReTagger.Info>();

		for (ReTagger.Info info : ReTagger.Info.values()) {
			if (!(info == this.sourceInfoSelected)) {
				destinations.add(info);
			}
		}

		Collections.sort(destinations);

		this.destinationInfo.removeAllItems();
		for (ReTagger.Info info : destinations) {
			this.destinationInfo.addItem(info);
		}
		this.destinationInfo.setSelectedItem(this.destinationInfoSelected);

		waitUpdateDestinationInfo = false;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object object = e.getSource();
		if (this.sourceInfo == object) {
			if (!this.waitUpdateSourcesInfo && this.sourceInfo.getSelectedItem() != null && this.sourceInfoSelected != this.sourceInfo.getSelectedItem()) {
				this.sourceInfoSelected = (Info) this.sourceInfo.getSelectedItem();
				this.updateDestinationInfo();
			}
		} else if (this.destinationInfo == object) {
			if (!this.waitUpdateDestinationInfo && this.destinationInfo.getSelectedItem() != null && this.destinationInfoSelected != this.destinationInfo.getSelectedItem()) {
				this.destinationInfoSelected = (Info) this.destinationInfo.getSelectedItem();
				this.updateSourcesInfo();
			}
		}
	}
}
