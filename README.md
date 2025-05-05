# Functional Requirements - GymCRM System

## User Authentication
1. The system must allow users to authenticate via the `AuthController`, which communicates with the `AuthService`.
2. The system must validate the username and password stored in the `users` table.

## User Management (via `UserFacade`)
3. The system must allow creation, editing, and activation/deactivation of user accounts.
4. The system must distinguish between user roles: administrator (`admins`), trainer (`trainers`), and trainee (`trainees`).

## Trainer Management
5. Users must be able to manage trainer information through the `TrainerController`.
6. The system must store each trainer’s specialization and associate them with a user (`trainers.user_id → users.id`).

## Trainee Management
7. Users must be able to view, create, and update trainee information via the `TraineeController`.
8. A trainee’s profile must include date of birth, address, and be linked to a user (`trainees.user_id → users.id`).

## Training Management
9. Users must be able to create training sessions via the `TrainingController`, specifying the trainee, trainer, training name, type, date, and duration.
10. The system must support storing training types in the `training_types` reference table.

## Trainer-Trainee Relationships
11. The system must support a many-to-many relationship between trainers and trainees via the `trainee_trainer` table.

## Database Interaction
12. The Spring Boot application must read from and write to the database based on interactions between controllers and facades.
13. The system must support full CRUD operations for all major entities: users, trainers, trainees, trainings, and training types.
