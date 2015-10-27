package mani.beatsalbumsearch.modules;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by maniselvaraj on 29/10/14.
 */
@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}