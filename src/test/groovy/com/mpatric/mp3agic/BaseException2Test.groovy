package com.mpatric.mp3agic

import spock.lang.Specification

public class BaseException2Test extends Specification {

	def "getMessage returns correct message"() {
		when: def baseException = new BaseException('ONE')
        then: baseException.getMessage() == 'ONE'
	}

    def "getDetailedMessage returns correct detailed message"() {
        when: def baseException = new BaseException('ONE')
        then: baseException.getDetailedMessage() == '[com.mpatric.mp3agic.BaseException: ONE]'
    }


    def "getMessage returns correct value for chained exceptions"() {
        when:
        BaseException e1 = new BaseException("ONE")
        BaseException e2 = new UnsupportedTagException("TWO", e1)
        then:
        e1.getMessage() == 'ONE'
        and:
        e2.getMessage() == 'TWO'
    }

	def "when a chained exception is given getDetailedMessage returns correct message"() {
        given:
        BaseException e1 = new BaseException("ONE")
		BaseException e2 = new UnsupportedTagException("TWO", e1)
		BaseException e3 = new NotSupportedException("THREE", e2)
		expect:
		e3.getDetailedMessage() ==
                '[com.mpatric.mp3agic.NotSupportedException: THREE] ' +
                'caused by [com.mpatric.mp3agic.UnsupportedTagException: TWO] ' +
                'caused by [com.mpatric.mp3agic.BaseException: ONE]'
	}

	def "when different chained exceptions are given getMessage returns correct message"() {
		expect: mixedChainedException.getMessage() == 'FIVE'
	}

    def "when different chained exceptions are given getDetailedMessage returns correct message"() {
        expect: mixedChainedException.getDetailedMessage() ==
                '[com.mpatric.mp3agic.InvalidDataException: FIVE] ' +
                'caused by [com.mpatric.mp3agic.NoSuchTagException: FOUR] ' +
                'caused by [java.lang.Exception: THREE] ' +
                'caused by [com.mpatric.mp3agic.UnsupportedTagException: TWO] ' +
                'caused by [com.mpatric.mp3agic.BaseException: ONE]'
    }

    private BaseException getMixedChainedException() {
        BaseException e1 = new BaseException('ONE')
        BaseException e2 = new UnsupportedTagException('TWO', e1)
        Exception e3 = new Exception('THREE', e2)
        BaseException e4 = new NoSuchTagException('FOUR', e3)
        BaseException e5 = new InvalidDataException('FIVE', e4)
        return e5
    }
}
