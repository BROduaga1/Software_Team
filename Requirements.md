# Requirements Specification


## 1. Functional Requirements

- **User Registration and Authentication:**  
  Users must be able to create accounts, log in, and recover access.

- **View Training Schedule:**  
  Users must be able to view the current schedule of group and individual sessions.

- **Booking Sessions:**  
  Users must be able to book a spot in a session through the web interface.

- **Purchase Memberships:**  
  Users must be able to purchase different types of memberships via the integrated payment system.

- **Manage Training Sessions:**  
  Admins must be able to create, edit, and delete training sessions.

- **User Management:**  
  Admins must be able to view a list of registered users and their activity.

- **Session Notifications:**  
  The system must send users reminders about upcoming sessions.

---

## 2. Quality Requirements

### 2.1 Reliability
- The system must maintain at least 99% uptime.
- In case of component failure, the system should automatically restart using Docker or Kubernetes mechanisms.

### 2.2 Scalability
- The system architecture must support horizontal scaling of backend services.
- Infrastructure must allow adding new component instances without system downtime.

### 2.3 Performance
- 90% of user requests must be processed within less than 1 second.
- The system must support at least 100 concurrent active users without performance degradation.

### 2.4 Security
- All connections must be protected via HTTPS.
- Access to resources must be secured through token-based authentication (JWT).
- User passwords must be stored only in hashed form.

### 2.5 Usability
- The user interface must be intuitive and optimized for mobile devices and tablets.
- Users should be able to perform core actions (booking, membership purchase) within a maximum of three clicks.

### 2.6 Maintainability
- The codebase must be modular and documented to ease future maintenance and development.
- Minimum unit test coverage must be 70%.
- Configuration parameters (database credentials, integration keys) must be stored in external settings (`.env` files or Kubernetes secrets).

---

# Вимоги до системи


## 1. Functional Requirements (Функціональні вимоги)

- **Реєстрація та автентифікація користувачів:**  
  Користувачі повинні мати можливість створювати облікові записи, входити в систему та відновлювати доступ до акаунту.

- **Перегляд розкладу тренувань:**  
  Користувачі повинні мати можливість переглядати актуальний розклад групових та індивідуальних занять.

- **Бронювання занять:**  
  Користувачі повинні мати можливість бронювати місце на тренування через веб-інтерфейс.

- **Купівля абонементів:**  
  Користувачі повинні мати можливість купувати різні типи абонементів через інтегровану платіжну систему.

- **Управління тренуваннями:**  
  Адміністратори повинні мати можливість створювати, редагувати та видаляти тренування.

- **Управління користувачами:**  
  Адміністратори повинні мати можливість переглядати список зареєстрованих користувачів та їхню активність.

- **Сповіщення про тренування:**  
  Система повинна надсилати користувачам сповіщення про заплановані тренування.

---

## 2. Quality Requirements (Якісні вимоги)

### 2.1 Надійність
- Система повинна забезпечувати доступність на рівні не менше 99% часу.
- У разі відмови окремого компонента система повинна автоматично здійснювати перезапуск через механізми Docker або Kubernetes.

### 2.2 Масштабованість
- Архітектура системи повинна підтримувати горизонтальне масштабування Backend-сервісів для обробки зростаючої кількості користувачів.
- Інфраструктура повинна дозволяти додавання нових екземплярів компонентів без переривання роботи системи.

### 2.3 Продуктивність
- 90% запитів користувачів повинні оброблятися менш ніж за 1 секунду.
- Система повинна підтримувати одночасну роботу мінімум 100 активних користувачів без деградації продуктивності.

### 2.4 Безпека
- Усі з'єднання повинні бути захищені протоколом HTTPS.
- Доступ до ресурсів повинен бути захищений через механізм токенів аутентифікації (JWT).
- Паролі користувачів повинні зберігатися в базі даних лише у вигляді захешованих значень.

### 2.5 Зручність використання
- Інтерфейс користувача має бути інтуїтивно зрозумілим, оптимізованим для мобільних пристроїв та планшетів.
- Користувач повинен мати можливість здійснювати основні дії (бронювання, купівля абонементу) максимум у три кліки.

### 2.6 Супровідність
- Кодова база повинна бути модульною та задокументованою для спрощення подальшої підтримки і розвитку.
- Мінімальний рівень покриття юніт-тестами має становити 70%.
- Конфігураційні параметри (бази даних, ключі інтеграцій тощо) повинні зберігатися у зовнішніх налаштуваннях (`.env` файлах або секретах Kubernetes).

---

# ✅ End of Requirements
