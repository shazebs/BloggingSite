
CREATE DATABASE blogsite;

CREATE TABLE blogsite.bloggers (
    user_name VARCHAR(255) PRIMARY KEY,
    pass_word VARCHAR(255) NOT NULL
);
CREATE TABLE blogsite.blogs (
	blog_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    blog_text VARCHAR(3500) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_name) REFERENCES blogsite.bloggers(user_name)
);
