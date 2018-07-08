# Specification for `1code` - binary encoding format of arbitrary complex data structures

1code is an extention of [`bencode`](https://en.wikipedia.org/wiki/Bencode), which adds string encoding, floating point numbers, booleans and other types, that are missing in `bencode`.

**Features**

- For each possible (complex) value, there is only a single valid 1coding; i.e. there is a bijection between values and their encodings. This has the advantage that applications may compare 1coded values by comparing their encoded forms, eliminating the need to decode the values.
- 1coding is binary format and not considered to be human-readable.

## boolean

| boolean | 1coded boolean |
|---------|----------------|
| true    | T              |
| false   | F              |

## numbers (integers, floats)

An integer is encoded as `i<number encoded in base ten ASCII>e`.
Leading zeros are not allowed (although the number zero is still represented as "0").
Negative values are encoded by prefixing the number with a minus sign.
The number `42` would thus be encoded as `i42e`, `0` as `i0e`, and `-42` as `i-42e`. Negative zero is not permitted.

| number                 | 1coded number            |
|------------------------|--------------------------|
| 0                      | i0e                      |
| 42                     | i42e                     |
| 1180591620717411303424 | i1180591620717411303424e |
| -1                     | i-1e                     |
| 1.5                    | i1.5e                    |
| 1,5                    | i1.5e                    |

Integer and decimal point are splitted with `dot .` symbol. All numbers are represented base 10.

## byte string

A byte string (a sequence of bytes, not necessarily characters) is encoded as `<length>:<contents>`.
The length is encoded in base 10, like integers, but must be non-negative (zero is allowed);
the contents are just the bytes that make up the string in UTF-8 encoding.

| string         | 1coded string     | bytes                                                                                   |
|----------------|-------------------|-----------------------------------------------------------------------------------------|
| <empty>        | 0:                | 0:                                                                                      |
|       0.1      |       3:0.1       | 3:0.1                                                                                   |
| hello world    | 11:hello world    | 11:hello world                                                                          |
| привет мир     | 19:привет мир     | 19:\xd0\xbf\xd1\x80\xd0\xb8\xd0\xb2\xd0\xb5\xd1\x82 \xd0\xbc\xd0\xb8\xd1\x80            |
| こんにちは世界 | 21:こんにちは世界 | 21:\xe3\x81\x93\xe3\x82\x93\xe3\x81\xab\xe3\x81\xa1\xe3\x81\xaf\xe4\xb8\x96\xe7\x95\x8c |

## list

A list of values is encoded as `l<contents>e` . The contents consist of the 1coded elements of the list, in order, concatenated. A list consisting of the string "spam" and the number 42 would be encoded as: `l4:spami42ee`. Note the absence of separators between elements, and the first character is the letter 'l', not digit '1'.

| list             | 1coded list    |
|------------------|----------------|
| [true, false, 1] | lTFi1ee        |
| [1, hi, 1.3]     | li1e2:hii1.3ee |

## dictionary

A dictionary is encoded as `d<contents>e`. The elements of the dictionary are 1coded each key immediately followed by its value. All keys must be byte strings and must appear in **lexicographical order**.

Keys with value `null` are ignored.

Example:
```
{
  "str": "string",
  "num": 100500,
  "bool": true,
  "nullable": null,
  "null": [1,2,3],
  "dict": {
    "a": 9,
    "c": 10
  }
}
```

1coded dictionary:

```
d4:boolT4:dictd1:ai9e1:ci10ee4:nullli1ei2ei3ee3:numi100500e3:str6:stringe
```

Or, pretty printed:
```
d
  4:bool T
  4:dict d
    1:a i9e
    1:c i10e
  e
  4:null l
    i1e i2e i3e
  e
  3:num i100500e
  3:str 6:string
e
```
