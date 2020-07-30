Just Journal currently runs on MySQL 5.7.  Included in this directory
are sql files to create the tables and stored procedures needed to run JJ.

MySQL Version Compatibility
MySQL 5.7.x is required since portions of the code now use stored procedures.

JDBC Driver
The production version of just journal is currently running on mysql-connector-j  8.0.19
Installation
____________

To create a jj database, run the jj_create.sql script.  Several stored procedures
are not created during this process.  Refer to the individual .sql files for each
stored procedure or view to install.

NOTE: in the process of migrating to flyway. 
