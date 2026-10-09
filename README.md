# Community Store

Java 17 / Spring Boot backend with a separate Supabase frontend in `frontend/`.

## Run Backend 3 for the demonstration

Requires a JDK 17 or newer and Maven 3.9 (or the included Maven wrapper).

```powershell
mvn --batch-mode '-Dspring.profiles.active=test' package
java -jar target/CommunityStore-0.0.1-SNAPSHOT.jar --spring.profiles.active=demo
```

Open **http://127.0.0.1:8080/backend3.html**. The demo binds to loopback and creates an isolated in-memory H2 database; stopping it discards the data. It does not change the team's MySQL database or Supabase data.

| Account | Role | Password |
|---|---|---|
| buyer@demo.local | Student / order owner | DemoPass123! |
| other@demo.local | Student / ownership checks | DemoPass123! |
| admin@demo.local | Moderator | DemoPass123! |

These credentials are public, disposable demo credentials, enabled only by the `demo` profile. Never deploy that profile publicly.

### Walkthrough

1. Sign in as the buyer. Create a payment for the R150 order and simulate success. The order changes to Paid and a notification appears. Repeating the same result does not duplicate the notification.
2. Use the R75 order to demonstrate failure. The order remains Pending; no real money is charged.
3. Filter notifications to unread, mark one read, and refresh to show persisted state.
4. Publish a community post, edit it, and report it using its displayed ID.
5. Sign in as the other student. The first student's payments, notifications and reports are inaccessible, and their post cannot be edited.
6. Sign in as the moderator. Review the report, then mark ActionTaken or Dismissed. A resolved report cannot be reopened through this API.

All rendering uses text content for user messages, avoiding HTML injection. No raw card fields are collected.

## MySQL mode

Without `demo`, the existing `application.properties` points to localhost MySQL and validates rather than creates the schema. Import `database/CommunityStoreDB.sql` into a development database, set `DB_USERNAME` and `DB_PASSWORD`, and run the JAR without a profile argument. The script contains schema, sample data, procedures and triggers; inspect it before running against an existing database.

The script's sample password hashes are placeholders and cannot authenticate. Accounts used with the Java APIs need valid BCrypt hashes and Active status. Account/role provisioning is an admin operation; coordinate credentials with the team. Do not commit real passwords, hashes or provider secrets.

Backend 3 does **not** connect to PayFast/Ozow. Payment records start Pending; only the isolated `demo` profile exposes simulated settlement. A live provider adapter and verified, idempotent callbacks are still needed before accepting money. Failed demo payments are terminal; use the second order or restart the disposable demo for a new attempt.

The existing frontend authenticates against Supabase, not the Java user table. Supabase JWTs cannot be used as Java HTTP Basic credentials. `backend3.html` is a working Java-backed feature demonstration, not a replacement for the full marketplace frontend. The existing checkout binding and the team-wide identity/data contract still need coordinated integration.

## API and permissions

Only Backend 3 routes require authentication and CSRF. Existing routes retain their original public access and behavior. Consequently, legacy account/order CRUD can bypass Backend 3 ownership guarantees; the owners must coordinate team-wide authentication before production use.

Backend 3 uses HTTP Basic with BCrypt account hashes. Use HTTPS outside localhost. Mutations also require a session CSRF token, obtained with `GET /api/backend3/session`; send the returned `csrfHeader`/`csrfToken` and retain the session cookie alongside HTTP Basic authentication. The browser page handles this automatically and keeps credentials only in memory. These APIs return scalar DTOs, not nested account records.

| Method | Path | Purpose / permission |
|---|---|---|
| GET | `/api/backend3/session` | Current account and CSRF token |
| GET | `/api/backend3/orders` | Current buyer's orders for checkout |
| POST / GET | `/api/payments` | Create pending payment / list own payments (admin sees all) |
| GET | `/api/payments/{id}` | Owner or admin |
| POST | `/api/payments/{id}/demo-result` | `{ "successful": true }`; demo only, owner or admin |
| POST / GET | `/api/community-posts` | Publish as authenticated author / list newest first |
| GET / PUT / DELETE | `/api/community-posts/{id}` | Read; update/delete restricted to author or admin |
| POST | `/api/notifications` | Send to user; admin only |
| GET | `/api/notifications?unreadOnly=true` | Own notifications |
| GET / DELETE | `/api/notifications/{id}` | Recipient or admin |
| PATCH | `/api/notifications/{id}/read` | Mark read; recipient or admin |
| POST / GET | `/api/reports` | Submit / own reports (admin sees all) |
| GET | `/api/reports/{id}` | Reporter or admin |
| PATCH | `/api/reports/{id}/review` | `{ "status": "Reviewed" }`; admin only |

Legacy-style `/create`, `/read/{id}`, `/getAll` aliases are available on the new controllers. Post update/delete also accept `/update/{id}` and `/delete/{id}`. There is intentionally no arbitrary payment update/delete endpoint or client-supplied success status.

Request examples:

```json
{ "orderId": 1, "amount": 150.00, "paymentMethod": "Demo" }
```

```json
{ "title": "Campus market", "content": "Meet at the courtyard on Friday." }
```

```json
{ "userId": 1, "message": "Your order is ready." }
```

```json
{ "targetType": "CommunityPost", "targetId": 1, "reason": "Spam", "description": "Repeated advertising." }
```

Report target types: Product, Review, CommunityPost, User. Review statuses: Reviewed, ActionTaken, Dismissed. ActionTaken records a moderation decision; it does not automatically delete content. The moderator may delete a community post separately.

Payments validate order ownership, total, Pending status and the unique order constraint. Pessimistic locks serialize duplicate creation and result handling. Settlement, order update and the result notification share one transaction. Existing SQL order-created notifications remain the trigger's responsibility, so the service does not duplicate them.

## Verification

```powershell
mvn --batch-mode '-Dspring.profiles.active=test' test
mvn --batch-mode '-Dspring.profiles.active=test' package
```

The test profile uses disposable H2 and requires no local MySQL. Integration tests exercise real Basic authentication, CSRF, HTTP validation, ownership, persistence, payment results/replays, notification filtering and moderation. Existing entity-to-SQL table-name differences are left unchanged for their owners; MySQL integration is not verified. A separate profile test proves demo settlement is unavailable without `demo`.

## Integration prerequisites owned by other members

This contribution leaves ProductImageController, all existing User/Role/Product/Category/Review entities, OrderRepository, Helper and their tests unchanged. The original ProductImageController imports and injects nonexistent ProductImageService; its owner needs to use the existing IProductImageService interface. Until that independent fix is merged, a clean whole-project Maven build fails before Backend 3 tests run.

The existing Java entities use table names that differ from the supplied SQL, and Product.condition needs quoting for MySQL. Their owners need to resolve these before testing against the supplied database. Supabase identity and Java BCrypt accounts also require a team integration decision.

The only existing shared build file changed is pom.xml: Spring Security and its test dependency support the new routes, and H2 runtime supports the isolated demo. No teammate source files outside the four assigned feature families are modified. Payment order locking uses EntityManager without changing OrderRepository; successful simulated payment still updates the existing order through its existing factory/repository contract.

Scope audit verification: in a separate verification copy with only the ProductImage compilation prerequisite corrected, all 13 Backend 3 tests pass. Across the full 29-test suite, 27 pass; the unchanged UserFactoryTest and HelperTest fail on the original multi-level email validation bug. Neither the controller correction nor the Helper correction is included in this PR. The earlier 30-test/Java 17 CI result applies to the broader revision, not this scoped revision.

