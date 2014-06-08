-- ============================
--derby工具： 
--1. 设置DERBY_HOME, 把bin目录加到PATH中即可 
--2. ij> connect 'jdbc:derby:example-derby;create=true'; run 'example-derby.sql'; 
--3. dblook -d 'jdbc:derby:example-derby' -o out.sql 
--4. startNetworkServer/stopNetworkServer
-- ----------------------------------------------

CREATE TABLE "APP"."IGNORE_THIS_TABLE" ("ID" BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1));

CREATE TABLE "APP"."AUTH_USERS" ("ID" BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "USER_ID" VARCHAR(40) NOT NULL UNIQUE, "LAST_NAME" VARCHAR(40), "FIRST_NAME" VARCHAR(40), "ORGANIZATION" VARCHAR(100), "DEPARTMENT" VARCHAR(100), "TELEPHONE" VARCHAR(20), "MOBILE" VARCHAR(20), "EMAIL" VARCHAR(60), "NOTE" VARCHAR(100), "PASSWORD" VARCHAR(200), "FAX" VARCHAR(20), "CELL" VARCHAR(20), "GENDER" SMALLINT DEFAULT 0, "IS_ACTIVE" BOOLEAN DEFAULT FALSE, "IS_SUPER" BOOLEAN DEFAULT FALSE, "DATE_JOINED" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "LAST_LOGIN" TIMESTAMP, "UPDATE_TIME" TIMESTAMP DEFAULT CURRENT_TIMESTAMP);