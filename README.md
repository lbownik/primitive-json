# A minimalistic JSON parser and generator.

The library represents JSON as
* 	java.util.HashMap,
* 	java.util.ArrayList,
* 	java.lang.String,
* 	java.lang.Long,
* 	java.lang.Double,   
* 	java.lang.Boolean,
* 	null

Just include [Parser.java](https://github.com/lbownik/primitive-json/blob/master/src/primitive/json/Parser.java) 
or [Generator.java](https://github.com/lbownik/primitive-json/blob/master/src/primitive/json/Generator.java) 
into Your project and use `new Parser().parse(...)` or `new Ganaretor().generate(...)`.

The library is optimized to avoid unnecessary object allocation.
