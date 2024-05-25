/*
Copyright 2023 Arete

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package it.aretesoftware.gdx.jvdf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Writes VDF documents into a {@link StringBuilder}.
 * @author BucketOfBroccoli */
public class VDFWriter {

    private static final String SPACE = " ";
    private static final String WHITESPACE = "    ";
    private static final String NEWLINE = "\n";
    private static final String QUOTES = "\"";
    private static final String NODE_START = "{";
    private static final String NODE_END = "}";

    private final StringBuilder builder, whitespace;

    public VDFWriter() {
        this.builder = new StringBuilder();
        this.whitespace = new StringBuilder();
    }


    /**
     * Writes the start of a {@link VDFNode}.
     * @param name the name of the newly started {@link VDFNode}
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeNodeStart(String name) {
        builder.append(whitespace)
                .append(QUOTES).append(name).append(QUOTES)
                .append(NEWLINE)
                .append(whitespace).append(NODE_START).append(NEWLINE);
        whitespace.append(WHITESPACE);
        return this;
    }

    /**
     * Writes the end of the previously started {@link VDFNode}.
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeNodeEnd() {
        if (whitespace.length() >= 4) {
            whitespace.setLength(whitespace.length() - 4);
        }
        builder.append(whitespace).append(NODE_END).append(NEWLINE);
        return this;
    }

    /**
     * Writes a node in the current {@link VDFNode}.
     * Note that this uses the {@link VDFNode}'s toVDFString() output,
     * meaning that the "prev" and "next" nodes will not be written.
     * @param node to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeNode(VDFNode node) {
        builder.append(whitespace)
                .append(node.toVDFString())
                .append(NEWLINE);
        return this;
    }

    /**
     * Writes multiple nodes in the current {@link VDFNode}.
     * Note that this uses the {@link VDFNode}'s toVDFString() output,
     * meaning that the "prev" and "next" nodes will not be written.
     * @param nodes to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeNodes(VDFNode... nodes) {
        for (VDFNode node : nodes) {
            writeNode(node);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a String
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, String value) {
        builder.append(whitespace)
                .append(QUOTES).append(name).append(QUOTES)
                .append(SPACE)
                .append(QUOTES).append(value).append(QUOTES)
                .append(NEWLINE);
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a float
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, float value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a double
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, double value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a long
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, long value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a int
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, int value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a boolean
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, boolean value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a byte
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, byte value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a short
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, short value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a char
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, char value) {
        return this.writeValue(name, String.valueOf(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a {@link Color}
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, Color value) {
        return this.writeValue(name, VDFUtils.toColorString(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a {@link Vector3}
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, Vector3 value) {
        return this.writeValue(name, VDFUtils.toVector3String(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as a {@link Vector2}
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeValue(String name, Vector2 value) {
        return this.writeValue(name, VDFUtils.toVector2String(value));
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated value
     * @param value the value to write, as an Enum
     * @return this {@link VDFWriter} for chaining */
    public <T extends Enum<T>> VDFWriter writeValue(String name, T value) {
        return this.writeValue(name, value.name());
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the String array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, String... values) {
        for (String value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the float array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, float... values) {
        for (float value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the double array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, double... values) {
        for (double value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the long array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, long... values) {
        for (long value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the int array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, int... values) {
        for (int value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the boolean array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, boolean... values) {
        for (boolean value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the byte array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, byte... values) {
        for (byte value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the short array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, short... values) {
        for (short value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the char array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, char... values) {
        for (char value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the {@link Color} array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, Color... values) {
        for (Color value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the {@link Vector3} array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, Vector3... values) {
        for (Vector3 value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the {@link Vector2} array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, Vector2... values) {
        for (Vector2 value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the Enum array of values to write
     * @return this {@link VDFWriter} for chaining */
    public <T extends Enum<T>> VDFWriter writeMultimapValue(String name, T... values) {
        for (T value : values) {
            writeValue(name, value);
        }
        return this;
    }

    /**
     * Writes a value in the current {@link VDFNode}.
     * @param name the name of the associated values
     * @param values the Object array of values to write
     * @return this {@link VDFWriter} for chaining */
    public VDFWriter writeMultimapValue(String name, Object[] values) {
        for (Object value : values) {
            this.writeValue(name, VDFUtils.toString(value));
        }
        return this;
    }

    @Override
    public String toString() {
        return toVDFString();
    }

    /**
     * Returns the contents of the {@link StringBuilder}, in a VDF format.
     * @return the VDF document as a String. */
    public String toVDFString() {
        return builder.toString();
    }

    /**
     * Returns the parsed contents of the {@link StringBuilder}.
     * @return the VDF document as a {@link VDFNode}. */
    public VDFNode toVDFNode() {
        return new VDFParser().parse(toVDFString());
    }

}
