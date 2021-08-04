package com.example.sseexperiment.dao;

import com.example.sseexperiment.ReadWriteLock;
import com.example.sseexperiment.exceptions.CandidateNotFoundException;
import com.example.sseexperiment.exceptions.LockTimeOutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CandidatesDao {
    // 应该从config里面load吗
    private static final CandidatesDao INSTANCE = new CandidatesDao();
    private final int MAX_CANDIDATES_NUM = 10;
    private Map<String, Integer> candidates = new HashMap<>(MAX_CANDIDATES_NUM);
    private final ReadWriteLock lock = new ReadWriteLock();
    private final long SYNC_TIME_OUT = 1000;

    private CandidatesDao() {
        init();
    }

    public static CandidatesDao getInstance() {
        return INSTANCE;
    }

    public boolean init() {
        candidates.put("A", 0);
        candidates.put("AA", 0);
        candidates.put("AAA", 0);
        candidates.put("B", 0);
        candidates.put("BB", 0);
        candidates.put("BBB", 0);
        candidates.put("C", 0);
        candidates.put("CC", 0);
        candidates.put("CCC", 0);
        candidates.put("D", 0);
        return true;
    }

    public void incr(String name) throws InterruptedException, LockTimeOutException, CandidateNotFoundException {

        lock.lockWrite();
        if (candidates.get(name) == null) {
            lock.unlockWrite();
            throw new CandidateNotFoundException();
        }
        candidates.replace(name, candidates.get(name) + 1);
        lock.unlockWrite();
    }

    public JSONObject getVotes() throws InterruptedException, LockTimeOutException {
        lock.lockRead();
        JSONObject jsonObj = new JSONObject();
        candidates.forEach((key, value) -> {
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
