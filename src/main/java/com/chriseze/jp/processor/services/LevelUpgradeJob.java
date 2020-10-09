package com.chriseze.jp.processor.services;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Startup
@Singleton
@AccessTimeout(unit = TimeUnit.MINUTES, value = 10)
public class LevelUpgradeJob {

    @Resource
    TimerService timerService;

    @Inject
    private LevelUpgradeService levelUpgradeService;

    @PostConstruct
    public void init(){
        initializeLevelUpgradeJob();
    }

    private void initializeLevelUpgradeJob() {
        long scheduleInterval = 120000L;

        if(log.isDebugEnabled()) {
            log.debug("scheduleInterval =========>>>>>>>>>>> {}", scheduleInterval);
        }
        timerService.createTimer(1000, scheduleInterval,"LevelUpgrade job");
    }

    @Timeout
    public void triggerTimeout(Timer timer){
        if(log.isDebugEnabled()) {
            log.debug(">>> Background Jobs triggered!!! {} : getNextTimeout {} : getTimeRemaining {}", new Date(),timer.getNextTimeout(), timer.getTimeRemaining());
        }
        levelUpgradeService.levelUpgrader();
    }

    @PreDestroy
    public void preDestroy(){
        Collection<Timer> allTimers = timerService.getTimers();
        if(allTimers == null || allTimers.isEmpty()){
            log.info("no available timers");
            return;
        }

        log.info("destroying all timers");
        for (Timer timer : allTimers) {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

}
