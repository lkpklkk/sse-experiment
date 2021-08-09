package com.example.sseexperiment;

import com.example.sseexperiment.exceptions.LockTimeOutException;
import com.example.sseexperiment.exceptions.NpcNotFoundException;
import com.example.sseexperiment.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpringExceptionHandlar {
    @ExceptionHandler(value = NpcNotFoundException.class)
    public ResponseEntity<Object> candidateNotFoundHandler() {
        return new ResponseEntity<>("Npc not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = LockTimeOutException.class)
    public ResponseEntity<Object> lockTimeOutHandlar() {
        return new ResponseEntity<>("read-write-lock time out", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundHandlar() {
        return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
    }
}
