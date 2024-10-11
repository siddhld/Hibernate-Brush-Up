# Hibernate BrushUp Questions

## 1. What is Hibernate and why is it used?

Hibernate is an Object-Relational Mapping (ORM) tool for Java. It's used to simplify database operations in Java applications.

Why use it:
- Reduces boilerplate code for database operations
- Manages database connections
- Provides caching mechanisms for better performance
- Allows working with Java objects instead of SQL queries

Example:
Instead of writing SQL:
```sql
INSERT INTO users (name, email) VALUES ('John', 'john@example.com');
```

With Hibernate, you can do:
```java
User user = new User("John", "john@example.com");
session.save(user);
```

## 2. What is the difference between get() and load() methods in Hibernate?

Both methods retrieve an object from the database, but they behave differently:

get():
- Hits the database immediately
- Returns null if the object is not found
- Always returns a fully initialized object

load():
- Returns a proxy object without hitting the database
- Throws an ObjectNotFoundException if the object doesn't exist when you try to use it
- Can return a proxy (lazy-loaded) object

When to use:
- Use get() when you need to make sure the object exists
- Use load() when you're certain the object exists and want to use it as a reference

Example:
```java
// Using get()
User user1 = session.get(User.class, 1);  // Hits the database immediately

// Using load()
User user2 = session.load(User.class, 2);  // Doesn't hit the database yet
user2.getName();  // Now it hits the database
```

## 3. What is the difference between Hibernate's save() and persist() methods?

Both methods are used to save a new entity, but they have some differences:

save():
- Returns the generated identifier immediately
- Can be used outside of a transaction
- May execute an INSERT statement immediately

persist():
- Doesn't return anything
- Should be used inside a transaction
- Guarantees that it will not execute an INSERT statement if it is called outside of transaction boundaries

When to use:
- Use save() when you need the identifier immediately
- Use persist() when working within a transaction and following JPA standards

Example:
```java
// Using save()
Long id = (Long) session.save(newUser);  // Returns the id immediately

// Using persist()
session.persist(newUser);  // Doesn't return anything
```

## 4. What is the Hibernate Session and SessionFactory?

SessionFactory:
- A thread-safe object that creates Session objects
- Usually created once for the whole application
- Expensive to create, so it's typically created during application startup

Session:
- Represents a single-threaded unit of work
- Used to get a physical connection with the database
- Short-lived object, created and destroyed as needed

When to use:
- Create one SessionFactory for your application
- Create a new Session for each database operation or unit of work

Example:
```java
// Creating SessionFactory (do this once)
SessionFactory factory = new Configuration().configure().buildSessionFactory();

// Creating a Session (do this for each unit of work)
Session session = factory.openSession();
try {
    // Use the session here
    User user = session.get(User.class, 1);
} finally {
    session.close();
}
```

## 5. What is lazy loading in Hibernate?

Lazy loading is a design pattern used in Hibernate where associated data is loaded from the database only when it's first accessed.

Why use it:
- Improves performance by loading data only when needed
- Reduces memory usage

When to use:
- When you have associations that are not always needed
- In scenarios where loading all associated data upfront would be inefficient

Example:
```java
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;
}

// Later in your code
User user = session.get(User.class, 1);
// Orders are not loaded yet
List<Order> orders = user.getOrders();  // Now orders are loaded
```

## 6. What is the difference between first-level cache and second-level cache in Hibernate?

First-level cache:
- Associated with the Session object
- Enabled by default
- Cannot be shared between sessions

Second-level cache:
- Associated with the SessionFactory
- Not enabled by default
- Can be shared across sessions
- Typically used for data that doesn't change often

When to use:
- First-level cache is always used (per session)
- Use second-level cache for frequently accessed, rarely changing data

Example:
```java
// First-level cache (automatic)
User user1 = session.get(User.class, 1);  // Hits the database
User user2 = session.get(User.class, 1);  // Retrieves from cache

// Second-level cache (needs configuration)
// In hibernate.cfg.xml
<property name="hibernate.cache.use_second_level_cache">true</property>
<property name="hibernate.cache.region.factory_class">
    org.hibernate.cache.ehcache.EhCacheRegionFactory
</property>

// In your entity class
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    // ...
}
```

## 7. What is the N+1 problem in Hibernate and how can it be solved?

The N+1 problem occurs when you fetch a list of N entities, and then Hibernate executes N additional queries to fetch a related entity for each of the N entities.

Why it's a problem:
- It can significantly impact performance due to the large number of database queries

How to solve it:
1. Use fetch joins
2. Use @BatchSize annotation
3. Use the @Fetch annotation with FetchMode.SUBSELECT

Example solving with fetch join:
```java
// Instead of this (which causes N+1 problem)
List<User> users = session.createQuery("from User").list();
for (User user : users) {
    System.out.println(user.getOrders().size());  // Causes N additional queries
}

// Use this (solves N+1 problem)
List<User> users = session.createQuery(
    "from User u left join fetch u.orders").list();
for (User user : users) {
    System.out.println(user.getOrders().size());  // No additional queries
}
```



# IMPORTANT

# Hibernate Key Concepts

## SessionFactory
- For each database, there would be a SessionFactory.
- It acts like a factory that manufactures Sessions.
- SessionFactory will only be created once the first request comes and will be alive until the application ends.
- It is a heavyweight object, so we only create it once; otherwise, it will affect the application's performance.
- Should be Singleton.

## Session
- It is generated by SessionFactory.
- Used to perform CRUD operations.

## session.get() vs session.load()
- `get()`:
  * Uses eager loading to load the object from DB immediately.
- `load()`:
  * Uses lazy loading.
  * Assigns a Proxy object (Uninitialized object).
  * When we access the object's properties, it hits the DB to access the object.

## session.save() vs session.persist()
- `save()`:
  * Immediately inserts data into the database.
  * Can be used outside a transaction.
  * DEPRECATED after Hibernate 6.0, so use `persist()` instead.
- `persist()`:
  * Marks an entity for insertion.
  * The actual save typically occurs when the transaction is committed.

## Transactions
Whenever we are adding, updating, or deleting data in a DB table, it should be covered under a Transaction, or else it won't reflect in the DB table.

## DataSource
In programming, especially with databases, a DataSource is an object that:
- Provides a way to connect to a database or other data storage.
- Manages a pool of database connections.
- Offers these connections to your application when needed.

Note: SessionFactory is equivalent to DATASOURCE in this context.
