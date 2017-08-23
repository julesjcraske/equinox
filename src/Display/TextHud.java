package Display;

import javax.swing.*;

import Game.*;

public class TextHud extends JPanel
{
    private JTextArea stats = new JTextArea();
    private GameClient client;

    public TextHud(GameClient c)
    {
        this.client = c;
        stats.setEditable(false);
        stats.setFocusable(false);
        add(stats);        
    }

    public void refresh() {
        stats.setText(
                "Bytes/sec: " + client.getBytesPerSecond() + "\n" +
                "Latency: " + client.getPlayer().getInfo().getLatency() + "\n");
    }
}
