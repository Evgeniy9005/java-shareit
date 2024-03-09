--CREATE TABLE IF NOT EXISTS USERS (ID BIGINT GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ) NOT NULL, NAME CHARACTER VARYING(64) NOT NULL, EMAIL CHARACTER VARYING(128) NOT NULL,CONSTRAINT pk_users PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    email character varying(128) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS public.items
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    description character varying(256) COLLATE pg_catalog."default" NOT NULL,
    is_available boolean NOT NULL,
    owner_id bigint NOT NULL,
    request_id bigint,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_users_items FOREIGN KEY (owner_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);