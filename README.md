# Dynarc
Dynarc [Dynamic Arrays in C] is a program/ system that enables/ enhances the usability of dynamic arays in C.

### Usage Instructions [I'm working towards making this more user friendly]:
  To run compile the code that you have written,
  1. ensure that you move the modcc script (.sh / .cmd depending on what operating system you are using)
     to the directory with your source code.
     
  2. On Windows, run ```modcc.cmd sourceFileName DesinationFileName```
   * On running this you will get a file with the name "DesinationFileName".
   * compile this file using your c compiler
   * run the executable
     
  3. For Non-Windows users, I don't have a script as of now, so please contribute !
  
### Documentation for Syntax:
  *Declaration of the dynamic array base data types (do this outside all functions, preferably just below the ```#include``` lines) (must be any combination of ```int, float, char, double```
   ```use dynarc [float, int];```  
  *Declaring a dynamic array
  ```// For example, a dynamic array of base type float would be decalred as: 
    dynarc_float constants;
  ```
  -Initialization for the first time can be done in bulk as:
  ```
    constants = {3.14, 1.618};
  ```
  -Alternatively, elements can always be manually entered:
  ```
    constants[2] = 6.28;
  ```
  *There is an inbuilt length variable that can be accessed to retrieve the length of the array at any time
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
     
  -The list of functions that go along with these dynamic arrays that you can use are:
  1. ```ARRAY_NAME.pop() //removes the last element in the array - the (length-1)th element```
  
  2. ```ARRAY_NAME.push(value) //adds the "value" to the end of the array```
  
  3. ```ARRAY_NAME.remove(index) //deletes the value stored at that index in the array.```
      
     Be warned that deleting only resets the value at that index to 0, it does not truly delete the value. This WILL be fixed later.
     
  4. ```ARRAY_NAME.insert(value, index)```
  
     Be warned that inserting only sets the value at that index to "value", it does not truly insert the value. This WILL be fixed later.
     
  5. ```ARRAY_NAME.removeN(value, N) // deletes the first N instances of the "value" from the array.```
  
  Be warned that deleting only resets the value at that index to 0, it does not truly delete the value. This WILL be fixed later.
  
  
  
If you want any particular feature to be added to the next release, please let me know !
