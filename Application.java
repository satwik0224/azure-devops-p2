public class Application {
    private long id;
    private long userId;
    private long jobId;
    private String appliedOn;
    private String resumeLink;

    public Application(long id, long userId, long jobId, String appliedOn, String resumeLink) {
        this.id = id;
        this.userId = userId;
        this.jobId = jobId;
        this.appliedOn = appliedOn;
        this.resumeLink = resumeLink;
    }

    public Application() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getAppliedOn() {
        return appliedOn;
    }

    public void setAppliedOn(String appliedOn) {
        this.appliedOn = appliedOn;
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public void setResumeLink(String resumeLink) {
        this.resumeLink = resumeLink;
    }

}
