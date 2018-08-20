package jp.co.soramitsu.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDateTime

class OneCoderTest extends Specification {

    def mapper = new OneCodeMapper()

    def complex = [
            "char"    : new Character('a' as char),
            "long"    : 28l,
            "chararr" : ['a', 'b', 'c'] as char[],
            "BigInt"  : new BigInteger("1337"),
            "BigDec"  : new BigDecimal("2.01"),
            "bool"    : true,
            "floating": 1.37f,
            "double"  : 0.99d,
            "string"  : "hello!" as String,
            "list"    : [1, 2, "zzx"],
            "map"     : [
                    "a"      : "b",
                    "c"      : false,
                    "null"   : "notnull",
                    "nullval": null
            ]
    ]

    def complexEncoded = "d6:BigDeci2.01e6:BigInti1337e4:boolT4:char1:a7:chararr3:abc6:doublei0.99e8:floatingi1.37e4:listli1ei2e3:zzxe4:longi28e3:mapd1:a1:b1:cF4:null7:notnulle6:string6:hello!e"

    def "should correctly encode complex object"() {
        given:
        complex

        when:
        def enc = mapper.writeValueAsString(complex)

        then:
        enc == complexEncoded
    }

    def "should handle utf-8"() {
        given:
        def m = [
                "ascii",
                "øutf-8ø",
                "こんにちは世界"
        ]

        when:
        String s = mapper.writeValueAsString(m)
        byte[] b = mapper.writeValueAsBytes(m)

        then:
        s == "l5:ascii9:øutf-8ø21:こんにちは世界e"
        b == [108, 53, 58, 97, 115, 99, 105, 105, 57, 58, -61, -72, 117, 116, 102, 45, 56, -61, -72, 50, 49, 58, -29, -127, -109, -29, -126, -109, -29, -127, -85, -29, -127, -95, -29, -127, -81, -28, -72, -106, -25, -107, -116, 101] as byte[]
    }

    def "should sort keys alphabetically"() {
        given:
        def m = [
                "c": 3,
                "b": 2,
                "a": 1
        ]

        when:
        def enc = mapper.writeValueAsString(m)

        then:
        enc == "d1:ai1e1:bi2e1:ci3ee"
    }

    def "should write to Writer"() {
        given:
        def writer = new StringWriter(100)

        when:
        mapper.writeValue(writer, complex)

        then:
        writer.toString() == complexEncoded
    }

    def "should write to a File"() {
        given:
        def file = File.createTempFile("1code", ".json")

        when:
        mapper.writeValue(file, complex)

        then:
        file.exists()
        file.newReader("utf-8").readLine() == complexEncoded
    }

    def "should work for complex json with no exceptions"() {
        given:
        def root = jsonMapper.readTree(json)

        when:
        String val = mapper.writeValueAsString(root)

        then:
        root != null
        noExceptionThrown()
        val == expected

        where:
        jsonMapper = new ObjectMapper()
        json = this.getClass().getResourceAsStream("/json/initial.json")
        expected = "d1:ai1e1:b6:string1:cli3ei2ei1ee1:dd1:1i1e1:2ld1:zi1eeee1:e36:complex_key/value with? empty objectde11:empty arrayle28:empty array of empty objectsldededeee"
    }

    def "should not include nulls"() {
        given:
        def json = '''
                {
                    "a": {},
                    "b": [],
                    "c": "",
                    "d": 0
                }
        '''

        def obj = [
                "a": [:],
                "b": [],
                "c": "",
                "d": 0
        ]

        def jsonMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        def tree = jsonMapper.readTree(json)


        when:
        def a1 = jsonMapper.writeValueAsString(tree)
        def a2 = jsonMapper.writeValueAsString(obj)

        then:
        a1 == a2
    }

    def "can process dates"(){
        given:
        def obj = [
                "date": new Date(123),
                "instant": Instant.ofEpochMilli(123)
        ]

        def mapper = new OneCodeMapper()

        when:
        def a = mapper.valueToTree(obj).toString()

        then:
        a == '{"date":"1970-01-01T00:00:00.123+0000","instant":"1970-01-01T00:00:00.123Z"}'
    }
}
