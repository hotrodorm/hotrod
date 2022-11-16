# The CRUD Module

The CRUD module is the foundation infrastructure for all HotRod modules. CRUD inspects the database and generates a persistent
layer that mimics the database structure.

Apart from including a layer of value objects (VOs) that represent tables and views, the CRUD module also generates data access 
objects (DAOs) with the basic persistence operations, such as `SELECT`, `INSERT`, `UPDATE`, and `DELETE`.

The main goal of the CRUD module is to provide the developer with a fully functional persistence layer that can be used in minutes
to start prototyping an application. The developer needs to provide minimal configuration for HotRod to inspect the database and
generate fully functional DAOs and VOs.

Finally, the DAOs and VOs provides the basic data model that is used by the LiveSQL and Nitro modules.


## The Value Objects


## The DAOs






