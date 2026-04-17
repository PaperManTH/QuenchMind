-- auth/user
-- 账号表
CREATE TABLE cz_auth_user
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name     VARCHAR(50)  NULL COMMENT '用户名',
    user_phone    VARCHAR(20)  NULL UNIQUE COMMENT '手机号',
    user_email    VARCHAR(100) NULL UNIQUE COMMENT '邮箱',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    nick_name     VARCHAR(100) NULL COMMENT '昵称',
    user_avatar   VARCHAR(255) NULL COMMENT '头像',
    user_gender   ENUM ('MALE','FEMALE','OTHER') DEFAULT 'OTHER' COMMENT '性别',
    birthday      DATE         NULL COMMENT '生日',
    status        TINYINT      NOT NULL          DEFAULT 1 COMMENT '状态 1正常 0禁用',
    last_login_at DATETIME     NULL COMMENT '最后登录时间',
    created_at    DATETIME                       DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    DATETIME     NULL,
    UNIQUE KEY uk_username (user_name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 角色表
CREATE TABLE cz_auth_role
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_code   VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色编码',
    role_name   VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色名',
    description VARCHAR(200) NULL COMMENT '角色描述',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 1正常 0禁用',
    created_at  DATETIME              DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME     NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 用户-角色关联表
CREATE TABLE cz_auth_user_role
(
    user_id    BIGINT NOT NULL COMMENT '用户ID',
    role_id    BIGINT NOT NULL COMMENT '角色ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES cz_auth_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES cz_auth_role (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- resource
-- 学习资料表
CREATE TABLE cz_resource
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT       NOT NULL COMMENT '上传用户ID',
    title          VARCHAR(200) NOT NULL COMMENT '资料标题',
    description    TEXT         NULL COMMENT '资料描述',
    file_name      VARCHAR(255) NOT NULL COMMENT '原文件名',
    file_type      VARCHAR(50)  NOT NULL COMMENT '文件类型 pdf/ppt/docx',
    mime_type      VARCHAR(100) NULL COMMENT 'MIME类型',
    file_size      BIGINT       NULL COMMENT '文件大小，字节',
    file_md5       VARCHAR(64)  NULL COMMENT '文件MD5',
    storage_bucket VARCHAR(100) NULL COMMENT '存储桶',
    file_path      VARCHAR(500) NOT NULL COMMENT '文件路径',
    parse_status   TINYINT      NOT NULL DEFAULT 0 COMMENT '解析状态 0未解析 1解析中 2成功 3失败',
    vector_status  TINYINT      NOT NULL DEFAULT 0 COMMENT '向量化状态 0未处理 1处理中 2成功 3失败',
    page_count     INT          NULL COMMENT '页数',
    created_at     DATETIME              DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     DATETIME     NULL,
    KEY idx_resource_user (user_id),
    KEY idx_resource_status (parse_status, vector_status),
    CONSTRAINT fk_resource_user FOREIGN KEY (user_id) REFERENCES cz_auth_user (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 知识点表
CREATE TABLE cz_resource_knowledge
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id BIGINT       NOT NULL COMMENT '所属资料ID',
    chunk_no    INT          NOT NULL COMMENT '分块序号',
    page_start  INT          NULL COMMENT '起始页',
    page_end    INT          NULL COMMENT '结束页',
    content     TEXT         NOT NULL COMMENT '分块内容',
    summary     TEXT         NULL COMMENT '分块摘要',
    keywords    VARCHAR(500) NULL COMMENT '关键词，逗号分隔',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME     NULL,
    KEY idx_rk_resource (resource_id),
    KEY idx_rk_chunk (resource_id, chunk_no),
    CONSTRAINT fk_rk_resource FOREIGN KEY (resource_id) REFERENCES cz_resource (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- learning
-- 学习计划表
CREATE TABLE cz_study_plan
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id            BIGINT        NOT NULL COMMENT '用户ID',
    source_resource_id BIGINT        NULL COMMENT '来源资料ID',
    title              VARCHAR(100)  NOT NULL COMMENT '计划标题',
    goal               VARCHAR(500)  NULL COMMENT '学习目标',
    start_date         DATE          NOT NULL COMMENT '开始日期',
    end_date           DATE          NOT NULL COMMENT '结束日期',
    total_days         INT           NULL COMMENT '总天数',
    daily_minutes      INT           NULL COMMENT '每日学习时长（分钟）',
    progress           DECIMAL(5, 2) NOT NULL                  DEFAULT 0.00 COMMENT '进度百分比',
    status             ENUM ('ACTIVE','COMPLETED','CANCELLED') DEFAULT 'ACTIVE',
    ai_model           VARCHAR(50)   NULL COMMENT '生成所用模型',
    prompt_version     VARCHAR(50)   NULL COMMENT 'Prompt版本',
    created_at         DATETIME                                DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at         DATETIME      NULL,
    KEY idx_plan_user (user_id),
    KEY idx_plan_source (source_resource_id),
    CONSTRAINT fk_plan_user FOREIGN KEY (user_id) REFERENCES cz_auth_user (id),
    CONSTRAINT fk_plan_resource FOREIGN KEY (source_resource_id) REFERENCES cz_resource (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 学习任务表
CREATE TABLE cz_study_task
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id      BIGINT        NOT NULL COMMENT '计划ID',
    task_date    DATE          NOT NULL COMMENT '学习日期',
    task_order   INT           NOT NULL                 DEFAULT 1 COMMENT '任务顺序',
    task_type    VARCHAR(30)   NULL COMMENT '任务类型：阅读/练习/复习/测验',
    title        VARCHAR(200)  NULL COMMENT '任务标题',
    content      TEXT          NOT NULL COMMENT '任务内容',
    priority     TINYINT       NOT NULL                 DEFAULT 3 COMMENT '优先级 1高 2中 3低',
    status       ENUM ('PENDING','COMPLETED','SKIPPED') DEFAULT 'PENDING',
    completed_at DATETIME      NULL,
    score        DECIMAL(5, 2) NULL COMMENT '完成评分',
    remark       VARCHAR(500)  NULL COMMENT '备注',
    created_at   DATETIME                               DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   DATETIME      NULL,
    KEY idx_task_plan (plan_id),
    KEY idx_task_date (task_date),
    CONSTRAINT fk_task_plan FOREIGN KEY (plan_id) REFERENCES cz_study_plan (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 错题复习表
CREATE TABLE cz_wrong_question
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id        BIGINT      NOT NULL COMMENT '任务ID',
    resource_id    BIGINT      NULL COMMENT '来源资料ID',
    knowledge_id   BIGINT      NULL COMMENT '关联知识点ID',
    question       TEXT        NOT NULL COMMENT '题目',
    question_type  VARCHAR(30) NULL COMMENT '题型',
    difficulty     TINYINT     NULL COMMENT '难度 1-5',
    correct_answer TEXT        NULL COMMENT '标准答案',
    user_answer    TEXT        NULL COMMENT '用户回答',
    is_correct     TINYINT     NOT NULL DEFAULT 0 COMMENT '是否答对',
    review_count   INT         NOT NULL DEFAULT 0 COMMENT '复习次数',
    next_review_at DATETIME    NULL COMMENT '下次复习时间',
    created_at     DATETIME             DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     DATETIME    NULL,
    KEY idx_wrong_task (task_id),
    KEY idx_wrong_next_review (next_review_at),
    CONSTRAINT fk_wrong_task FOREIGN KEY (task_id) REFERENCES cz_study_task (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- agent
-- 会话表
CREATE TABLE cz_agent_session_meta
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT NOT NULL COMMENT '关联用户ID',
    session_name VARCHAR(100) COMMENT '会话名称',
    status       VARCHAR(20) COMMENT '会话状态',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME,
    INDEX (user_id)
);
