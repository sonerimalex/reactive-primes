package com.example.reactive_primes;

import io.reactivex.Maybe;

public interface ISpectres {
    Maybe<Spectre> getMax();
    Maybe<Spectre> find();
}
