package Sound;

/**
 *
 * @author Julian Craske
 */

import java.io.*;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Mixer mixer = AudioSystem.getMixer(null);
    private File playing;
    private boolean stopped;

    public MusicPlayer() {

    }

    public synchronized void play() {
        try {
            AudioFormat af = AudioSystem.getAudioFileFormat(playing).getFormat();
            AudioInputStream audio = AudioSystem.getAudioInputStream(playing);
            DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) mixer.getLine(lineInfo);
            line.open(af);
            line.start();
            byte[] data = new byte[1024];
            while (audio.available() > 0 && !stopped) {
                if(line.available() > 512) {
                    int numBytesRead = audio.read(data, 0, Math.min(Math.min(audio.available(), line.available()), data.length));
                    line.write(data, 0, numBytesRead);
                }
                Thread.yield();
            }
            line.drain();
            line.stop();
            line.close();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }  catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void playAudioFile(String filepath) {
        stopped = true;
        changeTrack(filepath);
    }

    private synchronized void changeTrack(String filepath) {
        this.playing = new File(filepath);
    }
}

