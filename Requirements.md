GymCRM System
# Functional Requirements

## User (Client)

* Can register a personal account in the system
* Can log in and log out securely
* Can view the schedule of group and personal training sessions
* Can book group or one-on-one training sessions
* Can view personal visit history
* Can view and follow a personal training program created by a trainer
* Can manage subscriptions (purchase, extend, upgrade/downgrade)
* Can make online payments for subscriptions or single sessions
* Can communicate with a trainer via built-in chat or comments
* Can leave feedback on completed sessions
* Can upload personal documents (e.g., medical certificates)

## Trainer

* Can log into the trainer’s dashboard
* Can view the list of assigned clients
* Can create and update personal training programs for clients
* Can mark attendance and track client progress
* Can schedule personal consultations with clients
* Can receive notifications about schedule changes
* Can view health alerts or restrictions for clients
* Can communicate with clients through internal chat

## Administrator

* Can log into the admin dashboard
* Can add, update, or deactivate trainer accounts
* Can manage schedules for group classes
* Can set and update subscription plans and pricing
* Can view gym statistics including visits, income, and trainer activity
* Can generate and export financial and operational reports
* Can manage gym inventory and equipment records
* Can manage system-wide notifications (email, push)
* Can assign discounts, promotions, or loyalty rewards to users

## System

* Must automatically track and deduct visits from the user's subscription
* Must send reminders about upcoming sessions and expiring subscriptions
* Must store complete history of trainings and payments per user
* Must update the schedule in real-time across all interfaces
* Must prevent overbooking of sessions once capacity is reached
* Must perform regular data backups
* Must log all actions and changes for auditing and history tracking
* Must support multi-location setups (e.g., multiple gym branches)

# Non-Functional Requirements

## Performance

* The system must support at least 500 concurrent users without instability
* 95% of API requests should respond in under 200ms
* Updates to schedule or subscriptions must reflect in the interface within 2 seconds
* Search for clients, sessions, or records must return results within 1 second

## Reliability

* System availability must be at least 99.5% uptime per month
* Data backups must be performed every 2 hours
* In the event of a system failure, recovery must be possible from the latest backup
* No data (e.g., payments, sessions) should be lost during unexpected session termination

## Security

* Passwords must be stored using secure encryption (e.g., bcrypt)
* Authentication must use secure protocols (HTTPS + JWT)
* Payment information must comply with PCI DSS standards
* Admin access must require two-factor authentication (2FA)
* The system must restrict unauthorized access to user profiles, programs, and payment history

## Scalability

* The system must scale to support up to 100 gym locations and 50,000 users
* It must support multi-branch mode with independent schedules, admins, and trainers
* New user roles, services, or subscription types must be easily added without reworking the architecture

## Usability

* The interface must be responsive and work across mobile, tablet, and desktop devices
* The system must be accessible to users with disabilities (WCAG 2.1 Level AA compliant)
* The interface must support at least two languages: Ukrainian and English
* Booking a session should not require more than 3 clicks from the dashboard

## Analytics and Reporting

* The system must provide monthly analytics on visits, payments, and trainer performance
* Reports must be exportable in PDF or Excel formats
* All profile or session changes must be logged with timestamps for traceability

# Main Components

* **API Gateway (Spring Cloud Gateway)**: Routes incoming requests to appropriate backend services.
* **User Service**: Manages user registration, login, profile, and role-based access.
* **Trainer Service**: Handles trainer profiles, schedules, and assigned clients.
* **Booking Service**: Manages session scheduling, availability, and attendance.
* **Subscription & Payment Service**: Handles payments, subscriptions, invoices, and plan upgrades.
* **Notification Service**: Sends email and push notifications (e.g., reminders, confirmations).
* **Analytics Service**: Collects usage data and generates reports.
* **Chat Service (optional)**: Enables communication between clients and trainers.
* **Frontend Application (Vue.js)**: User interface for clients, trainers, and admins.
* **Kafka**: Event-driven communication between services.

# Technologies Stack

* **Spring Boot 3.x** – Backend framework for microservices
* **Spring Cloud Gateway** – API Gateway and routing
* **Apache Kafka** – Messaging and event distribution
* **PostgreSQL** – Relational database for persistent data storage
* **Vue.js** – Frontend framework (SPA)
* **Docker** – Containerization for deployment
* **Maven** – Build and dependency management
* **JWT + HTTPS** – Secure authentication and communication
