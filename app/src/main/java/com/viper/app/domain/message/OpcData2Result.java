package com.viper.app.domain.message;

public class OpcData2Result<T,M> {
    private final T T;
    private final M M;

    public OpcData2Result(T t, M m) {
       this.T = t;
       this.M = m;
    }

    public T getResultT() {
        return T;
    }
    public M getResultM() {
        return M;
    }

    public interface Result<T,M> {

        void onResult(OpcData2Result<T,M> result );
    }
}
