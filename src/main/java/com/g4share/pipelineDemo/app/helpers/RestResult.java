// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.helpers;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

public class RestResult<R, F> {

    private Consumer<R> successConsumer;
    private Consumer<F> failureConsumer;

    public RestResult<R, F> success(Consumer<R> consumer) {
        successConsumer = consumer;
        return this;
    }

    public RestResult<R, F> failure(Consumer<F> consumer) {
        failureConsumer = consumer;
        return this;
    }

    public EXECUTION_STATUS exec() {
        return exec(null);
    }

    public EXECUTION_STATUS exec(Action action) {
        switch (this) {
            case Success<R, F> s -> {
                successConsumer.accept(s.value);
                if (action != null) {
                    action.execute();
                }
                return EXECUTION_STATUS.SUCCESS;
            }
            case Failure<R, F> f -> {
                failureConsumer.accept(f.error);
                if (action != null) {
                    action.execute();
                }
                return EXECUTION_STATUS.FAILURE;
            }
            default -> {
                if (action != null) {
                    action.execute();
                }
                return EXECUTION_STATUS.UNDEFINED;
            }
        }
    }

    @RequiredArgsConstructor
    static class Success<R, F> extends RestResult<R, F> {
        private final R value;
    }

    @RequiredArgsConstructor
    static class Failure<R, F> extends RestResult<R, F> {
        private final F error;
    }
}