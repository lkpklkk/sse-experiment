package com.example.sseexperiment.controllors;

import com.example.sseexperiment.dao.NpcDao;
import com.example.sseexperiment.dao.UsersDao;
import com.example.sseexperiment.exceptions.LockTimeOutException;
import com.example.sseexperiment.exceptions.NpcNotFoundException;
import com.example.sseexperiment.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lekeping
 */
@RestController
public class VoteNpcControllor {
    private NpcDao npcDao = NpcDao.getInstance();
    private List<SseEmitter> sseEmitterList = new CopyOnWriteArrayList<>();
    private UsersDao usersDao = UsersDao.getInstance();
    //TODO: implement error handling?
    //TODO: implement daemon thread on timed update

    @CrossOrigin
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("Init"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitter.onCompletion(() -> sseEmitterList.remove(sseEmitter));
        sseEmitterList.add(sseEmitter);
        return sseEmitter;
    }

    @CrossOrigin
    @PostMapping(value = "/vote")
    public ResponseEntity<Object> vote(@RequestParam String candidateName, @RequestParam int userId) throws LockTimeOutException, InterruptedException, NpcNotFoundException, UserNotFoundException {
        if (usersDao.voteValidate(userId)) {
            npcDao.incrementVote(candidateName);
            usersDao.incrementVoteCount(userId);
        } else {
            return new ResponseEntity<>("Exceed daily vote limit", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * ??????Spring boot @Scheduled notation
     * ??????????????????
     *
     * @return
     * @throws LockTimeOutException
     * @throws InterruptedException
     */
    @Scheduled(fixedRate = 1000)
    private ResponseEntity<Object> pushVotes() throws LockTimeOutException, InterruptedException {

        String dataString = npcDao.getVotes().toString();

        sseEmitterList.forEach(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().name("newest votes").data(dataString));
            } catch (IOException e) {
                sseEmitterList.remove(sseEmitter);
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
