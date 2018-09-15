package jp.co.soramitsu.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class OneCoderTest extends Specification {

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

    def complexEncoded = "d6:BigDeci2.01e6:BigInti1337e4:boolT4:char1:a7:chararr3:abc6:doublei0.99e8:floatingi1.37e4:listli1ei2e3:zzxe4:longi28e3:mapd1:a1:b1:cF4:null7:notnull7:nullvalNe6:string6:hello!e"

    def "should correctly encode complex object"() {
        given:
        def mapper = new ObjectMapper()
        def json = mapper.writeValueAsString(complex)
        def node = mapper.readTree(json)
        def oneCoder = new OneCoder()

        when:
        oneCoder.writeJsonNode(node)
        def enc = oneCoder.toString()

        then:
        enc == complexEncoded
    }

    def "should handle utf-8"() {
        given:
        def mapper = new ObjectMapper()
        def m = [
                "ascii",
                "øutf-8ø",
                "こんにちは世界"
        ]
        def json = mapper.writeValueAsString(m)
        def node = mapper.readTree(json)
        def oneCoder = new OneCoder()

        when:
        oneCoder.writeJsonNode(node)
        def s = oneCoder.toString()

        then:
        s == "l5:ascii9:øutf-8ø21:こんにちは世界e"
    }

    def "should sort keys alphabetically"() {
        given:
        def mapper = new ObjectMapper()
        def m = [
                "c": 3,
                "b": 2,
                "a": 1
        ]
        def json = mapper.writeValueAsString(m)
        def node = mapper.readTree(json)
        def oneCoder = new OneCoder()

        when:
        oneCoder.writeJsonNode(node)
        def enc = oneCoder.toString()

        then:
        enc == "d1:ai1e1:bi2e1:ci3ee"
    }


    def "fields are sorted"() {
        given:
        def mapper = new ObjectMapper()
        def oneCoder = new OneCoder()
        def node = mapper.readTree(input)

        when:
        oneCoder.writeJsonNode(node)
        def onecoded = oneCoder.toString()

        then:
        onecoded == expected

        where:
        input                                                                                                                                                                                                                                                                                                                                                       | expected
        '{"id":"did:sora:username","created":"2018-09-14T10:07:30Z","publicKey":[{"id":"did:sora:username#keys-1","owner":"did:sora:username","publicKey":"b8bf218fe98e6b505b9ebdff5852d9db8df38d130ee914d531b06bb7be68efe4","type":"Ed25519Sha3VerificationKey"}],"authentication":[{"type":"Ed25519Sha3Authentication","publicKey":"did:sora:username#keys-1"}]}' | "d14:authenticationld9:publicKey24:did:sora:username#keys-14:type25:Ed25519Sha3Authenticationee7:created20:2018-09-14T10:07:30Z2:id17:did:sora:username9:publicKeyld2:id24:did:sora:username#keys-15:owner17:did:sora:username9:publicKey64:b8bf218fe98e6b505b9ebdff5852d9db8df38d130ee914d531b06bb7be68efe44:type26:Ed25519Sha3VerificationKeyeee"
        '{"b": {"b":1, "a":0},"a": {"a":1, "b":0}}'                                                                                                                                                                                                                                                                                                                 | 'd1:ad1:ai1e1:bi0ee1:bd1:ai0e1:bi1eee'
    }

    def "all types are handled correctly"() {
        given:
        def json = '{\n' +
                '  "str": "string",\n' +
                '  "num": 100500,\n' +
                '  "bool": true,\n' +
                '  "nullable": null,\n' +
                '  "null": [1,2,3],\n' +
                '  "dict": {\n' +
                '    "a": 9,\n' +
                '    "c": 10\n' +
                '  }\n' +
                '}'
        def mapper = new ObjectMapper()
        def oneCoder = new OneCoder()

        when:
        def node = mapper.readTree(json)
        oneCoder.writeJsonNode(node)
        def onecoding = oneCoder.toString()

        then:
        onecoding == 'd4:boolT4:dictd1:ai9e1:ci10ee4:nullli1ei2ei3ee8:nullableN3:numi100500e3:str6:stringe'
    }
}
