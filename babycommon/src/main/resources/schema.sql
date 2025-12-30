-- 创建 baby_license 表
CREATE TABLE IF NOT EXISTS baby_license (
    c_id INT(11) PRIMARY KEY AUTO_INCREMENT,
    c_system VARCHAR(255) NOT NULL COMMENT '系统名称',
    c_key VARCHAR(255) NOT NULL COMMENT '唯一授权ID',
    c_license TEXT COMMENT '文件内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统授权表';


-- 插入系统授权数据
--INSERT INTO baby_license (c_system, c_key, c_license) VALUES ('MES', 'f0f3fbb6-46d5-11e8-9132-484d7eae5c64','GZtkpb/a5/ke1poTwg3J3fUlUeMMEaiskCzvFHLya5YSlgmq/vFyTwxauJ/0TEML3JYCSoLnKhAxpMDT6w5Fhm/jxkc4zohClpEWulcQpwg/5pfUEPf7B6JUvA8h8Co9nA5mg756TcWembjvuhVTnyCqtc8Pxm/VsYOF0oZgV44H26pJ7KRztr6ZKtlI4/kibLWT+WQxiYycfbuHB+RV/rWKAwWRP3abaNDGgv3C8kWf/z2HQItQWp39+gO2FElwanwDf97gH8MEMtffl4cLE7qcAh3zewDR8Ikc8IiztOTSIyY5DXBRvwwBe7LzbEhY5CLj2kluqEmy2hYra+10GQ==');