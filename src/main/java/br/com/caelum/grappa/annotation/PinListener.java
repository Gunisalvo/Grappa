package br.com.caelum.grappa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metadata that binds an event to a number of hardware Grappa Context available electrical pins.
 * 
 * If used to mark a Class it must implement PinService
 * If used to mark a Method it must be inside a valid Grappa Device
 *
 * @author Gunisalvo
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PinListener {
	public int[] addresses();
	public PinListenerFilter[] filters() default {};
}
