# Dynarc
Dynarc [Dynamic Arrays in C] is a program/ system that enables/ enhances the usability of dynamic arays in C.
  
### Syntax
  Declaration of the dynamic array base data types (do this outside all functions, preferably just below the ```#include``` lines) (must be any combination of 
  ```int, float, char, double```
  For example, you may want float and int dynamic arrays so you can type the following.
   ```use dynarc [float, int];```  
  Declaring a dynamic array
  ```// For example, a dynamic array of base type float would be decalred as: 
    dynarc_float constants;
  ```
  Initialization for the first time can be done in bulk as:
  ```
    constants = {3.14, 1.618};
  ```
  Alternatively, elements can always be manually entered:
  ```
    constants[2] = 6.28;
  ```
  There is an inbuilt length variable that can be accessed to retrieve the length of the array at any time
  ```
  // The length can be accessed by using 
     constants.len
  ``` 
  
   An example use case:
      
  ```
    for(int i = 0; i < constants.len; i++) {
        printf("%0.3f\n", constants[i]);
    }
  ```
  Please refrain from using the length during input operations as it changes dynamically and running a loop such as the one below
  as it will most probably cause the program to go into an infinite loop on execution.
  ```
    for(int i = constants.len; i < constants.len * 8; i++) {
            constants[i] = (float)i;
    }
  ```
     
  The list of functions that go along with these dynamic arrays that you can use are:
  1. ```ARRAY_NAME.pop() //removes the last element in the array - the (length-1)th element```
  
  2. ```ARRAY_NAME.push(value) //adds the "value" to the end of the array```
  
  3. ```ARRAY_NAME.remove(index) //deletes the value stored at that index in the array.```
     
  4. ```ARRAY_NAME.insert(value, index)```
     
  5. ```ARRAY_NAME.removeN(value, N) // deletes the first N instances of the "value" from the array.```
  
  
If you want any particular feature to be added to the next release, please let me know !
