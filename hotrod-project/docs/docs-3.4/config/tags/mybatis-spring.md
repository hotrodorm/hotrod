# The `<mybatis-spring>` Tag

This tag can include one or more generators. At the time of this writing only one generator remains active: the MyBatis-Spring generator. Other
generators have been deprecated.

It can optionally include any of the following tags, in the order shown below:

- A `<daos>` tag to configure the details of the generated Java classes, such as base dir, packages, prefixed, and suffixes.
- An optional `<mappers>` tag to configure the location of the generated mapper files.
- An optional `<select-generation>` tag to configure the SQL Processor for Nitro queries.
- One or more `<property>` tags to set up internal details of the generator engine.
