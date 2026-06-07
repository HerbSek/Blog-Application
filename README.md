# Blog Application

A Jakarta EE blog platform with passwordless magic-link authentication. Users sign up and log in via a single-use, time-limited verification link sent to their email — no passwords stored.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | GlassFish 7 (Jakarta EE 10) |
| UI | JSF 4 / Facelets |
| REST | JAX-RS |
| Persistence | JPA 3 (EclipseLink) + PostgreSQL |
| Config | MicroProfile Config 3 |
| Email | Jakarta Mail (SMTP / Gmail) |

---

## Auth Flow

```
Sign up / Login
      │
      ▼
  Form submit
      │
      ▼
  Token issued (UUID, 5-min TTL, single-use)
      │
      ▼
  Verification email sent  ──►  "Check your inbox" page
      │
      ▼  (user clicks link)
  /v1/auth/verify-token
      │
      ├── expired?   → reject
      ├── used?      → reject
      └── valid      → invalidate token → redirect
                              │
                    ┌─────────┴─────────┐
                  signup              login
                    │                   │
              set verified        set session
                    │                   │
              → login page      → dashboard
```

---

## Project Structure

```
src/main/
├── java/.../blog/application/
│   ├── controllers/
│   │   ├── AuthController.java       # signup, login, logout
│   │   └── SessionController.java    # session-scoped user holder
│   ├── models/
│   │   ├── Users.java
│   │   └── Token.java                # expiry + valid + user ref
│   ├── resources/
│   │   └── AuthResource.java         # GET /v1/auth/verify-token
│   ├── services/
│   │   └── BlogService.java          # JPA CRUD wrapper
│   └── util/
│       └── EmailUtil.java            # async SMTP + HTML template rendering
├── resources/
│   ├── META-INF/
│   │   ├── persistence.xml
│   │   ├── microprofile-config.properties        ← gitignored (create from .sample)
│   │   └── microprofile-config.properties.sample
│   └── templates/
│       └── email-verify.html         # HTML email template
└── webapp/
    ├── WEB-INF/
    │   ├── glassfish-resources.xml   ← gitignored (create from .sample)
    │   └── glassfish-resources.xml.sample
    ├── index.xhtml                   # landing page
    ├── login.xhtml
    ├── signup.xhtml
    ├── check-email.xhtml             # post-submit confirmation
    └── dashboard.xhtml
```

---

## Setup

### Prerequisites

- GlassFish 7
- PostgreSQL database (tested on Supabase)
- Gmail account with an [App Password](https://support.google.com/accounts/answer/185833) enabled

### 1. JDBC connection pool

Copy the sample and fill in your database credentials:

```bash
cp src/main/webapp/WEB-INF/glassfish-resources.xml.sample \
   src/main/webapp/WEB-INF/glassfish-resources.xml
```

> **PostgreSQL note:** the `URL` property must include `?prepareThreshold=0` to prevent
> the `prepared statement "S_1" already exists` error with GlassFish's connection pool.
> The sample file includes this by default.

Deploy the datasource to GlassFish:

```bash
asadmin add-resources src/main/webapp/WEB-INF/glassfish-resources.xml
```

### 2. MicroProfile Config

Copy the sample and fill in your SMTP credentials:

```bash
cp src/main/resources/META-INF/microprofile-config.properties.sample \
   src/main/resources/META-INF/microprofile-config.properties
```

```properties
mail.smtp.host=smtp.gmail.com
mail.smtp.port=465
mail.smtp.user=your-email@gmail.com
mail.smtp.password=your-app-password
```

### 3. Build and deploy

```bash
mvn clean package
asadmin deploy target/Blog-Application-1.0.war
```

The app is available at `http://localhost:8080/Blog-Application`.

---

## Configuration Files (gitignored)

These files contain secrets and are excluded from version control. Committed sample files document their structure.

| Gitignored file | Sample to copy from |
|---|---|
| `WEB-INF/glassfish-resources.xml` | `WEB-INF/glassfish-resources.xml.sample` |
| `META-INF/microprofile-config.properties` | `META-INF/microprofile-config.properties.sample` |


<img width="1907" height="970" alt="Screenshot 2026-06-07 111925" src="https://github.com/user-attachments/assets/e3eb6b45-6f24-4081-ba6f-b98d042844bd" />

<img width="1542" height="811" alt="Screenshot 2026-06-07 112912" src="https://github.com/user-attachments/assets/67976e71-0b78-41bb-b0a6-fdda9a550dac" />

<img width="1891" height="899" alt="Screenshot 2026-06-07 113033" src="https://github.com/user-attachments/assets/9c6e0a25-b69d-4f5d-821e-3de5aae33b3b" />

<img width="1882" height="889" alt="image" src="https://github.com/user-attachments/assets/4bc37605-34bc-49b5-b5c3-ba1fa44c4124" />
