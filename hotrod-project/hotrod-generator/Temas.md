# Agenda

Éstos son los temas a conversar.

## 1. Nombres para el abstract VO y VO 

Con el fin de unificar:

- Configuración.
- Documentación.
- Mensajería de info y errores.

Vamos a usar:
- Layout (para la estructura re/generada)
- Model, para el objeto al que se le puede agregar comportamiento y propiedades extras.

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

Va a cambiar a:

```xml
<hotrod>
  <generators>
     <jdbc base-dir="src/main/java" 
           package="mi.persistencia"
           qualifier="">
       <dao prefix="" suffix="" base-dir="" sub-package="" />
       <layout prefix="" suffix="" base-dir="" sub-package="" />
       <model prefix="" suffix="" base-dir="" sub-package="" />
     </jdbc>
   </generators>
</hotrod>
```

## 3. Estructura de Packages

N/A

## 4. Optimistic Locking (OL)

En 4.x:

```java
accountDAO.update(Account a); // update by PK
accountDAO.update(Account a, Account example); // update by example
accountDAO.update(Account a, Table t, Predicate predicate); // update by example
accountDAO.update(Account a); // update by PK - OL with VERSION NUMBER

accountDAO.delete(Integer id); // delete by PK
accountDAO.delete(integer id, Account example); // delete by example
accountDAO.delete(Table t, Predicate predicate); // delete by criteria
accountDAO.delete(Integer id); // delete by PK - OL with VERSION NUMBER
```

En 5.x:

```java
accountDAO.update(Account a); // update by PK
accountDAO.update(Account a, Account example); // update by example
accountDAO.update(Account a, Table t, BooleanExpression predicate); // update by example
accountDAO.update(Account a, Account baseline); // update by PK - OL with VERSION NUMBER
accountDAO.update(Account a, Account baseline); // update by PK - OL with TIMESTAMP
accountDAO.update(Account a, Account baseline); // update by PK - OL with FULL ROW CHECK

accountDAO.delete(Account id); // delete by PK
accountDAO.delete(Integer id, Account example); // delete by example
accountDAO.delete(Table t, BooleanExpression predicate); // delete by criteria
accountDAO.delete(Integer id, Account baseline); // delete by PK - OL with VERSION NUMBER
accountDAO.delete(Integer id, Account baseline); // delete by PK - OL with TIMESTAMP
accountDAO.delete(Integer id, Account baseline); // delete by PK - OL with FULL ROW CHECK
```

