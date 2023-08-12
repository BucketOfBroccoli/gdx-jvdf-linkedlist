package it.aretesoftware.gdx.jvdf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.junit.Assert;
import org.junit.Test;

public class VDFValuesTest {

    private final VDFValues vdf = new VDFValues();

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