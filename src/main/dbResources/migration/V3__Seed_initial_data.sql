-- User Group data --
INSERT INTO user_group (
    id,
    name,
    fully_qualified_name,
    description
)
SELECT
    1,
    'LAZYCO',
    'LAZYCO',
    'Default root user group'
    WHERE NOT EXISTS (
    SELECT 1 FROM user_group WHERE name = 'LAZYCO'
);

-- Role data --
INSERT INTO role (
    id,
    name,
    description,
    role_type)
SELECT
    1,
    'ADMIN',
    'System administrator with full access',
    'ADMINISTRATOR'
    WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE name = 'ADMIN'
);

-- app user data --
INSERT INTO app_user (
    id,
    user_id,
    password,
    email,
    first_name,
    last_name,
    is_super_admin,
    is_administrator,
    user_group)
SELECT
    1,
    'admin',
    '$2a$10$SDeY4VLgoLAP7jyff5fIbeUS1.TBa8j03ezX81J./eZKAAOSiT.L6',
    'admin@gmail.com',
    'System',
    'Administrator',
    TRUE,
    TRUE,
    'LAZYCO'
    WHERE NOT EXISTS (
    SELECT 1 FROM app_user WHERE user_id = 'admin'
);

-- User Role data --
INSERT INTO user_role (
    id,
    app_user_id,
    role_id,
    user_group_id
)
SELECT
    1,
    1,
    1,
    1
WHERE NOT EXISTS (
    SELECT 1 FROM user_role WHERE app_user_id = 1 AND role_id = 1 AND user_group_id = 1
);