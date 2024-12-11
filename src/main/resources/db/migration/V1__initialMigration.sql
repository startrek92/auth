CREATE TABLE company (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT 1,
    created_on DATETIME NOT NULL,
    modified_on DATETIME NOT NULL
);

CREATE TABLE user (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fk_company_id INTEGER,
    failed_login_count INTEGER NOT NULL DEFAULT 0,
    is_locked BOOLEAN NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT 1,
    created_on DATETIME NOT NULL,
    modified_on DATETIME NOT NULL,
    CONSTRAINT fk_user_company_id FOREIGN KEY (fk_company_id) REFERENCES company(id)
);

CREATE TABLE client (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    client_secret VARCHAR(255) NOT NULL,
    grant_type ENUM('IMPLICIT', 'CLIENT_CREDENTIALS', 'AUTHORIZATION_CODE') NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT 1,
    created_on DATETIME NOT NULL,
    modified_on DATETIME NOT NULL
);

CREATE TABLE bearer_token (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    access_token LONGTEXT NOT NULL,
    refresh_token LONGTEXT,
    access_token_expires_on DATETIME NOT NULL,
    refresh_token_expires_on DATETIME,
    fk_user_id INTEGER NOT NULL,
    created_on DATETIME NOT NULL,
    modified_on DATETIME NOT NULL,
    CONSTRAINT uk_access_token UNIQUE (access_token(255)),
    CONSTRAINT uk_refresh_token UNIQUE (refresh_token(255)),
    CONSTRAINT fk_bearer_token_user FOREIGN KEY (fk_user_id) REFERENCES user(id)
);