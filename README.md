# StudentTaskManager
Student Task Manager App – Project Documentation
Developer: Iyezha Joy B. Rebalbos
Course: Application Development and Emerging Technologies
Platform: Android (Android Studio, Kotlin/Java)
Date: November 16, 2026

Introduction
Students often have many academic activities to track at the same time, such as assignments, projects, quizzes, and deadlines. It is easy to forget a task or to lose track of what is due next. The Student Task Manager App is an Android application that helps students organize these academic activities in one place. The user can add, edit, and delete tasks, set due dates, mark tasks as pending or completed, and see a summary of their workload on a dashboard.

Objectives
The main objectives of this project are:
•	To build a working Android application that helps students manage their academic tasks.
•	To practice user interface design using XML and Material Design components.
•	To store task data locally using SQLite or the Room Database.
•	To display task records in a list using RecyclerView.
•	To test the application and fix errors before the final presentation.

 Tools and Technologies
•	Programming language: Java
•	User interface: XML layouts
•	Database: SQLite
•	Development environment: Android Studio
•	Minimum Android version: API 23

Application Features
The application includes the following required modules:
•	Login Screen: Users can create an account and log in. Passwords are stored in hashed form, and the app remembers the logged-in user until logout.
•	Dashboard: Displays the total, pending, completed, and overdue task counts, with links to Manage Tasks, About, and Logout.
•	Add Task: Users enter a title, description, and due date, and the app checks that all fields are filled.
•	Edit Task: Users can change the title, description, and due date of an existing task.
•	Delete Task: Users can remove a task after confirming the action.
•	Task List: Displays all saved tasks, sorted by due date, as cards.
•	Task Status: Tapping a task switches it between Pending and Completed. Tasks past their due date are shown as Overdue.
•	Due Date: Users choose the due date from a date picker.
•	About Screen: Shows developer information and the technologies used.
How to Use the Application
1.	Open the app and tap Create Account, then enter a username and password.
2.	Log in with the account. The Dashboard appears.
3.	Tap Manage Tasks. Enter the title, description, and due date, then tap Add Task.
4.	Tap a task to mark it Completed or Pending.
5.	Press and hold a task to Edit or Delete it.
6.	Use the filter menu to view All, Pending, Completed, or Overdue tasks.
7.	Return to the Dashboard to see the updated summary.
8.	Tap About to view developer information, or Logout to sign out.

Project Structure
app/src/main/
├── java/com/example/studenttaskmanager/
│   ├── LoginActivity.java       Login and account registration
│   ├── DashboardActivity.java   Task summary and navigation
│   ├── MainActivity.java        Add, edit, delete, and filter tasks
│   ├── AboutActivity.java       Developer information
│   ├── DatabaseHelper.java      SQLite database operations
│   ├── Task.java                Task data model
│   └── TaskAdapter.java         Task card list adapter
└── res/
    ├── layout/                  Screen layouts (XML)
    └── drawable/                Backgrounds, buttons, and icons
    
Testing Results
No.	Test	Result
1	Application opens successfully	Passed
2	Can create an account and log in	Passed
3	Can add a new task	Passed
4	Can display saved tasks	Passed
5	Can edit an existing task	Passed
6	Can delete a task	Passed
7	Can change task status	Passed
8	Overdue tasks are marked correctly	Passed
9	Data remains saved after closing and reopening the app	Passed
10	Application runs without crashing	Passed

Future Enhancements
The app can be improved in several ways in the future:
•	Add push notifications to remind the student before a deadline.
•	Add a calendar view to show tasks by date.
•	Add cloud backup so data can be restored on a new phone.
•	Add a search and filter option by category or priority.
•	Add a dark mode theme.

Conclusion
The Student Task Manager App is a practical tool that helps students plan and track their academic work. Through this project, the developer applied skills in Android development, user interface design, local database management, and software testing. The app covers all the basics and gives us a good base to build on.

Project Links
•	Video demonstration: https://drive.google.com/file/d/1fCxO5vnf4O2XMqP46lBb7SrU17iXEyZ5/view?usp=sharing

Author
Iyezha Joy B. Rebalbos Bachelor of Science in Computer Science Bicol University Polangui
