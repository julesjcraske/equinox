/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Player;

/**
 *
 * @author Julian Craske
 */
public class PlayerInfo {
    private String name;
    private int team = 0;
    private int kills = 0;
    private int deaths = 0;
    private int score = 0;
    private long ping = -1;

    public PlayerInfo(String name) {
        this.name = name;
    }

    public PlayerInfo(String name, int team, int kills, int deaths, int score) {
        this.name = name;
        this.team = team;
        this.kills = kills;
        this.deaths = deaths;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getTeam() {
        return team;
    }

    public long getLatency() {
        return ping;
    }

    public void setLatency(long ping) {
        this.ping = ping;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getScore() {
        return score;
    }

    public void increaseKills() {
        kills++;
    }

    public void increaseDeaths() {
        deaths++;
    }

    public void increaseScore(int num) {
        score += num;
    }

    public void takeForm(PlayerInfo info) {
        this.team = info.team;
        this.kills = info.kills;
        this.deaths = info.deaths;
        this.score = info.score;
    }

    public String toString() {
        return name + "    " + kills + "    " + deaths + "    " + score;
    }
}
