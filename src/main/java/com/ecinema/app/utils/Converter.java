package com.ecinema.app.utils;

/**
 * The interface Converter.
 *
 * @param <A> the type parameter
 * @param <B> the type parameter
 */
public interface Converter<A, B> {

    /**
     * Convert a.
     *
     * @param b the b
     * @return the a
     */
    A convert(B b);

}
