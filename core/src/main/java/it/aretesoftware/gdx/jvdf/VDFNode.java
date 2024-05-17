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
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/** Container for a VDFNode.
 * <p>
 * VDFNode children are a linked list, therefore iteration is easily done using an iterator or the {@link #next()}
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
public class VDFNode {

    /** May be null. */
    private String value;

    public String name;
    /** May be null. */
    public VDFNode child, parent;
    /** May be null. When changing this field the parent {@link #size()} may need to be changed. */
    public VDFNode next, prev;
    public int size;

    public VDFNode() {
        this(null);
    }

    /** @param value May be null. */
    public VDFNode(String value) {
        this.value = value;
    }


    /** Returns the child at the specified index. This requires walking the linked list to the specified entry,
     * see {@link VDFNode} for how to iterate efficiently.
     * @param index of the child to return
     * @return the child, may be null */
    public VDFNode get (int index) {
        VDFNode current = child;
        while (current != null && index > 0) {
            index--;
            current = current.next;
        }
        return current;
    }

    /**
     * Returns the child node with the specified name.
     * @param name of the child
     * @return the child node, may be null */
    public VDFNode get (String name) {
        VDFNode current = child;
        while (current != null && (current.name == null || !current.name.equalsIgnoreCase(name)))
            current = current.next;
        return current;
    }

    /** Returns the child node with the specified name and index.
     * @param name of the child
     * @param index of the child
     * @return the child node, may be null */
    public VDFNode get (String name, int index) {
        VDFNode current = child;
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

    /** @return true if a child node with the specified name exists */
    public boolean has (String name) {
        return get(name) != null;
    }

    /** Returns an iterator for the child node with the specified name, or an empty iterator if no child is found. */
    public VDFIterator iterator (String name) {
        VDFNode current = get(name);
        if (current == null) {
            VDFIterator iter = new VDFIterator();
            iter.entry = null;
            return iter;
        }
        return current.iterator();
    }

    /** Returns the child node at the specified index. This requires walking the linked list to the specified entry, see
     * {@link VDFNode} for how to iterate efficiently.
     * @throws IllegalArgumentException if the child was not found */
    public VDFNode require (int index) {
        VDFNode current = get(index);
        if (current == null) throw new IllegalArgumentException("Child not found with index: " + index);
        return current;
    }

    /**
     * @return the child node with the specified name
     * @throws IllegalArgumentException if the child was not found */
    public VDFNode require (String name) {
        VDFNode current = get(name);
        if (current == null) throw new IllegalArgumentException("Child not found with name: " + name);
        return current;
    }

    /** Removes the child with the specified index. This requires walking the linked list to the specified entry, see
     * {@link VDFNode} for how to iterate efficiently.
     * @return the removed child, may be null */
    public VDFNode remove (int index) {
        VDFNode child = get(index);
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

    /** Removes the child node with the specified name.
     * @return the removed child, may be null */
    public VDFNode remove (String name) {
        VDFNode child = get(name);
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

    /** Removes this node from its parent.
     * @throws IllegalStateException if parent is null */
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

    /** @return true if this node has one or more children */
    public boolean notEmpty () {
        return size > 0;
    }

    /** @return true if this node has no children */
    public boolean isEmpty () {
        return size == 0;
    }

    /** @deprecated Use {@link #size} instead
     * @return the number of children of this node.*/
    @Deprecated
    public int size () {
        return size;
    }

    public int sizeOf(String name) {
        VDFNode current = child;
        int count = 0;
        while (current != null) {
            if (name.equalsIgnoreCase(current.name)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    /** @return this node's value as a String, may be null. */
    public String asString () {
        return value;
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public String asString (String defaultValue) {
        return !isNull() ? value : defaultValue;
    }

    /** @return this node's value as a float.
     * @throws IllegalStateException if value is null. */
    public float asFloat () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Float.parseFloat(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public float asFloat(float defaultValue) {
        try {
            return asFloat();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a double.
     * @throws IllegalStateException if value is null. */
    public double asDouble () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Double.parseDouble(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public double asDouble(double defaultValue) {
        try {
            return asDouble();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a long.
     * @throws IllegalStateException if value is null. */
    public long asLong () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Long.parseLong(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public long asLong(long defaultValue) {
        try {
            return asLong();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as an int
     * @throws IllegalStateException if value is null */
    public int asInt () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Integer.parseInt(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public int asInt(int defaultValue) {
        try {
            return asInt();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a boolean
     * @throws IllegalStateException if value is null */
    public boolean asBoolean () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public boolean asBoolean(boolean defaultValue) {
        try {
            return asBoolean();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a byte
     * @throws IllegalStateException if value is null */
    public byte asByte () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Byte.parseByte(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public byte asByte(byte defaultValue) {
        try {
            return asByte();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a short
     * @throws IllegalStateException if value is null */
    public short asShort () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return Short.parseShort(value);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public short asShort(short defaultValue) {
        try {
            return asShort();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public char asShort(char defaultValue) {
        try {
            return asChar();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a char
     * @throws IllegalStateException if value is null
     * @throws IllegalStateException if string has more than one character */
    public char asChar () {
        try {
            long value = asLong();
            return (char) value;
        }
        catch (Exception e) {
            if (isNull()) {
                throw new IllegalStateException("Value is null.");
            }
            if (value.length() > 1) throw new IllegalStateException("String has more than one character.");
            return value.charAt(0);
        }
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public char asChar (char defaultValue) {
        try {
            return asChar();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a {@link Color}
     * @throws IllegalStateException if value is null */
    public Color asColor () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return VDFUtils.toColor(asString());
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public Color asColor (Color defaultValue) {
        try {
            return asColor();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a {@link Vector3}
     * @throws IllegalStateException if value is null */
    public Vector3 asVector3 () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return VDFUtils.toVector3(asString());
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public Vector3 asVector3 (Vector3 defaultValue) {
        try {
            return asVector3();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** @return this node's value as a {@link Vector2}
     * @throws IllegalStateException if value is null */
    public Vector2 asVector2 () {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return VDFUtils.toVector2(asString());
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public Vector2 asVector2 (Vector2 defaultValue) {
        try {
            return asVector2();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return this node's value as an Enum
     * @throws IllegalStateException if value is null */
    public <T extends Enum<T>> T asEnum(Class<T> enumClass) {
        if (isNull()) {
            throw new IllegalStateException("Value is null.");
        }
        return VDFUtils.toEnum(asString(), enumClass);
    }

    /**
     * @param defaultValue to return if this node's value is null */
    public <T extends Enum<T>> T asEnum(T defaultValue) {
        try {
            if (isNull()) {
                throw new IllegalStateException("Value is null.");
            }
            return VDFUtils.toEnum(asString(), defaultValue);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /** Returns the children nodes as a newly allocated {@link Array}.
     * @param name of the children nodes
     * @return {@link Array} filled with said nodes */
    public Array<VDFNode> asArray(String name) {
        Array<VDFNode> list = new Array<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value);
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated String {@link Array}.
     * @param name of the children nodes
     * @return String {@link Array} filled with said nodes' values */
    public List<String> asStringArray (String name) {
        List<String> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.value);
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated float {@link Array}.
     * @param name of the children nodes
     * @return float {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Float> asFloatArray (String name) {
        List<Float> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asFloat());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated double {@link Array}.
     * @param name of the children nodes
     * @return double {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Double> asDoubleArray (String name) {
        List<Double> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asDouble());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated long {@link Array}.
     * @param name of the children nodes
     * @return long {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Long> asLongArray (String name) {
        List<Long> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asLong());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated int {@link Array}.
     * @param name of the children nodes
     * @return int {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Integer> asIntArray (String name) {
        List<Integer> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asInt());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated boolean {@link Array}.
     * @param name of the children nodes
     * @return boolean {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Boolean> asBooleanArray (String name) {
        List<Boolean> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asBoolean());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated byte {@link Array}.
     * @param name of the children nodes
     * @return byte {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Byte> asByteArray (String name) {
        List<Byte> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asByte());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated short {@link Array}.
     * @param name of the children nodes
     * @return short {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Short> asShortArray (String name) {
        List<Short> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asShort());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated char {@link Array}.
     * @param name of the children nodes
     * @return char {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Character> asCharArray (String name) {
        List<Character> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asChar());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated {@link Color} {@link Array}.
     * @param name of the children nodes
     * @return {@link Color} {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Color> asColorArray (String name) {
        List<Color> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asColor());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated {@link Vector3} {@link Array}.
     * @param name of the children nodes
     * @return {@link Vector3} {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Vector3> asVector3Array (String name) {
        List<Vector3> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asVector3());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated {@link Vector2} {@link Array}.
     * @param name of the children nodes
     * @return {@link Vector2} {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public List<Vector2> asVector2Array (String name) {
        List<Vector2> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asVector2());
            }
        }
        return list;
    }

    /** Returns the values of children nodes as a newly allocated Enum {@link Array}.
     * @param name of the children nodes
     * @param enumClass of the Enum the values represent
     * @return Enum {@link Array} filled with said nodes' values
     * @throws IllegalStateException if any of the values are null */
    public <T extends Enum<T>> List<T> asEnumArray (String name, Class<T> enumClass) {
        List<T> list = new ArrayList<>();
        int i = 0;
        for (VDFNode value = child; value != null; value = value.next, i++) {
            if (name.equals(value.name)) {
                list.add(value.asEnum(enumClass));
            }
        }
        return list;
    }

    /** @return true if a child with the specified name exists and has a child */
    public boolean hasChild (String name) {
        return getChild(name) != null;
    }

    /** Finds the child node with the specified name and returns its first child.
     * @return the node, may be null */
    public VDFNode getChild (String name) {
        VDFNode child = get(name);
        return child == null ? null : child.child;
    }

    /** Finds the child node with the specified name and returns its value as a String.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a String */
    public String getString (String name, String defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asString();
    }

    /** Finds the child node with the specified name and returns its value as a float.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a float */
    public float getFloat (String name, float defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asFloat();
    }

    /** Finds the child node with the specified name and returns its value as a double.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a double */
    public double getDouble (String name, double defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asDouble();
    }

    /** Finds the child node with the specified name and returns its value as a long.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a long */
    public long getLong (String name, long defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asLong();
    }

    /** Finds the child node with the specified name and returns its value as an int.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as an int */
    public int getInt (String name, int defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asInt();
    }

    /** Finds the child node with the specified name and returns its value as a boolean.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a boolean */
    public boolean getBoolean (String name, boolean defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asBoolean();
    }

    /** Finds the child node with the specified name and returns its value as a byte.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a byte */
    public byte getByte (String name, byte defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asByte();
    }

    /** Finds the child node with the specified name and returns its value as a short.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a short */
    public short getShort (String name, short defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asShort();
    }

    /** Finds the child node with the specified name and returns its value as a char.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a char */
    public char getChar (String name, char defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asChar();
    }

    /** Finds the child node with the specified name and returns its value as a {@link Color}.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a {@link Color} */
    public Color getColor (String name, Color defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asColor();
    }

    /** Finds the child node with the specified name and returns its value as a {@link Vector3}.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a {@link Vector3} */
    public Vector3 getVector3 (String name, Vector3 defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asVector3();
    }

    /** Finds the child node with the specified name and returns its value as a {@link Vector2}.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as a {@link Vector2} */
    public Vector2 getVector2 (String name, Vector2 defaultValue) {
        VDFNode child = get(name);
        return child == null ? defaultValue : child.asVector2();
    }

    /** Finds the child node with the specified name and returns its value as an Enum.
     * @param name of the child
     * @param defaultValue to return if no child is found
     * @return the child's value as an Enum */
    public <T extends Enum<T>> T getEnum (String name, T defaultValue) {
        VDFNode child = get(name);
        return (child == null) ? defaultValue : child.asEnum(defaultValue);
    }

    /** Finds the child node with the specified name and returns its value as a String.
     * @param name of the child
     * @return the child's value as a String
     * @throws IllegalArgumentException if the child was not found. */
    public String getString (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asString();
    }

    /** Finds the child node with the specified name and returns its value as a float.
     * @param name of the child
     * @return the child's value as a float
     * @throws IllegalArgumentException if the child was not found */
    public float getFloat (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asFloat();
    }

    /** Finds the child node with the specified name and returns its value as a double.
     * @param name of the child
     * @return the child's value as a double
     * @throws IllegalArgumentException if the child was not found */
    public double getDouble (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asDouble();
    }

    /** Finds the child node with the specified name and returns its value as a long.
     * @param name of the child
     * @return the child's value as a long
     * @throws IllegalArgumentException if the child was not found */
    public long getLong (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asLong();
    }

    /** Finds the child with the specified name and returns its value as an int.
     * @param name of the child node
     * @return the child's value as an int
     * @throws IllegalArgumentException if the child was not found */
    public int getInt (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asInt();
    }

    /** Finds the child node with the specified name and returns its value as a boolean.
     * @param name of the child
     * @return the child's value as a boolean
     * @throws IllegalArgumentException if the child was not found */
    public boolean getBoolean (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asBoolean();
    }

    /** Finds the child node with the specified name and returns its value as a byte.
     * @param name of the child
     * @return the child's value as a byte
     * @throws IllegalArgumentException if the child was not found */
    public byte getByte (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asByte();
    }

    /** Finds the child node with the specified name and returns its value as a short.
     * @param name of the child
     * @return the child's value as a short
     * @throws IllegalArgumentException if the child was not found */
    public short getShort (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asShort();
    }

    /** Finds the child node with the specified name and returns its value as a char.
     * @param name of the child
     * @return the child's value as a char
     * @throws IllegalArgumentException if the child was not found */
    public char getChar (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asChar();
    }

    /** Finds the child node with the specified name and returns its value as a {@link Color}.
     * @param name of the child
     * @return the child's value as a {@link Color}
     * @throws IllegalArgumentException if the child was not found */
    public Color getColor (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asColor();
    }

    /** Finds the child node with the specified name and returns its value as a {@link Vector3}.
     * @param name of the child
     * @return the child's value as a {@link Vector3}
     * @throws IllegalArgumentException if the child was not found */
    public Vector3 getVector3 (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asVector3();
    }

    /** Finds the child node with the specified name and returns its value as a {@link Vector2}.
     * @param name of the child
     * @return the child's value as a {@link Vector2}
     * @throws IllegalArgumentException if the child was not found */
    public Vector2 getVector2 (String name) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asVector2();
    }

    /** Finds the child node with the specified name and returns its value as an Enum.
     * @param name of the child
     * @param enumClass of the child's value
     * @return the child's value as an Enum
     * @throws IllegalArgumentException if the child was not found */
    public <T extends Enum<T>> T getEnum (String name, Class<T> enumClass) {
        VDFNode child = get(name);
        if (child == null) throw new IllegalArgumentException("Named value not found: " + name);
        return child.asEnum(enumClass);
    }

    /** Finds the child node with the specified index and returns its value as a String.
     * @param index of the child node
     * @return the child's value as a String
     * @throws IllegalArgumentException if the child was not found */
    public String getString (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asString();
    }

    /** Finds the child node with the specified index and returns its value as a float.
     * @param index of the child node
     * @return the child's value as a float
     * @throws IllegalArgumentException if the child was not found */
    public float getFloat (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asFloat();
    }

    /** Finds the child node with the specified index and returns its value as a double.
     * @param index of the child node
     * @return the child's value as a double
     * @throws IllegalArgumentException if the child was not found */
    public double getDouble (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asDouble();
    }

    /** Finds the child node with the specified index and returns its value as a long.
     * @param index of the child node
     * @return the child's value as a long
     * @throws IllegalArgumentException if the child was not found */
    public long getLong (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asLong();
    }

    /** Finds the child node with the specified index and returns its value as an int.
     * @param index of the child node
     * @return the child's value as an int
     * @throws IllegalArgumentException if the child was not found */
    public int getInt (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asInt();
    }

    /** Finds the child node with the specified index and returns its value as a boolean.
     * @param index of the child node
     * @return the child's value as a boolean
     * @throws IllegalArgumentException if the child was not found */
    public boolean getBoolean (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asBoolean();
    }

    /** Finds the child node with the specified index and returns its value as a byte.
     * @param index of the child node
     * @return the child's value as a byte
     * @throws IllegalArgumentException if the child was not found */
    public byte getByte (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asByte();
    }

    /** Finds the child node with the specified index and returns its value as a short.
     * @param index of the child node
     * @return the child's value as a short
     * @throws IllegalArgumentException if the child was not found */
    public short getShort (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asShort();
    }

    /** Finds the child node with the specified index and returns its value as a char.
     * @param index of the child node
     * @return the child's value as a char
     * @throws IllegalArgumentException if the child was not found */
    public char getChar (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asChar();
    }

    /** Finds the child node with the specified index and returns its value as a {@link Color}.
     * @param index of the child node
     * @return the child's value as a {@link Color}
     * @throws IllegalArgumentException if the child was not found */
    public Color getColor (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asColor();
    }

    /** Finds the child node with the specified index and returns its value as a {@link Vector3}.
     * @param index of the child node
     * @return the child's value as a {@link Vector3}
     * @throws IllegalArgumentException if the child was not found */
    public Vector3 getVector3 (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asVector3();
    }

    /** Finds the child node with the specified index and returns its value as a {@link Vector2}.
     * @param index of the child node
     * @return the child's value as a {@link Vector2}
     * @throws IllegalArgumentException if the child was not found */
    public Vector2 getVector2 (int index) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asVector2();
    }

    /** Finds the child node with the specified index and returns its value as an Enum.
     * @param index of the child node
     * @param enumClass of the child's value
     * @return the child's value as an Enum
     * @throws IllegalArgumentException if the child was not found */
    public <T extends Enum<T>> T getEnum (int index, Class<T> enumClass) {
        VDFNode child = get(index);
        if (child == null) throw new IllegalArgumentException("Indexed value not found: " + name);
        return child.asEnum(enumClass);
    }

    /**
     * @return this node's name */
    public String name () {
        return name;
    }

    /**
     * @return this node's parent, may be null */
    public VDFNode parent () {
        return parent;
    }

    /**
     * @return this node's first child, may be null */
    public VDFNode child () {
        return child;
    }

    /**
     * @return whether this node has a parent */
    public boolean hasParent () {
        return parent != null;
    }

    /**
     * @return whether the value of this node is null */
    public boolean isNull () {
        return value == null;
    }

    /** Sets the name of the specified node and adds it after the last child.
     * @param name of the node to add
     * @param node to add after the last child
     * @throws IllegalArgumentException if name is null */
    public void addChild (String name, VDFNode node) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null.");
        node.name = name;
        addChild(node);
    }

    /** Adds the specified node after the last child.
     * @throws IllegalStateException if the node's name is null */
    public void addChild (VDFNode node) {
        if (node.name == null) throw new IllegalStateException("An object child requires a name: " + node);
        node.parent = this;
        size++;
        VDFNode current = child;
        if (current == null)
            child = node;
        else {
            while (true) {
                if (current.next == null) {
                    current.next = node;
                    node.prev = current;
                    return;
                }
                current = current.next;
            }
        }
    }

    /**
     * @return the next sibling of this node, may be null. */
    public VDFNode next () {
        return next;
    }

    /**
     * @return the previous sibling of this node, may be null */
    public VDFNode prev () {
        return prev;
    }

    /**
     * @return the root node of the entire object graph */
    public VDFNode root () {
        VDFNode node = this;
        while (node.hasParent()) {
            node = node.parent;
        }
        return node;
    }

    /**
     * Sets this node's value.
     * @param value to set, may be null. */
    public void set (String value) {
        this.value = value;
    }

    public VDFIterator iterator () {
        return new VDFIterator();
    }

    @Override
    public String toString() {
        return toVDF();
    }

    /**
     * @return a human readable string representing this node and all of its children. */
    public String toVDF() {
        return toVDF(this, this, new StringBuilder(), new StringBuilder());
    }

    private String toVDF(VDFNode root, VDFNode current, StringBuilder whitespace, StringBuilder builder) {
        current = current.parent != null ? current : current.child;     // takes care of the root
        while (current != null) {
            builder.append(whitespace);
            builder.append("\"").append(current.name).append("\"");
            builder.append(" ");
            if (current.isEmpty() && !current.isNull()) {
                builder.append("\"").append(current.value).append("\"");
            }
            else {
                VDFNode child = current.child;
                builder.append("\n");
                builder.append(whitespace);
                builder.append("{");
                if (child != null) {
                    builder.append("\n");
                    whitespace.append("    ");
                    toVDF(root, current.child, whitespace, builder);
                    whitespace.setLength(whitespace.length() - 4);
                    builder.append(whitespace);
                }
                else {
                    builder.append("\n");
                    builder.append(whitespace);
                }
                builder.append("}");
            }
            if (current == root) {
                if (!current.isEmpty()) {
                    builder.append("\n");
                }
                break;
            }
            builder.append("\n");
            current = current.next;
        }
        return builder.toString();
    }


    public class VDFIterator implements Iterator<VDFNode>, Iterable<VDFNode> {
        VDFNode entry = child;
        VDFNode current;

        @Override
        public boolean hasNext () {
            return entry != null;
        }

        @Override
        public VDFNode next () {
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
        public Iterator<VDFNode> iterator () {
            return this;
        }
    }

}
