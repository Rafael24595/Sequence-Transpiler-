### Sequence structure:
**1.- COMMAND:** Field | Raw object/value(used in list type complex structures)
Types:
- **FIELD**: Define the beggining of an key-value structure.
- **$data**: Define a raw object/value.

**2.- NAME:** Field key
Types:
- **$Key**: Define the key of a object/value.

**3.- ACTION:** Action
types:
- **AS**: Create new object/value without merge it with base object/value.
- **MUTATE**: Merge actual object/value with base object/value (Raw values, will be overrided).
- **INCREMENT**: Add up base value with actual value.
- **DECREMENT**: Substract actual value to base value
- **DELETE**: Erase base object/value

**4.- VALUE:** Object/value
Types: 
- **OBJECT**: Define the beggining of a sub-structure with a key-value pattern, must be closed with CLOSE tag.
- **LIST**: Define the beggining of a list type sub-structure, must be closed with CLOSE tag.
- **$value**: Define a value.

### Example:
**Input object (JSON)**
```sh
{
   "$object_data_1":{
      "$integer":1,
      "$string":"empty 1",
      "$boolean":true,
      "$string_new":"str 1"
   },
   "$list_data":[
      0
   ],
   "$erase":"this_field_out",
   "object_data_complex_1":{
      "$integer":"3",
      "sub_level_1":{
         "$string":"str 4"
      }
   }
}
```
**Sequence instructions (Sequence format)**
```sh
FIELD $object_data_1 MUTATE OBJECT
    FIELD $integer INCREMENT 11.95
    FIELD $string MUTATE "str 2"
    FIELD $boolean MUTATE false
CLOSE
FIELD $list_data MUTATE LIST
    10 11 12 13
CLOSE
FIELD $complex_list_data AS LIST
    OBJECT
        FIELD $integer AS 311
        FIELD $string AS "str 3"
    CLOSE
CLOSE
FIELD $erase DELETE
FIELD object_data_complex_1 MUTATE OBJECT
    FIELD $integer AS 4
CLOSE
FIELD $object_data_complex_2 AS OBJECT
    FIELD $sub_level_2 AS OBJECT
        FIELD $integer AS 12
        FIELD $string AS "str 5"
        FIELD $boolean AS true
    CLOSE
CLOSE
```
**Output (JSON)**
```sh
{
    "$object_data_1":{
      "$integer":12.95,
      "$string":"str 2",
      "$boolean":false,
      "$string_new":"str 1"
   },
   "$list_data":[
      0.0, 10, 11, 12, 13
   ],
   "$complex_list_data":[
      {
         "$string":"str 3",
         "$integer":311
      }
   ],
   "object_data_complex_1":{
      "$integer":4,
      "sub_level_1":{
         "$string":"str 4"
      }
   },
   "$object_data_complex_2":{
      "$sub_level_2":{
         "$string":"str 5",
         "$boolean":true,
         "$integer":12
      }
   }
}
```