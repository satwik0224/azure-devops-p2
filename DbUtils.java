import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbUtils {
    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1.;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;INIT=runscript from 'classpath:schema.sql'";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static List<String> convertCSVToList(String csvString) {
        String[] csvArray = csvString.split(",");
        return new ArrayList<>(Arrays.asList(csvArray));
    }

    public static boolean isJobExists(long jobId) {
        String sql = "SELECT 1 FROM jobs WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertApplication(long userId, long jobId, String resumeLink) {
        String sql = "INSERT INTO applications (user_id, job_id, applied_on, resume_link) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, jobId);
            stmt.setString(3, getCurrentDateTime());
            stmt.setString(4, resumeLink);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Job job = new Job(
                        rs.getLong("id"),
                        rs.getLong("creator_id"),
                        rs.getString("company_name"),
                        rs.getString("logo_link"),
                        rs.getString("created_on"),
                        rs.getString("designation"),
                        rs.getString("location"),
                        rs.getString("description"),
                        convertCSVToList(rs.getString("skills"))
                );
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public static Job getJobById(long jobId) {
        String sql = "SELECT * FROM jobs WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Job(
                            rs.getLong("id"),
                            rs.getLong("creator_id"),
                            rs.getString("company_name"),
                            rs.getString("logo_link"),
                            rs.getString("created_on"),
                            rs.getString("designation"),
                            rs.getString("location"),
                            rs.getString("description"),
                            convertCSVToList(rs.getString("skills"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Application> getApplicationsByUserId(long userId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE user_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Application application = new Application(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getLong("job_id"),
                            rs.getString("applied_on"),
                            rs.getString("resume_link"));
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    public static Application getApplicationById(long applicationId) {
        String sql = "SELECT * FROM applications WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, applicationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Application(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getLong("job_id"),
                            rs.getString("applied_on"),
                            rs.getString("resume_link"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isApplicationExists(long applicationId) {
        String sql = "SELECT 1 FROM applications WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, applicationId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteApplication(long applicationId) {
        String sql = "DELETE FROM applications WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, applicationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Job> getJobsByUserId(long userId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE creator_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Job job = new Job(
                            rs.getLong("id"),
                            rs.getLong("creator_id"),
                            rs.getString("company_name"),
                            rs.getString("logo_link"),
                            rs.getString("created_on"),
                            rs.getString("designation"),
                            rs.getString("location"),
                            rs.getString("description"),
                            convertCSVToList(rs.getString("skills"))
                    );
                    jobs.add(job);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public static void updateJob(long jobId, Job updatedJob) {
        String sql = "UPDATE jobs SET company_name = ?, logo_link = ?, designation = ?, location = ?, description = ?, skills = ? WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedJob.getCompanyName());
            stmt.setString(2, updatedJob.getLogoLink());
            stmt.setString(3, updatedJob.getDesignation());
            stmt.setString(4, updatedJob.getLocation());
            stmt.setString(5, updatedJob.getDescription());
            stmt.setString(6, String.join(",", updatedJob.getSkills()));
            stmt.setLong(7, jobId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteJob(long jobId) {
        String sql = "DELETE FROM jobs WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, jobId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertJob(Job job) {
        String sql = "INSERT INTO jobs (creator_id, company_name, logo_link, created_on, designation, location, description, skills) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, job.getCreatorId());
            stmt.setString(2, job.getCompanyName());
            stmt.setString(3, job.getLogoLink());
            stmt.setString(4, job.getCreatedOn());
            stmt.setString(5, job.getDesignation());
            stmt.setString(6, job.getLocation());
            stmt.setString(7, job.getDescription());
            stmt.setString(8, String.join(",", job.getSkills()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Application> getApplicationsByJobId(long jobId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE job_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Application application = new Application(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getLong("job_id"),
                            rs.getString("applied_on"),
                            rs.getString("resume_link"));
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    public static String getCurrentDateTime() {
        // TODO
        return "";
    }
}
