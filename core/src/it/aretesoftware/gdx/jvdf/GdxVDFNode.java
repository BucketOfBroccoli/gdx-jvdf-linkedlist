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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/** Container for a VDFNode.
 * <p>
 * VDFNode children are a linked list. Iteration of arrays or objects is easily done using an iterator or the {@link #next()}
 * field, both shown below. This is much more efficient than accessing children by index when there are many children.<br>
 * </p>
 *
 * <pre>
 * VDFNode map = ...;
 * // Allocates an iterator:
 * for (VDFNode entry : map)
 * 	System.out.println(entry.name + " = " + entry.asString());
 * // No allocation:
 * for (VDFNode entry = map.child; entry != null; entry = entry.next)
 * 	System.out.println(entry.name + " = " + entry.asString());
 * </pre>
 *
 * @author Nathan Sweet
 * @author BucketOfBroccoli */
public class GdxVDFNode {

    /** May be null. */
    private String value;

    public String name;
    /** May be null. */
    public GdxVDFNode child, parent;
    /** May be null. When changing this field the parent {@link #size()} may need to be changed. */
    public GdxVDFNode next, prev;
    public int size;

    public GdxVDFNode() {
        this(null);
    }

    /** @param value May be null. */
    public GdxVDFNode(String value) {
        this.value = value;
    }


    /** Returns the child at the specified index. This requires walking the linked list to the specified entry, see
     * {@link GdxVDFNode} for how to iterate efficiently.
     * @return May be null. */
    public GdxVDFNode get (int index) {
        GdxVDFNode current = child;
        while (current != null && index > 0) {
            index--;
            current = current.next;
        }
        return current;
    }

    /** Returns the child with the specified name.
     * @return May be null. */
    public GdxVDFNode get (String name) {
        GdxVDFNode current = child;
        while (current != null && (current.name == null || !current.name.equalsIgnoreCase(name)))
            current = current.next;
        return current;
    }

    /**
     * Returns the child with the specified name & index.
     * @return May be null.
     */
    public GdxVDFNode get (String name, int index) {
        GdxVDFNode current = child;
        while (current != null && index >= 0) {
            if (name.equalsIgnoreCase(current.name)) {
                index--;
            }
            if (index >= 0) {
                current = current.next;
            }
        }
        return current;
    }

    /** Returns true if a child with the specified name exists. */
    public boolean has (String name) {
        return get(name) != null;
    }

    /** Returns an iterator for the child with the specified name, or an empty iterator if no child is found. */
    public GdxVDFIterator iterator (String name) {
        GdxVDFNode current = get(name);
        if (current == null) {
            GdxVDFIterator iter = new GdxVDFIterator();
            iter.entry = null;
            return iter;
        }
        return current.iterator();
    }

    /** Returns the child at the specified index. This requires walking the linked list to the specified entry, see
     * {@link GdxVDFNode} for how to iterate efficiently.
     * @throws IllegalArgumentException if the child was not found. */
    public GdxVDFNode require (int index) {
        GdxVDFNode current = get(index);
        if (current == null) throw new IllegalArgumentException("Child not found with index: " + index);
        return current;
    }

    /** Returns the child with the specified name.
     * @throws IllegalArgumentException if the child was not found. */
    public GdxVDFNode require (String name) {
        GdxVDFNode current = get(name);
        if (current == null) throw new IllegalArgumentException("Child not found with name: " + name);
        return current;
    }

    /** Removes the child with the specified index. This requires walking the linked list to the specified entry, see
     * {@link GdxVDFNode} for how to iterate efficiently.
     * @return May be null. */
    public GdxVDFNode remove (int index) {
        GdxVDFNode child = get(index);
        if (child == null) return null;
        if (child.prev == null) {
            this.child = child.next;
            if (this.child != null) this.child.prev = null;
        } else {
            child.prev.next = child.next;
            if (child.next != null) child.next.prev = child.prev;
        }
        size--;
        return child;
    }

    /** Removes the child with the specified name.
     * @return May be null. */
    public GdxVDFNode remove (String name) {
        GdxVDFNode child = get(name);
        if (child == null) return null;
        if (child.prev == null) {
            this.child = child.next;
            if (this.child != null) this.child.prev = null;
        } else {
            child.prev.next = child.next;
            if (child.next != null) child.next.prev = child.prev;
        }
        size--;
        return child;
    }

    /** Removes this value from its parent. */
    public void remove () {
        if (parent == null) throw new IllegalStateException();
        if (prev == null) {
            parent.child = next;
            if (parent.child != null) parent.child.prev = null;
        } else {
            prev.next = next;
            if (next != null) next.prev = prev;
        }
        parent.size--;
    }

    /** Returns true if there are one or more children in the array or object. */
    public boolean notEmpty () {
        return size > 0;
    }

    /** Returns true if there are not children in the array or object. */
    public boolean isEmpty () {
        return size == 0;
    }

    /** @deprecated Use {@link #size} instead. Returns this number of children in the array or object. */
    @Deprecated
    public int size () {
        return size;
    }

    public int sizeOf(String name) {
        GdxVDFNode current = child;
        int count = 0;
        while (current != null) {
            if (name.equalsIgnoreCase(current.name)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    /** Returns this value as a string.
     * @return May be null if this value is null.
     * @throws IllegalStateException if this an array or object. */
    public String asString () {
        return value;
    }

    public String asString (String defaultValue) {
        return !isNull() ? value : defaultValue;
    }

    /** Returns this value as a float.
     * @throws IllegalStateException if this an array or object. */
    public float asFloat () {
        checkNullValue("float");
        return Float.parseFloat(value);
    }

    public float asFloat(float defaultValue) {
        try {
            return asFloat();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns this value as a double.
     * @throws IllegalStateException if this an array or object. */
    public double asDouble () {
        checkNullValue("double");
        return Double.parseDouble(value);
    }

    public double asDouble(double defaultValue) {
        try {
            return asDouble();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns this value as a long.
     * @throws IllegalStateException if this an array or object. */
    public long asLong () {
        checkNullValue("long");
        return Long.parseLong(value);
    }

    public long asLong(long defaultValue) {
        try {
            return asLong();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns this value as an int.
     * @throws IllegalStateException if this an array or object. */
    public int asInt () {
        checkNullValue("int");
        return Integer.parseInt(value);
    }

    public long asInt(int defaultValue) {
        try {
            return asInt();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns this value as a boolean.
     * @throws IllegalStateException if this an array or object. */
    public boolean asBoolean () {
        checkNullValue("boolean");
        return Boolean.parseBoolean(value);
    }

    public boolean asBoolean(boolean defaultValue) {
        try {
            return asBoolean();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns this value as a byte.
     * @throws IllegalStateException if this an array or object. */
    public byte asByte () {
        checkNullValue("byte");
        return Byte.parseByte(value);
    }

    public byte asByte(byte defaultValue) {
        try {
            return asByte();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns this value as a short.
     * @throws IllegalStateException if this an array or object. */
    public short asShort () {
        checkNullValue("short");
        return Short.parseShort(value);
    }

    public short asShort(short defaultValue) {
        try {
            return asShort();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public char asShort(char defaultValue) {
        try {
            return asChar();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public char asChar () {
        try {
            long value = asLong();
            return (char) value;
        }
        catch (Exception e) {
            checkNullValue("char");
            if (value.length() > 1) throw new IllegalStateException("String has more than one character.");
            return value.charAt(0);
        }
    }

    public char asChar (char defaultValue) {
        try {
            return asChar();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public Color asColor () {
        checkNullValue("Color");
        return GdxVDFUtils.toColor(asString());
    }

    public Color asColor (Color defaultValue) {
        try {
            return asColor();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public Vector3 asVector3 () {
        checkNullValue("Vector3");
        return GdxVDFUtils.toVector3(asString());
    }

    public Vector3 asVector3 (Vector3 defaultValue) {
        try {
            return asVector3();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public Vector2 asVector2 () {
        checkNullValue("Vector2");
        return GdxVDFUtils.toVector2(asString());
    }

    public Vector2 asVector2 (Vector2 defaultValue) {
        try {
            return asVector2();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public <T extends Enum<T>> T asEnum(Class<T> enumClass) {
        checkNullValue(enumClass.getSimpleName());
        return GdxVDFUtils.toEnum(asString(), enumClass);
    }

    public <T extends Enum<T>> T asEnum(T defaultValue) {
        try {
            checkNullValue(defaultValue.getDeclaringClass().getSimpleName());
            return GdxVDFUtils.toEnum(asString(), defaultValue);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @throws IllegalStateException if this node's value is null. */
    private void checkNullValue(String type) {
        if (isNull()) {
            throw new IllegalStateException("Value cannot be converted to " + type);
        }
    }

    public List<GdxVDFNode> asArray(String key) {
        List<GdxVDFNode> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value);
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated String array.
     * @throws IllegalStateException if this is not an array. */
    public List<String> asStringArray (String key) {
        List<String> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.value);
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated float array.
     * @throws IllegalStateException if this is not an array. */
    public List<Float> asFloatArray (String key) {
        List<Float> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asFloat());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated double array.
     * @throws IllegalStateException if this is not an array. */
    public List<Double> asDoubleArray (String key) {
        List<Double> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asDouble());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated long array.
     * @throws IllegalStateException if this is not an array. */
    public List<Long> asLongArray (String key) {
        List<Long> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asLong());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated int array.
     * @throws IllegalStateException if this is not an array. */
    public List<Integer> asIntArray (String key) {
        List<Integer> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asInt());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated boolean array.
     * @throws IllegalStateException if this is not an array. */
    public List<Boolean> asBooleanArray (String key) {
        List<Boolean> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asBoolean());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated byte array.
     * @throws IllegalStateException if this is not an array. */
    public List<Byte> asByteArray (String key) {
        List<Byte> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asByte());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated short array.
     * @throws IllegalStateException if this is not an array. */
    public List<Short> asShortArray (String key) {
        List<Short> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asShort());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated char array.
     * @throws IllegalStateException if this is not an array. */
    public List<Character> asCharArray (String key) {
        List<Character> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asChar());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated Color array.
     * @throws IllegalStateException if this is not an array. */
    public List<Color> asColorArray (String key) {
        List<Color> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asColor());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated Vector3 array.
     * @throws IllegalStateException if this is not an array. */
    public List<Vector3> asVector3Array (String key) {
        List<Vector3> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asVector3());
            }
        }
        return list;
    }

    /** Returns the children of this value as a newly allocated Vector2 array.
     * @throws IllegalStateException if this is not an array. */
    public List<Vector2> asVector2Array (String key) {
        List<Vector2> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asVector2());
            }
        }
        return list;
    }

    public <T extends Enum<T>> List<T> asEnumArray (String key, Class<T> enumClass) {
        List<T> list = new ArrayList<>();
        int i = 0;
        for (GdxVDFNode value = child; value != null; value = value.next, i++) {
            if (key.equals(value.name)) {
                list.add(value.asEnum(enumClass));
            }
        }
        return list;
    }

    /** Returns true if a child with the specified name exists and has a child. */
    public boolean hasChild (String name) {
        return getChild(name) != null;
    }

    /** Finds the child with the specified name and returns its first child.
     * @return May be null. */
    public GdxVDFNode getChild (String name) {
        GdxVDFNode child = get(name);
        return child == null ? null : child.child;
    }

    /** Finds the child with the specified name & index and returns it as a string. Returns defaultValue if not found.
     * @param defaultValue May be null. */
    public String getStringOfIndex(String name, int namedIndex, String defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asString();
    }

    /** Finds the child with the specified name & index and returns it as a float. Returns defaultValue if not found. */
    public float getFloatOfIndex(String name, int namedIndex, float defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asFloat();
    }

    /** Finds the child with the specified name & index and returns it as a double. Returns defaultValue if not found. */
    public double getDoubleOfIndex(String name, int namedIndex, double defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asDouble();
    }

    /** Finds the child with the specified name & index and returns it as a long. Returns defaultValue if not found. */
    public long getLongOfIndex(String name, int namedIndex, long defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asLong();
    }

    /** Finds the child with the specified name & index and returns it as an int. Returns defaultValue if not found. */
    public int getIntOfIndex(String name, int namedIndex, int defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asInt();
    }

    /** Finds the child with the specified name & index and returns it as a boolean. Returns defaultValue if not found. */
    public boolean getBooleanOfIndex(String name, int namedIndex, boolean defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asBoolean();
    }

    /** Finds the child with the specified name & index and returns it as a byte. Returns defaultValue if not found. */
    public byte getByteOfIndex(String name, int namedIndex, byte defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asByte();
    }

    /** Finds the child with the specified name & index and returns it as a short. Returns defaultValue if not found. */
    public short getShortOfIndex(String name, int namedIndex, short defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asShort();
    }

    /** Finds the child with the specified name & index and returns it as a char. Returns defaultValue if not found. */
    public char getCharOfIndex(String name, int namedIndex, char defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asChar();
    }

    /** Finds the child with the specified name & index and returns it as a Color. Returns defaultValue if not found. */
    public Color getColorOfIndex(String name, int namedIndex, Color defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asColor();
    }

    /** Finds the child with the specified name & index and returns it as a Vector3. Returns defaultValue if not found. */
    public Vector3 getVector3OfIndex(String name, int namedIndex, Vector3 defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asVector3();
    }

    /** Finds the child with the specified name & index and returns it as a Vector2. Returns defaultValue if not found. */
    public Vector2 getVector2OfIndex(String name, int namedIndex, Vector2 defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asVector2();
    }

    /** Finds the child with the specified name & index and returns it as an Enum. Returns defaultValue if not found. */
    public <T extends Enum<T>> T getEnumOfIndex(String name, int namedIndex, T defaultValue) {
        GdxVDFNode child = get(name, namedIndex);
        return (child == null || child.isNull()) ? defaultValue : child.asEnum(defaultValue);
    }

    /** Finds the child with the specified name & index and returns it as a string.
     * @throws IllegalArgumentException if the child was not found. */
    public String getStringOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asString();
    }

    /** Finds the child with the specified name & index and returns it as a float.
     * @throws IllegalArgumentException if the child was not found. */
    public float getFloatOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asFloat();
    }

    /** Finds the child with the specified name & index and returns it as a double.
     * @throws IllegalArgumentException if the child was not found. */
    public double getDoubleOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asDouble();
    }

    /** Finds the child with the specified name & index and returns it as a long.
     * @throws IllegalArgumentException if the child was not found. */
    public long getLongOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asLong();
    }

    /** Finds the child with the specified name & index and returns it as an int.
     * @throws IllegalArgumentException if the child was not found. */
    public int getIntOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asInt();
    }

    /** Finds the child with the specified name & index and returns it as a boolean.
     * @throws IllegalArgumentException if the child was not found. */
    public boolean getBooleanOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asBoolean();
    }

    /** Finds the child with the specified name & index and returns it as a byte.
     * @throws IllegalArgumentException if the child was not found. */
    public byte getByteOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asByte();
    }

    /** Finds the child with the specified name & index and returns it as a short.
     * @throws IllegalArgumentException if the child was not found. */
    public short getShortOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asShort();
    }

    /** Finds the child with the specified name & index and returns it as a char.
     * @throws IllegalArgumentException if the child was not found. */
    public char getCharOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asChar();
    }

    /** Finds the child with the specified name & index and returns it as a Color.
     * @throws IllegalArgumentException if the child was not found. */
    public Color getColorOfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asColor();
    }

    /** Finds the child with the specified name & index and returns it as a Vector3.
     * @throws IllegalArgumentException if the child was not found. */
    public Vector3 getVector3OfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asVector3();
    }

    /** Finds the child with the specified name & index and returns it as a Vector2.
     * @throws IllegalArgumentException if the child was not found. */
    public Vector2 getVector2OfIndex(String name, int namedIndex) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asVector2();
    }

    /** Finds the child with the specified name & index and returns it as an Enum.
     * @throws IllegalArgumentException if the child was not found. */
    public <T extends Enum<T>> T getEnumOfIndex(String name, int namedIndex, Class<T> enumClass) {
        GdxVDFNode child = get(name, namedIndex);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name + " for index: " + namedIndex);
        return child.asEnum(enumClass);
    }

    /** Finds the child with the specified name and returns it as a string. Returns defaultValue if not found.
     * @param defaultValue May be null. */
    public String getString (String name, String defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asString();
    }

    /** Finds the child with the specified name and returns it as a float. Returns defaultValue if not found. */
    public float getFloat (String name, float defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asFloat();
    }

    /** Finds the child with the specified name and returns it as a double. Returns defaultValue if not found. */
    public double getDouble (String name, double defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asDouble();
    }

    /** Finds the child with the specified name and returns it as a long. Returns defaultValue if not found. */
    public long getLong (String name, long defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asLong();
    }

    /** Finds the child with the specified name and returns it as an int. Returns defaultValue if not found. */
    public int getInt (String name, int defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asInt();
    }

    /** Finds the child with the specified name and returns it as a boolean. Returns defaultValue if not found. */
    public boolean getBoolean (String name, boolean defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asBoolean();
    }

    /** Finds the child with the specified name and returns it as a byte. Returns defaultValue if not found. */
    public byte getByte (String name, byte defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asByte();
    }

    /** Finds the child with the specified name and returns it as a short. Returns defaultValue if not found. */
    public short getShort (String name, short defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asShort();
    }

    /** Finds the child with the specified name and returns it as a char. Returns defaultValue if not found. */
    public char getChar (String name, char defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asChar();
    }

    /** Finds the child with the specified name and returns it as a Color. Returns defaultValue if not found. */
    public Color getColor (String name, Color defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asColor();
    }

    /** Finds the child with the specified name and returns it as a Vector3. Returns defaultValue if not found. */
    public Vector3 getVector3 (String name, Vector3 defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asVector3();
    }

    /** Finds the child with the specified name and returns it as a Vector2. Returns defaultValue if not found. */
    public Vector2 getVector2 (String name, Vector2 defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asVector2();
    }

    /** Finds the child with the specified name and returns it as an Enum. Returns defaultValue if not found. */
    public <T extends Enum<T>> T getEnum (String name, T defaultValue) {
        GdxVDFNode child = get(name);
        return (child == null || child.isNull()) ? defaultValue : child.asEnum(defaultValue);
    }

    /** Finds the child with the specified name and returns it as a string.
     * @throws IllegalArgumentException if the child was not found. */
    public String getString (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asString();
    }

    /** Finds the child with the specified name and returns it as a float.
     * @throws IllegalArgumentException if the child was not found. */
    public float getFloat (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asFloat();
    }

    /** Finds the child with the specified name and returns it as a double.
     * @throws IllegalArgumentException if the child was not found. */
    public double getDouble (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asDouble();
    }

    /** Finds the child with the specified name and returns it as a long.
     * @throws IllegalArgumentException if the child was not found. */
    public long getLong (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asLong();
    }

    /** Finds the child with the specified name and returns it as an int.
     * @throws IllegalArgumentException if the child was not found. */
    public int getInt (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asInt();
    }

    /** Finds the child with the specified name and returns it as a boolean.
     * @throws IllegalArgumentException if the child was not found. */
    public boolean getBoolean (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asBoolean();
    }

    /** Finds the child with the specified name and returns it as a byte.
     * @throws IllegalArgumentException if the child was not found. */
    public byte getByte (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asByte();
    }

    /** Finds the child with the specified name and returns it as a short.
     * @throws IllegalArgumentException if the child was not found. */
    public short getShort (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asShort();
    }

    /** Finds the child with the specified name and returns it as a char.
     * @throws IllegalArgumentException if the child was not found. */
    public char getChar (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asChar();
    }

    /** Finds the child with the specified name and returns it as a Color.
     * @throws IllegalArgumentException if the child was not found. */
    public Color getColor (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asColor();
    }

    /** Finds the child with the specified name and returns it as a Vector3.
     * @throws IllegalArgumentException if the child was not found. */
    public Vector3 getVector3 (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asVector3();
    }

    /** Finds the child with the specified name and returns it as a Vector2.
     * @throws IllegalArgumentException if the child was not found. */
    public Vector2 getVector2 (String name) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asVector2();
    }

    /** Finds the child with the specified name and returns it as an Enum.
     * @throws IllegalArgumentException if the child was not found. */
    public <T extends Enum<T>> T getEnum (String name, Class<T> enumClass) {
        GdxVDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asEnum(enumClass);
    }

    /** Finds the child with the specified index and returns it as a string.
     * @throws IllegalArgumentException if the child was not found. */
    public String getString (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asString();
    }

    /** Finds the child with the specified index and returns it as a float.
     * @throws IllegalArgumentException if the child was not found. */
    public float getFloat (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asFloat();
    }

    /** Finds the child with the specified index and returns it as a double.
     * @throws IllegalArgumentException if the child was not found. */
    public double getDouble (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asDouble();
    }

    /** Finds the child with the specified index and returns it as a long.
     * @throws IllegalArgumentException if the child was not found. */
    public long getLong (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asLong();
    }

    /** Finds the child with the specified index and returns it as an int.
     * @throws IllegalArgumentException if the child was not found. */
    public int getInt (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asInt();
    }

    /** Finds the child with the specified index and returns it as a boolean.
     * @throws IllegalArgumentException if the child was not found. */
    public boolean getBoolean (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asBoolean();
    }

    /** Finds the child with the specified index and returns it as a byte.
     * @throws IllegalArgumentException if the child was not found. */
    public byte getByte (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asByte();
    }

    /** Finds the child with the specified index and returns it as a short.
     * @throws IllegalArgumentException if the child was not found. */
    public short getShort (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asShort();
    }

    /** Finds the child with the specified index and returns it as a char.
     * @throws IllegalArgumentException if the child was not found. */
    public char getChar (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asChar();
    }

    /** Finds the child with the specified index and returns it as a Color.
     * @throws IllegalArgumentException if the child was not found. */
    public Color getColor (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asColor();
    }

    /** Finds the child with the specified index and returns it as a Color.
     * @throws IllegalArgumentException if the child was not found. */
    public Vector3 getVector3 (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asVector3();
    }

    /** Finds the child with the specified index and returns it as a Color.
     * @throws IllegalArgumentException if the child was not found. */
    public Vector2 getVector2 (int index) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asVector2();
    }

    /** Finds the child with the specified index and returns it as an Enum.
     * @throws IllegalArgumentException if the child was not found. */
    public <T extends Enum<T>> T getEnum (int index, Class<T> enumClass) {
        GdxVDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asEnum(enumClass);
    }

    /** Returns the name for this object value.
     * @return May be null. */
    public String name () {
        return name;
    }

    /** Returns the parent for this value.
     * @return May be null. */
    public GdxVDFNode parent () {
        return parent;
    }

    /** Returns the first child for this object or array.
     * @return May be null. */
    public GdxVDFNode child () {
        return child;
    }

    /**
     * @return whether this node has a parent. */
    public boolean hasParent () {
        return parent != null;
    }

    /**
     * @return whether the value of this node is null. */
    public boolean isNull () {
        return value == null;
    }

    /** Sets the name of the specified value and adds it after the last child. */
    public void addChild (String name, GdxVDFNode value) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        value.name = name;
        addChild(value);
    }

    /** Adds the specified value after the last child.
     * @throws IllegalStateException if this is an object and the specified child's name is null. */
    public void addChild (GdxVDFNode value) {
        if (value.name == null) throw new IllegalStateException("An object child requires a name: " + value);
        value.parent = this;
        size++;
        GdxVDFNode current = child;
        if (current == null)
            child = value;
        else {
            while (true) {
                if (current.next == null) {
                    current.next = value;
                    value.prev = current;
                    return;
                }
                current = current.next;
            }
        }
    }

    /** Returns the next sibling of this value.
     * @return May be null. */
    public GdxVDFNode next () {
        return next;
    }

    /** Returns the previous sibling of this value.
     * @return May be null. */
    public GdxVDFNode prev () {
        return prev;
    }

    /** @param value May be null. */
    public void set (String value) {
        this.value = value;
    }

    public GdxVDFIterator iterator () {
        return new GdxVDFIterator();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("\"").append(name).append("\"");
            builder.append(" ");
            builder.append("\"").append(value).append("\"");
            return builder.toString();
        }
        else {
            return toVDF();
        }
    }

    /**
     * Returns a human readable string representing the path from the root of the VDF object graph to this value.
     */
    public String toVDF() {
        return toVDF(this, new StringBuilder(), new StringBuilder());
    }

    private String toVDF(GdxVDFNode root, StringBuilder whitespace, StringBuilder builder) {
        GdxVDFNode current = root.parent != null ? root : root.child;
        while (current != null) {
            builder.append(whitespace);
            builder.append("\"").append(current.name).append("\"");
            builder.append(" ");
            if (current.isEmpty() && !current.isNull()) {
                builder.append("\"").append(current.value).append("\"");
            }
            else {
                GdxVDFNode child = current.child;
                builder.append("\n");
                builder.append(whitespace);
                builder.append("{");
                if (child != null) {
                    builder.append("\n");
                    whitespace.append("    ");
                    toVDF(current.child, whitespace, builder);
                    whitespace.setLength(whitespace.length() - 4);
                    builder.append(whitespace);
                }
                else {
                    builder.append("\n");
                    builder.append(whitespace);
                }
                builder.append("}");
            }
            builder.append("\n");
            current = current.next;
        }
        return builder.toString();
    }


    public class GdxVDFIterator implements Iterator<GdxVDFNode>, Iterable<GdxVDFNode> {
        GdxVDFNode entry = child;
        GdxVDFNode current;

        @Override
        public boolean hasNext () {
            return entry != null;
        }

        @Override
        public GdxVDFNode next () {
            current = entry;
            if (current == null) throw new NoSuchElementException();
            entry = current.next;
            return current;
        }

        @Override
        public void remove () {
            if (current.prev == null) {
                child = current.next;
                if (child != null) child.prev = null;
            } else {
                current.prev.next = current.next;
                if (current.next != null) current.next.prev = current.prev;
            }
            size--;
        }

        @Override
        public Iterator<GdxVDFNode> iterator () {
            return this;
        }
    }

}
