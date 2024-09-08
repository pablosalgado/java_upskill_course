DROP FUNCTION IF EXISTS create_user;
DROP FUNCTION IF EXISTS find_user_by_id;
DROP FUNCTION IF EXISTS find_all_users;
DROP PROCEDURE IF EXISTS update_user;
DROP PROCEDURE IF EXISTS delete_user;
DROP FUNCTION IF EXISTS find_users_with_many_friends_and_likes;

DROP TABLE IF EXISTS public.friendships;
DROP TABLE IF EXISTS public.likes;
DROP TABLE IF EXISTS public.posts;
DROP TABLE IF EXISTS public.users;

CREATE TABLE public.users (
	id serial4 NOT NULL,
	"name" varchar(255) NULL,
	surname varchar(255) NULL,
	birthdate date NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.friendships (
	userid1 int4 NULL,
	userid2 int4 NULL,
	"timestamp" timestamp NULL,
	CONSTRAINT friendships_userid1_fkey FOREIGN KEY (userid1) REFERENCES public.users(id),
	CONSTRAINT friendships_userid2_fkey FOREIGN KEY (userid2) REFERENCES public.users(id)
);
CREATE UNIQUE INDEX idx_friendships_userid1_userid2 ON public.friendships USING btree (userid1, userid2);

CREATE TABLE public.posts (
	id serial4 NOT NULL,
	userid int4 NULL,
	"text" text NULL,
	"timestamp" timestamp NULL,
	CONSTRAINT posts_pkey PRIMARY KEY (id),
	CONSTRAINT posts_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(id)
);

CREATE TABLE public.likes (
	userid int4 NULL,
	postid int4 NULL,
	"timestamp" timestamp NULL,
	CONSTRAINT likes_postid_fkey FOREIGN KEY (postid) REFERENCES public.posts(id),
	CONSTRAINT likes_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(id)
);
CREATE UNIQUE INDEX idx_likes_userid_postid ON public.likes USING btree (userid, postid);

-- User functions and procedures

CREATE FUNCTION create_user(
    p_name VARCHAR(255),
    p_surname VARCHAR(255),
    p_birthdate DATE
)
RETURNS INTEGER
LANGUAGE SQL
AS '
INSERT INTO public.users (name, surname, birthdate)
VALUES (p_name, p_surname, p_birthdate)
RETURNING id;
';


CREATE FUNCTION find_user_by_id(p_id INT)
RETURNS SETOF users AS '
BEGIN
    RETURN QUERY SELECT * FROM public.users WHERE id = p_id;
END;
' LANGUAGE plpgsql;

CREATE FUNCTION find_all_users()
RETURNS SETOF users AS '
BEGIN
    RETURN QUERY SELECT * FROM public.users;
END;
' LANGUAGE plpgsql;

CREATE PROCEDURE update_user(
    IN p_id INT,
    IN p_name VARCHAR(255),
    IN p_surname VARCHAR(255),
    IN p_birthdate DATE
)
LANGUAGE SQL
AS '
UPDATE public.users
SET name = p_name, surname = p_surname, birthdate = p_birthdate
WHERE id = p_id;
';

CREATE PROCEDURE delete_user(
    IN p_id INT
)
LANGUAGE SQL
AS '
DELETE FROM public.users
WHERE id = p_id;
';


-- Report functions and procedures

CREATE FUNCTION get_user_stats(friends_before_date DATE, likes_start_date DATE, likes_end_date DATE)
RETURNS TABLE(name VARCHAR, surname VARCHAR, friends BIGINT, likes BIGINT) AS '
BEGIN
    RETURN QUERY
    WITH friends_count AS (
        SELECT
            person.id,
            person.name,
            person.surname,
            COUNT(*) AS friends
        FROM
            users person
        INNER JOIN friendships fs ON
            fs.userId1 = person.id
        WHERE
            fs.timestamp < friends_before_date
        GROUP BY 
            person.id,
            person.name,
            person.surname
        HAVING
            COUNT(fs.userId2) > 100
    ),
    likes_count AS (
        SELECT
            person.id,
            person.name,
            person.surname,
            COUNT(*) AS likes
        FROM 
            users person
        INNER JOIN likes ON
            likes.userId = person.id
        WHERE
            likes.timestamp BETWEEN likes_start_date AND likes_end_date
        GROUP BY 
            person.id,
            person.name,
            person.surname
        HAVING
            COUNT(likes.postId) > 100
    )
    SELECT
        f.name,
        f.surname,
        f.friends,
        l.likes
    FROM
        friends_count f
    INNER JOIN likes_count l ON
        f.id = l.id
    ORDER BY
        f.name,
        f.surname;
END;
' LANGUAGE plpgsql;