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

To reduce and optimize RAM usage for your Spring Boot app on fly.io:

- **Reduce JVM Heap Size:** Set lower `-Xmx` and `-Xms` values (e.g., `-Xmx256m`) in your deployment config or Dockerfile.
- **Use a Smaller Base Image:** Use a slim JDK base image (e.g., `eclipse-temurin:17-jre-jammy`).
- **Enable Spring Boot Lazy Initialization:** Add `spring.main.lazy-initialization=true` in `application.properties`.
- **Remove Unused Dependencies:** Clean up your `pom.xml` to avoid loading unnecessary libraries.
- **Disable Unused Auto-Configurations:** Use `@SpringBootApplication(exclude = {...})` to skip modules you don’t need.
- **Profile-Driven Bean Loading:** Use Spring profiles to load only required beans for production.
- **Tune Garbage Collection:** Use a lightweight GC like G1 or ZGC with appropriate flags.
- **Limit Thread Pools:** Configure thread pools (e.g., Tomcat, async tasks) to use fewer threads.
- **Monitor and Profile:** Use tools like VisualVM or Spring Boot Actuator to monitor memory usage and identify leaks.

Example JVM options for `fly.toml` or Docker:
```
JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseG1GC"
```

These steps help keep memory usage low and costs down.