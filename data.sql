-- 권한
INSERT INTO permission (id, name, description) VALUES
(1, 'USER_READ', '사용자 조회'),
(2, 'USER_WRITE', '사용자 생성/수정');

-- 역할
INSERT INTO role (id, name, description) VALUES
(1, 'ROLE_ADMIN', '관리자'),
(2, 'ROLE_USER', '일반 사용자');

-- 역할-권한 매핑
INSERT INTO role_permissions (role_id, permissions_id) VALUES
(1, 1), (1, 2),  -- ADMIN: USER_READ, USER_WRITE
(2, 1);          -- USER: USER_READ

-- 사용자
INSERT INTO user (id, username, password, email, created_at, updated_at, created_by, updated_by)
VALUES (1, 'admin', '{noop}admin123', 'admin@example.com', now(), now(), 'system', 'system');

-- 사용자-역할 매핑
INSERT INTO user_roles (user_id, roles_id) VALUES (1, 1); -- admin -> ROLE_ADMIN


-- 사용자 유형 코드 그룹 추가
INSERT INTO code_group (group_code, name_ko, name_en, description, created_at, created_by)
VALUES ('USER_TYPE', '사용자유형', 'User Type', '사용자 유형을 구분하는 코드 그룹', NOW(), 'admin');

-- 'USER_TYPE' 그룹에 대해 코드 추가 (관리자, 일반 사용자)
INSERT INTO code (group_id, code, value_ko, value_en, sort_order, is_active, created_at, created_by)
VALUES 
(1, 'ADMIN', '관리자', 'Administrator', 1, TRUE, NOW(), 'admin'),  -- 관리자 코드 추가
(1, 'USER', '일반 사용자', 'User', 2, TRUE, NOW(), 'admin');  -- 일반 사용자 코드 추가
