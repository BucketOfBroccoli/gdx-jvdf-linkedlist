package it.aretesoftware.gdx.jvdf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Various methods for parsing from/to strings/objects and vice versa.
 * @author AreteS0ftware */
public class GdxVDFValues {

    private final String WHITESPACE_REGEX = "[ \\t\\n]+";
    private final String TRAILLESS_NUMBER_REGEX = "^(\\+|-)?([0-9])*\\.?([0-9]+)?$";

    public GdxVDFValues() {

    }

    public Object toObject(String value) {
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
            NumberType type = getNumberType(value);
            if (type != NumberType.nan) {
                return toNumber(value, type);
            }
        }
        return value;
    }

    public boolean isNumber(String value) {
        return getNumberType(value) != NumberType.nan;
    }

    private NumberType getNumberType(String value) {
        boolean possibleHexadecimal = false;
        boolean possibleDecimal = false;
        boolean possibleScientificNotation = false;
        boolean signed = false;
        int dotIndex = -1, exponentIndex = -1;

        char[] charArray = value.toCharArray();
        int stringLength = charArray.length;
        for (int index = 0; index < charArray.length; index++) {
            char c = charArray[index];
            switch (c) {
                case '+':
                case '-':
                   signed = true;
                   continue;
                case '#':
                case 'A':
                case 'B':
                case 'C':
                case 'X':
                case 'a':
                case 'b':
                case 'c':
                case 'x':
                    possibleHexadecimal = true;
                    continue;
                case 'd':
                case 'f':
                case 'D':
                case 'F':
                    possibleDecimal = true;
                    possibleHexadecimal = true;
                    continue;
                case 'E':
                case 'e':
                    possibleScientificNotation = true;
                    possibleHexadecimal = true;
                    exponentIndex = index;
                    continue;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    continue;
                case '.':
                    if (dotIndex >= 0) {
                        // More than one dot detected means
                        // the string is not a number
                        return NumberType.nan;
                    }
                    possibleDecimal = true;
                    dotIndex = index;
                    continue;
                default:
                    // any other character means
                    // the string is not a number
                    return NumberType.nan;
            }
        }

        // Store whether the String starts with the following:
        // #
        // 0X
        // 0x
        int signOffset = 0;
        if (signed && (charArray[0] == '+' || charArray[0] == '-')) {
            signOffset = 1;
        }
        boolean startsWithPound = charArray[signOffset] == '#';
        boolean startsWithZeroX = charArray[signOffset] == '0' && (charArray[signOffset + 1] == 'X' || charArray[signOffset + 1] == 'x');

        if (!startsWithPound && !startsWithZeroX && possibleDecimal) {
            char trail = charArray[stringLength - 1];
            if (trail == 'f' || trail == 'F' || trail == 'd' || trail == 'D') {
                if (dotIndex == stringLength - 2 && dotIndex - 1 < 0) {
                    return NumberType.nan;
                }
                char beforeTrail = charArray[stringLength - 2];
                if (!Character.isDigit(beforeTrail) && beforeTrail != '.') {
                    return NumberType.nan;
                }
            }
        }

        if (possibleHexadecimal) {
            if (startsWithPound || startsWithZeroX) {
                int startOffset = startsWithPound ? 1 : 2;
                for (int index = signOffset + startOffset; index < stringLength; index++) {
                    char c = charArray[index];
                    switch (c) {
                        case '#':
                        case 'x':
                        case 'X':
                            return NumberType.nan;
                    }
                }
                return dotIndex < 0 ? NumberType.hexadecimal : NumberType.nan;
            }
        }

        if (!startsWithPound && !startsWithZeroX && possibleScientificNotation) {
            char next = charArray[exponentIndex + 1];
            if (!Character.isDigit(next) && next != '+' && next != '-') {
                return NumberType.nan;
            }
            if (dotIndex == exponentIndex - 1) {
                if (dotIndex - 1 < 0) {
                    return NumberType.nan;
                }
                char beforeDot = charArray[dotIndex - 1];
                if (!Character.isDigit(beforeDot)) {
                    return NumberType.nan;
                }
            }
            for (int index = exponentIndex + 2; index < stringLength; index++) {
                char character = charArray[index];
                if (Character.isDigit(character)) {
                    continue;
                }
                if (index == stringLength - 1) {
                    if (character == 'f' || character == 'F' || character == 'd' || character == 'D') {
                        return NumberType.scientific;
                    }
                }
                else {
                    return NumberType.nan;
                }
            }
            return NumberType.scientific;
        }

        return possibleDecimal ? NumberType.decimal : NumberType.integer;
    }

    public String toString(Object object) {
        if (object instanceof Color) {
            return toString((Color) object);
        }
        else if (object instanceof Vector3) {
            return toString((Vector3) object);
        }
        else if (object instanceof Vector2) {
            return toString((Vector2) object);
        }
        else if (object instanceof Boolean) {
            return object.toString();
        }
        else if (object instanceof Number) {
            return toString((Number) object);
        }
        return object.toString();    // Hexadecimal numbers stay as they are
    }

    public Number toNumber(String value, Number defaultValue) {
        return isNumber(value) ? toNumber(value) : defaultValue;
    }

    public Number toNumber(String value) {
        return toNumber(value, getNumberType(value));
    }

    private Number toNumber(String value, NumberType type) {
        switch (type) {
            case integer:
                return Integer.parseInt(value);
            case hexadecimal:
                return Integer.decode(value);
            case decimal:
            case scientific:
                if (value.endsWith("f") || value.endsWith("F")) {
                    return Float.parseFloat(value);
                }
                else if (value.endsWith("d") || value.endsWith("D")) {
                    return Double.parseDouble(value);
                }
                return Float.parseFloat(value);
            default:
                throw new GdxVDFValuesException("'" + value + "' is not a number.");
        }
    }

    public String toString(Number number) {
        if (number instanceof Float) {
            return number.floatValue() + "f";
        }
        else if (number instanceof Double) {
            return number.doubleValue() + "d";
        }
        else if (number instanceof Long) {
            return number.longValue() + "L";
        }
        else if (number instanceof Integer) {
            return String.valueOf(number.intValue());
        }
        return number.toString();   // Hexadecimal numbers stay as they are
    }

    public boolean isBoolean(String value) {
        value = value.trim();
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public boolean toBoolean(String value, boolean defaultValue) {
        return isBoolean(value) ? toBoolean(value) : defaultValue;
    }

    public boolean toBoolean(String value) {
        value = value.trim();
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        else if (value.equalsIgnoreCase("false")) {
            return false;
        }
        else {
            throw new GdxVDFValuesException("'" + value + "' is not a boolean.");
        }
    }

    public boolean isColor(String value) {
        return isValid(value, 4);
    }

    public Color toColor(String value, Color defaultValue) {
        return isColor(value) ? toColor(value) : defaultValue;
    }

    public Color toColor(String value) {
        value = value.trim();
        String[] split = checkConditions(value, 4,
                "'" + value + "' does not have the four RGBA values that make up a Color.",
                "One of the RGBA values in '" + value + "' is not a number.");
        float r = Float.parseFloat(split[0]);
        float g = Float.parseFloat(split[1]);
        float b = Float.parseFloat(split[2]);
        float a = Float.parseFloat(split[3]);
        return new Color(r, g, b, a);
    }

    public String toString(Color color) {
        return toColorString(color.r, color.g, color.b, color.a);
    }

    public String toColorString(float r, float g, float b, float a) {
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

    public boolean isVector3(String value) {
        return isValid(value, 3);
    }

    public Vector3 toVector3(String value, Vector3 defaultValue) {
        return isVector3(value) ? toVector3(value) : defaultValue;
    }

    public Vector3 toVector3(String value) {
        value = value.trim();
        String[] split = checkConditions(value, 3,
                "'" + value + "' does not have the three XYZ values that make up a Vector3.",
                "One of the XYZ values in '" + value + "' is not a number.");
        float x = Float.parseFloat(split[0]);
        float y = Float.parseFloat(split[1]);
        float z = Float.parseFloat(split[2]);
        return new Vector3(x, y, z);
    }

    public String toString(Vector3 vec3) {
        return toVector3String(vec3.x, vec3.y, vec3.z);
    }

    public String toVector3String(float x, float y, float z) {
        StringBuilder builder = new StringBuilder();
        builder.append(x);
        builder.append(" ");
        builder.append(y);
        builder.append(" ");
        builder.append(z);
        return builder.toString();
    }

    public boolean isVector2(String value) {
        return isValid(value, 2);
    }

    public Vector2 toVector2(String value, Vector2 defaultValue) {
        return isVector2(value) ? toVector2(value) : defaultValue;
    }

    public Vector2 toVector2(String value) {
        value = value.trim();
        String[] split = checkConditions(value, 2,
                "'" + value + "' does not have the two XY values that make up a Vector2.",
                "One of the XY values in '" + value + "' is not a number.");
        float x = Float.parseFloat(split[0]);
        float y = Float.parseFloat(split[1]);
        return new Vector2(x, y);
    }

    public String toString(Vector2 vec2) {
        return toVector2String(vec2.x, vec2.y);
    }

    public String toVector2String(float x, float y) {
        StringBuilder builder = new StringBuilder();
        builder.append(x);
        builder.append(" ");
        builder.append(y);
        return builder.toString();
    }

    public <T extends Enum<T>> T toEnum(String value, T defaultValue) {
        try {
            return toEnum(value, defaultValue.getDeclaringClass());
        }
        catch (GdxVDFValuesException e) {
            return defaultValue;
        }
    }

    public <T extends Enum<T>> T toEnum(String value, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, value);
        }
        catch (Exception e) {
            throw new GdxVDFValuesException("Couldn't get enum of type " + enumClass.getSimpleName());
        }
    }

    private boolean isValid(String value, int arrayLengthAllowed) {
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

    private String[] checkConditions(String value, int arrayLengthAllowed, String arrayLengthErrorMessage, String notNumericErrorMessage) throws GdxVDFValuesException {
        String[] split = value.trim().split(WHITESPACE_REGEX);
        if (split.length != arrayLengthAllowed) {
            throw new GdxVDFValuesException(arrayLengthErrorMessage);
        }
        for (String number : split) {
            if (!number.matches(TRAILLESS_NUMBER_REGEX)) {
                throw new GdxVDFValuesException(notNumericErrorMessage);
            }
        }
        return split;
    }

    enum NumberType {
        integer,
        decimal,
        hexadecimal,
        scientific,
        nan
    }

}
