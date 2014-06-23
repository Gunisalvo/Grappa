package br.com.caelum.grappa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Metadata that marks the injection point of your physical device layer in your Pin Listeners and Devices.
 *
 * @author Gunisalvo
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Hardware {

}
