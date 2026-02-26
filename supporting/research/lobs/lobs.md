# Streaming LOBs in CRUD

This initial analysis considers how CRUD could stream LOBs.

Considerations:

- It does not address LiveSQL streaming of LOBs; this needs to be considered separately
- It does not address Nitro streaming of LOBs; this needs to be considered separately
- It should consider all types of SQL types related to BLOBs. Namely: BLOB, BINARY, VARBINARY, VARCHAR FOR BIT DATA, BYTEA, IMAGE, SMALLBLOB, MEDIUMBLOB, LONGBLOB, RAW, LONG RAW. This list may be incomplete
- It should consider all types of SQL types related to CLOBs. Namely: CLOB, NCLOB, DBCLOB, TEXT, NTEXT, SMALLTEXT, MEDIUMTEXT, LONGTEXT, GRAPHIC, VARGRAPHIC. This list may be incomplete
- When streaming, LOBs can be sent to the database or received from it:
    - LOBs can be sent to the database to provide data for insertion or as part of the search criteria. In these cases the opening and closing of streams is fully managed by your app
    - LOBs can be received from the database in SELECT queries. In these cases the JDBC driver/database opens the data stream on your behalf, and your application can only use it (and maybe close it). Data streams opened by the JDBC driver/database only live while the row is being read. Therefore, they can only be used as Cursor&lt;Model>, but not as List&lt;Model>. As soon as you move to the second row the data stream of the first row is lost/closed
- LOB streaming is mutually exclusive with the Full Row Check streategy of Optimistic Locking. Use one or the other. The Version Number &amp; Timestamp Optimistic Locking strategies can still be combined with streaming
- PostgreSQL requires the query to be demarcated in a transaction to stream LOBs. If there's no active transaction the query will crash
- The initial solution shown below considers creating a separate *model*/*layout* class(es), such as `EmployeeStreaming` in addition to the typical model `Employee`. This is up for discussion and is not set in stone. This is just an initial strategy suitable to open the discussion
- The `EmployeeStreaming` class differs to the typical `Employee` class in the implementation of the LOBs. It defines the LOB members as `InputStream` or `Reader` instead of `byte[]` or `String`

In the examples we consider an EMPLOYEE table created as:

```sql
CREATE TABLE employee (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR(20),
  photo BLOB,
  document BLOB -- change this to CLOB to demo a java.io.Reader
);
```

## 1. Select By Primary Key

With no streaming:

```java
Employee emp = this.employeeDAO.select(1054);
int id = emp.getId();
String name = emp.getName();
byte[] photo = emp.getPhoto();
byte[] document = emp.getDocument();
```

With streaming:

```java
try (EmployeeStreaming emp = this.employeeDAO.selectForStreaming(1054)) {
  int id = emp.getId();
  String name = emp.getName();
  try (InputStream is = emp.getPhoto()) {
    // Do something with the photo
  }
  try (InputStream is = emp.getDocument()) {
    // Do something with the document
  }
}
```

## 2. Select By Example

With no streaming:

```java
EmployeeLayout example = new EmployeeLayout();
example.setBranch(52);
byte[] searchedPhoto = ...;
example.setPhoto(searchedPhoto);
List<Employee> emps = this.employeeDAO.select(example);
for (Employee e : emps) {
  int id = emp.getId();
  String name = emp.getName();
  byte[] photo = emp.getPhoto();
  byte[] document = emp.getDocument();
}
```

With streaming:

```java
try (InputStream isSearchedPhoto = ...) {
  EmployeeStreaming example = new EmployeeStreaming();
  example.setBranch(52);
  example.setPhoto(isSearchedPhoto);
  try (Cursor<EmployeeStreaming> emps = this.employeeDAO.selectForStreaming(example)) {
    for (Employee emp : emps) {
      int id = emp.getId();
      String name = emp.getName();
      try (InputStream is = emp.getPhoto()) {
        // Do something with the photo
      }
      try (InputStream is = emp.getDocument()) {
        // Do something with the document
      }
    }
  }
}
```

## 3. Select By Criteria

With no streaming:

```java
EmployeeTable t = this.employeeDAO.newTable();
List<Employee> emps = this.employeeDAO.select(t, t.branchId.eq(52)).execute();
for (Employee e : emps) {
  int id = emp.getId();
  String name = emp.getName();
  byte[] photo = emp.getPhoto();
  byte[] document = emp.getDocument();
}
```

With streaming:

```java
EmployeeTable t = this.employeeDAO.newTable();
try (Cursor<EmployeeStreaming> emps = this.employeeDAO.selectForStreaming(t, t.branchId.eq(52)).execute()) {
  for (EmployeeStreaming emp : emps) {
    int id = emp.getId();
    String name = emp.getName();
    try (InputStream is = emp.getPhoto()) {
      // Do something with the photo
    }
    try (InputStream is = emp.getDocument()) {
      // Do something with the document
    }
  }
}
```

**Note**: We exclude straming in the search criteria for now, until LiveSQL implements that logic.

## 4. Insert

With no streaming:

```java
EmployeeLayout emp = new EmployeeLayout();
emp.setName("Johnny");
byte[] photo = ...;
byte[] document = ...;
emp.setPhoto(photo);
emp.setDocument(document);
Employee inserted = this.employeeDAO.insert(emp);
```

With streaming:

```java
EmployeeStreaming emp = new EmployeeStreaming();
emp.setName("Johnny");
try (
    InputStream isPhoto = ...;
    InputStream isDocument = ...;
  ) {
  emp.setPhoto(isPhoto);
  emp.setDocument(isDocument);
  Employee inserted = this.employeeDAO.insertStreaming(emp);
}
```


## 5. Insert By Example

With no streaming:

```java
EmployeeLayout emp = new EmployeeLayout();
// emp.setName("Johnny"); // we exclude the column 'name'
byte[] photo = ...;
byte[] document = ...;
emp.setPhoto(photo);
emp.setDocument(document);
Employee inserted = this.employeeDAO.insert(emp);
```

With streaming:

```java
EmployeeStreaming emp = new EmployeeStreaming();
// emp.setName("Johnny"); // we exclude the column 'name'
try (
    InputStream isPhoto = ...;
    InputStream isDocument = ...;
  ) {
  emp.setPhoto(isPhoto);
  emp.setDocument(isDocument);
  Employee inserted = this.employeeDAO.insertStreaming(emp);
}
```

## 6. Update By Primary Key

With no streaming:

```java
Employee emp = this.employeeDAO.select(1054);
byte[] photo = ...;
byte[] document = ...;
emp.setPhoto(photo);
emp.setDocument(document);
int count = this.employeeDAO.update(emp);
```

With streaming:

```java
try (EmployeeStreaming emp = this.employeeDAO.selectForStreaming(154)) {
  try (
      InputStream isPhoto = ...;
      InputStream isDocument = ...;
    ) {
    emp.setPhoto(isPhoto);
    emp.setDocument(isDocument);
    int count = this.employeeDAO.updateStreaming(emp);
  }
}
```

## 7. Update By Example

With no streaming:

```java
EmployeeLayout example = new EmployeeLayout();
example.setBranch(52);
byte[] searchedPhoto = ...;
example.setPhoto(searchedPhoto);
EmployeeLayout values = new EmployeeLayout();
byte[] photo = ...;
byte[] document = ...;
values.setPhoto(photo);
values.setDocument(document);
int count = this.employeeDAO.update(example, values);
```

With streaming:

```java
try (
    InputStream isSearchedPhoto = ...;
    InputStream isPhoto = ...;
    InputStream isDocument = ...;
  ) {
  EmployeeStreaming example = new EmployeeStreaming();
  example.setBranch(52);
  example.setPhoto(isSearchedPhoto);
  EmployeeStreaming values = new EmployeeStreaming();
  values.setPhoto(isPhoto);
  values.setDocument(isDocument);
  int count = this.employeeDAO.updateStreaming(example, values);
}
```

## 8. Update By Criteria

With no streaming:

```java
EmployeeTable t = this.employeeDAO.newTable();
EmployeeLayout values = new EmployeeLayout();
byte[] photo = ...;
byte[] document = ...;
values.setPhoto(photo);
values.setDocument(document);
int count = this.employeeDAO.update(values, t.branchId.eq(52)).execute();
```

With streaming:

```java
EmployeeTable t = this.employeeDAO.newTable();
EmployeeStreaming values = new EmployeeStreaming();
try (
    InputStream isPhoto = ...;
    InputStream isDocument = ...;
  ) {
  values.setPhoto(isPhoto);
  values.setDocument(isDocument);
  int count = this.employeeDAO.updateStreaming(values, t.branchId.eq(52)).execute();
}
```

**Note**: We exclude streaming in the search criteria for now, until LiveSQL implements that logic.

## 9. Delete By Primary Key

With no streaming:

```java
int count = this.employeeDAO.delete(1054);
```

With streaming:

N/A, unless we use a LOB as primary key; unlikely.

## 10. Delete By Example

With no streaming:

```java
EmployeeLayout example = new EmployeeLayout();
byte[] photo = ...;
byte[] document = ...;
example.setPhoto(photo);
example.setDocument(document);
int count = this.employeeDAO.delete(example);
```

With streaming:

```java
try (
    InputStream isPhoto = ...;
    InputStream isDocument = ...;
  ) {
  EmployeeStreaming example = new EmployeeStreaming();
  example.setPhoto(isPhoto);
  example.setDocument(isDocument);
  int count = this.employeeDAO.deleteStreaming(example);
}
```

## 11. Delete By Criteria

With no streaming:

```java
EmployeeTable t = this.employeeDAO.newTable();
int count = this.employeeDAO.delete(values, t.branchId.eq(52)).execute();
```

With streaming:

N/A

**Note**: We exclude streaming in the search criteria for now, until LiveSQL implements that logic.
