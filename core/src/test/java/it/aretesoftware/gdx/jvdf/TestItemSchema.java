package it.aretesoftware.gdx.jvdf;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author BucketOfBroccoli
 */
public class TestItemSchema extends GdxBaseTest {

    @Test
    public void testItemsGame() {
        String contents = getFileContents("resources/items_game.txt");
        GdxVDFParser parser = new GdxVDFParser();

        long start, end;
        GdxVDFNode root = null;
        for (int counter = 1; counter <= 1; counter++) {
            start = System.nanoTime();
            root = parser.parse(contents);
            end = System.nanoTime();
            System.out.println(counter + ") Time to parse: " + ((end - start) / 1000000f) + " milliseconds");
        }

        GdxVDFPreprocessor preprocessor = new GdxVDFPreprocessor();
        String processed1 = preprocessor.process(contents);
        String processed2 = preprocessor.process(root.toVDF());
        Assert.assertEquals(processed1.length(), processed2.length());
        Assert.assertEquals(processed1, processed2);
    }

}
