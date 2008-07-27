Just Journal currently runs on MySQL 5.0.51a.  Included in this directory
are sql files to create the tables and stored procedures needed to run JJ.

MySQL Version Compatibility
MySQL 4.x and lower will not work with Just Journal.  MySQL 5.0.x is required
since portions of the code now use stored procedures.

JDBC Driver
The production version of just journal is currently running on mysql-connector-j
3.1.10.  3.1.8 and 3.1.7 are also known to work.  

Installation
____________

To create a jj database, run the jj_create.sql script.  Several stored procedures
are not created during this process.  Refer to the individual .sql files for each
stored procedure or view to install.