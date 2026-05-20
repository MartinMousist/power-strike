# Evidencia de ejecución de tests unitarios

**Fecha de ejecución:** 20/05/2026  
**Comando ejecutado:** `cd backend && mvn test`  
**Herramienta:** JUnit 5 + Mockito (via spring-boot-starter-test)

---

## Output crudo

```text
[INFO] Scanning for projects...
[INFO] --------------------< com.powerstrike:powerstrike >---------------------
[INFO] Building powerstrike 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- jacoco-maven-plugin:0.8.11:prepare-agent (prepare-agent) @ powerstrike ---
[INFO] argLine set to -javaagent:.../org.jacoco.agent-0.8.11-runtime.jar=destfile=.../jacoco.exec
[INFO]
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ powerstrike ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ powerstrike ---
[INFO] Compiling 3 source files with javac [debug release 17] to target/test-classes
[INFO]
[INFO] --- maven-surefire-plugin:3.1.2:test (default-test) @ powerstrike ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.powerstrike.service.ActivityServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.823 s -- in com.powerstrike.service.ActivityServiceTest
[INFO] Running com.powerstrike.service.AttendanceServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.335 s -- in com.powerstrike.service.AttendanceServiceTest
[INFO] Running com.powerstrike.service.UserServiceTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.102 s -- in com.powerstrike.service.UserServiceTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.857 s
[INFO] Finished at: 2026-05-20T17:23:13-03:00
[INFO] ------------------------------------------------------------------------
```

---

## Tabla resumen

| Clase | Tests | Pasaron | Fallaron |
|---|---|---|---|
| ActivityServiceTest | 7 | 7 | 0 |
| UserServiceTest | 8 | 8 | 0 |
| AttendanceServiceTest | 6 | 6 | 0 |
| **TOTAL** | **21** | **21** | **0** |

---

## Métodos cubiertos

**ActivityServiceTest (7):** getAllActivities, getActivityById (×2), createActivity, updateActivity (×2), deleteActivity

**UserServiceTest (8):** getAllUsers, getUserById (×2), createUser (verifica encoding de password), updateUser (×3), deleteUser

**AttendanceServiceTest (6):** getAllAttendances, getAttendancesByUser (×2), registerAttendance (×3 — happy path, fecha asignada, excepción)
