# SecurePass — Plan

Open-source Java authentication SDK with built-in token expiry, instant revocation, and single-use support.
Published to GitHub Packages as a Maven dependency. Zero external auth service required.

---

## Motivation

Extracted from the Blog Application's token auth pattern:
- `Token` entity with UUID value, `valid` flag, and `expiryTime`
- Tokens issued on signup/login, verified via a JAX-RS endpoint, invalidated on use (single-use)

The goal is to generalise that pattern into a reusable, drop-in SDK for any Jakarta EE project.

---

## Project Structure

```
securepass/
├── securepass-core/            # Interfaces and model — no framework dependencies
│   ├── SecureToken.java
│   ├── TokenType.java          # Enum: SINGLE_USE, MULTI_USE
│   ├── TokenService.java       # Interface
│   └── VerificationResult.java # Enum: VALID, EXPIRED, CONSUMED, REVOKED, NOT_FOUND
├── securepass-jpa/             # Jakarta EE / JPA implementation
│   ├── SecureTokenEntity.java  # JPA entity
│   ├── TokenRepository.java    # EntityManager wrapper
│   └── TokenServiceImpl.java   # @ApplicationScoped CDI bean
└── pom.xml                     # Parent POM, published to GitHub Packages
```

---

## Core Model

| Blog App (current)       | SecurePass (planned)                                      |
|--------------------------|-----------------------------------------------------------|
| `valid` (dual purpose)   | `valid` (revocation) + `consumed` (single-use) — separate |
| Tied to `Users` entity   | Generic `subject` (String) — caller maps to their entity  |
| Hardcoded 5-min TTL      | Configurable `Duration` per issuance call                 |
| No result type           | `VerificationResult` enum with named failure reasons      |
| Token type implicit      | `TokenType` enum: `SINGLE_USE`, `MULTI_USE`               |

---

## Token Service API

```java
// Issue a token for any subject (e.g. a user ID or email)
SecureToken token = tokenService.issue(subject, Duration.ofMinutes(5), TokenType.SINGLE_USE);

// Verify — returns a named result instead of throwing or returning null
VerificationResult result = tokenService.verify(tokenValue);
// => VALID | EXPIRED | CONSUMED | REVOKED | NOT_FOUND

// Instant revocation
tokenService.revoke(tokenValue);
```

---

## VerificationResult Cases

| Result       | Meaning                                               |
|--------------|-------------------------------------------------------|
| `VALID`      | Token exists, not expired, not consumed, not revoked  |
| `EXPIRED`    | Past `expiryTime`                                     |
| `CONSUMED`   | Single-use token already used                         |
| `REVOKED`    | Explicitly revoked via `revoke()`                     |
| `NOT_FOUND`  | No token with that value in the store                 |

---

## Publishing

- `<distributionManagement>` in parent `pom.xml` pointing to GitHub Packages
- GitHub Actions workflow: build + `mvn deploy` on tag push
- Consumers add a single Maven dependency:

```xml
<dependency>
    <groupId>io.securepass</groupId>
    <artifactId>securepass-jpa</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## How the Blog App Changes After Adoption

`AuthController` and `AuthResource` drop all raw token construction and replace it with:

```java
// signup / login
SecureToken token = tokenService.issue(user.getEmail(), Duration.ofMinutes(5), TokenType.SINGLE_USE);

// verify-token endpoint
VerificationResult result = tokenService.verify(tokenParam);
```

---

## Open Question

Should `securepass-core` be framework-agnostic (plain Java, no Jakarta EE deps) so the interfaces
work outside Jakarta EE containers? Or is Jakarta EE the only target?

- **Framework-agnostic core:** broader compatibility, slightly more layering
- **Jakarta EE only:** simpler, fewer modules, matches the blog app's stack directly
