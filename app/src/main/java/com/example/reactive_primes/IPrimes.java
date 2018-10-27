package com.example.reactive_primes;

import io.reactivex.Maybe;

public interface IPrimes {
    Maybe<Prime> getMax();
    Maybe<Prime> find();
}
