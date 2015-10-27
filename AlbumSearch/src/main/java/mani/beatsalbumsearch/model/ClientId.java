package mani.beatsalbumsearch.model;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** The beats developer api client ID. */
@Qualifier @Retention(RUNTIME)
public @interface ClientId {
}
