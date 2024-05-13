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
import com.badlogic.gdx.utils.GdxRuntimeException;

import it.aretesoftware.utils.NumberUtils;
import it.aretesoftware.utils.StringUtils;

/**
 * Various methods for parsing from/to strings/objects and vice versa.
 * @author BucketOfBroccoli */
public class GdxVDFUtils {

    private static final String WHITESPACE_REGEX = "[ \\t\\n]+";
    private static final String TRAILLESS_NUMBER_REGEX = "^(\\+|-)?([0-9])*\\.?([0-9]+)?(f|F)?$";

    private GdxVDFUtils() {

    }

    public static Object toObject(String value) {
        if (isColor(value)) {
            return toColor(value);
        }
        else if (isVector3(value)) {
            return toVector3(value);
        }
        else if (isVector2(value)) {
            return toVector2(value);
        }
        else if (isBoolean(value)) {
            return toBoolean(value);
        }
        else {
            if (NumberUtils.isNumber(value)) {
                return NumberUtils.toNumber(value);
            }
        }
        return value;
    }

    public static String toString(Object object) {
        if (object instanceof Color) {
            return toColorString((Color) object);
        }
        else if (object instanceof Vector3) {
            return toVector3String((Vector3) object);
        }
        else if (object instanceof Vector2) {
            return toVector2String((Vector2) object);
        }
        else if (object == null) {
            return null;
        }
        // Boolean & numeric values are here
        // Hexadecimal numbers stay as they are
        return object.toString();
    }

    public static boolean isBoolean(String value) {
        if (StringUtils.isNullOrEmpty(value)) {
            return false;
        }
        value = value.trim();
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public static boolean toBoolean(String value, boolean defaultValue) {
        return isBoolean(value) ? toBoolean(value) : defaultValue;
    }

    public static boolean toBoolean(String value) {
        value = value.trim();
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        else if (value.equalsIgnoreCase("false")) {
            return false;
        }
        else {
            throw new GdxVDFUtilsException("'" + value + "' is not a boolean.");
        }
    }

    public static String toBooleanString(boolean value) {
        return value ? "true" : "false";
    }

    public static String toBooleanString(int value) {
        return toBooleanString(value == 1);
    }

    public static boolean isColor(String value) {
        return isStringDataValid(value, 4);
    }

    public static Color toColor(String value, Color defaultValue) {
        return isColor(value) ? toColor(value) : defaultValue;
    }

    public static Color toColor(String value) {
        value = value.trim();
        String[] split = checkStringData(value, 4,
                "'" + value + "' does not have the four RGBA values that make up a Color.",
                "One of the RGBA values in '" + value + "' is not a number.");
        float r = Float.parseFloat(split[0]);
        float g = Float.parseFloat(split[1]);
        float b = Float.parseFloat(split[2]);
        float a = Float.parseFloat(split[3]);
        return new Color(r, g, b, a);
    }

    public static String toColorString(Color color) {
        return toColorString(color.r, color.g, color.b, color.a);
    }

    public static String toColorString(float r, float g, float b, float a) {
        StringBuilder builder = new StringBuilder();
        builder.append(r);
        builder.append(" ");
        builder.append(g);
        builder.append(" ");
        builder.append(b);
        builder.append(" ");
        builder.append(a);
        return builder.toString();
    }

    public static boolean isVector3(String value) {
        return isStringDataValid(value, 3);
    }

    public static Vector3 toVector3(String value, Vector3 defaultValue) {
        return isVector3(value) ? toVector3(value) : defaultValue;
    }

    public static Vector3 toVector3(String value) {
        value = value.trim();
        String[] split = checkStringData(value, 3,
                "'" + value + "' does not have the three XYZ values that make up a Vector3.",
                "One of the XYZ values in '" + value + "' is not a number.");
        float x = Float.parseFloat(split[0]);
        float y = Float.parseFloat(split[1]);
        float z = Float.parseFloat(split[2]);
        return new Vector3(x, y, z);
    }

    public static String toVector3String(Vector3 vec3) {
        return toVector3String(vec3.x, vec3.y, vec3.z);
    }

    public static String toVector3String(float x, float y, float z) {
        StringBuilder builder = new StringBuilder();
        builder.append(x);
        builder.append(" ");
        builder.append(y);
        builder.append(" ");
        builder.append(z);
        return builder.toString();
    }

    public static boolean isVector2(String value) {
        return isStringDataValid(value, 2);
    }

    public static Vector2 toVector2(String value, Vector2 defaultValue) {
        return isVector2(value) ? toVector2(value) : defaultValue;
    }

    public static Vector2 toVector2(String value) {
        value = value.trim();
        String[] split = checkStringData(value, 2,
                "'" + value + "' does not have the two XY values that make up a Vector2.",
                "One of the XY values in '" + value + "' is not a number.");
        float x = Float.parseFloat(split[0]);
        float y = Float.parseFloat(split[1]);
        return new Vector2(x, y);
    }

    public static String toVector2String(Vector2 vec2) {
        return toVector2String(vec2.x, vec2.y);
    }

    public static String toVector2String(float x, float y) {
        StringBuilder builder = new StringBuilder();
        builder.append(x);
        builder.append(" ");
        builder.append(y);
        return builder.toString();
    }

    public static <T extends Enum<T>> T toEnum(String value, T defaultValue) {
        try {
            return toEnum(value, defaultValue.getDeclaringClass());
        }
        catch (GdxVDFUtilsException e) {
            return defaultValue;
        }
    }

    public static <T extends Enum<T>> T toEnum(String value, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, value);
        }
        catch (Exception e) {
            throw new GdxVDFUtilsException("Couldn't get enum of type " + enumClass.getSimpleName());
        }
    }

    private static boolean isStringDataValid(String value, int arrayLengthAllowed) {
        if (StringUtils.isNullOrEmpty(value)) {
            return false;
        }
        String[] split = value.trim().split(WHITESPACE_REGEX);
        if (split.length != arrayLengthAllowed) {
            return false;
        }
        for (String number : split) {
            if (!number.matches(TRAILLESS_NUMBER_REGEX)) {
                return false;
            }
        }
        return true;
    }

    private static String[] checkStringData(String value, int arrayLengthAllowed, String arrayLengthErrorMessage, String notNumericErrorMessage) throws GdxVDFUtilsException {
        if (StringUtils.isNullOrEmpty(value)) {
            throw new GdxVDFUtilsException("Value is null or empty.");
        }
        String[] split = value.trim().split(WHITESPACE_REGEX);
        if (split.length != arrayLengthAllowed) {
            throw new GdxVDFUtilsException(arrayLengthErrorMessage);
        }
        for (String number : split) {
            if (!number.matches(TRAILLESS_NUMBER_REGEX)) {
                throw new GdxVDFUtilsException(notNumericErrorMessage);
            }
        }
        return split;
    }

    static class GdxVDFUtilsException extends GdxRuntimeException {
        GdxVDFUtilsException(String message) {
            super(message);
        }
    }

}
