package com.liumapp.qtools.property.gson;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.liumapp.qtools.property.core.ConfigurationNode;
import com.liumapp.qtools.property.core.ConfigurationOptions;
import com.liumapp.qtools.property.core.SimpleConfigurationNode;
import com.liumapp.qtools.property.core.loader.AbstractConfigurationLoader;
import com.liumapp.qtools.property.core.loader.CommentHandler;
import com.liumapp.qtools.property.core.loader.CommentHandlers;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * file GsonConfigurationLoader.java
 * author liumapp
 * github https://github.com/liumapp
 * email liumapp.com@gmail.com
 * homepage http://www.liumapp.com
 * date 2018/12/7
 * A loader for JSON-formatted configurations, using the GSON library for parsing and generation.
 */
public class GsonConfigurationLoader extends AbstractConfigurationLoader<ConfigurationNode> {

    /**
     * Creates a new {@link GsonConfigurationLoader} builder.
     *
     * @return A new builder
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds a {@link GsonConfigurationLoader}.
     */
    public static class Builder extends AbstractConfigurationLoader.Builder<Builder> {
        private boolean lenient = true;
        private int indent = 2;

        protected Builder() {
        }

        /**
         * Sets the level of indentation the resultant loader should use.
         *
         * @param indent The indent level
         * @return This builder (for chaining)
         */
        @NonNull
        public Builder setIndent(int indent) {
            this.indent = indent;
            return this;
        }

        /**
         * Gets the level of indentation to be used by the resultant loader.
         *
         * @return The indent level
         */
        public int getIndent() {
            return this.indent;
        }

        /**
         * Sets if the resultant loader should parse leniently.
         *
         * @see JsonReader#setLenient(boolean)
         * @param lenient Whether the parser should parse leniently
         * @return This builder (for chaining)
         */
        @NonNull
        public Builder setLenient(boolean lenient) {
            this.lenient = lenient;
            return this;
        }

        /**
         * Gets if the resultant loader should parse leniently.
         *
         * @return Whether the parser should parse leniently
         */
        public boolean isLenient() {
            return this.lenient;
        }

        @NonNull
        @Override
        public GsonConfigurationLoader build() {
            return new GsonConfigurationLoader(this);
        }
    }

    private final boolean lenient;
    private final String indent;

    private GsonConfigurationLoader(Builder builder) {
        super(builder, new CommentHandler[] {CommentHandlers.DOUBLE_SLASH, CommentHandlers.SLASH_BLOCK, CommentHandlers.HASH});
        this.lenient = builder.isLenient();
        this.indent = Strings.repeat(" ", builder.getIndent());
    }

    @Override
    protected void loadInternal(ConfigurationNode node, BufferedReader reader) throws IOException {
        reader.mark(1);
        if (reader.read() == -1) {
            return;
        }
        reader.reset();
        try (JsonReader parser = new JsonReader(reader)) {
            parser.setLenient(lenient);
            parseValue(parser, node);
        }
    }

    private void parseValue(JsonReader parser, ConfigurationNode node) throws IOException {
        JsonToken token = parser.peek();
        switch (token) {
            case BEGIN_OBJECT:
                parseObject(parser, node);
                break;
            case BEGIN_ARRAY:
                parseArray(parser, node);
                break;
            case NUMBER:
                double nextDouble = parser.nextDouble();
                int nextInt = (int) nextDouble;
                long nextLong = (long) nextDouble;
                if (nextInt == nextDouble) {
                    node.setValue(nextInt); // They don't do much for us here in Gsonland
                } else if (nextLong == nextDouble) {
                    node.setValue(nextLong);
                } else {
                    node.setValue(nextDouble);
                }
                break;
            case STRING:
                node.setValue(parser.nextString());
                break;
            case BOOLEAN:
                node.setValue(parser.nextBoolean());
                break;
            case NULL: // Ignored values
            case NAME:
                break;
            default:
                throw new IOException("Unsupported token type: " + token);
        }
    }

    private void parseArray(JsonReader parser, ConfigurationNode node) throws IOException {
        parser.beginArray();
        JsonToken token;
        while ((token = parser.peek()) != null) {
            switch (token) {
                case END_ARRAY:
                    parser.endArray();
                    return;
                default:
                    parseValue(parser, node.getAppendedNode());
            }
        }
        throw new JsonParseException("Reached end of stream with unclosed array at!");

    }

    private void parseObject(JsonReader parser, ConfigurationNode node) throws IOException {
        parser.beginObject();
        JsonToken token;
        while ((token = parser.peek()) != null) {
            switch (token) {
                case END_OBJECT:
                case END_DOCUMENT:
                    parser.endObject();
                    return;
                case NAME:
                    parseValue(parser, node.getNode(parser.nextName()));
                    break;
                default:
                    throw new JsonParseException("Received improper object value " + token);
            }
        }
        throw new JsonParseException("Reached end of stream with unclosed object!");
    }

    @Override
    public void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
        if (!lenient && !node.hasMapChildren()) {
            throw new IOException("Non-lenient json generators must have children of map type");
        }
        try (JsonWriter generator = new JsonWriter(writer)) {
            generator.setIndent(indent);
            generator.setLenient(lenient);
            generateValue(generator, node);
            generator.flush();
            writer.write(SYSTEM_LINE_SEPARATOR); // Jackson doesn't add a newline at the end of files by default
        }
    }

    @NonNull
    @Override
    public ConfigurationNode createEmptyNode(@NonNull ConfigurationOptions options) {
        options = options.setAcceptedTypes(ImmutableSet.of(Map.class, List.class, Double.class, Float.class,
                Long.class, Integer.class, Boolean.class, String.class));
        return SimpleConfigurationNode.root(options);
    }

    private static void generateValue(JsonWriter generator, ConfigurationNode node) throws IOException {
        if (node.hasMapChildren()) {
            generateObject(generator, node);
        } else if (node.hasListChildren()) {
            generateArray(generator, node);
        } else if (node.getKey() == null && node.getValue() == null) {
            generator.beginObject();
            generator.endObject();
        } else {
            Object value = node.getValue();
            if (value instanceof Double) {
                generator.value((Double) value);
            } else if (value instanceof Float) {
                generator.value((Float) value);
            } else if (value instanceof Long) {
                generator.value((Long) value);
            } else if (value instanceof Integer) {
                generator.value((Integer) value);
            } else if (value instanceof Boolean) {
                generator.value((Boolean) value);
            } else {
                generator.value(value.toString());
            }
        }
    }

    private static void generateObject(JsonWriter generator, ConfigurationNode node) throws IOException {
        if (!node.hasMapChildren()) {
            throw new IOException("Node passed to generateObject does not have map children!");
        }
        generator.beginObject();
        for (Map.Entry<Object, ? extends ConfigurationNode> ent : node.getChildrenMap().entrySet()) {
            generator.name(ent.getKey().toString());
            generateValue(generator, ent.getValue());
        }
        generator.endObject();
    }

    private static void generateArray(JsonWriter generator, ConfigurationNode node) throws IOException {
        if (!node.hasListChildren()) {
            throw new IOException("Node passed to generateArray does not have list children!");
        }
        List<? extends ConfigurationNode> children = node.getChildrenList();
        generator.beginArray();
        for (ConfigurationNode child : children) {
            generateValue(generator, child);
        }
        generator.endArray();
    }
}
