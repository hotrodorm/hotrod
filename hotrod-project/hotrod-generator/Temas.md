# Agenda

Éstos son los temas a conversar.

## 1. Nombres para el abstract VO y VO 

Puede ser:

   Prototype + Model
             + Entity
             + Domain
             + VO
             + otro?

## 2. Tags de configuración

Actualmente:

```xml
<hotrod>
  <generators>
     <mybatis-spring>
       <daos
         base-dir="src/main/java" package="mi.persistencia" primitives-package="primitives"
         dao-prefix="" dao-suffix=""
         abstract-vo-prefix="" abstract-vo-suffix=""
         vo-prefix="" vo-suffix=""
         ndao-prefix="" ndao-suffix=""
         nabstract-vo-prefix="" nabstract-vo-suffix=""
         nvo-prefix="" nvo-suffix=""
         bean-qualifier=""
       />
```

Podría cambiar a:

```xml
<hotrod>
  <generators>
     <jdbc base-dir="src/main/java" 
           package="mi.persistencia" 
           primitives-package="primitives" 
           beans-qualifier="">
       <dao prefix="" suffix="" />
       <prototype prefix="" suffix="" />
       <model prefix="" suffix="" />
       <nitro-dao prefix="" suffix="" />
```

## 3. Estructura de Packages

Actualmente:

```
   src/main/java/<package>/CuentaVO.java
                          /primitives/CuentaDAO.java
                          /primitives/CuentaPrototype.java
   src/main/java/<package>/<fragment-package>/ClienteVO.java
                                             /primitives/ClienteDAO.java
                                             /primitives/ClientePrototype.java
```

Tal vez:

```
   src/main/java/<package>/CuentaVO.java
   src/main/java/<package>/<fragment-package>/ClienteVO.java

   src/main/java/<package>/primitives/CuentaDAO.java
                                     /CuentaPrototype.java
                                     /<fragment-package>/ClienteDAO.java
                                     /<fragment-package>/ClientePrototype.java
```

## 4. 