# DA379A-LocalZero

Install dependencies...
```
mvn clean install -DskipTests
```

Run the application by executing the main method in `com.localzero.api.LocalZeroApplication`

For simplicity, you can use the following command:
```
mvn exec:java -Dexec.mainClass=com.localzero.api.LocalZeroApplication
```
Tip: If you're in intellij, just press the play button next to the commands above...

When the application is running, go to localhost:8080 in your browser. Log in with the following credentials:

Username: alice@example.com
Password: passw

Username: bob@example.com
Password: passw

Username: charlie@example.com
Password: passw

Or you can create a new user by clicking on the "Register" link and log in with the new user.