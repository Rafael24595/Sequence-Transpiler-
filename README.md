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
