//: com.yuli.springguru.rabbitmqlab.tutorials.Severity.java


package com.yuli.springguru.rabbitmqlab.tutorials;


public enum Severity {

    ERROR("error"), INFO("info"), WARNING("warning");

    private final String severity;

    Severity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return this.severity;
    }

    @Override
    public String toString() {
        return this.getSeverity();
    }

}///:~