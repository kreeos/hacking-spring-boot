package com.krislee.hackingspringboot.reactive;

import org.springframework.data.annotation.Id;
import org.springframework.boot.actuate.trace.http.HttpTrace;

public class HttpTraceWrapper {
    private @Id
    String id;

    private HttpTrace httpTrace;

    public HttpTraceWrapper(HttpTrace httpTrace) {
        this.httpTrace = httpTrace;
    }

    public HttpTrace getHttpTrace() {
        return httpTrace;
    }
}
