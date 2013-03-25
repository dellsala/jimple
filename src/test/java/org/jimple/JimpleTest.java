package org.jimple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class JimpleTest {
    private Jimple jimple;

    private class TestItem {
    }

    private class TestExtendedItem {
    }

    @Before
    public void setUp() throws Exception {
        this.jimple = new Jimple();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testShare() throws Exception {
        Assert.assertFalse(this.jimple.share(new Jimple.Item<TestItem>() {
            @Override
            public TestItem create(Jimple c) {
                return new TestItem();
            }
        }) instanceof Jimple.Item);
    }

    @Test
    public void testExtend() throws Exception {
        this.jimple.put("test", new Jimple.Item<TestItem>() {
            @Override
            public TestItem create(Jimple c) {
                return new TestItem();
            }
        });
        //noinspection unchecked
        Assert.assertFalse(this.jimple.extend("test", new Jimple.Extender<TestExtendedItem>() {
            @Override
            public TestExtendedItem extend(TestExtendedItem item) {
                return new TestExtendedItem();
            }
        }) instanceof Jimple.Item);
    }

    @Test
    public void testGetString() throws Exception {
        String item = "test";
        this.jimple.put("test", item);
        Assert.assertSame(item, this.jimple.get("test"));
    }

    @Test
    public void testGetItem() throws Exception {
        this.jimple.put("test", new Jimple.Item<TestItem>() {
            @Override
            public TestItem create(Jimple c) {
                return new TestItem();
            }
        });
        Assert.assertFalse(this.jimple.get("test") instanceof Jimple.Item);
        Assert.assertNotSame(this.jimple.get("test"), this.jimple.get("test"));
    }

    @Test
    public void testGetSharedItem() throws Exception {
        this.jimple.put("test", this.jimple.share(new Jimple.Item<TestItem>() {
            @Override
            public TestItem create(Jimple c) {
                return new TestItem();
            }
        }));
        Assert.assertFalse(this.jimple.get("test") instanceof Jimple.Item);
        Assert.assertSame(this.jimple.get("test"), this.jimple.get("test"));
    }

    @Test
    public void testPutString() throws Exception {
        this.jimple.put("test", "test");
        Assert.assertTrue(this.jimple.containsKey("test"));
    }

    @Test
    public void testPutItem() throws Exception {
        this.jimple.put("test", new Jimple.Item<TestItem>() {
            @Override
            public TestItem create(Jimple c) {
                return new TestItem();
            }
        });
        Assert.assertTrue(this.jimple.containsKey("test"));
    }

    @Test
    public void testExtendItem() throws Exception {
        this.jimple.put("test", new Jimple.Item<HashMap<String, String>>() {
            @Override
            public HashMap<String, String> create(Jimple c) {
                return new HashMap<String, String>();
            }
        });
        this.jimple.put("extendedTest", this.jimple.extend("test", new Jimple.Extender<HashMap<String, String>>() {
            @Override
            public <E extends HashMap<String, String>> E extend(HashMap<String, String> map) {
                map.put("test", "test");
                //noinspection unchecked
                return (E) map;
            }
        }));
        Assert.assertTrue(this.jimple.get("test") instanceof HashMap);
        Assert.assertTrue(this.jimple.get("extendedTest") instanceof HashMap);
        //noinspection unchecked
        Assert.assertTrue(((HashMap<String, String>) this.jimple.get("extendedTest")).containsKey("test"));
        //noinspection unchecked
        Assert.assertFalse(((HashMap<String, String>) this.jimple.get("test")).containsKey("test"));
    }

    @Test
    public void testRaw() throws Exception {
        Jimple.Item item = new Jimple.Item<TestItem>() {
            @Override
            public TestItem create(Jimple c) {
                return new TestItem();
            }
        };
        this.jimple.put("test", item);
        Assert.assertSame(item, this.jimple.raw("test"));
    }
}
