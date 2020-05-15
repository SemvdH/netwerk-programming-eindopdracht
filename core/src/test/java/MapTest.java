import netwerkprog.game.client.map.Map;
import org.junit.Assert;
import org.junit.Test;

public class MapTest {

    @Test
    public void testNull() {
        Map map = new Map((char[][]) null);

        Assert.assertEquals(-1,map.getHeight());
        Assert.assertEquals(-1,map.getWidth());
    }

    @Test
    public void testWidthAndHeight() {
        char[][] arr = new char[][] {
                {'#','#','#','#'},
                {'#','#','#','#'},
                {'#','#','#','#'}
        };

        Map map = new Map(arr);

        Assert.assertEquals(4,map.getWidth());
        Assert.assertEquals(3,map.getHeight());
    }

    @Test
    public void testGetElement() {
        char[][] arr = new char[][] {
                {'#','#','#','#'},
                {'#','#','#','#'},
                {'#','#','_','#'}
        };

        Map map = new Map(arr);

        Assert.assertEquals('#',map.get(1,1));
        Assert.assertEquals('_',map.get(2,2));
        Assert.assertEquals(Character.MIN_VALUE,map.get(1,8));
        Assert.assertEquals(Character.MIN_VALUE,map.get(4,2));
        Assert.assertEquals(Character.MIN_VALUE,map.get(4,8));
    }

    @Test
    public void testStringArray() {
        String[] strings = new String[]{
                "####",
                "#  #",
                "####"
        };

        char[][] testArr = new char[][]{
          strings[0].toCharArray(),
          strings[1].toCharArray(),
          strings[2].toCharArray()
        };

        Map map = new Map(strings);

        Assert.assertEquals(4,map.getWidth());
        Assert.assertEquals(3,map.getHeight());
        Assert.assertEquals('#',map.get(0,0));
        Assert.assertEquals(' ',map.get(1,1));
        Assert.assertArrayEquals(testArr,map.get());

    }
}
