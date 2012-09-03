package com.mpatric.mp3agic.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface FrameMember {
	int ordinal();
	int width() default -1;
	boolean encoded() default false;
	boolean terminated() default false;
}
