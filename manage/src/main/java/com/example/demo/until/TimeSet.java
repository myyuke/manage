package com.example.demo.until;

import com.example.demo.control.PersonControl;
import com.example.demo.model.Person;
import com.example.demo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Timer;
import java.util.TimerTask;




public class TimeSet {
    @Autowired
    private TokenRepository tokenRepository;

    public void timeStart(Integer id) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // task to run goes here
                PersonControl.map.put("token","");
                PersonControl.map.put("user",null);
                System.out.println("缓存清除成功");
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,20000);
    }

}
