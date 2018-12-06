/*
 * Configurate
 * Copyright (C) zml and Configurate contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liumapp.qtools.property.core.objectmapping;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * This subclass creates new object instances using a provided {@link Injector}.
 *
 * <p>This allows configuration objects to take additional arguments with Guice.</p>
 *
 * <p>Instances of this object should be reached using a {@link GuiceObjectMapperFactory}.</p>
 */
class GuiceObjectMapper<T> extends ObjectMapper<T> {
    private final Injector injector;
    private final Key<T> typeKey;

    /**
     * Create a new object mapper of a given type
     *
     * @param clazz The type this object mapper will work with
     * @throws ObjectMappingException if the provided class is in someway invalid
     */
    protected GuiceObjectMapper(@NonNull Injector injector, @NonNull Class<T> clazz) throws ObjectMappingException {
        super(clazz);
        this.injector = injector;
        this.typeKey = Key.get(clazz);
    }

    @Override
    public boolean canCreateInstances() {
        try {
            injector.getProvider(typeKey);
            return true;
        } catch (ConfigurationException ex) {
            return false;
        }
    }

    @Override
    protected T constructObject() throws ObjectMappingException {
        return injector.getInstance(this.typeKey);
    }
}
