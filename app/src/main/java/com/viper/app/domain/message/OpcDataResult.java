package com.viper.app.domain.message;

public class OpcDataResult<T> {
    private final T T;


    public OpcDataResult(T t) {
       this.T = t;
    }


    public T getResult() {
        return T;
    }


    public interface Result<T> {

        void onResult(OpcDataResult<T> result );
    }
}
