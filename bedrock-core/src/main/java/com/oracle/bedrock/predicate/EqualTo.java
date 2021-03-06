/*
 * File: EqualTo.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.bedrock.predicate;

import java.util.function.Predicate;

/**
 * A {@link Predicate} that determines if a value is equal to another.
 * <p>
 * Copyright (c) 2014. All Rights Reserved. Oracle Corporation.<br>
 * Oracle is a registered trademark of Oracle Corporation and/or its affiliates.
 *
 * @param <T>  the type of the {@link Predicate} value
 *
 * @author Brian Oliver
 */
public class EqualTo<T> implements Predicate<T>
{
    /**
     * The value to compare.
     */
    private T value;


    /**
     * Constructs an {@link EqualTo} {@link Predicate}.
     *
     * @param value  the required value to be equal
     */
    public EqualTo(T value)
    {
        this.value = value;
    }


    @Override
    public boolean test(T value)
    {
        return value == null && this.value == null || value != null && value.equals(this.value);
    }


    @Override
    public String toString()
    {
        return "EqualTo{" + value + "}";
    }
}
