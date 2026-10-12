# Smart Expense Tracker

A simple Spring Boot web application for tracking expenses, incomes, budgets, budget alerts, and monthly reports. Uses MySQL and Thymeleaf.

Features:
- User registration and login
- Add/View/Edit/Delete expenses
- Add/View/Edit/Delete incomes
- Set monthly budgets and view budget alerts
- Monthly expense reports with breakdown and CSV export

Requirements
- Java 17+
- Maven 3.6+
- MySQL server

Setup (quick)
1. Create the MySQL database and user (example below) or run `src/main/resources/schema.sql`.
2. Edit `src/main/resources/application.properties` with your DB credentials.
3. Build & run:
   - From command line: `mvn clean package` then `mvn spring-boot:run`
   - Or run the application class in your IDE (SmartExpenseTrackerApplication)

MySQL example (run in MySQL client):
```sql
CREATE DATABASE expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'expense_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON expense_tracker.* TO 'expense_user'@'localhost';
FLUSH PRIVILEGES;
```

Default login
- No default user is required. Use Register page to create a new user.

How to download the project as a ZIP and run in Eclipse (simple steps)
1. If you have the project folder locally:
   - Compress the project root (the folder that contains `pom.xml`) to a ZIP using your OS (right-click -> Compress or use `zip -r`).
   - Save the ZIP file.

2. If the project is on GitHub:
   - Go to the repository page -> Code -> Download ZIP.

3. Import into Eclipse (Maven project)
   - Open Eclipse.
   - File -> Import -> Maven -> Existing Maven Projects -> Next.
   - Select the unzipped project folder (the folder with `pom.xml`) and Finish.
   - Eclipse will download dependencies and build the project.

4. Configure the database
   - Edit `src/main/resources/application.properties` with your MySQL URL, username, password.
   - Run the SQL script `src/main/resources/schema.sql` in your MySQL server (or allow JPA to create tables).

5. Run the application
   - Right-click `SmartExpenseTrackerApplication` -> Run As -> Java Application, or Run as Spring Boot App.
   - Open browser: http://localhost:8080
   - Use Register to create a user, then login.

Notes:
- If you see permission/connection errors, double-check DB credentials and that MySQL allows connections from your app (localhost).
- To change the server port, edit `server.port` in `application.properties`.
