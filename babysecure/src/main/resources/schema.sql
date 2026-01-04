CREATE TABLE IF NOT EXISTS b_user (
    id INT(11) NOT NULL COMMENT '主键id',
    account VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
    name VARCHAR(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
    password VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
    email VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
    telephone VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
    pwd_valid_date datetime DEFAULT NULL COMMENT '密码有效日期',
    c_status int(11) NOT NULL COMMENT '用户状态',
    login_fail_count int(11) DEFAULT '0' COMMENT '登录失败次数',
    pwd_log text CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码修改日志',
    revision int(11) DEFAULT '1' COMMENT '修订号',
    uuid binary(16) DEFAULT NULL COMMENT 'uuid',
    c_extend_info text CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展信息',
    account_valid_date datetime DEFAULT NULL COMMENT '账户有效时间',
    signature_pwd varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '签名密码',
    c_image text CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
    c_signture_pwd_valid_date datetime DEFAULT NULL,
    c_signture_pwd_log text CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电子签名密码修改日志',
    c_his_uuid binary(16) DEFAULT NULL COMMENT '快照 uuid',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_account`(`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- 创建部门表
CREATE TABLE IF NOT EXISTS b_department (
      -- 【主键】自增 ID，作为每条记录的唯一标识
      id INT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',

      -- 【部门编码】字符串类型，最长50字符，不能为空，唯一
      code VARCHAR(50) NOT NULL UNIQUE COMMENT '部门编码',

      -- 【部门名称】字符串类型，最长100字符，不能为空
      name VARCHAR(100) NOT NULL COMMENT '部门名称',

      -- 【父部门ID】整数，允许为空（顶级部门的 parent_id 为 NULL）
      parent_id INT DEFAULT NULL COMMENT '父部门ID',

      -- 【部门状态】整数，默认1（1-启用，0-禁用）
      status INT DEFAULT 1 COMMENT '部门状态：1-启用，0-禁用',

      -- 【排序号】整数，用于部门列表的显示顺序
      sort_order INT DEFAULT 0 COMMENT '排序号',

      -- 【创建时间】时间戳，默认为当前时间
      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

      -- 【更新时间】时间戳，更新记录时自动更新为当前时间
      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

      -- 【索引】为 parent_id 创建索引，提高查询子部门的性能
      INDEX idx_parent_id (parent_id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

-- #  -- 插入测试数据
-- #  INSERT INTO department (code, name, parent_id, status, sort_order) VALUES
-- #  ('ROOT', '总公司', NULL, 1, 1),
-- #  ('DEPT001', '技术部', 1, 1, 2),
-- #  ('DEPT002', '市场部', 1, 1, 3),
-- #  ('DEPT003', '前端组', 3, 1, 4),
-- #  ('DEPT004', '后端组', 3, 1, 5);