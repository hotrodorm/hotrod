# The Internal Architecture of Generator

To make the Generator is highly configurable the metadata comes from multiple 


```mermaid
graph TD;

A[1. Properties Loader] --> B([Runtime Properties]);

B --> F[6. Database Loader];
G[(Live Database)] --> F;
C[3. Configuration Loader] --> D([HotRod Configuration]);
F --> E([Database Structure]);

E --> H[7. Meta Data Compiler];
D --> H;
H --> I([Meta Data]);

D --> J[4. Generator Factory];
J --> K[5. Generator];
I --> K;
L([Persistence Layer]) --> K;

M[2. Database Adapter Factory] --> N[8. Database Adapter];
N --> K;

```

