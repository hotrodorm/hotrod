# The Internal Architecture of Generator

To make the generator highly configurable the meta data is compiled first from multiple sources. Namely:

- Runtime Properties
- Live Database
- HotRod Layer Configuration

Then:

1. Once the meta data is compiled and validated, the dialect is prepared for the specific database.
2. Then, the specific generator is instantiated and fed with the meta data and the dialect.
3. Finally, with all those moving parts in place, the generator produces the persistence layer.

This can be visualized as:

```mermaid
graph TD;

A[1. Properties Loader] --> B([Runtime Properties]);

B --> F[2. Database Loader];
G[(Live Database)] --> F;
C[3. Configuration Loader] --> D([HotRod Configuration]);
F --> E([Database Structure]);

E --> H[4. Meta Data Compiler];
D --> H;
H --> I([Meta Data]);

D --> J[7. Generator Factory];
J --> K[8. Generator];
I --> K;
K --> L([Persistence Layer]);

M[5. Database Dialect Factory] --> N[6. Database Dialect];
N --> K;

```

