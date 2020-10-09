package com.chriseze.jp.processor.services;

import com.chriseze.jp.processor.enums.ResponseEnum;
import com.chriseze.jp.processor.pojos.Constants;
import com.chriseze.jp.processor.repositories.DataRepository;
import com.chriseze.jp.processor.restartifacts.BaseResponse;
import com.chriseze.jp.processor.restartifacts.GenericListResponse;
import com.chriseze.jp.processor.restartifacts.GenericResponse;
import com.chriseze.jp.processor.restartifacts.HireTalentPojo;
import com.chriseze.jp.processor.restartifacts.JobCompletionResponse;
import com.chriseze.jp.processor.restartifacts.JobPojo;
import com.chriseze.jp.processor.restartifacts.JobResponse;
import com.chriseze.jp.processor.restartifacts.RecommendationPojo;
import com.chriseze.jp.processor.restartifacts.SocialMediaPojo;
import com.chriseze.jp.processor.utils.ProxyUtil;
import com.chriseze.yonder.utils.entities.*;
import com.chriseze.yonder.utils.enums.Industry;
import com.chriseze.yonder.utils.enums.ProjectStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

@Stateless
public class JPService {

    @Inject
    private ProxyUtil proxyUtil;

    @Inject
    private DataRepository dataRepository;

    public BaseResponse postGig(JobPojo jobPojo) {
        BaseResponse response = new BaseResponse(ResponseEnum.ERROR);
        if (jobPojo == null) {
            return response;
        }

        String postedBy = jobPojo.getPostedBy();
        if (StringUtils.isBlank(postedBy)) {
            response.setDescription("Email cannot be blank");
            return response;
        }

        Client client = proxyUtil.executeWithNewTransaction(() -> dataRepository.getClientByEmailOrName(postedBy));
        if (client == null) {
            response.assignResponseEnum(ResponseEnum.NO_USER);
            return response;
        }

        Project project = new Project();
        project.setTitle(jobPojo.getTitle());
        project.setDescription(jobPojo.getDescription());
        project.setLocation(jobPojo.getLocation());
        project.setIndustry(EnumUtils.getEnum(Industry.class, jobPojo.getIndustry()));
        project.setFee(jobPojo.getFee());
        project.setClient(client);
        project.setStatus(ProjectStatus.OPEN);

        Set<Project> projects = client.getProjects();
        if (projects == null) {
            projects = new HashSet<>();
        }

        projects.add(project);
        client.setProjects(projects);

        Client updatedClient = proxyUtil.executeWithNewTransaction(() -> dataRepository.update(client));
        if (updatedClient == null) {
            response.setDescription("An error occurred while posting job");
            return response;
        }

        response.setDescription("You have successfully posted a new job");
        response.setCode(ResponseEnum.SUCCESS.getCode());
        return response;
    }

    public GenericListResponse<JobResponse> getAllGigs(String status) {
        GenericListResponse<JobResponse> response = new GenericListResponse<>(ResponseEnum.ERROR);
        if (StringUtils.isBlank(status) || EnumUtils.getEnum(ProjectStatus.class, status.toUpperCase()) == null) {
            return response;
        }

        ProjectStatus projectStatus = EnumUtils.getEnum(ProjectStatus.class, status.toUpperCase());

        List<Project> projects = proxyUtil.executeWithNewTransaction(() -> dataRepository.getAllProjects(projectStatus));
        if (projects == null || projects.isEmpty()) {
            response.setDescription("No projects found");
            response.setCode(ResponseEnum.ERROR.getCode());
            return response;
        }

        List<JobResponse> jobResponseList = new ArrayList<>();
        projects.forEach(project -> {
            jobResponseList.add(projectDetails(project));
        });

        response.setResults(jobResponseList);
        response.assignResponseEnum(ResponseEnum.SUCCESS);
        return response;
    }

    public GenericResponse<JobResponse> getGigById(String jobId) {
        GenericResponse<JobResponse> response = new GenericResponse<>(ResponseEnum.ERROR);
        if (StringUtils.isBlank(jobId)) {
            return response;
        }

        Long id = Long.valueOf(jobId);
        Project project = proxyUtil.executeWithNewTransaction(() -> dataRepository.findById(Project.class, id));
        if (project == null) {
            response.setDescription("No project with such id exists");
            return response;
        }

        response.setResult(projectDetails(project));
        response.assignResponseEnum(ResponseEnum.SUCCESS);
        return response;
    }

    public GenericListResponse<JobResponse> getGigByPoster(String posterEmail) {
        GenericListResponse<JobResponse> response = new GenericListResponse<>(ResponseEnum.ERROR);
        if (StringUtils.isBlank(posterEmail)) {
            return response;
        }

        Client client = proxyUtil.executeWithNewTransaction(() -> dataRepository.getClientByEmailOrName(posterEmail));
        if (client == null) {
            response.setDescription("The email address entered does not exist");
            return response;
        }

        List<Project> projects = proxyUtil.executeWithNewTransaction(() -> dataRepository.getAllProjectsByPoster(client));
        if (projects == null || projects.isEmpty()) {
            response.setDescription("The user with the email address hasn't created any project yet");
            return response;
        }

        List<JobResponse> jobResponseList = new ArrayList<>();
        projects.forEach(project -> {
            jobResponseList.add(projectDetails(project));
        });

        response.setResults(jobResponseList);
        response.assignResponseEnum(ResponseEnum.SUCCESS);
        return response;
    }

    public BaseResponse applyToGig(String email, String jobId) {
        BaseResponse response = new BaseResponse(ResponseEnum.ERROR);
        if (StringUtils.isBlank(email) || StringUtils.isBlank(jobId)) {
            return response;
        }

        Talent talent = proxyUtil.executeWithNewTransaction(() -> dataRepository.getTalentByEmailOrName(email));
        if (talent == null) {
            response.assignResponseEnum(ResponseEnum.NO_USER);
            return response;
        }

        Long id = Long.valueOf(jobId);
        Project project = proxyUtil.executeWithNewTransaction(() -> dataRepository.findById(Project.class, id));
        if (project == null) {
            response.setDescription("Such job opening does not exist");
            return response;
        }

        Set<ProjectApplicant> projectApplicants = project.getProjectApplicants();
        if (projectApplicants == null) {
            projectApplicants = new HashSet<>();
        }

        ProjectApplicant projectApplicant = new ProjectApplicant();
        projectApplicant.setApplicant(talent);
        projectApplicant.setProject(project);
        projectApplicants.add(projectApplicant);
        project.setProjectApplicants(projectApplicants);

        Integer numberOfApplicants = project.getNoOfApplicants();
        numberOfApplicants = numberOfApplicants == null? 1 : ++numberOfApplicants;
        project.setNoOfApplicants(numberOfApplicants);

        Project updatedProject = proxyUtil.executeWithNewTransaction(() -> dataRepository.update(project));
        if (updatedProject == null) {
            response.setDescription("An error occurred while processing project application");
            return response;
        }

        response.setDescription("You have successfully applied to the project and you will be notified once a decision is made.");
        response.setCode(ResponseEnum.SUCCESS.getCode());
        return response;
    }

    public GenericResponse<HireTalentPojo> hireTalent(String jobId, String talentEmail, Integer fee) {
        GenericResponse<HireTalentPojo> response = new GenericResponse<>(ResponseEnum.ERROR);
        if (StringUtils.isBlank(jobId) || StringUtils.isBlank(talentEmail)) {
            return response;
        }

        Long id = Long.valueOf(jobId);
        Project project = proxyUtil.executeWithNewTransaction(() -> dataRepository.findById(Project.class, id));
        if (project == null) {
            response.setDescription("No project with such id exists");
            return response;
        }

        Talent talent = proxyUtil.executeWithNewTransaction(() -> dataRepository.getTalentByEmailOrName(talentEmail));
        if (talent == null) {
            response.setDescription("No user with such email address exists");
            return response;
        }

        project.setImplementedBy(talent);
        project.setStatus(ProjectStatus.ONGOING);

        fee = fee == null? talent.getHourlyRate() * Constants.FLAT_HOURS : fee;
        project.setFee(fee);

//        Set<Project> projects = talent.getProjects();
//        if (projects == null) {
//            projects = new HashSet<>();
//        }
//        projects.add(project);
//        talent.setProjects(projects);

        Project updatedProject = proxyUtil.executeWithNewTransaction(() -> dataRepository.update(project));
//        Talent updatedTalent = proxyUtil.executeWithNewTransaction(() -> dataRepository.update(talent));

        if (updatedProject == null) {
            response.setDescription("Error occurred while hiring talent");
            return response;
        }

        HireTalentPojo hireTalentPojo = new HireTalentPojo();
        hireTalentPojo.setTalentEmail(talentEmail);
        hireTalentPojo.setClientEmail(project.getClient().getEmail());
        hireTalentPojo.setProjectTitle(project.getTitle());

        response.setResult(hireTalentPojo);
        response.assignResponseEnum(ResponseEnum.SUCCESS);
        return response;
    }

    public GenericListResponse<RecommendationPojo> generateRecommendations(String jobId) {
        GenericListResponse<RecommendationPojo> response = new GenericListResponse<>(ResponseEnum.ERROR);
        if (StringUtils.isBlank(jobId)) {
            return response;
        }

        Long id = Long.valueOf(jobId);
        Project project = proxyUtil.executeWithNewTransaction(() -> dataRepository.findById(Project.class, id));
        if (project == null) {
            response.setDescription("No project with such id exists");
            return response;
        }

        Industry industry = project.getIndustry();
        String location = project.getLocation();
        String title = project.getTitle();
        String description = project.getDescription();

        List<Talent> talents = proxyUtil.executeWithNewTransaction(() -> dataRepository.getTalents(industry != null? industry : Industry.IT, location));
        if (talents == null || talents.isEmpty()) {
            response.setDescription("Talent with such project requirements does not exist");
            return response;
        }

        List<String> titleAsList = toList(title);
        List<String> descriptionAsList = toList(description);

        talents = processListOfRecommendableTalents(talents, titleAsList, descriptionAsList);

        List<RecommendationPojo> recommendationPojoList = new ArrayList<>();
        int count = 0;

        for (Talent talent : talents) {
            if (count >= 4) {
                break;
            }

            RecommendationPojo recommendationPojo = new RecommendationPojo();
            recommendationPojo.setSkills(getSkills(talent.getSkills()));
            recommendationPojo.setSocialMediaPojos(getSocialMediaList(talent.getSocialMedia()));
            recommendationPojo.setTalentEmail(talent.getEmail());
            recommendationPojo.setTalentFullName(talent.getName());
            recommendationPojo.setTalentPhoneNumber(talent.getPhoneNumber());

            recommendationPojoList.add(recommendationPojo);

            saveRecommendations(talent, project);
            count++;
        }

        response.setResults(recommendationPojoList);
        response.assignResponseEnum(ResponseEnum.SUCCESS);
        return response;
    }

    private List<String> toList(String sentence) {
        if (StringUtils.isBlank(sentence)) {
            return Collections.emptyList();
        }
        return Stream.of(sentence.split("\\s+")).collect(Collectors.toList());
    }

    private boolean isSimilarityFound(List<String> list1, List<String> list2) {
        for (String item : list1) {
            if (list2.contains(item)) {
                return true;
            }
        }
        return false;
    }

    private List<Talent> processListOfRecommendableTalents(List<Talent> talents, List<String> titleAsList, List<String> descriptionAsList) {
        List<Talent> recommendedTalents = new ArrayList<>();

        for (Talent talent : talents) {
            Set<Project> projects = talent.getProjects();

            if (projects == null || projects.isEmpty()) {
                recommendedTalents.add(talent);
                continue;
            }

            int count = 0;

            for (Project project : projects) {

                if (StringUtils.isBlank(project.getTitle()) || StringUtils.isBlank(project.getDescription())) {
                    continue;
                }

                List<String> prevProjTitleAsList = toList(project.getTitle());
                List<String> prevProjDescriptionAsList = toList(project.getDescription());

                if (isSimilarityFound(titleAsList, prevProjTitleAsList) || isSimilarityFound(descriptionAsList, prevProjDescriptionAsList)) {
                    count++;
                }
            }

            if (count >= Constants.MIN_COUNT_OF_SIMILAR_KEYWORDS) {
                recommendedTalents.add(talent);
            }
        }
        return recommendedTalents;
    }

    private Set<String> getSkills(Set<Skill> skills) {
        if (skills == null || skills.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> skillList = new HashSet<>();
        skills.forEach(skill -> skillList.add(skill.getName()));
        return skillList;
    }

    private Set<SocialMediaPojo> getSocialMediaList(Set<SocialMedia> socialMediaSet) {
        if (socialMediaSet == null || socialMediaSet.isEmpty()) {
            return Collections.emptySet();
        }

        Set<SocialMediaPojo> socialMediaPojos = new HashSet<>();
        socialMediaSet.forEach(socialMedia -> socialMediaPojos.add(new SocialMediaPojo(socialMedia.getName(), socialMedia.getHandle())));
        return socialMediaPojos;
    }

    private Set<String> getAllApplicants(Set<ProjectApplicant> applicants) {
        if (applicants == null || applicants.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> jobApplicants = new HashSet<>();
        applicants.forEach(applicant -> {
            if (applicant.getApplicant() != null) {
                jobApplicants.add(applicant.getApplicant().getEmail());
            }
        });

        return jobApplicants;
    }

    public GenericResponse<JobCompletionResponse> endProject(String jobId) {
        GenericResponse<JobCompletionResponse> response = new GenericResponse<>(ResponseEnum.ERROR);
        if (StringUtils.isBlank(jobId)) {
            return response;
        }

        Long id = Long.valueOf(jobId);
        Project project = proxyUtil.executeWithNewTransaction(() -> dataRepository.findById(Project.class, id));
        if (project == null) {
            response.setDescription("No project with such id exists");
            return response;
        }

        project.setEndDate(LocalDate.now());
        project.setStatus(ProjectStatus.COMPLETED);

        Project updatedProject = proxyUtil.executeWithNewTransaction(() -> dataRepository.update(project));
        if (updatedProject == null) {
            response.setDescription("An error occurred while processing project application");
            return response;
        }

        JobCompletionResponse jobCompletionResponse = new JobCompletionResponse();
        jobCompletionResponse.setTalentEmail(updatedProject.getImplementedBy().getEmail());
        jobCompletionResponse.setClientEmail(updatedProject.getClient().getEmail());
        jobCompletionResponse.setNoOfApplicants(updatedProject.getNoOfApplicants());
        jobCompletionResponse.setProjectFee(updatedProject.getFee());
        jobCompletionResponse.setProjectTitle(updatedProject.getTitle());

        LocalDate createDate = updatedProject.getCreateDate();
        LocalDate endDate = updatedProject.getEndDate();

        jobCompletionResponse.setNoOfDaysTaken(ChronoUnit.DAYS.between(createDate, endDate));

        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setResult(jobCompletionResponse);
        response.setDescription("Project ended successfully");
        return response;
    }

    private void saveRecommendations(Talent talent, Project project) {
        if (talent == null || project == null) {
            return;
        }

        Recommendation recommendation = new Recommendation();
        recommendation.setProject(project);
        recommendation.setTalent(talent);

        Set<Recommendation> recommendations = talent.getRecommendations();
        if (recommendations == null) {
            recommendations = new HashSet<>();
        }
        recommendations.add(recommendation);
        talent.setRecommendations(recommendations);

//        Set<Recommendation> projRecommendation = project.getRecommendations();


        proxyUtil.executeAsync(() -> dataRepository.update(talent));

    }

    private JobResponse projectDetails(Project project) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setTitle(project.getTitle());
        jobResponse.setDescription(project.getDescription());
        jobResponse.setLocation(project.getLocation());
        jobResponse.setPostedBy(project.getClient().getEmail());
        jobResponse.setAppliedBy(getAllApplicants(project.getProjectApplicants()));

        Set<Recommendation> recommendations = project.getRecommendations();
        if (recommendations != null && !recommendations.isEmpty()) {

            Set<String> talentsRecommended = new HashSet<>();
            for (Recommendation recommendation : recommendations) {
                talentsRecommended.add(recommendation.getProject().getTitle());
            }
            jobResponse.setRecommendations(talentsRecommended);
        }
        return jobResponse;
    }
}
