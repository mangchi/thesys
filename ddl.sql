-- This SQL script creates tables in a MySQL database.

-- user_roles 테이블에서 users 참조를 삭제 (외래키 제약조건 제거)
ALTER TABLE user_roles DROP FOREIGN KEY fk_user_roles_user;

-- user_roles 테이블에서 roles 참조를 삭제 (외래키 제약조건 제거)
ALTER TABLE user_roles DROP FOREIGN KEY fk_user_roles_role;

-- role_permissions 테이블에서 roles 참조를 삭제 (외래키 제약조건 제거)
ALTER TABLE role_permissions DROP FOREIGN KEY fk_role_permissions_role;

-- user_rolrole_permissionses 테이블에서 permissionses 참조를 삭제 (외래키 제약조건 제거)
ALTER TABLE role_permissions DROP FOREIGN KEY fk_role_permissions_permission;

-- users 테이블 삭제
DROP TABLE IF EXISTS users ;

-- roles 테이블 삭제
DROP TABLE IF EXISTS roles ;

-- user_roles 테이블 삭제
DROP TABLE IF EXISTS user_roles ;

-- permissions 테이블 삭제
DROP TABLE IF EXISTS permissions ;

-- role_permissions 테이블 삭제
DROP TABLE IF EXISTS role_permissions ;

-- 사용자 테이블
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    username VARCHAR(100) NOT NULL UNIQUE COMMENT '사용자명',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호 (암호화)',
    name VARCHAR(100) COMMENT '실명',
    email VARCHAR(255) UNIQUE COMMENT '이메일',
    phone VARCHAR(20) COMMENT '전화번호',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성 사용자 여부',
    created_by BIGINT COMMENT '생성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) COMMENT='사용자 테이블';


-- 역활 테이블
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '역할 ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '역할 이름 (예: ADMIN, USER)',
    description VARCHAR(255) COMMENT '역할 설명',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) COMMENT='역할(Role) 테이블';


-- 사용자-역할 매핑 테이블
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    role_id BIGINT NOT NULL COMMENT '역할 ID',
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE ON UPDATE CASCADE 
) ENGINE=InnoDB COMMENT='사용자와 역할 간의 매핑 테이블';


-- 권한 테이블
CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '권한 ID',
    code VARCHAR(100) NOT NULL UNIQUE COMMENT '권한 코드 (예: USER_READ, POST_WRITE)',
    description VARCHAR(255) COMMENT '권한 설명',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) COMMENT='세부 권한 목록 테이블';



-- 역할-권한 매핑 테이블

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL COMMENT '역할 ID',
    permission_id BIGINT NOT NULL COMMENT '권한 ID',
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) 
        REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) 
        REFERENCES permissions(id) ON DELETE CASCADE ON UPDATE CASCADE 
) COMMENT='역할과 권한 간의 매핑 테이블';


-- -- 다국어 지원을 위한 언어 테이블
CREATE TABLE languages (
    code VARCHAR(10) PRIMARY KEY COMMENT '언어 코드 (예: en, ko, jp)',
    name VARCHAR(100) NOT NULL COMMENT '언어명',
    is_default BOOLEAN DEFAULT FALSE COMMENT '기본 언어 여부'
) COMMENT='지원 언어 목록 테이블';


-- 다국어 문자열 테이블
CREATE TABLE i18n_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_key VARCHAR(100) NOT NULL,
    lang_code VARCHAR(10),
    message TEXT,
    UNIQUE (message_key, lang_code),
    FOREIGN KEY (lang_code) REFERENCES languages(code)
);

-- Code 테이블에서 CodeGroup 참조를 삭제 (외래키 제약조건 제거)
ALTER TABLE code DROP FOREIGN KEY fk_code_group;

-- Code 테이블 삭제
DROP TABLE IF EXISTS code ;

-- CodeGroup 테이블 삭제
DROP TABLE IF EXISTS code_group ;

-- 코드 그룹 테이블

CREATE TABLE IF NOT EXISTS code_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID, 각 코드 그룹을 식별하는 기본키',
    group_code VARCHAR(50) NOT NULL UNIQUE COMMENT '그룹 코드, 고유한 코드 그룹 식별자 (예: USER_TYPE)',
    name_ko VARCHAR(255) COMMENT '그룹명 (한글), 코드 그룹을 한글로 설명',
    name_en VARCHAR(255) COMMENT '그룹명 (영문), 코드 그룹을 영어로 설명',
    description TEXT COMMENT '그룹 설명, 코드 그룹에 대한 추가적인 설명',
    created_at DATETIME COMMENT '생성 일시, 코드 그룹이 생성된 날짜와 시간',
    updated_at DATETIME COMMENT '수정 일시, 코드 그룹이 마지막으로 수정된 날짜와 시간',
    created_by VARCHAR(100) COMMENT '생성자, 코드 그룹을 만든 사용자 또는 시스템',
    updated_by VARCHAR(100) COMMENT '수정자, 코드 그룹을 마지막으로 수정한 사용자 또는 시스템'
) COMMENT '코드 그룹 테이블, 다양한 코드 항목을 그룹화하여 관리';


-- 코드 테이블

CREATE TABLE IF NOT EXISTS code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID, 각 코드 항목을 식별하는 기본키',
    group_id BIGINT NOT NULL COMMENT '코드 그룹 ID, 해당 코드가 속한 그룹의 ID (code_group 테이블의 id)',
    code VARCHAR(50) NOT NULL COMMENT '코드 값, 실제 코드 값 (예: "ADMIN", "USER")',
    value_ko VARCHAR(255) COMMENT '코드명 (한글), 코드 값에 대한 한글 설명 (예: "관리자", "일반 사용자")',
    value_en VARCHAR(255) COMMENT '코드명 (영문), 코드 값에 대한 영어 설명 (예: "Administrator", "User")',
    sort_order INT DEFAULT 0 COMMENT '정렬 순서, 코드 항목을 표시할 때의 순서를 지정',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부, 해당 코드가 활성화된 상태인지 여부 (TRUE: 활성화, FALSE: 비활성화)',
    description TEXT COMMENT '코드 설명, 코드에 대한 추가적인 설명',
    created_at DATETIME COMMENT '생성 일시, 코드 항목이 생성된 날짜와 시간',
    updated_at DATETIME COMMENT '수정 일시, 코드 항목이 마지막으로 수정된 날짜와 시간',
    created_by VARCHAR(100) COMMENT '생성자, 코드 항목을 만든 사용자 또는 시스템',
    updated_by VARCHAR(100) COMMENT '수정자, 코드 항목을 마지막으로 수정한 사용자 또는 시스템',
    CONSTRAINT fk_code_to_group FOREIGN KEY (code_group_id) 
        REFERENCES code_group(id) ON DELETE CASCADE ON UPDATE CASCADE 
) COMMENT '코드 테이블';



-- 게시판 테이블
CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시판 ID',
    name VARCHAR(100) NOT NULL COMMENT '게시판 이름',
    description TEXT COMMENT '게시판 설명',
    created_by BIGINT COMMENT '생성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
) COMMENT='게시판 메타 정보 테이블';


-- 게시글 테이블
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID',
    board_id BIGINT NOT NULL COMMENT '게시판 ID',
    title VARCHAR(255) NOT NULL COMMENT '제목',
    content TEXT COMMENT '내용',
    lang_code VARCHAR(10) COMMENT '언어 코드',
    views INT DEFAULT 0 COMMENT '조회수',
    created_by BIGINT COMMENT '작성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (board_id) REFERENCES boards(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    FOREIGN KEY (lang_code) REFERENCES languages(code)
) COMMENT='게시판 게시글 테이블';


-- 댓글 테이블
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT,
    author_id BIGINT,
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);


CREATE TABLE cms_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 ID',
    name VARCHAR(100) NOT NULL COMMENT '카테고리 이름',
    description TEXT COMMENT '카테고리 설명',
    created_by BIGINT COMMENT '생성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
) COMMENT='CMS 콘텐츠 카테고리 테이블';


CREATE TABLE cms_contents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '콘텐츠 ID',
    category_id BIGINT COMMENT '카테고리 ID',
    title VARCHAR(255) NOT NULL COMMENT '콘텐츠 제목',
    slug VARCHAR(255) UNIQUE COMMENT 'URL 슬러그 (고유)',
    content TEXT COMMENT '본문 콘텐츠',
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT' COMMENT '게시 상태',
    lang_code VARCHAR(10) COMMENT '언어 코드',
    created_by BIGINT COMMENT '생성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (category_id) REFERENCES cms_categories(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    FOREIGN KEY (lang_code) REFERENCES languages(code)
) COMMENT='CMS 콘텐츠 테이블';




CREATE TABLE cms_banners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '배너 ID',
    title VARCHAR(255) COMMENT '배너 제목',
    image_url VARCHAR(500) COMMENT '이미지 URL',
    link_url VARCHAR(500) COMMENT '링크 URL',
    position ENUM('MAIN', 'SIDEBAR', 'FOOTER') DEFAULT 'MAIN' COMMENT '배너 위치',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성 상태 여부',
    lang_code VARCHAR(10) COMMENT '언어 코드',
    created_by BIGINT COMMENT '생성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    FOREIGN KEY (lang_code) REFERENCES languages(code)
) COMMENT='CMS 배너 테이블';


CREATE TABLE cms_pages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '페이지 ID',
    title VARCHAR(255) NOT NULL COMMENT '페이지 제목',
    slug VARCHAR(255) UNIQUE COMMENT 'URL 슬러그 (고유)',
    content TEXT COMMENT '페이지 본문 콘텐츠',
    lang_code VARCHAR(10) COMMENT '언어 코드',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성 상태 여부',
    created_by BIGINT COMMENT '생성자 사용자 ID',
    updated_by BIGINT COMMENT '수정자 사용자 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    FOREIGN KEY (lang_code) REFERENCES languages(code)
) COMMENT='CMS 페이지 테이블';






-------------   사용자 -------------------

CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 ID',
    username VARCHAR(100) NOT NULL UNIQUE COMMENT '회원 계정 (로그인 ID)',
    password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    name VARCHAR(100) NOT NULL COMMENT '회원 이름',
    email VARCHAR(255) UNIQUE COMMENT '이메일 주소',
    phone_number VARCHAR(20) COMMENT '휴대폰 번호',
    gender CHAR(1) COMMENT '성별 (M/F)',
    birth_date DATE COMMENT '생년월일',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '회원 상태 (ACTIVE, INACTIVE, WITHDRAWN)',
    last_login_at DATETIME COMMENT '마지막 로그인 일시',
    login_fail_count INT DEFAULT 0 COMMENT '로그인 실패 횟수',
    password_changed_at DATETIME COMMENT '비밀번호 변경 일시',
    terms_agreed_at DATETIME COMMENT '약관 동의 일시',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    created_by VARCHAR(50) COMMENT '생성자',
    updated_by VARCHAR(50) COMMENT '수정자'
) COMMENT '회원 테이블';

CREATE TABLE IF NOT EXISTS member_login_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '로그인 이력 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    login_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '로그인 일시',
    login_ip VARCHAR(45) COMMENT '로그인 IP',
    user_agent TEXT COMMENT 'User-Agent 정보',
    login_result VARCHAR(20) COMMENT '로그인 결과 (SUCCESS, FAIL 등)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    CONSTRAINT fk_member_login_history_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE
) COMMENT '회원 로그인 이력 테이블';

CREATE TABLE IF NOT EXISTS member_address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주소 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    receiver_name VARCHAR(100) COMMENT '수령인 이름',
    phone_number VARCHAR(20) COMMENT '연락처',
    zip_code VARCHAR(10) COMMENT '우편번호',
    address1 VARCHAR(255) COMMENT '기본주소',
    address2 VARCHAR(255) COMMENT '상세주소',
    is_default BOOLEAN DEFAULT FALSE COMMENT '기본 배송지 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    created_by VARCHAR(50) COMMENT '생성자',
    updated_by VARCHAR(50) COMMENT '수정자',
    CONSTRAINT fk_member_address_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE
) COMMENT '회원 주소 정보 테이블';

CREATE TABLE IF NOT EXISTS member_point (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '포인트 내역 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    point INT NOT NULL COMMENT '변동 포인트 (양수/음수)',
    reason VARCHAR(255) COMMENT '포인트 사유',
    point_type VARCHAR(20) DEFAULT 'SAVE' COMMENT '포인트 타입 (SAVE, USE)',
    occurred_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '발생 일시',
    created_by VARCHAR(50) COMMENT '생성자',
    CONSTRAINT fk_member_point_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE
) COMMENT '회원 포인트 내역 테이블';

CREATE TABLE IF NOT EXISTS member_terms_agreement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '약관 동의 이력 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    terms_code VARCHAR(50) NOT NULL COMMENT '약관 코드',
    agreed BOOLEAN DEFAULT FALSE COMMENT '동의 여부',
    agreed_at DATETIME COMMENT '동의 일시',
    version VARCHAR(20) COMMENT '약관 버전',
    CONSTRAINT fk_member_terms_agreement_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE
) COMMENT '회원 약관 동의 이력 테이블';

CREATE TABLE IF NOT EXISTS member_activity_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 활동 로그 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    activity_type VARCHAR(100) NOT NULL COMMENT '활동 종류 (e.g., LOGIN, UPDATE_PROFILE, DELETE_COMMENT)',
    description TEXT COMMENT '활동 설명',
    activity_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '활동 일시',
    ip_address VARCHAR(45) COMMENT 'IP 주소',
    user_agent TEXT COMMENT 'User-Agent 정보',
    created_by VARCHAR(50) COMMENT '생성자',
    CONSTRAINT fk_member_activity_log_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE
) COMMENT '회원 활동 로그 테이블';

CREATE TABLE IF NOT EXISTS member_grade_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '등급 이력 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    previous_grade VARCHAR(50) COMMENT '변경 전 등급',
    new_grade VARCHAR(50) NOT NULL COMMENT '변경된 등급',
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '변경 일시',
    changed_by VARCHAR(50) COMMENT '변경자 (관리자 ID 등)',
    reason VARCHAR(255) COMMENT '변경 사유',
    CONSTRAINT fk_member_grade_history_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE
) COMMENT '회원 등급 변경 이력 테이블';









-- 권한회원 소셜 로그인 연동 테이블
CREATE TABLE IF NOT EXISTS member_social_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '소셜 계정 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    provider VARCHAR(50) NOT NULL COMMENT '소셜 제공자 (e.g., GOOGLE, NAVER, KAKAO)',
    provider_id VARCHAR(100) NOT NULL COMMENT '소셜 계정 고유 ID',
    email VARCHAR(255) COMMENT '소셜 이메일',
    connected_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '연동 일시',
    created_by VARCHAR(50) COMMENT '생성자',
    updated_by VARCHAR(50) COMMENT '수정자',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    CONSTRAINT fk_member_social_account_member_id FOREIGN KEY (member_id)
        REFERENCES member(id)
        ON DELETE CASCADE,
    UNIQUE KEY uk_member_provider (member_id, provider)
) COMMENT '회원 소셜 로그인 연동 테이블';
