package it.aretesoftware.gdx.jvdf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.junit.Assert;
import org.junit.Test;

public class TestGdxVDFValues {

    private final GdxVDFValues vdf = new GdxVDFValues();

    @Test
    public void toObject() {
        Assert.assertEquals(vdf.toObject("0 0 0 0"), Color.CLEAR);
        Assert.assertEquals(vdf.toObject("0 0 0"), Vector3.Zero);
        Assert.assertEquals(vdf.toObject("0 0"), Vector2.Zero);
        Assert.assertEquals(vdf.toObject("true"), true);
        Assert.assertEquals(vdf.toObject("0.1"), 0.1f);
        Assert.assertEquals(vdf.toObject("0.1f"), 0.1f);
        Assert.assertEquals(vdf.toObject("0.1F"), 0.1f);
        Assert.assertEquals(vdf.toObject("0.1d"), 0.1d);
        Assert.assertEquals(vdf.toObject("0.1D"), 0.1D);
        Assert.assertEquals(vdf.toObject("1"), 1);
        Assert.assertEquals(vdf.toObject("1000"), 1000);
        Assert.assertEquals(vdf.toObject("0x8B87"), 35719);
        Assert.assertEquals(vdf.toObject("#8B87"), 35719);
        Assert.assertEquals(vdf.toObject("#0x8B87"), "#0x8B87");
        Assert.assertEquals(vdf.toObject("1 2 3 4 5"), "1 2 3 4 5");
        Assert.assertEquals(vdf.toObject("String"), "String");
    }

    @Test
    public void toObjectString() {
        Object obj;
        Assert.assertEquals(vdf.toString(obj = Color.BLACK), "0.0 0.0 0.0 1.0");
        Assert.assertEquals(vdf.toString(obj = Vector3.Zero), "0.0 0.0 0.0");
        Assert.assertEquals(vdf.toString(obj = Vector2.Zero), "0.0 0.0");
        Assert.assertEquals(vdf.toString(obj = true), "true");
        Assert.assertEquals(vdf.toString(obj = 0.1f), "0.1f");
        Assert.assertEquals(vdf.toString(obj = 0.1), "0.1d");
        Assert.assertEquals(vdf.toString(obj = 0.1d), "0.1d");
        Assert.assertEquals(vdf.toString(obj = 1), "1");
        Assert.assertEquals(vdf.toString(obj = 1L), "1L");
        Assert.assertEquals(vdf.toString(obj = "0x8B87"), "0x8B87");
        Assert.assertEquals(vdf.toString(obj = "#8B87"), "#8B87");
        Assert.assertEquals(vdf.toString(obj = "#0x8B87"), "#0x8B87");
        Assert.assertEquals(vdf.toString(obj = "String"), "String");
    }

    @Test
    public void isNumberHexadecimal() {
        // #
        Assert.assertTrue(vdf.isNumber("#ABCDEF"));
        Assert.assertTrue(vdf.isNumber("#aBcDeF"));
        Assert.assertTrue(vdf.isNumber("#abcdef"));
        Assert.assertTrue(vdf.isNumber("+#ABCDEF"));
        Assert.assertTrue(vdf.isNumber("-#abcdef"));
        Assert.assertTrue(vdf.isNumber("#a0b1c2D3E4"));
        Assert.assertTrue(vdf.isNumber("#0123456789"));
        Assert.assertTrue(vdf.isNumber("+#A0B1C2D3E4"));
        Assert.assertTrue(vdf.isNumber("-#0123456789"));
        Assert.assertFalse(vdf.isNumber("#F33G1"));
        Assert.assertFalse(vdf.isNumber("+#F33G1"));
        Assert.assertFalse(vdf.isNumber("-#F33G1"));
        Assert.assertFalse(vdf.isNumber("#01234.56789"));
        Assert.assertFalse(vdf.isNumber("#ABC.DEF"));
        Assert.assertFalse(vdf.isNumber("#A0B1C2.D3E4F5"));
        Assert.assertFalse(vdf.isNumber("-#01234.56789"));
        Assert.assertFalse(vdf.isNumber("+#ABC.DEF"));
        Assert.assertFalse(vdf.isNumber("-#A0B1C2.D3E4F5"));
        // 0X
        Assert.assertTrue(vdf.isNumber("0XABCDEF"));
        Assert.assertTrue(vdf.isNumber("0XaBcDeF"));
        Assert.assertTrue(vdf.isNumber("0Xabcdef"));
        Assert.assertTrue(vdf.isNumber("+0XABCDEF"));
        Assert.assertTrue(vdf.isNumber("-0Xabcdef"));
        Assert.assertTrue(vdf.isNumber("0Xa0b1c2D3E4"));
        Assert.assertTrue(vdf.isNumber("0X0123456789"));
        Assert.assertTrue(vdf.isNumber("+0XA0B1C2D3E4"));
        Assert.assertTrue(vdf.isNumber("-0X0123456789"));
        Assert.assertFalse(vdf.isNumber("0XF33G1"));
        Assert.assertFalse(vdf.isNumber("+0XF33G1"));
        Assert.assertFalse(vdf.isNumber("-0XF33G1"));
        Assert.assertFalse(vdf.isNumber("0X01234.56789"));
        Assert.assertFalse(vdf.isNumber("0XABC.DEF"));
        Assert.assertFalse(vdf.isNumber("0XA0B1C2.D3E4F5"));
        Assert.assertFalse(vdf.isNumber("-0X01234.56789"));
        Assert.assertFalse(vdf.isNumber("+0XABC.DEF"));
        Assert.assertFalse(vdf.isNumber("-0XA0B1C2.D3E4F5"));
        // 0x
        Assert.assertTrue(vdf.isNumber("0xABCDEF"));
        Assert.assertTrue(vdf.isNumber("0xaBcDeF"));
        Assert.assertTrue(vdf.isNumber("0xabcdef"));
        Assert.assertTrue(vdf.isNumber("+0xABCDEF"));
        Assert.assertTrue(vdf.isNumber("-0xabcdef"));
        Assert.assertTrue(vdf.isNumber("0xa0b1c2D3E4"));
        Assert.assertTrue(vdf.isNumber("0x0123456789"));
        Assert.assertTrue(vdf.isNumber("+0xA0B1C2D3E4"));
        Assert.assertTrue(vdf.isNumber("-0x0123456789"));
        Assert.assertFalse(vdf.isNumber("0xF33G1"));
        Assert.assertFalse(vdf.isNumber("+0xF33G1"));
        Assert.assertFalse(vdf.isNumber("-0xF33G1"));
        Assert.assertFalse(vdf.isNumber("0x01234.56789"));
        Assert.assertFalse(vdf.isNumber("0xABC.DEF"));
        Assert.assertFalse(vdf.isNumber("0xA0B1C2.D3E4F5"));
        Assert.assertFalse(vdf.isNumber("-0x01234.56789"));
        Assert.assertFalse(vdf.isNumber("+0xABC.DEF"));
        Assert.assertFalse(vdf.isNumber("-0xA0B1C2.D3E4F5"));
    }

    @Test
    public void isNumberScientificNotation() {
        // Unsigned
        Assert.assertTrue(vdf.isNumber("1.2846202978398e+19"));
        Assert.assertTrue(vdf.isNumber("1e3"));
        Assert.assertTrue(vdf.isNumber("100e5"));
        // Signed
        Assert.assertTrue(vdf.isNumber("-1.2846202978398e+19"));
        Assert.assertTrue(vdf.isNumber("+1e3"));
        Assert.assertTrue(vdf.isNumber("-100e5"));
        // Unsigned exponent w/ trail
        Assert.assertTrue(vdf.isNumber("1.57e3f"));
        Assert.assertTrue(vdf.isNumber("1.57e3F"));
        Assert.assertTrue(vdf.isNumber("1.57e3d"));
        Assert.assertTrue(vdf.isNumber("1.57e3D"));
        // Signed exponent w/ trail
        Assert.assertTrue(vdf.isNumber("1.57e-3f"));
        Assert.assertTrue(vdf.isNumber("1.57e+3F"));
        Assert.assertTrue(vdf.isNumber("1.57e+3d"));
        Assert.assertTrue(vdf.isNumber("1.57e-3D"));
        // Edge cases
        Assert.assertTrue(vdf.isNumber("0.e3"));
        Assert.assertTrue(vdf.isNumber("0.e3f"));
        Assert.assertFalse(vdf.isNumber(".e3"));
        Assert.assertFalse(vdf.isNumber(".e-3"));
    }

    @Test
    public void isNumberInteger() {
        // Unsigned integer
        Assert.assertTrue(vdf.isNumber("321"));
        Assert.assertTrue(vdf.isNumber("123"));
        Assert.assertTrue(vdf.isNumber("999"));
        // Signed integer
        Assert.assertTrue(vdf.isNumber("-321"));
        Assert.assertTrue(vdf.isNumber("+123"));
        Assert.assertTrue(vdf.isNumber("-999"));
    }

    @Test
    public void isNumberDecimal() {
        // Unsigned decimal
        Assert.assertTrue(vdf.isNumber("1.0"));
        Assert.assertTrue(vdf.isNumber("0.1"));
        Assert.assertTrue(vdf.isNumber("0.12345"));
        // Unsigned decimal w/ trail
        Assert.assertTrue(vdf.isNumber("0.1f"));
        Assert.assertTrue(vdf.isNumber("0.1F"));
        Assert.assertTrue(vdf.isNumber("0.1d"));
        Assert.assertTrue(vdf.isNumber("0.1D"));
        // Signed decimal
        Assert.assertTrue(vdf.isNumber("+1.0"));
        Assert.assertTrue(vdf.isNumber("-0.1"));
        Assert.assertTrue(vdf.isNumber("-0.12345"));
        // Signed decimal w/ trail
        Assert.assertTrue(vdf.isNumber("+0.1f"));
        Assert.assertTrue(vdf.isNumber("-0.1F"));
        Assert.assertTrue(vdf.isNumber("+0.1d"));
        Assert.assertTrue(vdf.isNumber("-0.1D"));
        // Edge cases
        Assert.assertTrue(vdf.isNumber("1.f"));
        Assert.assertTrue(vdf.isNumber("0.f"));
        Assert.assertTrue(vdf.isNumber("0.d"));
        Assert.assertFalse(vdf.isNumber(".f"));
        Assert.assertFalse(vdf.isNumber(".d"));
    }


    @Test
    public void toNumber() {
        // Float & Double
        final float delta = 0.00000001f;
        Assert.assertEquals(vdf.toNumber("0.1").floatValue(), 0.1, delta);
        Assert.assertEquals(vdf.toNumber("0.1f").floatValue(), 0.1, delta);
        Assert.assertEquals(vdf.toNumber(".1f").floatValue(), 0.1, delta);
        Assert.assertEquals(vdf.toNumber("1.f").floatValue(), 1, delta);
        Assert.assertEquals(vdf.toNumber("0.1").doubleValue(), 0.1, delta);
        Assert.assertEquals(vdf.toNumber("0.1d").doubleValue(), 0.1, delta);
        Assert.assertEquals(vdf.toNumber(".1d").doubleValue(), 0.1, delta);
        Assert.assertEquals(vdf.toNumber("1.d").doubleValue(), 1, delta);
        Assert.assertEquals(vdf.toNumber("wasd", 0.1f), 0.1f);

        // Integer & Long
        Assert.assertEquals(vdf.toNumber("1").intValue(), 1, 0);
        Assert.assertEquals(vdf.toNumber("1").longValue(), 1, 0);

        // Hexadecimal Integers
        Assert.assertEquals(vdf.toNumber("0x8B87").intValue(), 35719, 0);
        Assert.assertEquals(vdf.toNumber("#8B87").intValue(), 35719, 0);
        Assert.assertEquals(vdf.toNumber("0x8009").intValue(), 32777, 0);
        Assert.assertEquals(vdf.toNumber("#8009").intValue(), 32777, 0);
        Assert.assertEquals(vdf.toNumber("8009").intValue(), 8009, 0);
    }

    @Test
    @SuppressWarnings("All")
    public void toNumberString() {
        Assert.assertEquals(vdf.toString(0), "0");
        Assert.assertEquals(vdf.toString(0.1f), "0.1f");
        Assert.assertEquals(vdf.toString(0.1d), "0.1d");
        Assert.assertEquals(vdf.toString(0.1), "0.1d");
        Assert.assertEquals(vdf.toString(1l), "1L");
        Assert.assertEquals(vdf.toString(1L), "1L");
        Assert.assertEquals(vdf.toString(0x8009), "32777");
    }

    @Test
    public void isBoolean() {
        Assert.assertTrue(vdf.isBoolean("true"));
        Assert.assertTrue(vdf.isBoolean("false"));
        Assert.assertFalse(vdf.isBoolean("0"));
        Assert.assertFalse(vdf.isBoolean("1"));
        Assert.assertFalse(vdf.isBoolean("wasd"));
    }

    @Test
    public void toBoolean() {
        Assert.assertTrue(vdf.toBoolean("true"));
        Assert.assertFalse(vdf.toBoolean("false"));
        Assert.assertTrue(vdf.toBoolean("wasd", true));
    }

    @Test
    public void isColor() {
        Assert.assertTrue(vdf.isColor("1 0 0 0"));
        Assert.assertTrue(vdf.isColor("1.0 0.0 0.0 0.0"));
        Assert.assertTrue(vdf.isColor("1 0.0 0.4 0"));
        Assert.assertTrue(vdf.isColor("+1 0 0 0"));
        Assert.assertTrue(vdf.isColor("-1 0 0 0"));
        Assert.assertFalse(vdf.isColor("w a s d"));
        Assert.assertFalse(vdf.isColor("1 0 0"));
    }

    @Test
    public void toColor() {
        Assert.assertEquals(vdf.toColor("1 1 1 1"), new Color(1, 1, 1, 1));
        Assert.assertEquals(vdf.toColor("0.1 0.2 0.3 0.4"), new Color(0.1f, 0.2f, 0.3f, 0.4f));
        Assert.assertEquals(vdf.toColor("255 255 255 255"), new Color(255, 255, 255, 255));
        Assert.assertEquals(vdf.toColor("0   0 0   0"), Color.CLEAR);
        Assert.assertEquals(vdf.toColor("1 1 1", Color.CLEAR), Color.CLEAR);
    }

    @Test
    public void toColorString() {
        Assert.assertEquals(vdf.toString(Color.BLACK), "0.0 0.0 0.0 1.0");
        Assert.assertEquals(vdf.toString(Color.WHITE), "1.0 1.0 1.0 1.0");
        Assert.assertEquals(vdf.toString(new Color(0.1f, 0.2f, 0.3f, 0.4f)), "0.1 0.2 0.3 0.4");
    }

    @Test
    public void isVector3() {
        Assert.assertTrue(vdf.isVector3("0 0 0"));
        Assert.assertTrue(vdf.isVector3(" 0 0 0"));
        Assert.assertTrue(vdf.isVector3("0 0 0 "));
        Assert.assertTrue(vdf.isVector3(" 0 0 0 "));
        Assert.assertTrue(vdf.isVector3(" 0   0 0 "));
        Assert.assertTrue(vdf.isVector3("0.1 0.2 0.3"));
        Assert.assertFalse(vdf.isVector3("1 1"));
        Assert.assertFalse(vdf.isVector3("1"));
        Assert.assertFalse(vdf.isVector3(""));
    }

    @Test
    public void toVector3() {
        Assert.assertEquals(vdf.toVector3("0 0 0"), new Vector3());
        Assert.assertEquals(vdf.toVector3("1 1 1"), new Vector3(1, 1, 1));
        Assert.assertEquals(vdf.toVector3("0.1 0.2 0.3"), new Vector3(0.1f, 0.2f, 0.3f));
        Assert.assertEquals(vdf.toVector3(" 0   1 2"), new Vector3(0, 1, 2));
        Assert.assertEquals(vdf.toVector3("", Vector3.Zero), Vector3.Zero);
    }

    @Test
    public void toVector3String() {
        Assert.assertEquals(vdf.toString(new Vector3()), "0.0 0.0 0.0");
        Assert.assertEquals(vdf.toString(new Vector3(1, 1, 1)), "1.0 1.0 1.0");
        Assert.assertEquals(vdf.toString(new Vector3(0.1f, 0.2f, 0.3f)), "0.1 0.2 0.3");
    }

    @Test
    public void isVector2() {
        Assert.assertTrue(vdf.isVector2("0 0"));
        Assert.assertTrue(vdf.isVector2(" 0 0"));
        Assert.assertTrue(vdf.isVector2("0 0 "));
        Assert.assertTrue(vdf.isVector2(" 0 0 "));
        Assert.assertTrue(vdf.isVector2(" 0   0 "));
        Assert.assertTrue(vdf.isVector2("0.1 0.2"));
        Assert.assertFalse(vdf.isVector2("1"));
        Assert.assertFalse(vdf.isVector2(""));
    }

    @Test
    public void toVector2() {
        Assert.assertEquals(vdf.toVector2("0 0"), new Vector2());
        Assert.assertEquals(vdf.toVector2("1 1"), new Vector2(1, 1));
        Assert.assertEquals(vdf.toVector2("0.1 0.2"), new Vector2(0.1f, 0.2f));
        Assert.assertEquals(vdf.toVector2(" 0   1 "), new Vector2(0, 1));
        Assert.assertEquals(vdf.toVector2("", Vector2.Zero), Vector2.Zero);
    }

    @Test
    public void toVector2String() {
        Assert.assertEquals(vdf.toString(new Vector2()), "0.0 0.0");
        Assert.assertEquals(vdf.toString(new Vector2(1, 1)), "1.0 1.0");
        Assert.assertEquals(vdf.toString(new Vector2(0.1f, 0.2f)), "0.1 0.2");
    }
}