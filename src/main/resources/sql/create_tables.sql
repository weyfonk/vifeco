CREATE TABLE IF NOT EXISTS USER (
       ID int NOT NULL PRIMARY KEY,
       FIRST_NAME varchar(30) NOT NULL,
       LAST_NAME varchar(30) NOT NULL,
       EMAIL varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS CATEGORY (
       ID int NOT NULL PRIMARY KEY,
       NAME varchar(30) NOT NULL,
       ICON varchar(3000) NOT NULL,
       SHORTCUT varchar(1) NOT NULL,
       CONSTRAINT category_shortcut_uniq UNIQUE (SHORTCUT)
);

CREATE TABLE IF NOT EXISTS VIDEO (
       ID int NOT NULL PRIMARY KEY,
       PATH varchar(3000) NOT NULL,
       DURATION DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS POINT (
     ID int NOT NULL PRIMARY KEY,
     X double NOT NULL,
     Y double NOT NULL,
     VIDEO_ID int NOT NULL,
     USER_ID int NOT NULL,
     CATEGORY_ID int NOT NULL,
     START double NOT NULL,
     CONSTRAINT point_video FOREIGN KEY (VIDEO_ID) REFERENCES VIDEO (ID) ON DELETE CASCADE,
     CONSTRAINT point_user FOREIGN KEY (USER_ID) REFERENCES USER (ID) ON DELETE CASCADE,
     CONSTRAINT point_category FOREIGN KEY (CATEGORY_ID) REFERENCES USER (ID) ON DELETE CASCADE
);