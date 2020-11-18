# hotrod.xml : name-solver tag

## Reference Documentation
El objetivo de este tag es permitir establecer reglas de derivación de nombres en Java a partir de nobres de objetos de base de datos a partir de los cuales los primeros se derivan.
Actualmente, estas reglas sólo afectan a objetos de base de datos del tipo 'table' o sus 'atributes', además del nombre de objetos 'view'.


>**tag**: *name-solver*</br>
**parent tag**: *hotrod*</br>
**attributes**: -none-</br>
**subtags**: *when*</br>
**description**: Agrupa todas las reglas de transformación de nombres de objetos especificadas en una lista de subtags "when". Estas reglas utilizan expresiones regulares (regex) para la detección y transformación de nombres. En el caso de que más de una regla "when" aplique a un mismo objeto, sólo se aplicará la primera de ellas, considerando el orden en que estén especificadas dentro del tag XML.


>**tag**: *when*</br>
**parent tag**: *name-solver*</br>
**attributes**: *match*, *replace*, *scope*</br>
**subtags**: -none-</br>
**description**: Define una regla de transformación de nombre de objeto de base de datos a su correspondiente en el código Java generado.</br>
Los atributos son:</br>
>>*match*: corresponde a la expresión regular (regex) a evaluar para un nombre de objeto de base de datos.</br>
*replace*: corresponde a la regla de transformación del nombre de objeto de base de datos, en el escenario en que la expresión especificada en el atributo "match" resulte ser válida para dicho nombre. Esta expresión puede contener elementos derivados de la expresión regular aplicada o ser una constante (ver ejemplos más abajo).</br>
*scope*: dominio de objetos en que la regla será probada. Los valores posibles de este atributo son: "table", "column" y "view", o cualquier combinación de ellos (ver ejemplos más abajo). </br>

Ejemplos:

	<name-solver>
		<when match="^CLI_(.+)$" replace="$1" scope="column">
		<when match="^(.+)_TBL$" replace="$1" scope="table", "view"">
		<when match="^\w{3,3}_(\w+)_CLI$" replace="$1" scope="table,column">
		<when match="^CLI_(\w.+)_(\w+)_\w{2,3}$" replace="$1$2" scope=""table,column,view"">
	</name-solver>



match| replace | input | output</br>
^CLI_(\w.+)_(\w+)_\w{2,3}$	$1$2	CLI_otra_cosiaca_ATT	otracosiaca</br>
^CLI_(\w.+)_(\w+)_\w{2,3}$	$2	CLI_otra_cosiaca_AT	cosiaca</br>
^CLI_(\w.+)_(\w+)_\w{2,3}$	$1$2	CLI_otra_cosiaca_A	CLI_otra_cosiaca_A</br>

