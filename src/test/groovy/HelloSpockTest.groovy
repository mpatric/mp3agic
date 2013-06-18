import spock.lang.Unroll

class HelloSpockTest extends spock.lang.Specification {

    @Unroll
    def "the name #name has the length #length"() {
        expect:
        name.size() == length

        where:
        name     | length
        "Spock"  | 5
        "Kirk"   | 4
        "Scotty" | 6
    }
}