# Stream Path

Stream Path (Spath) is a Java API for filtering streams of hierarchically structured data.

The most useful application for it is to process arbitrarily large streams of XML data.
The mechanism can be applied to any structured data including json and yaml.

The syntax of Spath expressions is similar to Xpath, however since Spath works on streams it does not have the full
functionality available to Xpath. The advantage of Spath over Xpath is that it can process arbitrarily large documents.

## Spath expression syntax

Spath uses the abbreviated expression syntax from xpath.
Node selectors such as "decendant" are not supported.

|| Expression || Description ||
| nodename | Selects all elements with the name "nodename" |
|   /name       | Selects from the root node
|   //name      | Selects nodes in the document from the current node that match the selection no matter where they are |
 
Note . and .. are not supported.

## Spath predicate expressions

Spath predicates have the general form 
  [ @ attribute operator value ]

* value can be a quoted string or a decimal number
* strings are surrounded by single quotes; numbers are not
* operators "=" and "!=" may be applied to strings
* operators "=" "!=" "<" "<=" ">" and ">=" may be applied to numbers
* operator and value may be omitted.

|| Expression || Description ||
| [@name]           | Selects nodes where the attribute "name" exists |
| [@name='value']   | Selects nodes where "name" is equal to value |
| [@name!='value']  | Selects nodes where "name" is not equal to value |
| [@value=3.141]    | Selects nodes where "value" is equal to the decimal number 3.141 |
| [@value!=3.141]   | Selects nodes where "value" is not equal to the decimal number 3.141 |
| [@price<1.25]     | Selects nodes where "price" is less than the number 1.25 |
| [@price<=1.25]    | Selects nodes where "price" is less than or equal to the number 1.25 |
| [@price>1.25]     | Selects nodes where "price" is greater than the number 1.25 |
| [@price>=1.25]    | Selects nodes where "price" is greater than or equal to the number 1.25 |
| [@price>=1.00 and @price<2.00]  | Selects nodes where both conditions are true |
| [@price<1.00 or @price>=2.00]  | Selects nodes where either condition is true |
