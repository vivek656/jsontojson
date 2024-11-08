This is a library that will help you map one JSON to another JSON. <Br>
Many times in our code we map one rest Api out put to our data class, which is basically mapping from one JSON to another. 
<br>
Errors happen during these mappings,
and sometimes the mappings are obvious still we need to add those codes transforming one JSON to another.<br>
This library tries to externalize these JSON to JSON mapping.
Where you provide the input to output mappings in a JSON file and library will create outJson from input JSON using the mapping json.

It may feel that code is being transferred from classes to JsonMapper.<br>
That's somewhat true, but many advantages of this approach is
1. duplication of logic is now avoided. e.g., get from x, set in y etc.
2. declaring mappings beforehand makes the code more readable. making all mappings at single place.
3. same mappings can be reused at multiple places if the input structure is used at multiple places. 

________________

# How to construct Mapper

**M**apper.json is the file that you will add that contains mapping information
how a path in input.json maps to output.json.

Suppose this is Input.json that we want to map to other JSON
```json
  {
  "address": {
    "postCode": "AXBHI",
    "line1" : "789"
  },
  "uprn" : "6789",
  "owners" : [
    {
      "name" : "viv",
      "type" : "freehold"
    }
  ]
}
```

Mapper can be like this 

```json
{
 "mappings": {
  "/address/postCode": {
   "pathToMap": "/address_postcode"
  },
  "/address/line1": {
   "pathToMap": "/address_line1"
  },
  "/uprn": {
   "pathToMap": "/uprn"
  },
  "/owners/-/name": {
   "pathToMap": "/owner/name"
  },
  "/owners/--/type": {
   "pathToMap": "/ownerTypes"
  }
 },
 "mappings_complex": [
  {
   "operationType": "function",
   "pathToMap": "/name/full_name",
   "mapping_spec": {
    "function_name": "concat",
    "args": {
     "values": [
      {
       "function_name": "uppercase",
       "args": {
        "value": { "path": "/name/fn"}
       }
      },
      { "path": "/name/mn" },
      { "path": "/name/ln" }
     ],
     "separator": " "
    }
   }
  }
 ]
}
```
in this JSON **mappings** contains all the straight forward mappings from input keys to output keys.

**Mapping to output json path**

you need to provide a key path from root
e.g. `/address_line1`add data at root.address_line1
while `/address/line1` will mean add data at ``root.address.line``.

example of one such mapping will be described here </br>
`/address/postcode` is the key path in input.json.
`pathToMap` tells the path in outputJson which is `/address_postcode` in this case

**complex** mappings is where we can do transformation on input keys before mapping it to output json path. <br>
  - **operationType** </br>
    this field tells the parser the operation that we want to perform on input json before mapping.
    Currently only `function` is supported </b>
 - **pathToMap** </br>
    with one mapping input we can map to only one output json key, specified in path to map.</br>
 - **mapping_spec** </br>
   Similar to k8s object specs, specs structure can vary based on an operation type.</br>
   As we can see specs can contain nested specs as well.
   

When we apply mapper.json to input.json we get output like this 

```json
{
  "address_postcode" : "AXBh1",
  "address_line1" : "789",
  "uprn" : "6789",
  "owner" : {
    "name" : "viv"
  },
  "ownerTypes" : [
    "freehold"
  ]
}
```
### Keys Rules
One common rule is keys mostly follows JSON Pointer convention except some 
special cases like arrays.
see the JSON pointer rfc [here](https://www.rfc-editor.org/rfc/rfc6901)

- **Simple JSON Path** </br>
path like `/address/postCode` directly matched to postcode inside address in the root folder. 

- **Matching array item** <br>
  - We can match array item by index (zero indexed)
    like `/owners/0/name` this means match key traversal like this
     ``. -> owners -> 0th element -> name``

- **Matching All array Items**
    - We can tell the parser that inside each array element grab one key and convert to a new array using `--`
        e.g.``/owners/--/type`` we are saying maps each type field inside each array item in owners
        this programmatically means ```owners.map{ a => a.type }```

### Path construct 
the object ```{ "path": "/path/to/field" } ``` is a special specs. that we can use to specify simple path mappings.

### supported functions
 every function should have an ``args`` field , which contain args to the function.
 Below contains all supported functions
- ####  concat
    - as name suggest this concat the ``values`` with the provided ``seperator`` default separator will be empty char "".
 values can itself we function or specs that resolves to a value.

- ####  uppercase
    - uppercase will uppercase the ``value``
 ``value`` can be a function or any other spec.


