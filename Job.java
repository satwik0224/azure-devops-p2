import java.util.List;

public class Job {
    private long id;
    private long creatorId;
    private String companyName;
    private String logoLink;
    private String createdOn;
    private String designation;
    private String location;
    private String description;
    private List<String> skills;

    public Job(long id, long creatorId, String companyName, String logoLink, String createdOn, String designation,
            String location, String description, List<String> skills) {
        this.id = id;
        this.creatorId = creatorId;
        this.companyName = companyName;
        this.logoLink = logoLink;
        this.createdOn = createdOn;
        this.designation = designation;
        this.location = location;
        this.description = description;
        this.skills = skills;
    }

    public Job() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Job [id=" + id + ", creatorId=" + creatorId + ", companyName=" + companyName + ", logoLink=" + logoLink
                + ", createdOn=" + createdOn + ", designation=" + designation + ", location=" + location
                + ", description=" + description + ", skills=" + skills + "]";
    }

}