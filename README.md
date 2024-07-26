This is a library that will help you map Json to Another Json.
Many times in our code we map one rest Api out put to our data class, which is basically mapping from one Json to another . 

Errors happen during these mappings and sometime the mappings are very obvious still we need to add those code transforming one json to another.
This library tries to externalize these Json to Json mapping.
Where you provide the input to output mappings in a Json file and library will contruct outJson from input Json from the mapping json.

It may feel that code is being transferred from classes to JsonMapper .
Thats somewhat true but many advantages of this approach is
1. duplication of logic is now avoided . e.g. get from x , set in y etc.
2. declaring mappings before hand make the code more readable . making all mappings at single place.
3. same mappings can be reused at multiple place if input structure is used at multiple places. 
