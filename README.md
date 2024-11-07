This is a library that will help you map Json to Another JSON.
Many times in our code we map one rest Api out put to our data class, which is basically mapping from one JSON to another. 

Errors happen during these mappings,
and sometimes the mappings are very obvious still we need to add those codes transforming one json to another.
This library tries to externalize these JSON to JSON mapping.
Where you provide the input to output mappings in a JSON file and library will contract outJson from input JSON from the mapping json.

It may feel that code is being transferred from classes to JsonMapper.
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
    "/owners/-/name" : {
      "pathToMap" : "/owner/name"
    },
    "/owners/--/type" : {
      "pathToMap" : "/ownerTypes"
    }
  }
}
```
in this json **mappings** contains all the mappings from input keys to output keys.

example of one such mapping will be described here </br>
`/address/postcode` is the key path in input.json.
`pathToMap` tells the path in outputJson which is `/address_postcode` in this case

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
#### Keys Rules
One common rule is keys mostly follows JSON Pointer convention except some 
special cases like arrays.
see the JSON pointer rfc [here](https://www.rfc-editor.org/rfc/rfc6901)

**Simple JSON Path** </br>
path like `/address/postCode` directly matched to postcode inside address in the root folder. 

**Matching array item**

We can match array item by index (zero indexed)
like `/owners/0/name` this means match key traversal like this
 ``. -> owners -> 0th element -> name``

** Matching All array Items **

We can tell the parser that inside each array element grab one key and convert to a new array using `--`
e.g.``/owners/--/type`` we are saying maps each type field inside each array item in owners
this programmatically means ```owners.map{ a => a.type }```

**Mapping to output json path**

you need to provide a key path from root 
e.g. `/address_line1`add data at root.address_line1
while `/address/line1` will mean add data at root.address.line .




