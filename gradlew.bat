@rem Gradle startup script for Windows
@rem
@if "%DEBUG%"=="" @echo off
setlocal

set APP_HOME=%~dp0
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

java -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
```

---

#### File 3 — `gradle/wrapper/gradle-wrapper.jar`

This is a binary `.jar` file — **you cannot create it manually on GitHub.** You have two options:

---

### Option A — Download it directly from Gradle's GitHub

1. Go to this URL:
👉 [github.com/gradle/gradle/blob/v8.8.0/gradle/wrapper/gradle-wrapper.jar](https://github.com/gradle/gradle/blob/v8.8.0/gradle/wrapper/gradle-wrapper.jar)

2. Click **Download raw file** (the download icon)
3. In your GitHub repo go to `gradle/wrapper/`
4. Click **Add file** → **Upload files**
5. Drag the downloaded `gradle-wrapper.jar` in
6. Commit it

---

### Option B — Use the Fabric template repo (EASIEST)

Fabric provides an official template that already has everything including `gradlew`:

1. Go to 👉 [github.com/FabricMC/fabric-example-mod](https://github.com/FabricMC/fabric-example-mod)
2. Click **Use this template** → **Create a new repository**
3. Name it `noclip-mod`
4. Then just **replace** the example files with your mod files:
   - Replace `src/main/java/...` files with your `NoclipMod.java` and `NoclipCommand.java`
   - Replace `src/main/resources/fabric.mod.json` with yours
   - Replace `gradle.properties` with yours
   - Delete any files you don't need

This is the cleanest approach because the template already has `gradlew`, `gradlew.bat`, `gradle-wrapper.jar`, and a working `build.gradle` all set up correctly for Fabric 1.21.

---

### Summary — Recommended path
```
1. Use Fabric template repo  →  has gradlew already
2. Replace the source files  →  paste your mod code
3. Add .github/workflows/build.yml  →  GitHub builds the jar automatically
4. Push → GitHub Actions runs → download your .jar from the Actions tab
