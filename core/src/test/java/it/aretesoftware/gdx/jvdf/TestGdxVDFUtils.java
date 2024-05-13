package it.aretesoftware.gdx.jvdf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author BucketOfBroccoli
 */
public class TestGdxVDFUtils {

    @Test
    public void toObject() {
        Assert.assertEquals(GdxVDFUtils.toObject("0 0 0 0"), Color.CLEAR);
        Assert.assertEquals(GdxVDFUtils.toObject("0.0 0.0 0.0 0.0"), Color.CLEAR);
        Assert.assertEquals(GdxVDFUtils.toObject(".0 0.0f 0F 0.0"), Color.CLEAR);
        Assert.assertEquals(GdxVDFUtils.toObject("0 0 0"), Vector3.Zero);
        Assert.assertEquals(GdxVDFUtils.toObject("0 0"), Vector2.Zero);
        Assert.assertEquals(GdxVDFUtils.toObject("true"), true);
        Assert.assertEquals(GdxVDFUtils.toObject("0.1"), 0.1f);
        Assert.assertEquals(GdxVDFUtils.toObject("0.1f"), 0.1f);
        Assert.assertEquals(GdxVDFUtils.toObject("0.1F"), 0.1f);
        Assert.assertEquals(GdxVDFUtils.toObject("0.1d"), 0.1d);
        Assert.assertEquals(GdxVDFUtils.toObject("0.1D"), 0.1D);
        Assert.assertEquals(GdxVDFUtils.toObject("1"), 1);
        Assert.assertEquals(GdxVDFUtils.toObject("1000"), 1000);
        Assert.assertEquals(GdxVDFUtils.toObject("0x8B87"), 35719);
        Assert.assertEquals(GdxVDFUtils.toObject("#8B87"), 35719);
        Assert.assertEquals(GdxVDFUtils.toObject("#0x8B87"), "#0x8B87");
        Assert.assertEquals(GdxVDFUtils.toObject("1 2 3 4 5"), "1 2 3 4 5");
        Assert.assertEquals(GdxVDFUtils.toObject("String"), "String");
        Assert.assertEquals(GdxVDFUtils.toObject("0"), 0);
        Assert.assertEquals(GdxVDFUtils.toObject("#"), "#");
        Assert.assertEquals(GdxVDFUtils.toObject("0x"), "0x");
        Assert.assertEquals(GdxVDFUtils.toObject("0X"), "0X");
        Assert.assertEquals(GdxVDFUtils.toObject(""), "");
        Assert.assertNull(GdxVDFUtils.toObject(null));
    }

    @Test
    public void toObjectString() {
        Object obj;
        Assert.assertEquals(GdxVDFUtils.toString(obj = Color.BLACK), "0.0 0.0 0.0 1.0");
        Assert.assertEquals(GdxVDFUtils.toString(obj = Vector3.Zero), "0.0 0.0 0.0");
        Assert.assertEquals(GdxVDFUtils.toString(obj = Vector2.Zero), "0.0 0.0");
        Assert.assertEquals(GdxVDFUtils.toString(obj = true), "true");
        Assert.assertEquals(GdxVDFUtils.toString(obj = 0.1f), "0.1");
        Assert.assertEquals(GdxVDFUtils.toString(obj = 0.1), "0.1");
        Assert.assertEquals(GdxVDFUtils.toString(obj = 0.1d), "0.1");
        Assert.assertEquals(GdxVDFUtils.toString(obj = 1), "1");
        Assert.assertEquals(GdxVDFUtils.toString(obj = 1L), "1");
        Assert.assertEquals(GdxVDFUtils.toString(obj = "0x8B87"), "0x8B87");
        Assert.assertEquals(GdxVDFUtils.toString(obj = "#8B87"), "#8B87");
        Assert.assertEquals(GdxVDFUtils.toString(obj = "#0x8B87"), "#0x8B87");
        Assert.assertEquals(GdxVDFUtils.toString(obj = "String"), "String");
        Assert.assertNull(GdxVDFUtils.toString(obj = null));
    }

    @Test
    public void isBoolean() {
        Assert.assertTrue(GdxVDFUtils.isBoolean("true"));
        Assert.assertTrue(GdxVDFUtils.isBoolean("false"));
        Assert.assertFalse(GdxVDFUtils.isBoolean("0"));
        Assert.assertFalse(GdxVDFUtils.isBoolean("1"));
        Assert.assertFalse(GdxVDFUtils.isBoolean("Test"));
    }

    @Test
    public void toBoolean() {
        Assert.assertTrue(GdxVDFUtils.toBoolean("true"));
        Assert.assertFalse(GdxVDFUtils.toBoolean("false"));
        Assert.assertTrue(GdxVDFUtils.toBoolean("Test", true));
    }

    @Test
    public void toBooleanString() {
        Assert.assertEquals(GdxVDFUtils.toBooleanString(true), "true");
        Assert.assertEquals(GdxVDFUtils.toBooleanString(false), "false");
        Assert.assertEquals(GdxVDFUtils.toBooleanString(0), "false");
        Assert.assertEquals(GdxVDFUtils.toBooleanString(1), "true");
        Assert.assertEquals(GdxVDFUtils.toBooleanString(-1), "false");
    }

    @Test
    public void isColor() {
        Assert.assertTrue(GdxVDFUtils.isColor("1 0 0 0"));
        Assert.assertTrue(GdxVDFUtils.isColor("1.0 0.0 0.0 0.0"));
        Assert.assertTrue(GdxVDFUtils.isColor("1 0.0 0.4 0"));
        Assert.assertTrue(GdxVDFUtils.isColor("+1 0 0 0"));
        Assert.assertTrue(GdxVDFUtils.isColor("-1 0 0 0"));
        Assert.assertFalse(GdxVDFUtils.isColor("t e s t"));
        Assert.assertFalse(GdxVDFUtils.isColor("1 0 0"));
    }

    @Test
    public void toColor() {
        Assert.assertEquals(GdxVDFUtils.toColor("1 1 1 1"), new Color(1, 1, 1, 1));
        Assert.assertEquals(GdxVDFUtils.toColor("0.1 0.2 0.3 0.4"), new Color(0.1f, 0.2f, 0.3f, 0.4f));
        Assert.assertEquals(GdxVDFUtils.toColor("255 255 255 255"), new Color(255, 255, 255, 255));
        Assert.assertEquals(GdxVDFUtils.toColor("0   0 0   0"), Color.CLEAR);
        Assert.assertEquals(GdxVDFUtils.toColor("1 1 1", Color.CLEAR), Color.CLEAR);
        Assert.assertEquals(GdxVDFUtils.toColor("-1 +1 0 0"), new Color(-1, +1, 0, 0));

    }

    @Test
    public void toColorString() {
        Assert.assertEquals(GdxVDFUtils.toColorString(Color.BLACK), "0.0 0.0 0.0 1.0");
        Assert.assertEquals(GdxVDFUtils.toColorString(Color.WHITE), "1.0 1.0 1.0 1.0");
        Assert.assertEquals(GdxVDFUtils.toColorString(new Color(0.1f, 0.2f, 0.3f, 0.4f)), "0.1 0.2 0.3 0.4");
    }

    @Test
    public void isVector3() {
        Assert.assertTrue(GdxVDFUtils.isVector3("0 0 0"));
        Assert.assertTrue(GdxVDFUtils.isVector3(" 0 0 0"));
        Assert.assertTrue(GdxVDFUtils.isVector3("0 0 0 "));
        Assert.assertTrue(GdxVDFUtils.isVector3(" 0 0 0 "));
        Assert.assertTrue(GdxVDFUtils.isVector3(" 0   0 0 "));
        Assert.assertTrue(GdxVDFUtils.isVector3("0.1 0.2 0.3"));
        Assert.assertFalse(GdxVDFUtils.isVector3("1 1"));
        Assert.assertFalse(GdxVDFUtils.isVector3("1"));
        Assert.assertFalse(GdxVDFUtils.isVector3(""));
    }

    @Test
    public void toVector3() {
        Assert.assertEquals(GdxVDFUtils.toVector3("0 0 0"), new Vector3());
        Assert.assertEquals(GdxVDFUtils.toVector3("1 1 1"), new Vector3(1, 1, 1));
        Assert.assertEquals(GdxVDFUtils.toVector3("0.1 0.2 0.3"), new Vector3(0.1f, 0.2f, 0.3f));
        Assert.assertEquals(GdxVDFUtils.toVector3(" 0   1 2"), new Vector3(0, 1, 2));
        Assert.assertEquals(GdxVDFUtils.toVector3("", Vector3.Zero), Vector3.Zero);
        Assert.assertEquals(GdxVDFUtils.toVector3("-1 +1 0"), new Vector3(-1, +1, 0));
    }

    @Test
    public void toVector3String() {
        Assert.assertEquals(GdxVDFUtils.toVector3String(new Vector3()), "0.0 0.0 0.0");
        Assert.assertEquals(GdxVDFUtils.toVector3String(new Vector3(1, 1, 1)), "1.0 1.0 1.0");
        Assert.assertEquals(GdxVDFUtils.toVector3String(new Vector3(0.1f, 0.2f, 0.3f)), "0.1 0.2 0.3");
        Assert.assertEquals(GdxVDFUtils.toVector3String(new Vector3(-1, 1, 0)), "-1.0 1.0 0.0");
    }

    @Test
    public void isVector2() {
        Assert.assertTrue(GdxVDFUtils.isVector2("0 0"));
        Assert.assertTrue(GdxVDFUtils.isVector2(" 0 0"));
        Assert.assertTrue(GdxVDFUtils.isVector2("0 0 "));
        Assert.assertTrue(GdxVDFUtils.isVector2(" 0 0 "));
        Assert.assertTrue(GdxVDFUtils.isVector2(" 0   0 "));
        Assert.assertTrue(GdxVDFUtils.isVector2("0.1 0.2"));
        Assert.assertFalse(GdxVDFUtils.isVector2("1"));
        Assert.assertFalse(GdxVDFUtils.isVector2(""));
    }

    @Test
    public void toVector2() {
        Assert.assertEquals(GdxVDFUtils.toVector2("0 0"), new Vector2());
        Assert.assertEquals(GdxVDFUtils.toVector2("1 1"), new Vector2(1, 1));
        Assert.assertEquals(GdxVDFUtils.toVector2("0.1 0.2"), new Vector2(0.1f, 0.2f));
        Assert.assertEquals(GdxVDFUtils.toVector2(" 0   1 "), new Vector2(0, 1));
        Assert.assertEquals(GdxVDFUtils.toVector2("", Vector2.Zero), Vector2.Zero);
        Assert.assertEquals(GdxVDFUtils.toVector2("-1 +1"), new Vector2(-1, +1));
    }

    @Test
    public void toVector2String() {
        Assert.assertEquals(GdxVDFUtils.toVector2String(new Vector2()), "0.0 0.0");
        Assert.assertEquals(GdxVDFUtils.toVector2String(new Vector2(1, 1)), "1.0 1.0");
        Assert.assertEquals(GdxVDFUtils.toVector2String(new Vector2(0.1f, 0.2f)), "0.1 0.2");
        Assert.assertEquals(GdxVDFUtils.toVector2String(new Vector2(-1, 1)), "-1.0 1.0");
    }
}