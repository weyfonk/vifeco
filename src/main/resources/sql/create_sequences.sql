DROP SEQUENCE IF EXISTS user_id;
DROP SEQUENCE IF EXISTS point_id;
DROP SEQUENCE IF EXISTS video_id;
DROP SEQUENCE IF EXISTS category_id;

CREATE SEQUENCE category_id  AS INTEGER START WITH 1 INCREMENT BY 1 NO CYCLE MINVALUE 1;
CREATE SEQUENCE user_id  AS INTEGER START WITH 1 INCREMENT BY 1 NO CYCLE MINVALUE 1;
CREATE SEQUENCE point_id  AS INTEGER START WITH 1 INCREMENT BY 1 NO CYCLE MINVALUE 1;
CREATE SEQUENCE video_id  AS INTEGER START WITH 1 INCREMENT BY 1 NO CYCLE MINVALUE 1;