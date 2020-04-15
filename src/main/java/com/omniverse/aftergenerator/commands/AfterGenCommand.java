package com.omniverse.aftergenerator.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterGenCommand {
    String name() default "";
    String[] aliases() default {};
    String[] args() default {};
}
