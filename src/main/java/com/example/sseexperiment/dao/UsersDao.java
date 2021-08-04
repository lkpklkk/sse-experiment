package com.example.sseexperiment.dao;

import com.example.sseexperiment.ReadWriteLock;
import com.example.sseexperiment.exceptions.LockTimeOutException;
import com.example.sseexperiment.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class UsersDao {
    private static final int MAX_VOTE_ALLOWED = 10;
    private Map<Integer, Integer> voteCount = new HashMap<>();
    private ReadWriteLock readWriteLock = new ReadWriteLock();
    private static final UsersDao INSTANCE = new UsersDao();

    public static UsersDao getInstance() {
        return INSTANCE;
    }

    public void incrVote(int userId) throws LockTimeOutException, InterruptedException, UserNotFoundException {
        readWriteLock.lockWrite();
        Integer oldValue;
        if ((oldValue = voteCount.get(userId)) == null) {
            throw new UserNotFoundException();
        }
        voteCount.replace(userId, oldValue + 1);
        readWriteLock.unlockWrite();
    }

    public void removeUser(int userId) throws LockTimeOutException, InterruptedException, UserNotFoundException {
        readWriteLock.lockWrite();
        if (voteCount.get(userId) == null) {
            readWriteLock.unlockWrite();
            throw new UserNotFoundException();
        }
        voteCount.remove(userId);
        readWriteLock.unlockWrite();
    }

    public void voteCountReset() throws LockTimeOutException, InterruptedException {
        readWriteLock.lockWrite();
        voteCount.forEach((key, value) -> {
            voteCount.replace(key, 0);
        });
        readWriteLock.unlockWrite();
    }

    public void addUser(int userId) throws LockTimeOutException, InterruptedException {
        readWriteLock.lockWrite();
        voteCount.putIfAbsent(userId, 0);
        readWriteLock.unlockWrite();
    }

    public boolean authenticate(int userId) throws LockTimeOutException, InterruptedException {
        readWriteLock.lockRead();
        if (voteCount.containsKey(userId)) {
            readWriteLock.unlockRead();
            return true;
        } else {
            readWriteLock.unlockRead();
            return false;
        }
    }

    public boolean voteValidate(int userId) throws UserNotFoundException {
        readWriteLock.unlockRead();
        if (voteCount.containsKey(userId)) {
            if (voteCount.get(userId) < MAX_VOTE_ALLOWED) {
                readWriteLock.unlockRead();
                return true;
            }
        } else {
            throw new UserNotFoundException();
        }
        readWriteLock.unlockRead();
        return false;
    }
}
