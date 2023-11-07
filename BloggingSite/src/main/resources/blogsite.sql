-- For MySQL (database server)

CREATE TABLE bloggers (
    user_name VARCHAR(255) PRIMARY KEY,
    pass_word VARCHAR(255) NOT NULL
);

CREATE TABLE blogs (
	blog_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    blog_text VARCHAR(3500) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_name) REFERENCES bloggers(user_name)
);

CREATE TABLE comments (
	comment_id INT AUTO_INCREMENT PRIMARY KEY,
    blog_id INT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    comment_text VARCHAR(3500) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    FOREIGN KEY (blog_id) REFERENCES blogs(blog_id),
    FOREIGN KEY (user_name) REFERENCES bloggers(user_name)
);

-- For Microsoft SQL Server (database)
/*

CREATE TABLE bloggers (
    user_name VARCHAR(255) PRIMARY KEY,
    pass_word VARCHAR(255) NOT NULL
);

CREATE TABLE blogs (
	blog_id INT IDENTITY(1,1) PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    blog_text VARCHAR(3500) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_name) REFERENCES bloggers(user_name)
);

CREATE TABLE comments (
	comment_id INT IDENTITY(1,1) PRIMARY KEY,
    blog_id INT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    comment_text VARCHAR(3500) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    FOREIGN KEY (blog_id) REFERENCES blogs(blog_id),
    FOREIGN KEY (user_name) REFERENCES bloggers(user_name)
);

*/