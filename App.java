import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;

public class App {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8000);

        app.post("/apply", ctx -> {
            Application application = objectMapper.readValue(ctx.body(), Application.class);
            long jobId = application.getJobId();
            long userId = application.getUserId();
            String resumeLink = application.getResumeLink();

            if (DbUtils.isJobExists(jobId)) {
                DbUtils.insertApplication(userId, jobId, resumeLink);
                ctx.result("Application submitted successfully!");
            } else {
                ctx.status(404).result("Job not found");
            }
        });

        app.get("/applications/user/{userId}", ctx -> {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            List<Application> userApplications = DbUtils.getApplicationsByUserId(userId);
            ctx.json(userApplications);
        });

        app.get("/application/{id}", ctx -> {
            long applicationId = Long.parseLong(ctx.pathParam("id"));
            Application application = DbUtils.getApplicationById(applicationId);
            if (application != null) {
                ctx.json(application);
            } else {
                ctx.status(404).result("Application not found");
            }
        });

        app.delete("/application/{id}", ctx -> {
            long applicationId = Long.parseLong(ctx.pathParam("id"));
            if (DbUtils.isApplicationExists(applicationId)) {
                DbUtils.deleteApplication(applicationId);
                ctx.result("Application withdrawn successfully!");
            } else {
                ctx.status(404).result("Application not found");
            }
        });

        app.post("/job/create", ctx -> {
            Job job = objectMapper.readValue(ctx.body(), Job.class);
            DbUtils.insertJob(job);
            ctx.result("Job created successfully!");
            
        });

        app.get("/jobs", ctx -> {
            List<Job> jobs = DbUtils.getAllJobs();
            ctx.json(jobs);
        });

        app.get("/job/{id}", ctx -> {
            long jobId = Long.parseLong(ctx.pathParam("id"));
            Job job = DbUtils.getJobById(jobId);
            if (job != null) {
                ctx.json(job);
            } else {
                ctx.status(404).result("Job not found");
            }
        });

        app.get("/jobs/{userId}", ctx -> {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            List<Job> userJobs = DbUtils.getJobsByUserId(userId);
            ctx.json(userJobs);
        });

        app.patch("/job/{id}", ctx -> {
            long jobId = Long.parseLong(ctx.pathParam("id"));
            Job updatedJob = objectMapper.readValue(ctx.body(), Job.class);
            if (DbUtils.isJobExists(jobId)) {
                DbUtils.updateJob(jobId, updatedJob);
                ctx.result("Job updated successfully!");
            } else {
                ctx.status(404).result("Job not found");
            }
        });

        app.delete("/job/{id}", ctx -> {
            long jobId = Long.parseLong(ctx.pathParam("id"));
            if (DbUtils.isJobExists(jobId)) {
                DbUtils.deleteJob(jobId);
                ctx.result("Job deleted successfully!");
            } else {
                ctx.status(404).result("Job not found");
            }
        });

        app.get("/applications/job/{jobId}", ctx -> {
            long jobId = Long.parseLong(ctx.pathParam("jobId"));
            List<Application> jobApplications = DbUtils.getApplicationsByJobId(jobId);
            ctx.json(jobApplications);
        });
    }
}

