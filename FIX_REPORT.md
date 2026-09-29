# Campus Facility Booking — Project Fix Report

**Prepared by:** Milani Sani (230371574)
**Date:** 23 September 2026

This document lists every problem found in the project and how each was fixed.
Nothing here changes anyone's design decisions — these are correctness fixes
so the application actually compiles, runs, and meets the assignment brief.

---

## Summary

| # | Problem | Severity | Files touched |
|---|---|---|---|
| 1 | Builder Pattern missing from 12 of 16 entities | **Critical (marks)** | 12 domain + 12 factory |
| 2 | No `getAll()` anywhere — every list page was dead | **Critical (runtime)** | 1 + 16 + 16 |
| 3 | `/time-slot` vs `/timeslot` URL typo | High | `booking.js` |
| 4 | `invoice.js` called an endpoint that didn't exist | High | `Invoice.java`, `InvoiceController`, `invoice.js` |
| 5 | Passwords printed to console in plain text | **Security** | `LoginServiceImpl` |
| 6 | 3 pages loaded a `.js` file that didn't exist | High | 3 new JS files |
| 7 | 5 pages linked in the nav didn't exist | Medium | 3 new pages + 3 link fixes |
| 8 | `InvoiceFactory` argument order was wrong | High | `InvoiceFactory.java` |

---

## 1. Builder Pattern restored (12 entities)

**The problem.** The Domain milestone brief says entities must be built using
the Builder Pattern. Only `Booking`, `Department`, `Notification` and `Staff`
still had one. The other 12 had been rewritten to use plain public
constructors:

```java
// what was there
public User(String userId, String firstName, ...) { ... }
```

**The fix.** Every entity now has a nested `Builder` using the `setXxx()`
naming already used by `Booking`, `Department`, `Notification` and `Staff`,
so the whole codebase is consistent:

```java
public static class Builder {
    private String userId;
    ...
    public Builder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public User build() {
        return new User(this);
    }
}
```

The existing public constructors were **left in place**, so nothing that
already called them breaks.

**Entities fixed:** Address, Contact, Equipment, Facility, Invoice, Login,
MaintenanceRequest, Register, Student, TimeSlot, User, UserRole.

**Factories fixed.** A Builder that nothing uses doesn't demonstrate the
pattern, so all 12 matching factories were switched over:

```java
// before
return new User(userId, firstName, lastName, email, dateOfBirth, departmentId);

// after
return new User.Builder()
        .setUserId(userId)
        .setFirstName(firstName)
        .setLastName(lastName)
        .setEmail(email)
        .setDateOfBirth(dateOfBirth)
        .setDepartmentId(departmentId)
        .build();
```

All validation rules inside the factories were left exactly as they were.

---

## 2. `getAll()` added across the stack

**The problem.** `IService` only had `create`, `read`, `update`, `delete`.
There was no way to fetch a list of anything. But the frontend calls
`/booking/all`, `/user/all`, `/facility/all`, `/timeslot/all` and
`/invoice/all` — all of which returned **404**.

The practical effect: the bookings table, the invoices table, and every
dropdown (user, facility, time slot, booking) were permanently empty. You
could not create a booking through the UI at all.

**The fix — three layers.**

`IService.java`:
```java
public interface IService<T, ID> {

    java.util.List<T> getAll();

    T create(T t);
    T read(ID id);
    T update(T t);
    boolean delete(ID id);
}
```

Every `ServiceImpl`:
```java
@Override
public List<Booking> getAll() {
    return repository.findAll();
}
```

Every controller:
```java
@GetMapping("/all")
public ResponseEntity<List<Booking>> getAll() {
    return ResponseEntity.ok(service.getAll());
}
```

`LoginServiceImpl` holds four repositories, so its `getAll()` explicitly uses
`loginRepository.findAll()`.

Backend endpoints went from 66 to 83.

---

## 3. Time slot URL typo

`booking.js` line 16:

```js
// before — no such endpoint
const TIME_SLOT_API = `${API_BASE_URL}/time-slot`;

// after — matches @RequestMapping("/timeslot")
const TIME_SLOT_API = `${API_BASE_URL}/timeslot`;
```

---

## 4. Invoice "mark as paid" had nothing behind it

**The problem.** `invoice.js` had a tick button calling
`PUT /invoice/update-status/{id}?status=paid`. That endpoint did not exist,
and more fundamentally `Invoice` had **no paid field at all**, so there was
nothing to update.

**The fix.** Rather than delete the feature, the field was added (the original
UML did include `paid: boolean`):

- `Invoice.java` — added `private boolean paid = false;`, its getter/setter,
  the Builder field and `setPaid()`
- `InvoiceController` — added `PUT /invoice/mark-paid/{id}`
- `invoice.js` — now calls `/invoice/mark-paid/{id}`

The `InvoiceFactory` signature was **not** changed, so existing tests still
compile; `paid` simply defaults to `false` on creation.

---

## 5. Passwords logged in plain text

`LoginServiceImpl.authenticate()` contained:

```java
System.out.println("Password: " + password);
```

Any password typed into the app was printed to the server console. All 20
debug `System.out.println` lines were removed from `src/main`.

**Still outstanding (see below):** passwords are stored and compared in plain
text in the database. That is a design decision for the group to make.

---

## 6. Three pages loaded JavaScript that didn't exist

`facility.html`, `user.html` and `student-dashboard.html` each had a
`<script src="...">` pointing at a file that was never committed. The pages
rendered but every button did nothing.

Created:

- **`facility.js`** — create / read / update / delete wired to `/facility/*`
- **`user.js`** — create / read / update / delete / clear wired to `/user/*`
- **`student-dashboard.js`** — loads the signed-in user, populates the four
  statistic cards from `/booking/all`, `/facility/all`, `/notification/all`

All three use the DOM element IDs that were already in the HTML, so no HTML
changes were needed.

**Note:** `student-dashboard.js` reads `sessionStorage.getItem("loggedInUser")`
— the same key `script.js` writes on login.

---

## 7. Dead navigation links

Five pages were linked from the sidebar but never existed.

| Link | Fix |
|---|---|
| `equipment.html` | **Created** (+ `.css`, `.js`) — full CRUD on `/equipment` |
| `maintenance.html` | **Created** (+ `.css`, `.js`) — full CRUD on `/maintenancerequest` |
| `login.html` | **Created** — clears the session and redirects to `index.html` (the sign-in form lives in `index.html`, so the Logout button now works) |
| `student-booking.html` | Relinked to existing `booking.html` |
| `student-facilities.html` | Relinked to existing `facility.html` |
| `student-notifications.html` | Relinked to existing `notification.html` |

`equipment.css` and `maintenance.css` are derived from `booking.css` so the
styling is identical — only the class prefixes and the status badge colours
differ.

---

## 8. `InvoiceFactory` argument order

`Invoice` declares its fields in the order
`invoiceId, amount, issueDate, dueDate, booking`, but its constructor takes
them as `invoiceId, booking, amount, issueDate, dueDate`.

The factory was rewritten to map each argument to the correct setter:

```java
return new Invoice.Builder()
        .setInvoiceId(invoiceId)
        .setBooking(booking)
        .setAmount(amount)
        .setIssueDate(issueDate)
        .setDueDate(dueDate)
        .build();
```

---

## Verification performed

| Check | Result |
|---|---|
| Entities with Builder | 16 / 16 |
| Factories using Builder | 16 / 16 |
| Service impls with `getAll()` | 16 / 16 |
| Controllers with `/all` | 16 / 16 |
| Frontend calls with no matching endpoint | 0 |
| Nav links pointing at a missing page | 0 |
| `<script>` / `<link>` references that don't resolve | 0 |
| Java files with unbalanced braces | 0 of 156 |
| JavaScript files failing `node --check` | 0 of 12 |
| `System.out.println` in `src/main` | 0 |

Every factory's Builder chain was also machine-checked to confirm each
argument lands in the matching setter.

**Not verified:** a real `mvn clean package`. Maven could not be run in the
environment used, so **someone must run the build and the test suite before
this is merged.** Everything above is static verification only.

---

## Still outstanding — group decisions needed

1. **Passwords are stored in plain text.** `LoginServiceImpl` compares them
   with `.equals()`. Hashing (e.g. BCrypt) would be the proper fix.
2. **`Register` / `Login` use `registrarId`** where the UML says `registerId`.
   Cosmetic, but a marker may notice the mismatch.
3. **Duplicate CORS configuration.** `WebConfig` sets an allow-list, while
   three controllers also carry `@CrossOrigin(origins = "*")`. Pick one.
4. **Repositories declare unused finder methods.** `UserRepository` has five;
   only `findByEmail` is used. They're harmless but dead code.
5. **`Invoice` holds a `Booking` object** while every other entity stores a
   plain ID string. It works, but it's inconsistent and makes the JSON heavier.
6. **`equipment.js` and `maintenance.js` are new** — please review them as you
   would any pull request rather than assuming they're correct.
