CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME CHARACTER VARYING(64) NOT NULL,
    EMAIL CHARACTER VARYING(128) NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (ID),
    UNIQUE (EMAIL)
);
CREATE UNIQUE INDEX IF NOT EXISTS CONSTRAINT_INDEX_4 ON PUBLIC.USERS (EMAIL);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_4 ON PUBLIC.USERS (ID);

CREATE TABLE IF NOT EXISTS PUBLIC.ITEMS
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME CHARACTER VARYING(64) NOT NULL,
    DESCRIPTION CHARACTER VARYING(1024) NOT NULL,
    IS_AVAILABLE BOOLEAN DEFAULT FALSE NOT NULL,
    OWNER_ID BIGINT NOT NULL,
    REQUEST_ID BIGINT,
    CONSTRAINT PK_ITEMS PRIMARY KEY (ID),
    CONSTRAINT FK_USERS_ITEMS FOREIGN KEY (OWNER_ID)
    REFERENCES PUBLIC.USERS (ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS FK_USERS_ITEMS_INDEX_4 ON PUBLIC.ITEMS (OWNER_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_42 ON PUBLIC.ITEMS (ID);


--CREATE TABLE IF NOT EXISTS PUBLIC.STATUS
--(ID BIGINT NOT NULL,
--    CODE CHARACTER VARYING(16) NOT NULL,
--    CONSTRAINT PK_STATUS PRIMARY KEY (ID),);


CREATE TABLE IF NOT EXISTS PUBLIC.BOOKINGS
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    START_DATE TIMESTAMP NOT NULL,
    END_DATE TIMESTAMP NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    BOOKER_ID BIGINT NOT NULL,
    STATUS CHARACTER VARYING(16) NOT NULL,
    CONSTRAINT PK_BOOKINGS PRIMARY KEY (ID),
    CONSTRAINT FK_BOOKINGS_ITEMS FOREIGN KEY (ITEM_ID)
    REFERENCES PUBLIC.ITEMS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_BOOKINGS_USERS FOREIGN KEY (BOOKER_ID)
    REFERENCES PUBLIC.USERS (ID) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE INDEX IF NOT EXISTS FK_BOOKINGS_ITEMS_INDEX_A ON PUBLIC.BOOKINGS (ITEM_ID);
CREATE INDEX IF NOT EXISTS FK_BOOKINGS_USERS_INDEX_A ON PUBLIC.BOOKINGS (BOOKER_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_A ON PUBLIC.BOOKINGS (ID);

CREATE TABLE IF NOT EXISTS PUBLIC.COMMENTS
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    TEXT CHARACTER VARYING(512) NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    AUTHOR_ID BIGINT NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    CONSTRAINT PK_COMMENTS PRIMARY KEY (ID),
        CONSTRAINT FK_COMMENTS_ITEMS FOREIGN KEY (ITEM_ID)
        REFERENCES PUBLIC.ITEMS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT FK_COMMENTS_USERS FOREIGN KEY (AUTHOR_ID)
        REFERENCES PUBLIC.USERS (ID) ON DELETE CASCADE ON UPDATE CASCADE
);