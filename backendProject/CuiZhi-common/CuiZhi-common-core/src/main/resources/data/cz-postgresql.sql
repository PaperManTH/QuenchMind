-- =====================================================
-- QuenchMind 淬知 数据库初始化脚本（v2.0 — 完整版）
-- PostgreSQL 15+ / pgvector
-- 生成日期：2026-07-07
--
-- ID 策略：
--   VARCHAR(36) UUID — 用户可见实体（cz_user/group/resource/session/plan）
--   BIGINT 自增     — 内部高频实体（chunk/message/embedding/retrieval/task）
--
-- 按照 PO 类实际字段生成，全部表含软删除（deleted_at）
-- =====================================================

-- ============================================================
-- 1. pgvector 扩展
-- ============================================================
CREATE EXTENSION IF NOT EXISTS vector;

-- ============================================================
-- 2. cz_user — 用户账号表
--    所属模块：CuiZhi-auth / CuiZhi-user
--    ID：VARCHAR(36) UUID
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_user (
    id              VARCHAR(36)  PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_name       VARCHAR(50)  NOT NULL,
    user_phone      VARCHAR(20),
    user_email      VARCHAR(100),
    password_hash   VARCHAR(255) NOT NULL,
    login_type      VARCHAR(20)  NOT NULL DEFAULT 'PASSWORD',
    nick_name       VARCHAR(100),
    user_avatar     VARCHAR(255),
    user_gender     VARCHAR(10)  NOT NULL DEFAULT 'OTHER',
    birthday        DATE,
    status          SMALLINT     NOT NULL DEFAULT 1,
    last_login_at   TIMESTAMP,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at      TIMESTAMP,

    CONSTRAINT uk_cz_user_user_name UNIQUE (user_name),
    CONSTRAINT uk_cz_user_phone     UNIQUE (user_phone),
    CONSTRAINT uk_cz_user_email     UNIQUE (user_email),
    CONSTRAINT chk_cz_user_gender   CHECK (user_gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_cz_user_status   CHECK (status IN (0, 1))
);

COMMENT ON TABLE  cz_user IS '用户账号表';
COMMENT ON COLUMN cz_user.id             IS '用户ID（UUID）';
COMMENT ON COLUMN cz_user.user_name      IS '用户名（唯一）';
COMMENT ON COLUMN cz_user.user_phone     IS '手机号（唯一，可空）';
COMMENT ON COLUMN cz_user.user_email     IS '邮箱（唯一，可空）';
COMMENT ON COLUMN cz_user.password_hash  IS '密码哈希（bcrypt）';
COMMENT ON COLUMN cz_user.login_type     IS '注册/登录来源：PASSWORD / EMAIL_CODE / WECHAT';
COMMENT ON COLUMN cz_user.nick_name      IS '昵称';
COMMENT ON COLUMN cz_user.user_avatar    IS '头像URL';
COMMENT ON COLUMN cz_user.user_gender    IS '性别：MALE / FEMALE / OTHER';
COMMENT ON COLUMN cz_user.birthday       IS '生日';
COMMENT ON COLUMN cz_user.status         IS '状态：1启用 0禁用';
COMMENT ON COLUMN cz_user.last_login_at  IS '最近一次登录时间';
COMMENT ON COLUMN cz_user.created_at     IS '创建时间';
COMMENT ON COLUMN cz_user.updated_at     IS '更新时间';
COMMENT ON COLUMN cz_user.deleted_at     IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_user_email ON cz_user (user_email);
CREATE INDEX IF NOT EXISTS idx_cz_user_phone ON cz_user (user_phone);

-- ============================================================
-- 3. cz_study_group — 学习小组表
--    所属模块：CuiZhi-user
--    ID：VARCHAR(36) UUID
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_study_group (
    id          VARCHAR(36)  PRIMARY KEY DEFAULT gen_random_uuid()::text,
    group_name  VARCHAR(100) NOT NULL,
    description TEXT,
    owner_id    VARCHAR(36)  NOT NULL,
    max_members INT          DEFAULT 50,
    status      SMALLINT     DEFAULT 1,
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMP,

    CONSTRAINT fk_group_owner FOREIGN KEY (owner_id) REFERENCES cz_user (id)
);

COMMENT ON TABLE  cz_study_group IS '学习小组表';
COMMENT ON COLUMN cz_study_group.id          IS '小组ID（UUID）';
COMMENT ON COLUMN cz_study_group.group_name  IS '小组名称';
COMMENT ON COLUMN cz_study_group.description IS '小组描述';
COMMENT ON COLUMN cz_study_group.owner_id    IS '创建者ID → cz_user';
COMMENT ON COLUMN cz_study_group.max_members IS '最大成员数';
COMMENT ON COLUMN cz_study_group.status      IS '状态：1正常 0禁用';
COMMENT ON COLUMN cz_study_group.created_at  IS '创建时间';
COMMENT ON COLUMN cz_study_group.updated_at  IS '更新时间';
COMMENT ON COLUMN cz_study_group.deleted_at  IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_group_owner ON cz_study_group (owner_id);

-- ============================================================
-- 4. cz_group_member — 小组成员表（联合主键）
--    所属模块：CuiZhi-user
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_group_member (
    group_id  VARCHAR(36) NOT NULL,
    user_id   VARCHAR(36) NOT NULL,
    role      VARCHAR(20) DEFAULT 'MEMBER',
    joined_at TIMESTAMP   NOT NULL DEFAULT now(),

    PRIMARY KEY (group_id, user_id),
    CONSTRAINT fk_member_group FOREIGN KEY (group_id) REFERENCES cz_study_group (id) ON DELETE CASCADE,
    CONSTRAINT fk_member_user  FOREIGN KEY (user_id)  REFERENCES cz_user (id)        ON DELETE CASCADE,
    CONSTRAINT chk_member_role CHECK (role IN ('OWNER', 'ADMIN', 'MEMBER'))
);

COMMENT ON TABLE  cz_group_member IS '小组成员表';
COMMENT ON COLUMN cz_group_member.group_id  IS '小组ID → cz_study_group';
COMMENT ON COLUMN cz_group_member.user_id   IS '用户ID → cz_user';
COMMENT ON COLUMN cz_group_member.role      IS '角色：OWNER 所有者 / ADMIN 管理员 / MEMBER 普通成员';
COMMENT ON COLUMN cz_group_member.joined_at IS '加入时间';

CREATE INDEX IF NOT EXISTS idx_member_user  ON cz_group_member (user_id);
CREATE INDEX IF NOT EXISTS idx_member_group ON cz_group_member (group_id);

-- ============================================================
-- 5. cz_resource — 学习资料表
--    所属模块：CuiZhi-resource
--    ID：VARCHAR(36) UUID
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_resource (
    id              VARCHAR(36)  PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_id         VARCHAR(36)  NOT NULL,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    file_name       VARCHAR(255),
    file_type       VARCHAR(50),
    file_ext        VARCHAR(20),
    file_size       BIGINT,
    file_md5        VARCHAR(64),
    storage_bucket  VARCHAR(100),
    file_path       VARCHAR(500),
    parse_status    SMALLINT     NOT NULL DEFAULT 0,
    parse_error_msg TEXT,
    vector_status   SMALLINT     NOT NULL DEFAULT 0,
    page_count      INT,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at      TIMESTAMP,

    CONSTRAINT fk_cz_resource_user FOREIGN KEY (user_id) REFERENCES cz_user (id)
);

COMMENT ON TABLE  cz_resource IS '学习资料表（文档/视频/图片/网页等）';
COMMENT ON COLUMN cz_resource.id              IS '资料ID（UUID）';
COMMENT ON COLUMN cz_resource.user_id         IS '所属用户ID → cz_user';
COMMENT ON COLUMN cz_resource.title           IS '资料标题';
COMMENT ON COLUMN cz_resource.description     IS '资料描述/摘要（可空）';
COMMENT ON COLUMN cz_resource.file_name       IS '原始文件名';
COMMENT ON COLUMN cz_resource.file_type       IS '文件类型：pdf / docx / pptx / mp4 / img';
COMMENT ON COLUMN cz_resource.file_ext        IS '文件后缀：pdf / docx / mp4 等';
COMMENT ON COLUMN cz_resource.file_size       IS '文件大小（字节）';
COMMENT ON COLUMN cz_resource.file_md5        IS '文件 MD5（用于秒传）';
COMMENT ON COLUMN cz_resource.storage_bucket  IS 'MinIO 存储桶名';
COMMENT ON COLUMN cz_resource.file_path       IS 'MinIO 对象路径';
COMMENT ON COLUMN cz_resource.parse_status    IS '解析状态：0未解析 1解析中 2成功 3失败';
COMMENT ON COLUMN cz_resource.parse_error_msg IS '解析错误信息';
COMMENT ON COLUMN cz_resource.vector_status   IS '向量化状态：0未处理 1处理中 2成功 3失败';
COMMENT ON COLUMN cz_resource.page_count      IS '页数/时长（秒）';
COMMENT ON COLUMN cz_resource.created_at      IS '创建时间';
COMMENT ON COLUMN cz_resource.updated_at      IS '更新时间';
COMMENT ON COLUMN cz_resource.deleted_at      IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_resource_user_id    ON cz_resource (user_id);
CREATE INDEX IF NOT EXISTS idx_cz_resource_created_at ON cz_resource (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_cz_resource_file_md5   ON cz_resource (file_md5);

-- ============================================================
-- 6. cz_resource_chunk — 资料文本切块表
--    所属模块：CuiZhi-resource
--    ID：BIGINT 自增（内部高频实体）
--    FK resource_id → cz_resource.id (VARCHAR)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_resource_chunk (
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    resource_id VARCHAR(36) NOT NULL,
    chunk_no    INT         NOT NULL,
    page_start  INT,
    page_end    INT,
    content     TEXT        NOT NULL,
    token_count INT,
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMP,

    CONSTRAINT fk_cz_resource_chunk_resource
        FOREIGN KEY (resource_id) REFERENCES cz_resource (id) ON DELETE CASCADE,
    CONSTRAINT uk_cz_resource_chunk_no UNIQUE (resource_id, chunk_no)
);

COMMENT ON TABLE  cz_resource_chunk IS '资料文本切块表（RAG 检索数据源）';
COMMENT ON COLUMN cz_resource_chunk.id          IS '切块ID（自增）';
COMMENT ON COLUMN cz_resource_chunk.resource_id IS '所属资料ID → cz_resource';
COMMENT ON COLUMN cz_resource_chunk.chunk_no    IS '切块序号（同一资料内唯一）';
COMMENT ON COLUMN cz_resource_chunk.page_start  IS '起始页';
COMMENT ON COLUMN cz_resource_chunk.page_end    IS '结束页';
COMMENT ON COLUMN cz_resource_chunk.content     IS '切块文本内容';
COMMENT ON COLUMN cz_resource_chunk.token_count IS 'Token 数量（可空）';
COMMENT ON COLUMN cz_resource_chunk.created_at  IS '创建时间';
COMMENT ON COLUMN cz_resource_chunk.updated_at  IS '更新时间';
COMMENT ON COLUMN cz_resource_chunk.deleted_at  IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_resource_chunk_resource_id ON cz_resource_chunk (resource_id);

-- ============================================================
-- 7. cz_resource_knowledge — 知识卡片表
--    所属模块：CuiZhi-resource
--    ID：BIGINT 自增
--    FK resource_id → cz_resource.id (VARCHAR)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_resource_knowledge (
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    resource_id VARCHAR(36),
    chunk_id    BIGINT,
    title       VARCHAR(200),
    summary     TEXT,
    keywords    VARCHAR(500),
    difficulty  SMALLINT,
    category    VARCHAR(100),
    importance  DECIMAL(3,2),
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMP,

    CONSTRAINT fk_cz_resource_knowledge_resource
        FOREIGN KEY (resource_id) REFERENCES cz_resource (id) ON DELETE CASCADE,
    CONSTRAINT fk_cz_resource_knowledge_chunk
        FOREIGN KEY (chunk_id) REFERENCES cz_resource_chunk (id) ON DELETE SET NULL,
    CONSTRAINT chk_difficulty CHECK (difficulty IS NULL OR difficulty BETWEEN 1 AND 5)
);

COMMENT ON TABLE  cz_resource_knowledge IS '知识卡片表（AI 从资料中提炼的结构化知识点）';
COMMENT ON COLUMN cz_resource_knowledge.id          IS '知识点ID（自增）';
COMMENT ON COLUMN cz_resource_knowledge.resource_id IS '来源资料ID → cz_resource';
COMMENT ON COLUMN cz_resource_knowledge.chunk_id    IS '关联切块ID → cz_resource_chunk';
COMMENT ON COLUMN cz_resource_knowledge.title       IS '知识点标题';
COMMENT ON COLUMN cz_resource_knowledge.summary     IS '知识点摘要';
COMMENT ON COLUMN cz_resource_knowledge.keywords    IS '关键词（逗号分隔）';
COMMENT ON COLUMN cz_resource_knowledge.difficulty  IS '难度等级 1–5';
COMMENT ON COLUMN cz_resource_knowledge.category    IS '知识分类';
COMMENT ON COLUMN cz_resource_knowledge.importance  IS '重要程度 0.00–1.00';
COMMENT ON COLUMN cz_resource_knowledge.created_at  IS '创建时间';
COMMENT ON COLUMN cz_resource_knowledge.updated_at  IS '更新时间';
COMMENT ON COLUMN cz_resource_knowledge.deleted_at  IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_resource_knowledge_resource_id ON cz_resource_knowledge (resource_id);
CREATE INDEX IF NOT EXISTS idx_cz_resource_knowledge_chunk_id    ON cz_resource_knowledge (chunk_id);

-- ============================================================
-- 8. cz_agent_session_meta — AI 会话元数据表
--    所属模块：CuiZhi-agent
--    ID：VARCHAR(36) UUID（用户可见）
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_agent_session_meta (
    id                VARCHAR(36)  PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_id           VARCHAR(36)  NOT NULL,
    session_name      VARCHAR(100),
    conversation_type VARCHAR(30)  NOT NULL DEFAULT 'LEARNING_QA',
    model_name        VARCHAR(50),
    status            SMALLINT     DEFAULT 1,
    last_message_at   TIMESTAMP,
    message_count     INT          DEFAULT 0,
    created_at        TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at        TIMESTAMP,

    CONSTRAINT fk_cz_agent_session_user FOREIGN KEY (user_id) REFERENCES cz_user (id)
);

COMMENT ON TABLE  cz_agent_session_meta IS 'AI 会话表（会话元数据）';
COMMENT ON COLUMN cz_agent_session_meta.id                IS '会话ID（UUID，对外暴露）';
COMMENT ON COLUMN cz_agent_session_meta.user_id           IS '所属用户ID → cz_user';
COMMENT ON COLUMN cz_agent_session_meta.session_name      IS '会话名称';
COMMENT ON COLUMN cz_agent_session_meta.conversation_type IS '会话类型：LEARNING_QA / TASK_GENERATION / WRONG_QUESTION ...';
COMMENT ON COLUMN cz_agent_session_meta.model_name        IS '使用的模型名称';
COMMENT ON COLUMN cz_agent_session_meta.status            IS '状态：1正常 0关闭 2归档';
COMMENT ON COLUMN cz_agent_session_meta.last_message_at   IS '最后一条消息时间';
COMMENT ON COLUMN cz_agent_session_meta.message_count     IS '消息总数';
COMMENT ON COLUMN cz_agent_session_meta.created_at        IS '创建时间';
COMMENT ON COLUMN cz_agent_session_meta.updated_at        IS '更新时间';
COMMENT ON COLUMN cz_agent_session_meta.deleted_at        IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_agent_session_user_id    ON cz_agent_session_meta (user_id);
CREATE INDEX IF NOT EXISTS idx_cz_agent_session_created_at ON cz_agent_session_meta (created_at DESC);

-- ============================================================
-- 9. cz_ai_message — AI 对话消息表
--    所属模块：CuiZhi-agent
--    ID：BIGINT 自增（内部高频实体）
--    FK session_id → cz_agent_session_meta.id (VARCHAR)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_ai_message (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    session_id   VARCHAR(36) NOT NULL,
    role         VARCHAR(20) NOT NULL,
    content      TEXT,
    model_name   VARCHAR(50),
    token_count  INT,
    latency_ms   INT,
    created_at   TIMESTAMP   NOT NULL DEFAULT now(),

    CONSTRAINT fk_cz_ai_message_session
        FOREIGN KEY (session_id) REFERENCES cz_agent_session_meta (id) ON DELETE CASCADE,
    CONSTRAINT chk_cz_ai_message_role CHECK (role IN ('system', 'user', 'assistant', 'tool'))
);

COMMENT ON TABLE  cz_ai_message IS 'AI 对话消息表';
COMMENT ON COLUMN cz_ai_message.id          IS '消息ID（自增）';
COMMENT ON COLUMN cz_ai_message.session_id  IS '会话ID → cz_agent_session_meta';
COMMENT ON COLUMN cz_ai_message.role        IS '角色：system / user / assistant / tool';
COMMENT ON COLUMN cz_ai_message.content     IS '消息内容';
COMMENT ON COLUMN cz_ai_message.model_name  IS '模型名称';
COMMENT ON COLUMN cz_ai_message.token_count IS '消耗 Token 数';
COMMENT ON COLUMN cz_ai_message.latency_ms  IS '延迟（毫秒）';
COMMENT ON COLUMN cz_ai_message.created_at  IS '创建时间';

CREATE INDEX IF NOT EXISTS idx_cz_ai_message_session_id ON cz_ai_message (session_id);
CREATE INDEX IF NOT EXISTS idx_cz_ai_message_created_at ON cz_ai_message (created_at DESC);

-- ============================================================
-- 10. cz_ai_embedding — 向量存储表
--     所属模块：CuiZhi-agent
--     ID：BIGINT 自增
--     FK chunk_id → cz_resource_chunk.id (BIGINT)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_ai_embedding (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    chunk_id   BIGINT      NOT NULL,
    embedding  vector(1536),
    model_name VARCHAR(50),
    created_at TIMESTAMP   NOT NULL DEFAULT now(),

    CONSTRAINT fk_cz_ai_embedding_chunk
        FOREIGN KEY (chunk_id) REFERENCES cz_resource_chunk (id) ON DELETE CASCADE
);

COMMENT ON TABLE  cz_ai_embedding IS '向量存储表（chunk embedding，pgvector）';
COMMENT ON COLUMN cz_ai_embedding.id         IS '向量ID（自增）';
COMMENT ON COLUMN cz_ai_embedding.chunk_id   IS '切块ID → cz_resource_chunk';
COMMENT ON COLUMN cz_ai_embedding.embedding  IS '向量（默认1536维）';
COMMENT ON COLUMN cz_ai_embedding.model_name IS 'Embedding 模型名称';
COMMENT ON COLUMN cz_ai_embedding.created_at IS '创建时间';

-- 同一 chunk 只保留一条 embedding
CREATE UNIQUE INDEX IF NOT EXISTS uk_cz_ai_embedding_chunk_id ON cz_ai_embedding (chunk_id);

-- HNSW 向量索引（余弦距离）
CREATE INDEX IF NOT EXISTS idx_cz_ai_embedding_vector_hnsw
    ON cz_ai_embedding USING hnsw (embedding vector_cosine_ops);

-- ============================================================
-- 11. cz_ai_retrieval_log — RAG 检索日志表
--     所属模块：CuiZhi-agent
--     ID：BIGINT 自增
--     FK session_id → cz_agent_session_meta.id (VARCHAR)
--     FK message_id → cz_ai_message.id (BIGINT)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_ai_retrieval_log (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    session_id VARCHAR(36),
    message_id BIGINT,
    query      TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_cz_ai_retrieval_log_session
        FOREIGN KEY (session_id) REFERENCES cz_agent_session_meta (id) ON DELETE SET NULL,
    CONSTRAINT fk_cz_ai_retrieval_log_message
        FOREIGN KEY (message_id) REFERENCES cz_ai_message (id) ON DELETE SET NULL
);

COMMENT ON TABLE  cz_ai_retrieval_log IS 'RAG 检索日志表（每次检索的 query 记录）';
COMMENT ON COLUMN cz_ai_retrieval_log.id         IS '检索ID（自增）';
COMMENT ON COLUMN cz_ai_retrieval_log.session_id IS '会话ID → cz_agent_session_meta（可空）';
COMMENT ON COLUMN cz_ai_retrieval_log.message_id IS '关联消息ID → cz_ai_message（可空）';
COMMENT ON COLUMN cz_ai_retrieval_log.query      IS '检索 query 文本';
COMMENT ON COLUMN cz_ai_retrieval_log.created_at IS '创建时间';

CREATE INDEX IF NOT EXISTS idx_cz_ai_retrieval_log_session_id ON cz_ai_retrieval_log (session_id);
CREATE INDEX IF NOT EXISTS idx_cz_ai_retrieval_log_created_at ON cz_ai_retrieval_log (created_at DESC);

-- ============================================================
-- 12. cz_ai_retrieval_item — 检索结果明细表
--     所属模块：CuiZhi-agent
--     ID：BIGINT 自增
--     FK retrieval_id → cz_ai_retrieval_log.id (BIGINT)
--     FK chunk_id     → cz_resource_chunk.id    (BIGINT)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_ai_retrieval_item (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    retrieval_id BIGINT  NOT NULL,
    chunk_id     BIGINT  NOT NULL,
    score        DOUBLE PRECISION,

    CONSTRAINT fk_cz_ai_retrieval_item_retrieval
        FOREIGN KEY (retrieval_id) REFERENCES cz_ai_retrieval_log (id) ON DELETE CASCADE,
    CONSTRAINT fk_cz_ai_retrieval_item_chunk
        FOREIGN KEY (chunk_id) REFERENCES cz_resource_chunk (id) ON DELETE CASCADE
);

COMMENT ON TABLE  cz_ai_retrieval_item IS '检索结果明细表（召回的 chunk 与相似度得分）';
COMMENT ON COLUMN cz_ai_retrieval_item.id           IS '明细ID（自增）';
COMMENT ON COLUMN cz_ai_retrieval_item.retrieval_id IS '检索ID → cz_ai_retrieval_log';
COMMENT ON COLUMN cz_ai_retrieval_item.chunk_id     IS '切块ID → cz_resource_chunk';
COMMENT ON COLUMN cz_ai_retrieval_item.score        IS '相似度得分';

CREATE INDEX IF NOT EXISTS idx_cz_ai_retrieval_item_retrieval_id ON cz_ai_retrieval_item (retrieval_id);
CREATE INDEX IF NOT EXISTS idx_cz_ai_retrieval_item_chunk_id     ON cz_ai_retrieval_item (chunk_id);

-- ============================================================
-- 13. cz_study_plan — 学习计划表
--     所属模块：CuiZhi-learning（迁移到 PostgreSQL）
--     ID：VARCHAR(36) UUID
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_study_plan (
    id                 VARCHAR(36)  PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_id            VARCHAR(36)  NOT NULL,
    source_resource_id VARCHAR(36),
    title              VARCHAR(200) NOT NULL,
    goal               TEXT,
    start_date         DATE,
    end_date           DATE,
    total_days         INT,
    daily_minutes      INT,
    progress           DECIMAL(5,2) DEFAULT 0,
    status             VARCHAR(20)  DEFAULT 'ACTIVE',
    ai_model           VARCHAR(50),
    prompt_version     VARCHAR(20),
    created_at         TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at         TIMESTAMP,

    CONSTRAINT fk_cz_study_plan_user
        FOREIGN KEY (user_id) REFERENCES cz_user (id),
    CONSTRAINT fk_cz_study_plan_resource
        FOREIGN KEY (source_resource_id) REFERENCES cz_resource (id) ON DELETE SET NULL
);

COMMENT ON TABLE  cz_study_plan IS '学习计划表';
COMMENT ON COLUMN cz_study_plan.id                 IS '计划ID（UUID）';
COMMENT ON COLUMN cz_study_plan.user_id            IS '所属用户ID → cz_user';
COMMENT ON COLUMN cz_study_plan.source_resource_id IS '来源资料ID → cz_resource';
COMMENT ON COLUMN cz_study_plan.title              IS '计划标题';
COMMENT ON COLUMN cz_study_plan.goal               IS '学习目标';
COMMENT ON COLUMN cz_study_plan.start_date         IS '开始日期';
COMMENT ON COLUMN cz_study_plan.end_date           IS '结束日期';
COMMENT ON COLUMN cz_study_plan.total_days         IS '总天数';
COMMENT ON COLUMN cz_study_plan.daily_minutes      IS '每日学习时长（分钟）';
COMMENT ON COLUMN cz_study_plan.progress           IS '进度百分比 0.00–100.00';
COMMENT ON COLUMN cz_study_plan.status             IS '计划状态：ACTIVE / COMPLETED / PAUSED';
COMMENT ON COLUMN cz_study_plan.ai_model           IS '生成所用 AI 模型';
COMMENT ON COLUMN cz_study_plan.prompt_version     IS '生成所用 Prompt 版本';
COMMENT ON COLUMN cz_study_plan.created_at         IS '创建时间';
COMMENT ON COLUMN cz_study_plan.updated_at         IS '更新时间';
COMMENT ON COLUMN cz_study_plan.deleted_at         IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_study_plan_user_id ON cz_study_plan (user_id);

-- ============================================================
-- 14. cz_study_task — 学习任务表
--     所属模块：CuiZhi-learning
--     ID：BIGINT 自增
--     FK plan_id → cz_study_plan.id (VARCHAR)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_study_task (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    plan_id      VARCHAR(36)  NOT NULL,
    task_date    DATE,
    task_order   INT,
    task_type    VARCHAR(20)  DEFAULT '阅读',
    title        VARCHAR(200),
    content      TEXT,
    priority     SMALLINT     DEFAULT 2,
    status       VARCHAR(20)  DEFAULT 'PENDING',
    completed_at TIMESTAMP,
    score        DECIMAL(3,1),
    remark       TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at   TIMESTAMP,

    CONSTRAINT fk_cz_study_task_plan
        FOREIGN KEY (plan_id) REFERENCES cz_study_plan (id) ON DELETE CASCADE
);

COMMENT ON TABLE  cz_study_task IS '学习任务表';
COMMENT ON COLUMN cz_study_task.id           IS '任务ID（自增）';
COMMENT ON COLUMN cz_study_task.plan_id      IS '所属计划ID → cz_study_plan';
COMMENT ON COLUMN cz_study_task.task_date    IS '学习日期';
COMMENT ON COLUMN cz_study_task.task_order   IS '任务顺序';
COMMENT ON COLUMN cz_study_task.task_type    IS '任务类型：阅读 / 练习 / 复习 / 测验';
COMMENT ON COLUMN cz_study_task.title        IS '任务标题';
COMMENT ON COLUMN cz_study_task.content      IS '任务内容';
COMMENT ON COLUMN cz_study_task.priority     IS '优先级：1高 2中 3低';
COMMENT ON COLUMN cz_study_task.status       IS '任务状态：PENDING / IN_PROGRESS / COMPLETED';
COMMENT ON COLUMN cz_study_task.completed_at IS '完成时间';
COMMENT ON COLUMN cz_study_task.score        IS '完成评分 0.0–10.0';
COMMENT ON COLUMN cz_study_task.remark       IS '备注';
COMMENT ON COLUMN cz_study_task.created_at   IS '创建时间';
COMMENT ON COLUMN cz_study_task.updated_at   IS '更新时间';
COMMENT ON COLUMN cz_study_task.deleted_at   IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_study_task_plan_id ON cz_study_task (plan_id);

-- ============================================================
-- 15. cz_study_plan_resource — 学习计划-资料关联表（多对多）
--     所属模块：CuiZhi-learning
--     联合主键
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_study_plan_resource (
    plan_id     VARCHAR(36) NOT NULL,
    resource_id VARCHAR(36) NOT NULL,
    sort_order  INT,
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),

    PRIMARY KEY (plan_id, resource_id),
    CONSTRAINT fk_study_plan_resource_plan
        FOREIGN KEY (plan_id) REFERENCES cz_study_plan (id) ON DELETE CASCADE,
    CONSTRAINT fk_study_plan_resource_resource
        FOREIGN KEY (resource_id) REFERENCES cz_resource (id) ON DELETE CASCADE
);

COMMENT ON TABLE  cz_study_plan_resource IS '学习计划-资料关联表（N:M）';
COMMENT ON COLUMN cz_study_plan_resource.plan_id     IS '学习计划ID → cz_study_plan';
COMMENT ON COLUMN cz_study_plan_resource.resource_id IS '资料ID → cz_resource';
COMMENT ON COLUMN cz_study_plan_resource.sort_order  IS '排序顺序';
COMMENT ON COLUMN cz_study_plan_resource.created_at  IS '创建时间';

CREATE INDEX IF NOT EXISTS idx_study_plan_resource_resource ON cz_study_plan_resource (resource_id);

-- ============================================================
-- 16. cz_wrong_question — 错题本表
--     所属模块：CuiZhi-learning
--     ID：BIGINT 自增
--     FK task_id → cz_study_task.id (BIGINT)
-- ============================================================
CREATE TABLE IF NOT EXISTS cz_wrong_question (
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    task_id        BIGINT       NOT NULL,
    resource_id    VARCHAR(36),
    knowledge_id   BIGINT,
    question       TEXT         NOT NULL,
    question_type  VARCHAR(30),
    difficulty     SMALLINT,
    correct_answer TEXT,
    user_answer    TEXT,
    is_correct     SMALLINT,
    review_count   INT          DEFAULT 0,
    next_review_at TIMESTAMP,
    created_at     TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT now(),
    deleted_at     TIMESTAMP,

    CONSTRAINT fk_cz_wrong_question_task
        FOREIGN KEY (task_id) REFERENCES cz_study_task (id) ON DELETE CASCADE,
    CONSTRAINT fk_cz_wrong_question_resource
        FOREIGN KEY (resource_id) REFERENCES cz_resource (id) ON DELETE SET NULL,
    CONSTRAINT fk_cz_wrong_question_knowledge
        FOREIGN KEY (knowledge_id) REFERENCES cz_resource_knowledge (id) ON DELETE SET NULL,
    CONSTRAINT chk_wq_difficulty CHECK (difficulty IS NULL OR difficulty BETWEEN 1 AND 5)
);

COMMENT ON TABLE  cz_wrong_question IS '错题本表';
COMMENT ON COLUMN cz_wrong_question.id             IS '错题ID（自增）';
COMMENT ON COLUMN cz_wrong_question.task_id        IS '所属任务ID → cz_study_task';
COMMENT ON COLUMN cz_wrong_question.resource_id    IS '来源资料ID → cz_resource';
COMMENT ON COLUMN cz_wrong_question.knowledge_id   IS '关联知识点ID → cz_resource_knowledge';
COMMENT ON COLUMN cz_wrong_question.question       IS '题目内容';
COMMENT ON COLUMN cz_wrong_question.question_type  IS '题型：单选 / 多选 / 填空 / 简答';
COMMENT ON COLUMN cz_wrong_question.difficulty     IS '难度 1–5';
COMMENT ON COLUMN cz_wrong_question.correct_answer IS '正确答案';
COMMENT ON COLUMN cz_wrong_question.user_answer    IS '用户答案';
COMMENT ON COLUMN cz_wrong_question.is_correct     IS '是否答对：1正确 0错误';
COMMENT ON COLUMN cz_wrong_question.review_count   IS '复习次数（用于间隔复习算法）';
COMMENT ON COLUMN cz_wrong_question.next_review_at IS '下次复习时间（间隔复习算法）';
COMMENT ON COLUMN cz_wrong_question.created_at     IS '创建时间';
COMMENT ON COLUMN cz_wrong_question.updated_at     IS '更新时间';
COMMENT ON COLUMN cz_wrong_question.deleted_at     IS '软删除时间';

CREATE INDEX IF NOT EXISTS idx_cz_wrong_question_task_id ON cz_wrong_question (task_id);
