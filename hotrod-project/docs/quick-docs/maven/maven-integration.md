# Maven Integration

HotRod can be executed from Maven. It provides four goals:

<table>
  <tr><th style="text-align: left;">Goal</th><th style="text-align: left;">Description</th>
  <tr><td>hotrod:gen</td><td>Main goal that generates all codes.</td></tr>
  <tr><td>hotrod:purge</td><td>Cleans up temporary database views that may have remaines after an execution error.</td></tr>
  <tr><td>hotrod:export-columns-txt</td><td>Generates a TXT report of all columns from tables and views on the
          database schema. Quite useful to write the &lt;type-solver> logic.</td></tr>
  <tr><td>hotrod:export-columns-xlsx</td><td>Generates an XLSX report of all columns from tables and views on the
          database schema. Quite useful to write the &lt;type-solver> logic.</td></tr>
</table>



 
 