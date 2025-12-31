-- Insert default roles
INSERT INTO roles (name, description, created_at) VALUES
                                                      ('USER', 'Default user role', NOW()),
                                                      ('ADMIN', 'Administrator role', NOW())
    ON DUPLICATE KEY UPDATE name=name;

-- Insert default admin user
-- Password: Admin@123456
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS/O.
INSERT INTO users (username, email, password_hash, is_active, created_at, updated_at) VALUES
    ('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS/O.', true, NOW(), NOW())
    ON DUPLICATE KEY UPDATE username=username;

-- Assign ADMIN role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@example.com' AND r.name = 'ADMIN'
    ON DUPLICATE KEY UPDATE user_id=user_id;
