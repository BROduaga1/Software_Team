CREATE TABLE IF NOT EXISTS training_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS trainees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_of_birth DATE,
    address VARCHAR(255),
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_trainee_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS admins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS trainers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    specialization BIGINT NOT NULL,
    CONSTRAINT fk_trainer_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_trainer_specialization FOREIGN KEY (specialization) REFERENCES training_types(id)
);

CREATE TABLE IF NOT EXISTS trainings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trainee_id BIGINT,
    trainer_id BIGINT,
    training_name VARCHAR(255) NOT NULL,
    training_type_id BIGINT,
    date DATE NOT NULL,
    duration INTEGER NOT NULL,
    CONSTRAINT fk_training_trainee FOREIGN KEY (trainee_id) REFERENCES trainees(id),
    CONSTRAINT fk_training_trainer FOREIGN KEY (trainer_id) REFERENCES trainers(id),
    CONSTRAINT fk_training_type FOREIGN KEY (training_type_id) REFERENCES training_types(id)
);

CREATE TABLE IF NOT EXISTS trainee_trainer (
    trainee_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    PRIMARY KEY (trainee_id, trainer_id),
    CONSTRAINT fk_trainee_trainer_trainee FOREIGN KEY (trainee_id) REFERENCES trainees(id),
    CONSTRAINT fk_trainee_trainer_trainer FOREIGN KEY (trainer_id) REFERENCES trainers(id)
);
