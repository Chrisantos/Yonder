package com.chriseze.jp.processor.resources;

import com.chriseze.jp.processor.restartifacts.HireTalentPojo;
import com.chriseze.jp.processor.restartifacts.JobCompletionResponse;
import com.chriseze.jp.processor.restartifacts.JobResponse;
import com.chriseze.jp.processor.restartifacts.BaseResponse;
import com.chriseze.jp.processor.restartifacts.GenericListResponse;
import com.chriseze.jp.processor.restartifacts.GenericResponse;
import com.chriseze.jp.processor.restartifacts.JobPojo;
import com.chriseze.jp.processor.restartifacts.RecommendationPojo;
import com.chriseze.jp.processor.services.JPService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobController {

    @Inject
    private JPService jpService;

    @POST
    @Path("/post-job")
    public BaseResponse postGig(@Valid JobPojo jobPojo) {
        return jpService.postGig(jobPojo);
    }

    @POST
    @Path("/apply")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public BaseResponse applyToGig(@NotBlank @FormParam("email") String email, @NotBlank @FormParam("jobId") String jobId) {
        return jpService.applyToGig(email, jobId);
    }

    @POST
    @Path("/recommend-talents")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public GenericListResponse<RecommendationPojo> generateRecommendations(@NotBlank @FormParam("jobId") String jobId) {
        return jpService.generateRecommendations(jobId);
    }

    @POST
    @Path("/hire-talent")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public GenericResponse<HireTalentPojo> hireTalent(
            @NotBlank @FormParam("jobId") String jobId,
            @NotBlank @FormParam("talentEmail") String talentEmail,
            @NotNull @FormParam("fee") Integer fee) {

        return jpService.hireTalent(jobId, talentEmail, fee);
    }

    @POST
    @Path("/end-project")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public GenericResponse<JobCompletionResponse> endProject(@NotBlank @FormParam("jobId") String jobId) {
        return jpService.endProject(jobId);
    }

    @GET
    @Path("/jobs")
    public GenericListResponse<JobResponse> getAllGigs(@NotBlank @QueryParam("status") String status) {
        return jpService.getAllGigs(status);
    }

    @GET
    @Path("/job/{id}")
    public GenericResponse<JobResponse> getJobById(@NotBlank @PathParam("id") String jobId) {
        return jpService.getGigById(jobId);
    }

    @GET
    @Path("/job")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public GenericListResponse<JobResponse> getJobByPoster(@NotBlank @FormParam("email") String posterEmail) {
        return jpService.getGigByPoster(posterEmail);
    }

}
//Accept member
