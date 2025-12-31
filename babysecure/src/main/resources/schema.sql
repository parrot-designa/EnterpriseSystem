-- 创建部门表
CREATE TABLE IF NOT EXISTS department (
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
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

  -- 插入测试数据
#  INSERT INTO department (code, name, parent_id, status, sort_order) VALUES
#  ('ROOT', '总公司', NULL, 1, 1),
#  ('DEPT001', '技术部', 1, 1, 2),
#  ('DEPT002', '市场部', 1, 1, 3),
#  ('DEPT003', '前端组', 3, 1, 4),
#  ('DEPT004', '后端组', 3, 1, 5);