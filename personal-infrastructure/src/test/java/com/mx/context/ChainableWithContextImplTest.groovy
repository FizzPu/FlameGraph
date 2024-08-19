package com.mx.context

import spock.lang.Specification

/**
 *
 * @author FizzPu
 * @since 2024/5/20 上午11:50
 */
class ChainableWithContextImplTest extends Specification {
    static class LogContextVar extends AbstractContextVar<String> {
        private final List<String> logs = new ArrayList<>();
        private String value;

        LogContextVar(String value) {
            this.value = value
        }

        @Override
        String name() {
            return "loggable context var";
        }

        @Override
        String get() {
            return value;
        }

        @Override
        void set(String value) {
            logs.add("set " + value);
            this.value = value;
        }

        @Override
        void remove() {
            logs.add("remove");
            this.value = null;
        }


        @Override
        public String toString() {
            return "LogContextVar{" +
                    "logs=" + logs +
                    '}';
        }
    }

    def "链式上下文管理"() {
        given:
        LogContextVar logContextVar = new LogContextVar("a")
        ChainableWithContext chainable = new ChainableWithContextImpl(logContextVar, "b")
        chainable.withContext(logContextVar, "c")
        chainable.withContext(logContextVar, "d")

        when:
        chainable.call {}

        then:
        println logContextVar
    }
}
