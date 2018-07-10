package jp.co.soramitsu.jackson

import spock.lang.Specification;

class OneCoderTest extends Specification {

    def mapper = new OneCodeMapper()

    def "should correctly encode complex object"() {
        given:
        def m = [
                "bool"    : true,
                "floating": 1.37f,
                "double"  : 0.99d,
                "string"  : "hello!",
                "list"    : [1, 2, "zzx"],
                "map"     : [
                        "a"      : "b",
                        "c"      : false,
                        "null"   : "notnull",
                        "nullval": null
                ]
        ]

        when:
        def enc = mapper.writeValueAsString(m)

        then:
        enc == "d4:boolT6:doublei0.99e8:floatingi1.37e4:listli1ei2e3:zzxe3:mapd1:a1:b1:cF4:null7:notnulle6:string6:hello!e"
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
}
