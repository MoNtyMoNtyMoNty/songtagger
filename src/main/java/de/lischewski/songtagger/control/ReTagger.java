package de.lischewski.songtagger.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class ReTagger {

	public enum Info {
		FileName,
		// Version,
		Track,
		Artist,
		Title,
		Album,
		Year,
		GenreDescription,
		Comment,
		// Grouping,
		// Key,
		// Date,
		// Composer,
		// Publisher,
		// OriginalArtist,
		// AlbumArtist,
		// Copyright,
		// ArtistUrl,
		// CommercialUrl,
		// CopyrightUrl,
		// AudiofileUrl,
		// AudioSourceUrl,
		// RadiostationUrl,
		// PaymentUrl,
		// PublisherUrl,
		// Url,
		// PartOfSet,
		// Encoder,
		// AlbumImageMimeType,
		// ItunesComment;
	}

	private ArrayList<Mp3File> mp3Files;
	private ArrayList<File> files;

	private Info sourceInfo;
	private Info destinationInfo;

	private int number;
	private int maxNumber;

	public ReTagger() {
		mp3Files = new ArrayList<Mp3File>();
		files = new ArrayList<File>();
		this.sourceInfo = null;
		this.destinationInfo = null;
		this.number = -1;
	}

	public boolean setDirectory(File directory) {
		if (directory != null && directory.isDirectory()) {
			this.mp3Files.clear();
			this.files.clear();
			File[] files = directory.listFiles();
			for (File file : files) {
				try {
					Mp3File mp3file = new Mp3File(file);
					mp3Files.add(mp3file);
					this.files.add(file);
				} catch (InvalidDataException exception) {
					System.out.println("ignored File: " + file.getName());
				} catch (UnsupportedTagException | IOException exception) {
					System.out.println("ignored File: " + file.getName());
					exception.printStackTrace();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void makeChanges() throws NotSupportedException, IOException {
		for (int i = 0; i < this.mp3Files.size(); i++) {
			this.setInfo(i, destinationInfo, getFormattedNumber(i));
		}
	}

	private void setMaxNumber() {
		int max = 0;
		for (int i = 0; i < this.getNumberOfSongs(); ++i) {
			int tmp = getNumber(i).length();
			if (max < tmp) {
				max = tmp;
			}
		}
		this.maxNumber = max;
	}

	private String getFormattedNumber(int number) {
		String ret = "";
		String tmp = getNumber(number);
		while (tmp.length() < maxNumber) {
			tmp += "0";
			ret += "0";
		}
		ret += getNumber(number);
		return ret;
	}

	private String getNumber(int number) {
		String str = this.getInfo(number, sourceInfo);
		boolean inNumber = false;
		int i = 0;
		String ret = "";
		for (int j = 0; j < str.length(); ++j) {
			if (!Character.isDigit(str.charAt(j))) {
				if (inNumber) {
					inNumber = false;
				}
			} else {
				if (!inNumber) {
					i++;
					inNumber = true;
				}
				if (i == this.number) {
					ret += str.charAt(j);
				}
			}
		}
		return ret;
	}

	public int getNumberOfMaxNumbers() {
		int ret = -1;
		for (int i = 0; i < this.getNumberOfSongs(); ++i) {
			String str = this.getInfo(i, sourceInfo);
			int tmp = getNumberOfNumbers(str);
			if (ret == -1) {
				ret = tmp;
			} else if (ret > tmp) {
				ret = tmp;
			}
		}
		return ret;
	}

	private int getNumberOfNumbers(String str) {
		boolean inNumber = false;
		int i = 0;
		for (int j = 0; j < str.length(); ++j) {
			if (!Character.isDigit(str.charAt(j))) {
				if (inNumber) {
					inNumber = false;
				}
			} else {
				if (!inNumber) {
					i++;
					inNumber = true;
				}
			}
		}
		return i;
	}

	/**
	 * <font color="red"> fghh</font>
	 * 
	 * @param str
	 * @param number
	 * @return
	 */
	public String getHTMLHighlighted(String str) {
		String ret = "";
		boolean inNumber = false;
		int i = 0;
		for (int j = 0; j < str.length(); ++j) {
			if (!Character.isDigit(str.charAt(j))) {
				if (inNumber) {
					if (i == number) {
						ret += "</font>";
					}
					inNumber = false;
				}
			} else {
				if (!inNumber) {
					i++;
					if (i == number) {
						ret += "<font color=\"red\">";
					}
					inNumber = true;
				}
			}
			ret += str.charAt(j);
		}
		if (inNumber) {
			ret += "</font>";
			inNumber = false;
		}
		return ret;
	}

	public int getNumberOfSongs() {
		return this.mp3Files.size();
	}

	public String getHTMLInfo(int number, Info info) {
		String ret = getInfo(number, info);
		if (this.sourceInfo != null && info == this.sourceInfo) {
			ret = getHTMLHighlighted(ret);
		} else if (this.destinationInfo != null && info == this.destinationInfo) {
			String numberfomatted = getFormattedNumber(number);
			ret = "<s>" + ret + "</s>";
			ret += " | <font color=\"red\">" + numberfomatted + "</font>";
		}
		return "<html>" + ret + "</html>";
	}

	private String getInfo(int number, Info info) {
		Mp3File mp3file = this.mp3Files.get(number);
		File file = this.files.get(number);
		if (mp3file.hasId3v1Tag()) {
			ID3v1 id3v1 = mp3file.getId3v1Tag();
			switch (info) {
			case FileName:
				return file.getName();
//			case Version:
//				return id3v1.getVersion();
			case Track:
				return id3v1.getTrack();
			case Artist:
				return id3v1.getArtist();
			case Title:
				return id3v1.getTitle();
			case Album:
				return id3v1.getAlbum();
			case Year:
				return id3v1.getYear();
			case GenreDescription:
				return id3v1.getGenreDescription();
			case Comment:
				return id3v1.getComment();
			default:
				return null;
			}
		} else if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2 = mp3file.getId3v2Tag();
			switch (info) {
			case FileName:
				return file.getName();
//			case Version:
//				return id3v2.getVersion();
			case Track:
				return id3v2.getTrack();
			case Artist:
				return id3v2.getArtist();
			case Title:
				return id3v2.getTitle();
			case Album:
				return id3v2.getAlbum();
			case Year:
				return id3v2.getYear();
			case GenreDescription:
				return id3v2.getGenreDescription();
			case Comment:
				return id3v2.getComment();
			// case Grouping:
			// return id3v2.getGrouping();
			// case Key:
			// return id3v2.getKey();
			// case Date:
			// return id3v2.getDate();
			// case Composer:
			// return id3v2.getComposer();
			// case Publisher:
			// return id3v2.getPublisher();
			// case OriginalArtist:
			// return id3v2.getOriginalArtist();
			// case AlbumArtist:
			// return id3v2.getAlbumArtist();
			// case Copyright:
			// return id3v2.getCopyright();
			// case ArtistUrl:
			// return id3v2.getArtistUrl();
			// case CommercialUrl:
			// return id3v2.getCommercialUrl();
			// case CopyrightUrl:
			// return id3v2.getCopyrightUrl();
			// case AudiofileUrl:
			// return id3v2.getAudiofileUrl();
			// case AudioSourceUrl:
			// return id3v2.getAudioSourceUrl();
			// case RadiostationUrl:
			// return id3v2.getRadiostationUrl();
			// case PaymentUrl:
			// return id3v2.getPaymentUrl();
			// case PublisherUrl:
			// return id3v2.getPublisherUrl();
			// case Url:
			// return id3v2.getUrl();
			// case PartOfSet:
			// return id3v2.getPartOfSet();
			// case Encoder:
			// return id3v2.getEncoder();
			// case AlbumImageMimeType:
			// return id3v2.getAlbumImageMimeType();
			// case ItunesComment:
			// return id3v2.getItunesComment();
			default:
				throw new RuntimeException("unkown Info: " + info);
			}

		} else {
			return null;
		}
	}

	private void renameFile(File file, String newName) {
		String newPath = file.getParent() + "\\" + newName;
		file.renameTo(new File(newPath));
	}

	private void setInfo(int number, Info info, String str) throws NotSupportedException, IOException {
		Mp3File mp3file = this.mp3Files.get(number);
		File file = this.files.get(number);
		if (mp3file.hasId3v1Tag()) {
			ID3v1 id3v1 = mp3file.getId3v1Tag();
			switch (info) {
			case FileName:
				renameFile(file, str);
				break;
			// case Version:
			// return;
			case Track:
				id3v1.setTrack(str);
				break;
			case Artist:
				id3v1.setArtist(str);
				break;
			case Title:
				id3v1.setTitle(str);
				break;
			case Album:
				id3v1.setAlbum(str);
				break;
			case Year:
				id3v1.setYear(str);
				break;
			case Comment:
				id3v1.setComment(str);
				break;
			default:
				return;
			}
			mp3file.save(mp3file.getFilename());
		} else if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2 = mp3file.getId3v2Tag();
			switch (info) {
			case FileName:
				renameFile(file, str);
				break;
			// case Version:
			// return;
			case Track:
				id3v2.setTrack(str);
				break;
			case Artist:
				id3v2.setArtist(str);
				break;
			case Title:
				id3v2.setTitle(str);
				break;
			case Album:
				id3v2.setAlbum(str);
				break;
			case Year:
				id3v2.setYear(str);
				break;
			case Comment:
				id3v2.setComment(str);
				break;
			default:
				return;
			}
			mp3file.save(mp3file.getFilename());

		} else {
			return;
		}
	}

	// private boolean isId3v1Info(Info info) {
	// switch (info) {
	// case FileName:
	// return true;
	// case Version:
	// return true;
	// case Track:
	// return true;
	// case Artist:
	// return true;
	// case Title:
	// return true;
	// case Album:
	// return true;
	// case Year:
	// return true;
	// case GenreDescription:
	// return true;
	// case Comment:
	// return true;
	// // case Grouping:
	// // return false;
	// // case Key:
	// // return false;
	// // case Date:
	// // return false;
	// // case Composer:
	// // return false;
	// // case Publisher:
	// // return false;
	// // case OriginalArtist:
	// // return false;
	// // case AlbumArtist:
	// // return false;
	// // case Copyright:
	// // return false;
	// // case ArtistUrl:
	// // return false;
	// // case CommercialUrl:
	// // return false;
	// // case CopyrightUrl:
	// // return false;
	// // case AudiofileUrl:
	// // return false;
	// // case AudioSourceUrl:
	// // return false;
	// // case RadiostationUrl:
	// // return false;
	// // case PaymentUrl:
	// // return false;
	// // case PublisherUrl:
	// // return false;
	// // case Url:
	// // return false;
	// // case PartOfSet:
	// // return false;
	// // case Encoder:
	// // return false;
	// // case AlbumImageMimeType:
	// // return false;
	// // case ItunesComment:
	// // return false;
	// default:
	// throw new RuntimeException("unkown Info: " + info);
	// }
	// }

	public Info getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(Info sourceInfo) {
		this.sourceInfo = sourceInfo;
		setMaxNumber();
	}

	public Info getDestinationInfo() {
		return destinationInfo;
	}

	public void setDestinationInfo(Info destinationInfo) {
		this.destinationInfo = destinationInfo;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		setMaxNumber();
	}

	public int getMaxNumberLength() {
		return this.maxNumber;
	}

}
