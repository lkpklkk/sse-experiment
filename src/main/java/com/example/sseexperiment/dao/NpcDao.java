package com.example.sseexperiment.dao;

import com.example.sseexperiment.ReadWriteLock;
import com.example.sseexperiment.exceptions.LockTimeOutException;
import com.example.sseexperiment.exceptions.NpcNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lekeping
 */
public class NpcDao {

    private static final NpcDao INSTANCE = new NpcDao();
    private final int MAX_NPC_NUM = 10;
    private Map<String, Integer> npcs = new HashMap<>(MAX_NPC_NUM);
    private final ReadWriteLock lock = new ReadWriteLock();

    private NpcDao() {
        init();
    }

    public static NpcDao getInstance() {
        return INSTANCE;
    }

    /**
     * mock data here
     *
     * @return
     */
    public void init() {
        npcs.put("A", 0);
        npcs.put("AA", 0);
        npcs.put("AAA", 0);
        npcs.put("B", 0);
        npcs.put("BB", 0);
        npcs.put("BBB", 0);
        npcs.put("C", 0);
        npcs.put("CC", 0);
        npcs.put("CCC", 0);
        npcs.put("D", 0);
    }

    public void incrementVote(String name) throws InterruptedException, LockTimeOutException, NpcNotFoundException {

        lock.lockWrite();
        if (npcs.get(name) == null) {
            lock.unlockWrite();
            throw new NpcNotFoundException();
        }
        npcs.replace(name, npcs.get(name) + 1);
        lock.unlockWrite();
    }

    public JSONObject getVotes() throws InterruptedException, LockTimeOutException {
        lock.lockRead();
        JSONObject jsonObj = new JSONObject();
        npcs.forEach((key, value) -> {
            try {
                jsonObj.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        lock.unlockRead();
        return jsonObj;
    }


}
