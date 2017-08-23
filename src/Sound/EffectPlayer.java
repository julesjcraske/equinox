package Sound;

/**
 *
 * @author Julian Craske
 */

import java.io.*;
import sun.audio.*;
import java.util.*;

import Core.*;
import Player.*;

public class EffectPlayer implements SoundListener {
    private static String filepathPrefix = "src\\sounds\\";

    private Player player;
    private HashMap<String, AudioStream> streams = new HashMap<String, AudioStream>();

    public EffectPlayer(Player player) {
        this.player = player;
    }

    public void playIfFocus(Entity source, String filepath) {
        if(source == player.getFocus()) {
            play(source, filepath);
        }
    }

    public void play(Entity source, String filepath) {
        if(player.getFocus().getSectorID() == source.getSectorID() &&
                player.getFocus().getDistanceToEntity(source) < 1000) {
            try {
                AudioStream audioStream;

                if(streams.containsKey(filepathPrefix + filepath)) {
                    audioStream = streams.get(filepath);
                } else {
                    audioStream = new AudioStream(new FileInputStream(filepathPrefix + filepath));
                }

                AudioPlayer.player.start(audioStream);

            } catch (IOException ex) {
                System.out.println("Error playing sound effect: " + filepathPrefix + filepath + "\n" + ex);

            }
        }
    }
}


