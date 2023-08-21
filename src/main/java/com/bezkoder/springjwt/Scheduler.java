package com.bezkoder.springjwt;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Scheduler {

    @Scheduled(cron = "15 * * * * *")
    public void runWithdrawal(){

    }
}
