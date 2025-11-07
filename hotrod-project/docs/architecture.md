# The Internal Architecture of Generator

To make the generator highly configurable the meta data is compiled first from multiple sources. Namely:

- Runtime Properties
- Live Database
- HotRod Layer Configuration

Once the meta data is compiled and validated, the dialect adapter is prepared for the specific database. Then the specific generator is instantiated and fed the meta data and the dialect adapter. With all those moving parts, the generator generates the persistence layer.

The explanation above can be visualized as:

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

M[5. Database Adapter Factory] --> N[5. Database Adapter];
N --> K;

```

