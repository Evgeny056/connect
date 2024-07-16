CREATE TABLE IF NOT EXISTS public.users (
                                            user_id UUID                PRIMARY KEY,
                                            first_name                  VARCHAR(30)         NOT NULL,
                                            last_name                   VARCHAR(30)         NOT NULL,
                                            mobile_phone                VARCHAR(12)  UNIQUE NOT NULL,
                                            email                       VARCHAR(50)         NOT NULL
);

CREATE TABLE IF NOT EXISTS public.publications (

                                            publication_id          UUID         PRIMARY KEY,
                                            author_id               UUID,
                                            message                 TEXT,
                                            date_publication        TIMESTAMP,
                                            like_count              INTEGER,
                                            dislike_count           INTEGER,
                                            repost_count            INTEGER,
                                            CONSTRAINT fk_author FOREIGN KEY(author_id) REFERENCES public.users(user_id)
);

CREATE TABLE IF NOT EXISTS public.comments (
                                            comment_id              UUID        PRIMARY KEY,
                                            publication_id          UUID,
                                            author_id               UUID,
                                            text                    TEXT,
                                            date                    TIMESTAMP,
                                            CONSTRAINT fk_publication FOREIGN KEY(publication_id) REFERENCES publications(publication_id),
                                            CONSTRAINT fk_comment_author FOREIGN KEY(author_id) REFERENCES public.users(user_id)
);

CREATE TABLE IF NOT EXISTS public.user_followers (
                                            user_id                 UUID,
                                            follower_id             UUID,
                                            CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES public.users(user_id),
                                            CONSTRAINT fk_follower FOREIGN KEY(follower_id) REFERENCES public.users(user_id),
                                            PRIMARY KEY (user_id, follower_id)
);