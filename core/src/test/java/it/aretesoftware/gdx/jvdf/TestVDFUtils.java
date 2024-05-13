package it.aretesoftware.gdx.jvdf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author BucketOfBroccoli
 */
public class TestVDFUtils {

    @Test
    public void toObject() {
        Assert.assertEquals(VDFUtils.toObject("0 0 0 0"), Color.CLEAR);
        Assert.assertEquals(VDFUtils.toObject("0.0 0.0 0.0 0.0"), Color.CLEAR);
        Assert.assertEquals(VDFUtils.toObject(".0 0.0f 0F 0.0"), Color.CLEAR);
        Assert.assertEquals(VDFUtils.toObject("0 0 0"), Vector3.Zero);
        Assert.assertEquals(VDFUtils.toObject("0 0"), Vector2.Zero);
        Assert.assertEquals(VDFUtils.toObject("true"), true);
        Assert.assertEquals(VDFUtils.toObject("0.1"), 0.1f);
        Assert.assertEquals(VDFUtils.toObject("0.1f"), 0.1f);
        Assert.assertEquals(VDFUtils.toObject("0.1F"), 0.1f);
        Assert.assertEquals(VDFUtils.toObject("0.1d"), 0.1d);
        Assert.assertEquals(VDFUtils.toObject("0.1D"), 0.1D);
        Assert.assertEquals(VDFUtils.toObject("1"), 1);
        Assert.assertEquals(VDFUtils.toObject("1000"), 1000);
        Assert.assertEquals(VDFUtils.toObject("0x8B87"), 35719);
        Assert.assertEquals(VDFUtils.toObject("#8B87"), 35719);
        Assert.assertEquals(VDFUtils.toObject("#0x8B87"), "#0x8B87");
        Assert.assertEquals(VDFUtils.toObject("1 2 3 4 5"), "1 2 3 4 5");
        Assert.assertEquals(VDFUtils.toObject("String"), "String");
        Assert.assertEquals(VDFUtils.toObject("0"), 0);
        Assert.assertEquals(VDFUtils.toObject("#"), "#");
        Assert.assertEquals(VDFUtils.toObject("0x"), "0x");
        Assert.assertEquals(VDFUtils.toObject("0X"), "0X");
        Assert.assertEquals(VDFUtils.toObject(""), "");
        Assert.assertNull(VDFUtils.toObject(null));
    }

    @Test
    public void toObjectString() {
        Object obj;
        Assert.assertEquals(VDFUtils.toString(obj = Color.BLACK), "0.0 0.0 0.0 1.0");
        Assert.assertEquals(VDFUtils.toString(obj = Vector3.Zero), "0.0 0.0 0.0");
        Assert.assertEquals(VDFUtils.toString(obj = Vector2.Zero), "0.0 0.0");
        Assert.assertEquals(VDFUtils.toString(obj = true), "true");
        Assert.assertEquals(VDFUtils.toString(obj = 0.1f), "0.1");
        Assert.assertEquals(VDFUtils.toString(obj = 0.1), "0.1");
        Assert.assertEquals(VDFUtils.toString(obj = 0.1d), "0.1");
        Assert.assertEquals(VDFUtils.toString(obj = 1), "1");
        Assert.assertEquals(VDFUtils.toString(obj = 1L), "1");
        Assert.assertEquals(VDFUtils.toString(obj = "0x8B87"), "0x8B87");
        Assert.assertEquals(VDFUtils.toString(obj = "#8B87"), "#8B87");
        Assert.assertEquals(VDFUtils.toString(obj = "#0x8B87"), "#0x8B87");
        Assert.assertEquals(VDFUtils.toString(obj = "String"), "String");
        Assert.assertNull(VDFUtils.toString(obj = null));
    }

    @Test
    public void isBoolean() {
        Assert.assertTrue(VDFUtils.isBoolean("true"));
        Assert.assertTrue(VDFUtils.isBoolean("false"));
        Assert.assertFalse(VDFUtils.isBoolean("0"));
        Assert.assertFalse(VDFUtils.isBoolean("1"));
        Assert.assertFalse(VDFUtils.isBoolean("Test"));
    }

    @Test
    public void toBoolean() {
        Assert.assertTrue(VDFUtils.toBoolean("true"));
        Assert.assertFalse(VDFUtils.toBoolean("false"));
        Assert.assertTrue(VDFUtils.toBoolean("Test", true));
    }

    @Test
    public void toBooleanString() {
        Assert.assertEquals(VDFUtils.toBooleanString(true), "true");
        Assert.assertEquals(VDFUtils.toBooleanString(false), "false");
        Assert.assertEquals(VDFUtils.toBooleanString(0), "false");
        Assert.assertEquals(VDFUtils.toBooleanString(1), "true");
        Assert.assertEquals(VDFUtils.toBooleanString(-1), "false");
    }

    @Test
    public void isColor() {
        Assert.assertTrue(VDFUtils.isColor("1 0 0 0"));
        Assert.assertTrue(VDFUtils.isColor("1.0 0.0 0.0 0.0"));
        Assert.assertTrue(VDFUtils.isColor("1 0.0 0.4 0"));
        Assert.assertTrue(VDFUtils.isColor("+1 0 0 0"));
        Assert.assertTrue(VDFUtils.isColor("-1 0 0 0"));
        Assert.assertFalse(VDFUtils.isColor("t e s t"));
        Assert.assertFalse(VDFUtils.isColor("1 0 0"));
    }

    @Test
    public void toColor() {
        Assert.assertEquals(VDFUtils.toColor("1 1 1 1"), new Color(1, 1, 1, 1));
        Assert.assertEquals(VDFUtils.toColor("0.1 0.2 0.3 0.4"), new Color(0.1f, 0.2f, 0.3f, 0.4f));
        Assert.assertEquals(VDFUtils.toColor("255 255 255 255"), new Color(255, 255, 255, 255));
        Assert.assertEquals(VDFUtils.toColor("0   0 0   0"), Color.CLEAR);
        Assert.assertEquals(VDFUtils.toColor("1 1 1", Color.CLEAR), Color.CLEAR);
        Assert.assertEquals(VDFUtils.toColor("-1 +1 0 0"), new Color(-1, +1, 0, 0));

    }

    @Test
    public void toColorString() {
        Assert.assertEquals(VDFUtils.toColorString(Color.BLACK), "0.0 0.0 0.0 1.0");
        Assert.assertEquals(VDFUtils.toColorString(Color.WHITE), "1.0 1.0 1.0 1.0");
        Assert.assertEquals(VDFUtils.toColorString(new Color(0.1f, 0.2f, 0.3f, 0.4f)), "0.1 0.2 0.3 0.4");
    }

    @Test
    public void isVector3() {
        Assert.assertTrue(VDFUtils.isVector3("0 0 0"));
        Assert.assertTrue(VDFUtils.isVector3(" 0 0 0"));
        Assert.assertTrue(VDFUtils.isVector3("0 0 0 "));
        Assert.assertTrue(VDFUtils.isVector3(" 0 0 0 "));
        Assert.assertTrue(VDFUtils.isVector3(" 0   0 0 "));
        Assert.assertTrue(VDFUtils.isVector3("0.1 0.2 0.3"));
        Assert.assertFalse(VDFUtils.isVector3("1 1"));
        Assert.assertFalse(VDFUtils.isVector3("1"));
        Assert.assertFalse(VDFUtils.isVector3(""));
    }

    @Test
    public void toVector3() {
        Assert.assertEquals(VDFUtils.toVector3("0 0 0"), new Vector3());
        Assert.assertEquals(VDFUtils.toVector3("1 1 1"), new Vector3(1, 1, 1));
        Assert.assertEquals(VDFUtils.toVector3("0.1 0.2 0.3"), new Vector3(0.1f, 0.2f, 0.3f));
        Assert.assertEquals(VDFUtils.toVector3(" 0   1 2"), new Vector3(0, 1, 2));
        Assert.assertEquals(VDFUtils.toVector3("", Vector3.Zero), Vector3.Zero);
        Assert.assertEquals(VDFUtils.toVector3("-1 +1 0"), new Vector3(-1, +1, 0));
    }

    @Test
    public void toVector3String() {
        Assert.assertEquals(VDFUtils.toVector3String(new Vector3()), "0.0 0.0 0.0");
        Assert.assertEquals(VDFUtils.toVector3String(new Vector3(1, 1, 1)), "1.0 1.0 1.0");
        Assert.assertEquals(VDFUtils.toVector3String(new Vector3(0.1f, 0.2f, 0.3f)), "0.1 0.2 0.3");
        Assert.assertEquals(VDFUtils.toVector3String(new Vector3(-1, 1, 0)), "-1.0 1.0 0.0");
    }

    @Test
    public void isVector2() {
        Assert.assertTrue(VDFUtils.isVector2("0 0"));
        Assert.assertTrue(VDFUtils.isVector2(" 0 0"));
        Assert.assertTrue(VDFUtils.isVector2("0 0 "));
        Assert.assertTrue(VDFUtils.isVector2(" 0 0 "));
        Assert.assertTrue(VDFUtils.isVector2(" 0   0 "));
        Assert.assertTrue(VDFUtils.isVector2("0.1 0.2"));
        Assert.assertFalse(VDFUtils.isVector2("1"));
        Assert.assertFalse(VDFUtils.isVector2(""));
    }

    @Test
    public void toVector2() {
        Assert.assertEquals(VDFUtils.toVector2("0 0"), new Vector2());
        Assert.assertEquals(VDFUtils.toVector2("1 1"), new Vector2(1, 1));
        Assert.assertEquals(VDFUtils.toVector2("0.1 0.2"), new Vector2(0.1f, 0.2f));
        Assert.assertEquals(VDFUtils.toVector2(" 0   1 "), new Vector2(0, 1));
        Assert.assertEquals(VDFUtils.toVector2("", Vector2.Zero), Vector2.Zero);
        Assert.assertEquals(VDFUtils.toVector2("-1 +1"), new Vector2(-1, +1));
    }

    @Test
    public void toVector2String() {
        Assert.assertEquals(VDFUtils.toVector2String(new Vector2()), "0.0 0.0");
        Assert.assertEquals(VDFUtils.toVector2String(new Vector2(1, 1)), "1.0 1.0");
        Assert.assertEquals(VDFUtils.toVector2String(new Vector2(0.1f, 0.2f)), "0.1 0.2");
        Assert.assertEquals(VDFUtils.toVector2String(new Vector2(-1, 1)), "-1.0 1.0");
    }
}