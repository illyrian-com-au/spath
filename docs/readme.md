# Stream Path

Stream Path (Spath) is a Java API for filtering streams of hierarchically structured data.

The most useful application for it is to process arbitrarily large streams of XML data.
The mechanism can be applied to any structured data including json and yaml.

The syntax of Spath expressions is similar to Xpath, however since Spath works on streams it does not have the full
functionality available to Xpath. The advantage of Spath over Xpath is that it can process arbitrarily large documents.

## Spath expression syntax

Spath uses the abbreviated expression syntax from xpath.
Node selectors such as "decendent" are not supported.

<table>
<tr><th> Expression </th><th> Description </th></tr>
<tr><td>   /person     </td><td> Selects the root node called person</td></tr>
<tr><td>   //name      </td><td> Selects any node called name anywhere in the document </td></tr>
<tr><td>   name        </td><td> Selects any node called "name" anywhere in the document; same as //name </td></tr>
<tr><td>   /person/name </td><td> Selects a node called "name" immediately beneath the root node called person </td></tr>
<tr><td>   //person/name </td><td> Selects a node called "name" immediately beneath a node called person anywhere in the document</td></tr>
<tr><td>   /person//name </td><td> Selects a node called "name" anywhere beneath the root node called person </td></tr>
<tr><td>   //person//name </td><td> Selects a node called "name" anywhere beneath a node called person anywhere in the document</td></tr>
</table>

Note . and .. are not supported.

## Spath predicate expressions

Spath predicates have the general form 
  [ @ attribute operator value ]

* value can be a quoted string, a boolean or a decimal number
* strings are surrounded by single quotes; booleans and numbers are not
* operators "=" and "!=" may be applied to booleans
* operators "=" "!=" "<" "<=" ">" and ">=" may be applied to strings and numbers
* operator and value may be omitted.

<table>
<tr><th> Expression </th><th> Description </th></tr>
<tr><td> [@name]           </th><th> Selects nodes where the attribute "name" exists </th></tr>
<tr><td> [@name='value']   </th><th> Selects nodes where "name" is equal to value </th></tr>
<tr><td> [@name!='value']  </th><th> Selects nodes where "name" is not equal to value </th></tr>
<tr><td> [@check=true]     </th><th> Selects nodes where "check" is equal to 'true' (case insensitive)</th></tr>
<tr><td> [@check!=True]    </th><th> Selects nodes where "check" is not equal to 'true' (case insensitive)</th></tr>
<tr><td> [@check=false]    </th><th> Selects nodes where "check" is equal to 'false' (case insensitive)</th></tr>
<tr><td> [@check!=FALSE]   </th><th> Selects nodes where "check" is not equal to 'false' (case insensitive)</th></tr>
<tr><td> [@value=3.141]    </th><th> Selects nodes where "value" is equal to the decimal number 3.141 </th></tr>
<tr><td> [@value!=3.141]   </th><th> Selects nodes where "value" is not equal to the decimal number 3.141 </th></tr>
<tr><td> [@price<1.25]     </th><th> Selects nodes where "price" is less than the number 1.25 </th></tr>
<tr><td> [@price<=1.25]    </th><th> Selects nodes where "price" is less than or equal to the number 1.25 </th></tr>
<tr><td> [@price>1.25]     </th><th> Selects nodes where "price" is greater than the number 1.25 </th></tr>
<tr><td> [@price>=1.25]    </th><th> Selects nodes where "price" is greater than or equal to the number 1.25 </th></tr>
<tr><td> [@price>=1.00 and @price<2.00] </th><th> Selects nodes where both conditions are true </th></tr>
<tr><td> [@price<1.00 or @price>=2.00]  </th><th> Selects nodes where either condition is true </th></tr>
</table>

Note that boolean comparisons are case insensitive while string comparisons are not.
