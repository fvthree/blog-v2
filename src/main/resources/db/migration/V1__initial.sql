--------------- DROP TABLES
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS profile_views;
DROP TABLE IF EXISTS followers;
DROP TABLE IF EXISTS blocked_users;
DROP TABLE IF EXISTS created_posts;
DROP TABLE IF EXISTS liked_posts;
DROP TABLE IF EXISTS disliked_posts;
DROP TABLE IF EXISTS category_posts;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS posts_comment;

------------------------------------------------------------------
--------------- TABLE users 
----- account_level, is_verified, last_login, username, role
CREATE TABLE users(
	user_id SERIAL PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(255) NOT NULL,
	last_login TIMESTAMP,
	is_verified BOOLEAN,
	account_level VARCHAR(255),
	profile_picture VARCHAR(255),
	cover_image VARCHAR(255),
	bio VARCHAR(255),
	location VARCHAR(255),
	profile_viewers VARCHAR(255),
	notification_preferences VARCHAR(255),
	gender VARCHAR(255),
	password_reset_token VARCHAR(255),
	password_reset_expires TIMESTAMP,
	account_verify_token VARCHAR(255),
	account_verify_expires TIMESTAMP,
	date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_updated TIMESTAMP
);

-- Unique Email
CREATE UNIQUE INDEX uidx_u_email ON users (email);

CREATE INDEX IF NOT EXISTS idx_email ON users(email);

CREATE UNIQUE INDEX uidx_u_username ON users (username);

CREATE INDEX IF NOT EXISTS idx_username ON users(username);
-------------------------------------------------------------------
---------------- TABLE category

CREATE TABLE category(
	category_id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	author VARCHAR(255) NOT NULL,
	shares INTEGER NOT NULL,
	date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_updated TIMESTAMP
);

-- Unique category name
CREATE UNIQUE INDEX uidx_c_name ON category (name);
--------------------------------------------------------------------
--------------- TABLE post
CREATE TABLE post(
	post_id SERIAL PRIMARY KEY,
	title TEXT NOT NULL, -- 
	image TEXT NOT NULL, --
	claps INTEGER NOT NULL,
	content TEXT NOT NULL, --
	author INTEGER NOT NULL, --
	shares INTEGER,
	num_of_post_views INTEGER,
	category_id INTEGER NOT NULL,
	published TIMESTAMP,
	date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_updated TIMESTAMP
);

-- Unique Title
CREATE UNIQUE INDEX uidx_p_title ON post (title);

-- FK post categoryid:post_id
ALTER TABLE IF EXISTS post 
ADD CONSTRAINT fk_p_categoryid_categoryid FOREIGN KEY (category_id)
    REFERENCES category (category_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

-- FK post categoryid:post_id
ALTER TABLE IF EXISTS post
ADD CONSTRAINT fk_p_author_userid FOREIGN KEY (author)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
-------------------------------------------------------------
--------------- TABLE category posts
CREATE TABLE category_posts(
	cp_id SERIAL PRIMARY KEY,
	category_id INTEGER NOT NULL,
	post_id INTEGER NOT NULL
);
    
CREATE INDEX IF NOT EXISTS idx_cp_categoryid ON category_posts(category_id);
    
-- FK category_posts post_id:post_id
ALTER TABLE IF EXISTS category_posts
ADD CONSTRAINT fk_cp_postid_postid FOREIGN KEY (post_id)
    REFERENCES post (post_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_cp_postid ON category_posts(post_id);
--------------- TABLE comment
CREATE TABLE comment(
	comment_id SERIAL PRIMARY KEY,
	comment TEXT NOT NULL,
	author INTEGER NOT NULL,
	date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FK comment author:user_id
ALTER TABLE IF EXISTS comment
ADD CONSTRAINT fk_c_author_user_id FOREIGN KEY (author)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_c_author ON comment (author);

CREATE TABLE post_comments(
	post_comment_id SERIAL PRIMARY KEY,
	post_id INTEGER NOT NULL,
	comment_id INTEGER NOT NULL
);

-- FK post_comments post_id:post_id
ALTER TABLE IF EXISTS post_comments
ADD CONSTRAINT fk_pc_post_id_post_id FOREIGN KEY (post_id)
    REFERENCES post (post_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_pc_postid ON post_comments(post_id);    
-- FK post_comments comment_id:comment_id
ALTER TABLE IF EXISTS post_comments
ADD CONSTRAINT fk_pc_cid_cid FOREIGN KEY (comment_id)
    REFERENCES comment (comment_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_pc_commentid ON post_comments(comment_id);
------------------------------------------------------------------
-------------- TABLE profile_views
CREATE TABLE profile_views(
	profile_view_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	viewed_user INTEGER NOT NULL
);

-- FK profile_views userid:userid
ALTER TABLE IF EXISTS profile_views
ADD CONSTRAINT fk_pv_userid_userid FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_pv_userid ON profile_views(user_id);
    
-- FK profile_views viewed_user:user_id
ALTER TABLE IF EXISTS profile_views
ADD CONSTRAINT fk_pv_vieweduser_userid FOREIGN KEY (viewed_user)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_pv_vieweduser ON profile_views(viewed_user);
------------------------------------------------------------------
--------------- TABLE followers
CREATE TABLE followers(
	follower_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	followed_user INTEGER NOT NULL
);

-- FK followers userid:userid
ALTER TABLE IF EXISTS followers
ADD CONSTRAINT fk_f_userid_userid FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_f_userid ON followers(user_id);
    
-- FK followers viewed_user:user_id
ALTER TABLE IF EXISTS followers
ADD CONSTRAINT fk_f_vieweduser FOREIGN KEY (followed_user)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_f_followeduser ON followers(followed_user);
-------------------------------------------------------------------
---------------- TABLE blocked_users
CREATE TABLE blocked_users(
	blocked_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	blocked_user_id INTEGER NOT NULL
);

-- FK blocked_users userid:userid
ALTER TABLE IF EXISTS blocked_users
ADD CONSTRAINT fk_bu_userid_userid FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_bu_userid ON blocked_users(user_id);
    
-- FK blocked_users blocked_user:user_id
ALTER TABLE IF EXISTS blocked_users
ADD CONSTRAINT fk_bu_buuserid FOREIGN KEY (blocked_user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_bu_buuserid ON blocked_users(blocked_user_id);
-------------------------------------------------------------------
---------------- TABLE created_posts
CREATE TABLE created_posts(
	p_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	post_id INTEGER NOT NULL
);

-- FK created_posts userid:userid
ALTER TABLE IF EXISTS created_posts
ADD CONSTRAINT fk_cp_user_id_user_id FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_cp_user_id ON created_posts(user_id);
    
-- FK created_posts post_id:user_id
ALTER TABLE IF EXISTS created_posts
ADD CONSTRAINT fk_cp_postid_postid FOREIGN KEY (post_id)
    REFERENCES post (post_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_cp_postid ON created_posts(post_id);
-------------------------------------------------------------------
---------------- TABLE liked_posts
CREATE TABLE liked_posts(
	liked_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	post_id INTEGER NOT NULL
);

-- FK liked_posts userid:userid
ALTER TABLE IF EXISTS liked_posts
ADD CONSTRAINT fk_lp_userid_userid FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_lp_userid ON liked_posts(user_id);
    
-- FK liked_posts post_id:post_id
ALTER TABLE IF EXISTS liked_posts
ADD CONSTRAINT fk_lp_postid_postid FOREIGN KEY (post_id)
    REFERENCES post (post_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_lp_postid ON liked_posts(post_id);
-------------------------------------------------------------------
---------------- TABLE disliked_posts
CREATE TABLE disliked_posts(
	disliked_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	post_id INTEGER NOT NULL
);

-- FK disliked_posts userid:userid
ALTER TABLE IF EXISTS disliked_posts
ADD CONSTRAINT fk_dp_userid_userid FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_dp_userid ON disliked_posts(user_id);
    
-- FK disliked_posts post_id:post_id
ALTER TABLE IF EXISTS disliked_posts
ADD CONSTRAINT fk_dp_postid_postid FOREIGN KEY (post_id)
    REFERENCES post (post_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_dp_postid ON disliked_posts(post_id);
-------------------------------------------------------------------
---------------- TABLE roles
CREATE TABLE roles(
	role_id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_updated TIMESTAMP
);
-------------------------------------------------------------------
---------------- TABLE user_roles
CREATE TABLE user_roles(
	user_role_id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	role_id INTEGER NOT NULL,
	date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_updated TIMESTAMP
);

ALTER TABLE IF EXISTS user_roles
ADD CONSTRAINT fk_ur_userid_userid FOREIGN KEY (user_id)
    REFERENCES users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_ur_userid ON user_roles(user_id);

ALTER TABLE IF EXISTS user_roles
    ADD CONSTRAINT fk_ur_roleid_roleid FOREIGN KEY (role_id)
    REFERENCES roles (role_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
CREATE INDEX IF NOT EXISTS idx_ur_roleid ON user_roles(role_id);
    
------------------------------------------------------------------