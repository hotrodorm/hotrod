# Hotrod Version History

## 3.0.0
- HotRod released to Maven Central Repository.

## 3.0.1
- Fixing Java 9 JAXB implementation.

## 3.0.2
- Added Spring, Maven, web POC.

## 3.0.3
- Adding tar.gz packaging for Docker releases.
- Fully dockerized example: web + hotrod + docker + java11 + maven + spring.

## 3.0.4
- Fixing prefixes and suffixes for abstract VOs.
- Removed default LiveSQL preview in logging. Can be enabled by DEBUG logging level.

## 3.0.5
- Fixing structured VOs prefixes and suffixes.

## 3.1.0
- Maven Plugin released.
- Full &lt;type-solver> implemented with OGNL logic.
- TXT column export implemented.
- XLSX column export implemented.
- HotRod PURGE operation implemented to remove residual temporary views.

## 3.2.0
- Simplified hotrod.xml configuration file with many sensible default values.
- Standard archetype implemented for a Spring, Maven, MyBatis, command-line app.

## 3.2.1
- Removing "final" to VO getters and setters.
- Experimental implementation of table inheritance.

## 3.2.2
- Fixed connection exception reporting.
- Cursors implemented for selectByExample().
- AOP tested with experimental SQL metrics.

## 3.3.0
- All functionality tested with SpringBoot release and web components.
- Removed MyBatis Configuration file; replaced by mapper scanning annotations.
- Experimental SQL metrics developed.

## 3.3.1
- LiveSQL mapper is now implemented as a Java mapper; no XML file is generated anymore.
- All HotRod Spring configuration can now be made using Java annotations.
- Experimental FK navigation.

## 3.3.2
- Maintenance release.

## 3.3.3
- Implemented classic FK navigation (globally enabled or disabled).

## 3.4.0
- Classic FK navigation can now be enabled on a per-table basis.
- Cursors implemented for LiveSQL, Nitro Selects, selectByExample(), and selectByCriteria().
- New &lt;select> tag processor "result-set" offers many benefits compared to traditional "create view" processor.
- New &lt;name-solver> tag, automatically names tables, views, and columns according to regex naming rules.
- LiveSQL function infrastructure: developers can now add built-in and also user-defined functions to LiveSQL queries.
- LiveSQL dialect can be designated at runtime using application.properties, with fallback to discovery mode.
- LiveSQL fully typed SQL CASE and OVER clauses.
- LiveSQL RANGE window frame implemented for numbers (date/time are not yet supported since we don't model intervals yet).
- Fully independent Spring MyBatis generator.
- REST metrics implemented (experimental).
- Added "force-jdbc-type-on-write" attribute to <type-solver> functionality.
- Added extra prefixes and suffixes for Nitro daos and vos.

## 3.4.1
- Improving select tag messaging for SQL errors.
- Fixing default catalog/schema for nitro module.
- Adding generation output and summary line.

## 3.4.4
- Removing Log4j from runtimes (hotrod-jar and hotrod-livesql.jar).


 