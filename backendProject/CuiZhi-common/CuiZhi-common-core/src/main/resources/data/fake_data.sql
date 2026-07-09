-- =====================================================
-- QuenchMind 测试数据脚本（PostgreSQL）
-- 说明：生成完整的测试数据，包含用户、小组、资源、会话等
-- =====================================================

-- =====================================================
-- 1. 用户数据
-- =====================================================
INSERT INTO cz_user (id, user_name, user_phone, user_email, password_hash, login_type, nick_name, user_avatar, user_gender, birthday, status, last_login_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'zhangsan', '13800138001', 'zhangsan@example.com', '$2a$10$ClRQdoM7.YeQyuLUwy9MZe4sca2VugSOxBRVC5h3kN3dadI3msgva', 'PASSWORD', '张三', 'https://example.com/avatar1.jpg', 'MALE', '1990-05-15', 1, '2026-05-19 10:30:00'),
    ('550e8400-e29b-41d4-a716-446655440002', 'lisi', '13800138002', 'lisi@example.com', '$2a$10$ClRQdoM7.YeQyuLUwy9MZe4sca2VugSOxBRVC5h3kN3dadI3msgva', 'PASSWORD', '李四', 'https://example.com/avatar2.jpg', 'FEMALE', '1992-08-20', 1, '2026-05-18 15:45:00'),
    ('550e8400-e29b-41d4-a716-446655440003', 'wangwu', '13800138003', 'wangwu@example.com', '$2a$10$ClRQdoM7.YeQyuLUwy9MZe4sca2VugSOxBRVC5h3kN3dadI3msgva', 'PASSWORD', '王五', 'https://example.com/avatar3.jpg', 'MALE', '1988-12-10', 1, '2026-05-17 09:20:00'),
    ('550e8400-e29b-41d4-a716-446655440004', 'zhaoliu', '13800138004', 'zhaoliu@example.com', '$2a$10$ClRQdoM7.YeQyuLUwy9MZe4sca2VugSOxBRVC5h3kN3dadI3msgva', 'PASSWORD', '赵六', NULL, 'OTHER', NULL, 1, '2026-05-16 14:10:00'),
    ('550e8400-e29b-41d4-a716-446655440005', 'testuser', NULL, 'test@example.com', '$2a$10$ClRQdoM7.YeQyuLUwy9MZe4sca2VugSOxBRVC5h3kN3dadI3msgva', 'EMAIL_CODE', '测试用户', 'https://example.com/avatar5.jpg', 'FEMALE', '1995-03-25', 0, NULL);

-- =====================================================
-- 2. 学习小组数据
-- =====================================================
INSERT INTO cz_study_group (id, group_name, description, owner_id, max_members, status)
VALUES
    ('6a0e8400-e29b-41d4-a716-446655440001', 'Java学习小组', '一起学习Java编程语言，分享学习资源和经验', '550e8400-e29b-41d4-a716-446655440001', 50, 1),
    ('6a0e8400-e29b-41d4-a716-446655440002', 'Python数据分析', 'Python数据处理与分析技术交流', '550e8400-e29b-41d4-a716-446655440002', 30, 1),
    ('6a0e8400-e29b-41d4-a716-446655440003', '前端开发交流', 'HTML/CSS/JavaScript前端技术讨论', '550e8400-e29b-41d4-a716-446655440003', 40, 1),
    ('6a0e8400-e29b-41d4-a716-446655440004', '算法刷题群', 'LeetCode算法题交流与解答', '550e8400-e29b-41d4-a716-446655440001', 100, 1);

-- =====================================================
-- 3. 小组成员数据
-- =====================================================
INSERT INTO cz_group_member (group_id, user_id, role)
VALUES
    ('6a0e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'OWNER'),
    ('6a0e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 'MEMBER'),
    ('6a0e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', 'MEMBER'),
    ('6a0e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', 'OWNER'),
    ('6a0e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440004', 'MEMBER'),
    ('6a0e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', 'OWNER'),
    ('6a0e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', 'ADMIN'),
    ('6a0e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440001', 'OWNER'),
    ('6a0e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', 'ADMIN'),
    ('6a0e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440003', 'MEMBER'),
    ('6a0e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440004', 'MEMBER');

-- =====================================================
-- 4. 学习资料数据
-- =====================================================
INSERT INTO cz_resource (user_id, title, description, file_name, file_type, file_path, parse_status, vector_status)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Java核心技术卷I', 'Java编程基础知识详解', 'java_core_vol1.pdf', 'pdf', '/resources/java_core_vol1.pdf', 1, 1),
    ('550e8400-e29b-41d4-a716-446655440001', 'Spring Boot实战', 'Spring Boot框架开发指南', 'springboot_practice.pdf', 'pdf', '/resources/springboot_practice.pdf', 1, 1),
    ('550e8400-e29b-41d4-a716-446655440002', 'Python数据科学手册', 'Python数据分析与可视化', 'python_data_science.pdf', 'pdf', '/resources/python_data_science.pdf', 1, 1),
    ('550e8400-e29b-41d4-a716-446655440002', 'Pandas快速入门', 'Pandas数据处理教程', 'pandas_intro.pptx', 'pptx', '/resources/pandas_intro.pptx', 1, 0),
    ('550e8400-e29b-41d4-a716-446655440003', 'Vue.js官方文档', 'Vue.js前端框架学习', 'vuejs_docs.html', 'html', '/resources/vuejs_docs.html', 1, 1),
    ('550e8400-e29b-41d4-a716-446655440003', 'React学习笔记', 'React组件化开发实践', 'react_notes.docx', 'docx', '/resources/react_notes.docx', 0, 0),
    ('550e8400-e29b-41d4-a716-446655440004', '算法导论第三版', '经典算法教材', 'algorithms_intro.pdf', 'pdf', '/resources/algorithms_intro.pdf', 1, 1),
    ('550e8400-e29b-41d4-a716-446655440001', '设计模式精讲', '23种设计模式详解', 'design_patterns.pdf', 'pdf', '/resources/design_patterns.pdf', 1, 1);

-- =====================================================
-- 5. 文本切块数据
-- =====================================================
INSERT INTO cz_resource_chunk (resource_id, chunk_no, content, token_count)
VALUES
    -- Java核心技术卷I 的切块
    (1, 1, 'Java是一种广泛使用的计算机编程语言，拥有跨平台、面向对象、泛型编程的特性。', 45),
    (1, 2, 'Java虚拟机（JVM）是Java程序运行的基础，它提供了内存管理、垃圾回收等功能。', 42),
    (1, 3, '面向对象编程是Java的核心特性，包括封装、继承和多态三个基本特征。', 38),

    -- Spring Boot实战 的切块
    (2, 1, 'Spring Boot简化了Spring应用的初始搭建以及开发过程，通过自动配置减少XML配置。', 48),
    (2, 2, '@SpringBootApplication注解是Spring Boot的核心注解，它组合了@Configuration、@EnableAutoConfiguration和@ComponentScan。', 52),

    -- Python数据科学手册 的切块
    (3, 1, 'NumPy是Python中用于科学计算的基础包，提供了多维数组对象和多种派生对象。', 46),
    (3, 2, 'Pandas是基于NumPy的数据分析工具，提供了DataFrame和Series两种主要数据结构。', 44),
    (3, 3, 'Matplotlib是Python的绘图库，可以生成各种静态、动态和交互式的可视化图表。', 43),

    -- Vue.js官方文档 的切块
    (5, 1, 'Vue.js是一套用于构建用户界面的渐进式框架，核心库只关注视图层。', 40),
    (5, 2, 'Vue的响应式系统基于Object.defineProperty或Proxy实现数据的双向绑定。', 42),

    -- 算法导论 的切块
    (7, 1, '算法是解决特定问题的一系列明确指令，必须具备输入、输出、确定性、有限性和可行性。', 47),
    (7, 2, '时间复杂度用于描述算法运行时间随输入规模增长的变化趋势，常用大O表示法。', 44),
    (7, 3, '排序算法包括冒泡排序、快速排序、归并排序等，各有不同的时间和空间复杂度。', 45),

    -- 设计模式精讲 的切块
    (8, 1, '设计模式是在特定环境下解决常见设计问题的可复用方案，分为创建型、结构型和行为型三类。', 50),
    (8, 2, '单例模式确保一个类只有一个实例，并提供全局访问点，常用于数据库连接池等场景。', 46),
    (8, 3, '观察者模式定义了对象间的一对多依赖关系，当一个对象状态改变时，所有依赖者都会收到通知。', 51);

-- =====================================================
-- 6. AI会话数据
-- =====================================================
INSERT INTO cz_agent_session_meta (user_id, session_name, conversation_type)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Java学习问答', 'LEARNING_QA'),
    ('550e8400-e29b-41d4-a716-446655440001', '生成学习计划', 'TASK_GENERATION'),
    ('550e8400-e29b-41d4-a716-446655440002', 'Python数据分析咨询', 'LEARNING_QA'),
    ('550e8400-e29b-41d4-a716-446655440003', '前端技术问题', 'LEARNING_QA'),
    ('550e8400-e29b-41d4-a716-446655440004', '算法题解析', 'WRONG_QUESTION');

-- =====================================================
-- 7. AI消息数据
-- =====================================================
INSERT INTO cz_ai_message (session_id, role, content, model_name, total_tokens, latency_ms)
VALUES
    -- 会话1：Java学习问答
    (1, 'user', '什么是Java的多态性？', 'gpt-4', 15, 200),
    (1, 'assistant', '多态性是面向对象的三大特性之一，指同一个行为具有多个不同表现形式。在Java中主要通过方法重载（编译时多态）和方法重写（运行时多态）来实现。', 'gpt-4', 120, 1500),
    (1, 'user', '能举个例子吗？', 'gpt-4', 10, 180),
    (1, 'assistant', '当然！比如Animal类有speak()方法，Dog和Cat子类重写了这个方法。当调用animal.speak()时，会根据实际对象类型调用对应的方法，这就是运行时多态。', 'gpt-4', 135, 1600),

    -- 会话2：生成学习计划
    (2, 'user', '帮我制定一个Java学习计划', 'gpt-4', 18, 220),
    (2, 'assistant', '好的！我建议分三个阶段：1）基础语法（2周）2）面向对象（2周）3）高级特性（3周）。每天学习2小时，配合实践项目。', 'gpt-4', 110, 1400),

    -- 会话3：Python数据分析
    (3, 'user', 'Pandas如何处理缺失值？', 'gpt-4', 16, 190),
    (3, 'assistant', 'Pandas提供了多种处理缺失值的方法：dropna()删除、fillna()填充、interpolate()插值。具体选择取决于数据特性和业务需求。', 'gpt-4', 125, 1550),

    -- 会话4：前端技术
    (4, 'user', 'Vue和React有什么区别？', 'gpt-4', 14, 175),
    (4, 'assistant', '主要区别：1）Vue是双向数据绑定，React是单向数据流 2）Vue使用模板语法，React使用JSX 3）Vue更易于上手，React生态更丰富。', 'gpt-4', 140, 1700),

    -- 会话5：算法题解析
    (5, 'user', '快速排序的时间复杂度是多少？', 'gpt-4', 17, 210),
    (5, 'assistant', '快速排序的平均时间复杂度是O(n log n)，最坏情况是O(n²)。最坏情况发生在每次选择的基准元素都是最大或最小值时。', 'gpt-4', 130, 1650);

-- =====================================================
-- 8. 向量数据（示例向量，实际应使用embedding模型生成）
-- =====================================================
INSERT INTO cz_ai_embedding (chunk_id, embedding, model_name)
VALUES
    (1, '[0.1,0.2,0.3,0.4,0.5]' || repeat(',0.1', 1531)::vector, 'text-embedding-ada-002'),
    (2, '[0.2,0.3,0.4,0.5,0.6]' || repeat(',0.2', 1531)::vector, 'text-embedding-ada-002'),
    (3, '[0.3,0.4,0.5,0.6,0.7]' || repeat(',0.3', 1531)::vector, 'text-embedding-ada-002'),
    (4, '[0.4,0.5,0.6,0.7,0.8]' || repeat(',0.4', 1531)::vector, 'text-embedding-ada-002'),
    (5, '[0.5,0.6,0.7,0.8,0.9]' || repeat(',0.5', 1531)::vector, 'text-embedding-ada-002'),
    (7, '[0.6,0.7,0.8,0.9,1.0]' || repeat(',0.6', 1531)::vector, 'text-embedding-ada-002'),
    (8, '[0.7,0.8,0.9,1.0,0.1]' || repeat(',0.7', 1531)::vector, 'text-embedding-ada-002'),
    (9, '[0.8,0.9,1.0,0.1,0.2]' || repeat(',0.8', 1531)::vector, 'text-embedding-ada-002'),
    (10, '[0.9,1.0,0.1,0.2,0.3]' || repeat(',0.9', 1531)::vector, 'text-embedding-ada-002'),
    (11, '[1.0,0.1,0.2,0.3,0.4]' || repeat(',1.0', 1531)::vector, 'text-embedding-ada-002'),
    (12, '[0.15,0.25,0.35,0.45,0.55]' || repeat(',0.15', 1531)::vector, 'text-embedding-ada-002'),
    (13, '[0.25,0.35,0.45,0.55,0.65]' || repeat(',0.25', 1531)::vector, 'text-embedding-ada-002'),
    (14, '[0.35,0.45,0.55,0.65,0.75]' || repeat(',0.35', 1531)::vector, 'text-embedding-ada-002'),
    (15, '[0.45,0.55,0.65,0.75,0.85]' || repeat(',0.45', 1531)::vector, 'text-embedding-ada-002'),
    (16, '[0.55,0.65,0.75,0.85,0.95]' || repeat(',0.55', 1531)::vector, 'text-embedding-ada-002');

-- =====================================================
-- 9. RAG检索日志
-- =====================================================
INSERT INTO cz_ai_retrieval_log (session_id, message_id, query)
VALUES
    (1, 2, 'Java多态性详解'),
    (1, 4, '多态性示例代码'),
    (3, 8, 'Pandas缺失值处理'),
    (4, 10, 'Vue React对比'),
    (5, 12, '快速排序复杂度');

-- =====================================================
-- 10. 检索结果明细
-- =====================================================
INSERT INTO cz_ai_retrieval_item (retrieval_id, chunk_id, score)
VALUES
    (1, 3, 0.92),
    (1, 1, 0.85),
    (2, 3, 0.88),
    (3, 8, 0.95),
    (3, 9, 0.87),
    (4, 10, 0.91),
    (4, 11, 0.84),
    (5, 14, 0.93),
    (5, 12, 0.86);

-- =====================================================
-- 11. 知识点数据
-- =====================================================
INSERT INTO cz_resource_knowledge (resource_id, title, summary, embedding)
VALUES
    (1, 'Java语言特性', 'Java具有跨平台、面向对象、安全性高等特点，通过JVM实现一次编写到处运行。', '[0.1,0.2,0.3,0.4,0.5]' || repeat(',0.1', 1531)::vector),
    (1, '面向对象编程', 'OOP三大特性：封装隐藏内部实现、继承实现代码复用、多态提供灵活接口。', '[0.2,0.3,0.4,0.5,0.6]' || repeat(',0.2', 1531)::vector),
    (2, 'Spring Boot自动配置', '通过@EnableAutoConfiguration实现自动配置，根据classpath中的依赖自动配置Bean。', '[0.3,0.4,0.5,0.6,0.7]' || repeat(',0.3', 1531)::vector),
    (3, 'NumPy数组操作', 'NumPy提供高效的数组运算，支持广播机制和向量化操作，性能远超原生Python。', '[0.4,0.5,0.6,0.7,0.8]' || repeat(',0.4', 1531)::vector),
    (5, 'Vue响应式原理', 'Vue 2使用Object.defineProperty，Vue 3使用Proxy实现数据劫持和响应式更新。', '[0.5,0.6,0.7,0.8,0.9]' || repeat(',0.5', 1531)::vector),
    (7, '算法复杂度分析', '时间复杂度衡量算法执行时间，空间复杂度衡量内存占用，用大O表示法描述增长趋势。', '[0.6,0.7,0.8,0.9,1.0]' || repeat(',0.6', 1531)::vector),
    (8, '单例模式实现', '单例模式确保全局唯一实例，懒汉式延迟加载，饿汉式线程安全，双重检查锁定兼顾性能和安全性。', '[0.7,0.8,0.9,1.0,0.1]' || repeat(',0.7', 1531)::vector);

-- =====================================================
-- 12. 学习计划
-- =====================================================
INSERT INTO cz_study_plan (user_id, title)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Java后端开发学习计划'),
    ('550e8400-e29b-41d4-a716-446655440002', 'Python数据分析入门'),
    ('550e8400-e29b-41d4-a716-446655440003', '前端框架深入学习'),
    ('550e8400-e29b-41d4-a716-446655440004', '算法刷题计划');

-- =====================================================
-- 13. 学习任务
-- =====================================================
INSERT INTO cz_study_task (plan_id, content)
VALUES
    (1, '学习Java基础语法：变量、数据类型、运算符'),
    (1, '掌握面向对象编程：类、对象、继承、多态'),
    (1, '学习集合框架：List、Set、Map的使用'),
    (2, '安装Python环境和Jupyter Notebook'),
    (2, '学习NumPy数组基础操作'),
    (2, '掌握Pandas DataFrame数据处理'),
    (3, '学习Vue.js基础语法和指令'),
    (3, '理解组件化开发和props传递'),
    (4, '刷LeetCode简单难度题目10道'),
    (4, '学习排序算法：冒泡、快排、归并');

-- =====================================================
-- 14. 错题数据
-- =====================================================
INSERT INTO cz_wrong_question (task_id, question, correct_answer, user_answer)
VALUES
    (1, 'Java中int和Integer的区别是什么？', 'int是基本数据类型，Integer是包装类。Integer可以为null，支持自动装箱拆箱。', '没有区别'),
    (3, 'HashMap和HashTable的区别？', 'HashMap非线程安全，允许null键值；HashTable线程安全，不允许null。', 'HashMap更快'),
    (5, 'NumPy中array和matrix的区别？', 'array是N维数组，matrix始终是2维。推荐使用array，matrix已废弃。', 'matrix是多维的'),
    (9, '快速排序的最坏时间复杂度？', 'O(n²)，发生在每次划分都极不平衡时。', 'O(n log n)'),
    (10, '什么是稳定排序？', '稳定排序保证相等元素的相对顺序不变，如冒泡排序、归并排序。', '排序速度快');
