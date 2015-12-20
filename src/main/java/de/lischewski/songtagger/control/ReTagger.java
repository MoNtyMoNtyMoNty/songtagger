package de.lischewski.songtagger.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class ReTagger {

	public enum Info {
		FileName,
		Version,
		Track,
		Artist,
		Title,
		Album,
		Year,
		GenreDescription,
		Comment,
		Grouping,
		Key,
		Date,
		Composer,
		Publisher,
		OriginalArtist,
		AlbumArtist,
		Copyright,
		ArtistUrl,
		CommercialUrl,
		CopyrightUrl,
		AudiofileUrl,
		AudioSourceUrl,
		RadiostationUrl,
		PaymentUrl,
		PublisherUrl,
		Url,
		PartOfSet,
		Encoder,
		AlbumImageMimeType,
		ItunesComment;
	}

	private ArrayList<Mp3File> mp3Files;

	public ReTagger(File directory) {
		mp3Files = new ArrayList<Mp3File>();
		if (directory != null && directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				try {
					Mp3File mp3file = new Mp3File(file);
					mp3Files.add(mp3file);
				} catch (InvalidDataException exception) {
					System.out.println("ignored File: " + file.getName());
				} catch (UnsupportedTagException | IOException exception) {
					System.out.println("ignored File: " + file.getName());
					exception.printStackTrace();
				}
			}
		} else {
			
		}
	}

	public ArrayList<ArrayList<Integer>> getNumbers(Info info) {
		ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < mp3Files.size(); ++i) {
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			if (hasInfo(i, info)) {
				Pattern p = Pattern.compile("[0-9]+");
				Matcher m = p.matcher(getInfo(i, info));
				while (m.find()) {
					numbers.add(Integer.parseInt(m.group()));
				}
			}
			ret.add(numbers);
		}
		return ret;
	}

	public int getNumberOfSongs() {
		return this.mp3Files.size();
	}

	public String getInfo(int number, Info info) {
		Mp3File mp3file = this.mp3Files.get(number);
		if (isId3v1Info(info)) {
			ID3v1 id3v1 = mp3file.getId3v1Tag();
			switch (info) {
			case FileName:
				return mp3file.getFilename();
			case Version:
				return id3v1.getVersion();
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
				return mp3file.getFilename();
			case Version:
				return id3v2.getVersion();
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
			case Grouping:
				return id3v2.getGrouping();
			case Key:
				return id3v2.getKey();
			case Date:
				return id3v2.getDate();
			case Composer:
				return id3v2.getComposer();
			case Publisher:
				return id3v2.getPublisher();
			case OriginalArtist:
				return id3v2.getOriginalArtist();
			case AlbumArtist:
				return id3v2.getAlbumArtist();
			case Copyright:
				return id3v2.getCopyright();
			case ArtistUrl:
				return id3v2.getArtistUrl();
			case CommercialUrl:
				return id3v2.getCommercialUrl();
			case CopyrightUrl:
				return id3v2.getCopyrightUrl();
			case AudiofileUrl:
				return id3v2.getAudiofileUrl();
			case AudioSourceUrl:
				return id3v2.getAudioSourceUrl();
			case RadiostationUrl:
				return id3v2.getRadiostationUrl();
			case PaymentUrl:
				return id3v2.getPaymentUrl();
			case PublisherUrl:
				return id3v2.getPublisherUrl();
			case Url:
				return id3v2.getUrl();
			case PartOfSet:
				return id3v2.getPartOfSet();
			case Encoder:
				return id3v2.getEncoder();
			case AlbumImageMimeType:
				return id3v2.getAlbumImageMimeType();
			case ItunesComment:
				return id3v2.getItunesComment();
			default:
				throw new RuntimeException("unkown Info: " + info);
			}

		} else {
			return null;
		}
	}

	private boolean hasInfo(int number, Info info) {
		Mp3File mp3file = this.mp3Files.get(number);
		if (mp3file.hasId3v2Tag()) {
			return true;
		} else if (mp3file.hasId3v1Tag()) {
			return isId3v1Info(info);
		} else {
			return false;
		}
	}

	private boolean isId3v1Info(Info info) {
		switch (info) {
		case FileName:
			return true;
		case Version:
			return true;
		case Track:
			return true;
		case Artist:
			return true;
		case Title:
			return true;
		case Album:
			return true;
		case Year:
			return true;
		case GenreDescription:
			return true;
		case Comment:
			return true;
		case Grouping:
			return false;
		case Key:
			return false;
		case Date:
			return false;
		case Composer:
			return false;
		case Publisher:
			return false;
		case OriginalArtist:
			return false;
		case AlbumArtist:
			return false;
		case Copyright:
			return false;
		case ArtistUrl:
			return false;
		case CommercialUrl:
			return false;
		case CopyrightUrl:
			return false;
		case AudiofileUrl:
			return false;
		case AudioSourceUrl:
			return false;
		case RadiostationUrl:
			return false;
		case PaymentUrl:
			return false;
		case PublisherUrl:
			return false;
		case Url:
			return false;
		case PartOfSet:
			return false;
		case Encoder:
			return false;
		case AlbumImageMimeType:
			return false;
		case ItunesComment:
			return false;
		default:
			throw new RuntimeException("unkown Info: " + info);
		}
	}

}
