--CREATE TABLE IF NOT EXISTS USERS (ID BIGINT GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ) NOT NULL, NAME CHARACTER VARYING(64) NOT NULL, EMAIL CHARACTER VARYING(128) NOT NULL,CONSTRAINT pk_users PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    email character varying(128) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS public.ITEM_REQUESTS
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    DESCRIPTION character varying(512) COLLATE pg_catalog."default" NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    REQUESTER_ID bigint NOT NULL,
    CONSTRAINT PK_ITEM_REQUESTS PRIMARY KEY (id),
    CONSTRAINT FK_ITEM_REQUESTS_USERS FOREIGN KEY (REQUESTER_ID)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.items
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    description character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    is_available boolean DEFAULT FALSE NOT NULL,
    owner_id bigint NOT NULL,
    request_id bigint NULL,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_users_items FOREIGN KEY (owner_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.BOOKINGS
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    START_DATE TIMESTAMP NOT NULL,
    END_DATE TIMESTAMP NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    BOOKER_ID BIGINT NOT NULL,
    STATUS character varying(16) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT PK_BOOKINGS PRIMARY KEY (id),
    CONSTRAINT FK_BOOKINGS_ITEMS FOREIGN KEY (ITEM_ID)
        REFERENCES public.ITEMS (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT FK_BOOKINGS_USERS FOREIGN KEY (BOOKER_ID)
        REFERENCES public.USERS (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS PUBLIC.COMMENTS
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    TEXT character varying(512) COLLATE pg_catalog."default" NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    AUTHOR_ID BIGINT NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    CONSTRAINT PK_COMMENTS PRIMARY KEY (ID),
        CONSTRAINT FK_COMMENTS_ITEMS FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS (ID) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT FK_COMMENTS_USERS FOREIGN KEY (AUTHOR_ID) REFERENCES PUBLIC.USERS (ID) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);