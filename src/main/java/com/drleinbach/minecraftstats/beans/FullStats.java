package com.drleinbach.minecraftstats.beans;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 5/20/13
 *
 * @author Daniel
 */
public class FullStats {

    private static final Logger LOGGER = Logger.getLogger(FullStats.class);

    public static FullStats getFullStats(String[] serverResponse) {

        FullStats stats = new FullStats();
        int x = 0;
        for (x = 2; x < serverResponse.length; x += 2) {
            String key = serverResponse[x].trim();
            if (key.length() > 0) {
                stats.put(key, serverResponse[x + 1].trim());
            } else {
                x += 3;
                break;
            }
        }

        for (; x < serverResponse.length; x++) {
            stats.addPlayer(serverResponse[x]);
        }

        return stats;

    }


    private Map<String, String> serverValues;
    private List<String> players;

    private FullStats() {
        serverValues = new HashMap<String, String>();
        players = new ArrayList<String>();
    }

    public Map<String, String> getServerValues() {
        return serverValues;
    }

    public void setServerValues(Map<String, String> serverValues) {
        this.serverValues = serverValues;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public String put(String key, String value) {
        return getServerValues().put(key, value);
    }

    public String get(Object key) {
        return getServerValues().get(key);
    }

    public boolean addPlayer(String s) {
        return players.add(s);
    }

    @Override
    public String toString() {
        ObjectMapper serializer = new ObjectMapper();
        try{
        return "FullStats{" +
                "serverValues=" + serializer.writeValueAsString(serverValues) +
                ", players=" + serializer.writeValueAsString(players) +
                '}';
        } catch (Exception e) {

        }
        return null;
    }
}
