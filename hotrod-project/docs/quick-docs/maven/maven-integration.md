# Maven Integration

HotRod can be executed from Maven. It provides four goals:

 - Generate
 - Purge
 - TXT Column Metadata Report
 - XLSX Column Metadata Report
 
# Generate

The `gen` goal is the main HotRod goal that generates all source code.

TBD

# Purge

The `purge` goal cleans up temporary database views that may have remained after an execution error.

TBD

# TXT Column Metadata Report

The `export-columns-txt` generates a TXT Column Metadata Report for tables and views in the
database schema. The properties shown in this report are essential to write custom logic in the &lt;type-solver> tags.

See [TXT Column Metadata Export](command-export-columns-txt.md) for details and example.

# XLSX Column Metadata Report

The `export-columns-xlsx` generates an XLSX Column Metadata Report for tables and views in the
database schema. The properties shown in this report are essential to write custom logic in the &lt;type-solver> tags.

This report provides the same information as the TXT report described before.
 