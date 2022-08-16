# Quiz Manager

A simple quiz manager which allows an admin user to add, update, and delete questions; add and delete students;
and also take or export the quiz.
<br />
A student can take or export the quiz and see their score.
### User Guide
There is only one Launcher for this project.
The Launcher is located at /src/fr.epita.quiz.launcher/Launcher.java.
<br />
Before you can run the Launcher, you need to provide the following information.
<br />
In the db.properties file,

```
url=<your_postgres_db_url>
username=<db_user>
password=<db_password>
```
In the credentials.properties file,

```
adminUser=<admin_user>
adminPassword=<admin_password>
studentUser=<student_user>
studentPassword=<student_password>
```
### Technical Guide
The project is built considering PostgreSQL as the database.
The sql queries written are in regards to the PostgreSQL database.

The driver for PostgreSQL is available in the lib folder.