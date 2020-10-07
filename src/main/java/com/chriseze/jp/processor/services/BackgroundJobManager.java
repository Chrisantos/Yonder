package com.chriseze.jp.processor.services;

import com.chriseze.jp.processor.entities.Recommendation;
import com.chriseze.jp.processor.repositories.DataRepository;
import com.chriseze.jp.processor.utils.ProxyUtil;
import com.chriseze.jp.processor.entities.Project;
import com.chriseze.jp.processor.entities.Talent;
import com.chriseze.yonder.utils.enums.Level;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Startup
@Singleton
public class BackgroundJobManager {

    @Inject
    private ProxyUtil proxyUtil;

    @Inject
    private DataRepository dataRepository;

    @PostConstruct
    private void init() {
        log.debug("Job is running >>>>> ");
    }

    @Schedule(hour = "*/24", persistent = false)
    public void levelUpgrader() {
        List<Talent> talents = proxyUtil.executeWithNewTransaction(() -> dataRepository.getAllTalents());
        if (talents == null || talents.isEmpty()) {
            log.error("No talents exist");
            return;
        }

        talents.forEach(talent -> {
            Set<Recommendation> recommendations = talent.getRecommendations();
            if (recommendations != null) {
                talent.setLevel(Level.getLevel(recommendations.size(), getNumberOfCompletedProjects(talent.getProjects()), talent.getLevel()));
                proxyUtil.executeAsync(() -> dataRepository.update(talent));
            }
        });
    }

    private int getNumberOfCompletedProjects(Set<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Project project : projects) {
            if (project.getEndDate() == null) {
                count++;
            }
        }
        return count;
    }


}
