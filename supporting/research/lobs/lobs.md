# LOBs in CRUD

This initial analysis considers how CRUD could stream LOBs. It does not address LiveSQL nor Nitro.

In the examples we consider an EMPLOYEE table created as:

```sql
CREATE TABLE employee (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR(20),
  photo BLOB,
  document BLOB
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
EmployeeLayout example = new EmployeeLayout();
example.setBranch(52);
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
  Employee inserted = this.employeeDAO.insertByExampleStreaming(emp);
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
EmployeeLayout values = new EmployeeLayout();
byte[] photo = ...;
byte[] document = ...;
values.setPhoto(photo);
values.setDocument(document);
int count = this.employeeDAO.update(example, values);
```

With streaming:

```java
EmployeeLayout example = new EmployeeLayout();
example.setBranch(52);
EmployeeStreaming values = new EmployeeStreaming();
try (
    InputStream isPhoto = ...;
    InputStream isDocument = ...;
  ) {
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

## 9. Delete By Primary Key

Similar to Update by Primary Key

## 10. Delete By Example

Similar to Update by Example

## 11. Delete By Criteria

Similar to Update by Criteria


