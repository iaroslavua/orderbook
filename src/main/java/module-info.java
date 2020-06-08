open module orderbook {
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.web;
    requires org.slf4j;
    requires spring.beans;
    requires reactor.core;

    requires com.fasterxml.classmate;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires lombok;
    requires org.mapstruct.processor;
}