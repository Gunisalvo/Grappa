package br.com.caelum.grappa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metadata that allows a Java Bean (JSR 303) to be initialized by Grappa.
 * 
 * Used to share a Class state between several Pin Listeners
 *
 * @author Gunisalvo
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Device {
	String nome() default "";
}
