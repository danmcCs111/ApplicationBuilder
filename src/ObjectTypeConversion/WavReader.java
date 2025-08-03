package ObjectTypeConversion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import ActionListeners.WavReaderSubscriber;
import Properties.LoggingMessages;

public class WavReader 
{
	private File f;
	private ArrayList<WavReaderSubscriber> subs = new ArrayList<WavReaderSubscriber>();
	
	public WavReader(String arg)
	{
		this.f = new File(arg);
	}
	
	public String getAbsolutePath()
	{
		return f.getAbsolutePath();
	}
	
	public void read()
	{
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(f);
			int len = aff.getByteLength();
			AudioFormat af = aff.getFormat();
			
			LoggingMessages.printOut(
				f.getAbsolutePath() + " " 
				+ "byte length: " + len + " "
				+ "channels: " + af.getChannels() + " "
				+ "Sample Rate: " + af.getSampleRate() + " "
			);
			
			playAudio();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playAudio()
	{
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(f);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void readAudio(AudioFormat af)
	{
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
		SourceDataLine sourceLine = null;
        try {
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceLine.open(af);
			sourceLine.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void addWavReaderSubscriber(WavReaderSubscriber sub)
	{
		subs.add(sub);
	}
}
