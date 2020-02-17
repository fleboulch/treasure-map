package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ExplorerConfiguration {

    String name() default "Laura";
    int xCoordinates() default 1;
    int yCoordinates() default 2;

    OrientationType orientationType() default OrientationType.E;

    int nbTreasures() default 0;

    MovementType[] movements() default {};
    boolean isExampleSequenceMovements() default false;

}
