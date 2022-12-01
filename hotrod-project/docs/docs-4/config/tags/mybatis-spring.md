# The `<mybatis-spring>` Tag

This tag can include one or more generators. At the time of this writing only one generator remains active: the MyBatis-Spring generator. Other
generators have been deprecated.

It can optionally include any of the following tags, in the order shown below:

- A `<daos>` tag to configure the details of the generated Java classes, such as base dir, packages, prefixed, and suffixes.
- A `<mappers>` tag to configure the details of the generated mapper files, such as base dir and folder.
- A `<select-generation>` tag to configure the SQL Processor for Nitro queries. It's recommended to include this tag.
- A `<classic-fk-generation>` tag to activate the CRUD foreign key navigation methods. It's recommended to include this tag.
- One or more `<property>` tags to set up internal details of the generator engine.
