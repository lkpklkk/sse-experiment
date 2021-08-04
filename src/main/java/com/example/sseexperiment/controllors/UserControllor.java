package com.example.sseexperiment.controllors;

import com.example.sseexperiment.dao.UsersDao;
import com.example.sseexperiment.exceptions.LockTimeOutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lekeping
 */
@RestController
public class UserControllor {
    private AtomicInteger curId = new AtomicInteger(0);
    private UsersDao usersDao = UsersDao.getInstance();


    @CrossOrigin
    @GetMapping("/signup")
    public ResponseEntity<Object> signup() throws JSONException, LockTimeOutException, InterruptedException {
        int userId = curId.getAndIncrement();
        usersDao.addUser(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/signin")
    public ResponseEntity<Object> login(@RequestParam int userId) throws LockTimeOutException, InterruptedException {
        boolean authenticated = usersDao.authenticate(userId);
        if (authenticated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("unauthorized (user doesn't exist)", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 使用Spring boot @Scheduled notation
     * 实现每天重置每人票数
     *
     * @throws LockTimeOutException
     * @throws InterruptedException
     */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void reset() throws LockTimeOutException, InterruptedException {
        usersDao.voteCountReset();
    }

}
