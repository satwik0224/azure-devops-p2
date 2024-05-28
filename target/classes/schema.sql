CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT PRIMARY KEY,
    creator_id BIGINT,
    company_name VARCHAR(255),
    logo_link VARCHAR(255),
    created_on VARCHAR(255),
    designation VARCHAR(255),
    location VARCHAR(255),
    description VARCHAR(1000),
    skills VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS applications (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    job_id BIGINT,
    applied_on VARCHAR(255),
    resume_link VARCHAR(255)
);
