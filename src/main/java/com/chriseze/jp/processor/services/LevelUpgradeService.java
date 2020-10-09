package com.chriseze.jp.processor.services;

import com.chriseze.jp.processor.repositories.DataRepository;
import com.chriseze.jp.processor.utils.ProxyUtil;
import com.chriseze.yonder.utils.entities.Project;
import com.chriseze.yonder.utils.entities.Recommendation;
import com.chriseze.yonder.utils.entities.Talent;
import com.chriseze.yonder.utils.enums.Level;
import java.util.List;
import java.util.Set;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Stateless
public class LevelUpgradeService {

    @Inject
    private ProxyUtil proxyUtil;

    @Inject
    private DataRepository dataRepository;

    @Asynchronous
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
                log.info("\nEmail: {}\nLevel: {}\nRecommendations: {}\nCompleted Projects: {}", talent.getEmail(), talent.getLevel().name(), recommendations.size(), getNumberOfCompletedProjects(talent.getProjects()), talent.getLevel());
                proxyUtil.executeAsync(() -> dataRepository.update(talent));
            }
        });
    }

    private int getNumberOfCompletedProjects(Set<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return 0;
        }
        log.info("\nProjects: {}", projects.size());
        int count = 0;
        for (Project project : projects) {
            if (project.getEndDate() != null) {
                count++;
            }
        }
        return count;
    }
}
