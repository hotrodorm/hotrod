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
style B fill:#fcf8d2

B --> F[3. Database Loader];
G[(Live Database)] --> F;
style G fill:#fcf8d2
C[2. Configuration Loader] --> D([HotRod Configuration]);
style D fill:#fcf8d2
D --> F;
F --> E([Database Structure]);
style E fill:#fcf8d2

E --> H[4. Meta Data Compiler];
D --> H;
H --> I([Meta Data]);
style I fill:#fcf8d2

D --> J[7. Generator Factory];
J --> K[8. Generator];
I --> K;
K --> L([Persistence Layer]);
style L fill:#fcf8d2

M[5. Database Dialect Factory] --> N[6. Database Dialect];
N --> K;

```

