-- ============================
--derby工具： 
--1. 设置DERBY_HOME, 把bin目录加到PATH中即可 
--2. ij> connect 'jdbc:derby:example-derby;create=true'; run 'gb-derby.sql'; 
--3. dblook -d 'jdbc:derby:example-derby' -o out.sql 
--4. startNetworkServer/stopNetworkServer
-- ----------------------------------------------
DROP TABLE IGNORE_THIS_TABLE;
CREATE TABLE IGNORE_THIS_TABLE (ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1));

DROP TABLE gb_user; --用户表
CREATE TABLE gb_user ( 
  uid BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 100, INCREMENT BY 1),
  user_id VARCHAR(128) NOT NULL UNIQUE,
  password VARCHAR(256) DEFAULT NULL, -- 密码
  isactive SMALLINT NOT NULL DEFAULT 0, --是否激活, 0-未激活, 1-已激活
  issuper SMALLINT NOT NULL DEFAULT 0, --是否超级管理员, 0-不是, 1-是
  nick VARCHAR(128) DEFAULT NULL, --昵称
  date_joined TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 新建用户时间
  last_login TIMESTAMP DEFAULT NULL,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 最后更新时间
  PRIMARY KEY (uid)
);
CREATE INDEX nick_idx ON gb_user (nick);

DROP TABLE gb_permission; --权限表
CREATE TABLE gb_permission (
  pid BIGINT NOT NULL,
  name VARCHAR(56) NOT NULL UNIQUE, --权限名称
  ptype INTEGER NOT NULL DEFAULT 0, --权限类型 可用于分组过滤
  remark VARCHAR(128) DEFAULT NULL, --备注
  PRIMARY KEY (pid)
);
CREATE INDEX ptype_idx ON gb_permission (ptype);

DROP TABLE gb_role; --角色描述
CREATE TABLE gb_role (
  rid BIGINT NOT NULL,
  name VARCHAR(56) NOT NULL UNIQUE, --角色名称
  remark VARCHAR(128) DEFAULT NULL, --备注
  PRIMARY KEY (rid)
);


DROP TABLE gb_role_permission; --角色权限关联表
CREATE TABLE gb_role_permission (
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  rid BIGINT NOT NULL, --角色ID,
  pid BIGINT NOT NULL, --权限ID,
  PRIMARY KEY (id)
);
CREATE UNIQUE INDEX rp_unique_idx ON gb_role_permission (rid, pid);


DROP TABLE gb_user_permission; --用户权限关联表
CREATE TABLE gb_user_permission (
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  uid BIGINT NOT NULL, --用户ID
  pid BIGINT NOT NULL, --权限ID
  PRIMARY KEY (id)
);
CREATE UNIQUE INDEX up_unique_idx ON gb_user_permission (uid, pid);

DROP TABLE gb_user_role; --用户角色关联表
CREATE TABLE gb_user_role (
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  uid BIGINT NOT NULL, --用户ID
  rid BIGINT NOT NULL, --角色ID
  PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ur_unique_idx ON gb_user_role (uid, rid);

DROP TABLE gb_admin_log;
CREATE TABLE gb_admin_log
(
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  operator_id BIGINT NOT NULL,
  operation_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operation_name VARCHAR(64) NOT NULL,
  operation_type VARCHAR(8) NOT NULL,
  remark VARCHAR(1024),
  operation_ip VARCHAR(32),
  target_obj_key VARCHAR(128) NOT NULL,
  target_obj_before VARCHAR(1024),
  target_obj_after VARCHAR(1024),
  PRIMARY KEY (id)
);
CREATE INDEX al_target_key_idx ON gb_admin_log (target_obj_key);
CREATE INDEX al_operator_id_idx ON gb_admin_log (operator_id);


INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (100, 1, '查看系统用户', '系统管理');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (101, 1, '创建和更新系统用户', '系统管理');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (102, 1, '删除系统用户', '系统管理');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (103, 1, '查看用户权限信息', '系统管理');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (104, 1, '更新用户权限信息', '系统管理');

INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (400, 2, '查看调度器信息', '调度器');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (401, 2, '查看任务信息', '调度器');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (402, 2, '查看触发器信息', '调度器');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (403, 2, '增加任务', '调度器');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (404, 2, '执行任务', '调度器');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (405, 2, '删除任务', '调度器');

INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (1000, 0, '进入主页', 'MISC');
INSERT INTO gb_permission (pid, ptype, name, remark) VALUES (1001, 0, '测试', 'MISC');

INSERT INTO gb_role (rid, name, remark) VALUES (0, '系统普通用户', '系统普通用户：拥有系统默认权限配置');

INSERT INTO gb_role_permission (rid, pid) VALUES (0, 1000);