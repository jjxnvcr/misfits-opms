# Misfits-OPMS

An order processing/point-of-sale management system for a certain thrift shop

## How to Use

#### Tools
- Java JDK 21 +
- Code Editor or IDE (Ideally VSCode)
- SQL Server 22 +
- SQL Manager (SSMS or Microsoft Azure)

#### Dependencies
- FlatLaf 3.5.4
- FlatLaf Inter Font 4.1
- FlatLaf Extras 3.5.4
- MigLayout Swing 11.4.2
- Microsoft SQLServer JDBC 12.10.0

> [!IMPORTANT]
> Tools and dependencies' versions listed below are the ones used whilst developing the system. Lower versions might work but it would be best to follow what the environment use for development.

#### Set Up

Assuming the tools above are already installed in your system, open up your SQL Server Manager (SSMS or Microsoft Azure) and **`create a database named Misfits`**. By default, we use **`SQLEXPRESS`** for the instance name, **`Windows Authentication`** for authentication, **`Optional`** for encryption, and **`Enabled`** trust server certificate.

![A visual guide of the server set up](image.png)

After creating, we want to set up our newly created database and fill it with objects and records. Files to easily do this process are available inside the source code in the **[src/database](src/database/)** directory. Run the sql files in this order:

1. creation.sql
2. insertion.sql
3. obj.sql

Notice that there is a `DLL` file along with the SQL files in the directory. We use this to establish a connection between the program and the database. Download the file and put it inside the your Java PATH. **DO NOT** rename the file no matter what.

> [!NOTE]
> To check for your Java PATH, search 'Edit system environment variables' in your windows search and click Environment Variables. Under the System variables, look for the variable 'JAVA_HOME'. If not present, double click on the Path variable under User variables and look for a directory associated with Java

After putting the file inside the PATH, navigate to **[Connect.java](src/main/java/app/db/Connect.java)** and put your own credentials.

By default, you don't need to change anything with the credentials with the exception of **`encrypted`** and **`trustCert`** (depending on your server set up). If you have different values, replace the default ones with yours.

Once all of that is finished, we now have set up the database and the connection between it and the program.
