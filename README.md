# RBAC (Role-Based Access Control) 系统

## 项目简介
这是一个基于角色的访问控制（RBAC）系统，旨在为应用程序提供灵活且高效的权限管理。通过定义角色和权限，系统能够根据用户的角色分配相应的权限，从而实现对资源的安全访问控制。

## 核心功能
- **用户管理**：支持用户注册、登录、获取用户信息、更新用户信息、删除用户，以及为用户分配角色和移除角色。
- **角色管理**：创建、更新、删除角色，以及为角色分配权限和移除权限。
- **权限管理**：创建、更新、删除权限，以及查看所有权限和特定权限的详细信息。
- **认证与授权**：通过自定义注解 `@RequirePermission` 实现基于角色的权限验证，确保用户只能访问其被授权的资源。

## 技术栈
- **后端框架**：Spring Boot
- **依赖管理**：Maven
- **数据库**：MySQL（或其他兼容数据库）
- **API 文档**：Swagger OpenAPI 3.0

## 项目结构
```
com.daisyPig
├── annotation
│   └── RequirePermission.java
├── controller
│   ├── AuthController.java
│   ├── PermissionController.java
│   ├── RoleController.java
│   └── UserController.java
├── dto
│   ├── ApiResponse.java
│   ├── LoginRequest.java
│   └── RegisterRequest.java
├── entity
│   ├── Permission.java
│   ├── Role.java
│   └── User.java
└── service
    ├── PermissionService.java
    ├── RoleService.java
    └── UserService.java
```

## 使用方法
1. **环境准备**
   - 确保已安装 Java JDK 1.8+ 和 Maven。
   - 配置数据库连接信息（在 `application.properties` 中）。
2. **项目启动**
   - 克隆项目到本地。
   - 使用 Maven 构建项目：`mvn clean install`。
   - 启动项目：`java -jar target/your-application.jar`。
3. **API 文档**
   - 访问 `http://localhost:8080/swagger-ui.html` 查看 API 文档。(访问地址：http://localhost:9081/swagger-ui/index.html#/)

## 示例用例
### 用户管理
- **注册用户**
  ```json
  POST /api/auth/register
  {
    "username": "exampleUser",
    "password": "examplePassword"
  }
  ```
- **登录用户**
  ```json
  POST /api/auth/login
  {
    "username": "exampleUser",
    "password": "examplePassword"
  }
  ```
- **获取当前用户信息**
  ```json
  GET /api/auth/current-user
  ```

### 角色管理
- **创建角色**
  ```json
  POST /api/roles
  {
    "name": "exampleRole",
    "description": "Example role"
  }
  ```
- **为角色添加权限**
  ```json
  POST /api/roles/{roleId}/permissions/{permissionId}
  ```

### 权限管理
- **创建权限**
  ```json
  POST /api/permissions
  {
    "name": "examplePermission",
    "description": "Example permission"
  }
  ```

## 贡献指南
欢迎参与项目贡献！请遵循以下步骤：
1. 提交 Issue 或 Pull Request。
2. 确保代码风格一致。
3. 添加必要的测试用例。

## 如何使用
1. 修改application.yml中的数据库配置
2. 导入初始化表
```sql
-- 权限表，用于存储系统中定义的各种权限信息
-- 每条记录包含一个唯一的权限 ID、权限名称和权限描述
-- 权限名称必须唯一，以确保系统中权限的标识不会冲突
CREATE TABLE permissions (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(50) NOT NULL,
    description     TEXT NULL,
    CONSTRAINT permission_name UNIQUE (permission_name)
);

-- 角色 - 权限关联表，用于建立角色和权限之间的多对多关系
-- 每条记录表示一个角色拥有的一个权限，通过角色 ID 和权限 ID 进行关联
-- 角色 ID 和权限 ID 的组合必须唯一，确保不会重复关联
CREATE TABLE role_permissions (
    role_id       INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

-- 为权限 ID 字段创建索引，以提高根据权限 ID 查询关联记录的性能
CREATE INDEX permission_id ON role_permissions (permission_id);

-- 角色表，用于存储系统中定义的各种角色信息
-- 每条记录包含一个唯一的角色 ID、角色名称和角色描述
-- 角色名称必须唯一，以确保系统中角色的标识不会冲突
CREATE TABLE roles (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL,
    description TEXT NULL,
    CONSTRAINT role_name UNIQUE (role_name)
);

-- 用户 - 角色关联表，用于建立用户和角色之间的多对多关系
-- 每条记录表示一个用户拥有的一个角色，通过用户 ID 和角色 ID 进行关联
-- 用户 ID 和角色 ID 的组合必须唯一，确保不会重复关联
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- 为角色 ID 字段创建索引，以提高根据角色 ID 查询关联记录的性能
CREATE INDEX role_id ON user_roles (role_id);

-- 用户表，用于存储系统中的用户信息
-- 每条记录包含一个唯一的用户 ID、用户名、密码、电子邮件地址和用户创建时间
-- 用户名必须唯一，以确保系统中用户的标识不会冲突
-- 用户创建时间默认设置为记录插入时的当前时间
CREATE TABLE users (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT username UNIQUE (username)
);    
```
3. 初始化一个超级管理员，有最高权限
```sql
-- 插入权限数据
INSERT INTO permissions (permission_name, description) VALUES
('role:create', '创建角色的权限'),
('permission:create', '创建权限的权限'),
('role:assign_permission', '为角色分配权限的权限'),
('user:assign_role', '为用户分配角色的权限'),
('role:view', '查看角色的权限'),
('role:edit', '编辑角色的权限'),
('role:delete', '删除角色的权限'),
('role:remove_permission', '移除角色权限的权限'),
('user:view', '查看用户的权限'),
('user:edit', '编辑用户的权限'),
('user:delete', '删除用户的权限'),
('user:remove_role', '移除用户角色的权限'),
('permission:view', '查看权限的权限'),
('permission:edit', '编辑权限的权限'),
('permission:delete', '删除权限的权限');

-- 创建管理员角色
INSERT INTO roles (role_name, description) VALUES
('Administrator', '系统管理员，拥有最高权限');

-- 获取管理员角色的 ID
SET @admin_role_id = (SELECT id FROM roles WHERE role_name = 'Administrator');

-- 为管理员角色分配权限
INSERT INTO role_permissions (role_id, permission_id)
SELECT @admin_role_id, id FROM permissions;

-- 创建管理员用户，密码：123456
INSERT INTO users (username, password, email) VALUES
('admin', '$2a$10$uvUD8VzLflTk1jrs2ByjMOeWXr38r0b.J6ht3R2C8DexKXSM9xhaa', 'admin@example.com');

-- 获取管理员用户的 ID
SET @admin_user_id = (SELECT id FROM users WHERE username = 'admin');

-- 为管理员用户分配管理员角色
INSERT INTO user_roles (user_id, role_id) VALUES
(@admin_user_id, @admin_role_id);    
```
4. 初始化一个默认角色RegularUser，用户注册默认拥有查看权限
```sql
-- 插入权限数据
INSERT INTO permissions (permission_name, description) VALUES
('user:view_self', '查看自身用户信息的权限'),
('role:view_list', '查看角色列表的权限'),
('permission:view_list', '查看权限列表的权限');

-- 创建默认角色
INSERT INTO roles (role_name, description) VALUES ('RegularUser', '普通用户角色');

-- 获取默认角色的 ID
SET @regular_user_role_id = (SELECT id FROM roles WHERE role_name = 'RegularUser');

-- 为默认角色分配权限
INSERT INTO role_permissions (role_id, permission_id)
SELECT @regular_user_role_id, id FROM permissions 
WHERE permission_name IN ('user:view_self', 'role:view_list', 'permission:view_list');
```
5. 其它（一些查询语句）
```sql
-- 查看用户名拥有的权限
SELECT p.permission_name, p.description
FROM users u
         JOIN user_roles ur ON u.id = ur.user_id
         JOIN roles r ON ur.role_id = r.id
         JOIN role_permissions rp ON r.id = rp.role_id
         JOIN permissions p ON rp.permission_id = p.id
WHERE u.username = 'libai';
-- 查看角色名拥有的权限
SELECT p.permission_name, p.description
FROM roles r
         JOIN role_permissions rp ON r.id = rp.role_id
         JOIN permissions p ON rp.permission_id = p.id
WHERE r.role_name = 'RegularUser';
```

## 许可证
本项目采用 [MIT License](LICENSE)。
