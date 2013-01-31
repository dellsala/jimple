package org.jimple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JimpleTest {
    private Jimple jimple;

    @Before
    public void setUp() throws Exception {
        this.jimple = new Jimple();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testShare() throws Exception {
        Assert.assertFalse(this.jimple.share(new Jimple.Item() {
            @Override
            public Object create(Jimple c) {
                return new Object();
            }
        }) instanceof Jimple.Item);
    }

    @Test
    public void testExtend() throws Exception {
        this.jimple.put("test", new Jimple.Item() {
            @Override
            public Object create(Jimple c) {
                return new Object();
            }
        });
        Assert.assertFalse(this.jimple.extend("test", new Jimple.Extender() {
            @Override
            public Object extend(Object object) {
                return new Object();
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
        this.jimple.put("test", new Jimple.Item() {
            @Override
            public Object create(Jimple c) {
                return new Object();
            }
        });
        Assert.assertFalse(this.jimple.get("test") instanceof Jimple.Item);
        Assert.assertNotSame(this.jimple.get("test"), this.jimple.get("test"));
    }

    @Test
    public void testGetSharedItem() throws Exception {
        this.jimple.put("test", this.jimple.share(new Jimple.Item() {
            @Override
            public Object create(Jimple c) {
                return new Object();
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
        this.jimple.put("test", new Jimple.Item() {
            @Override
            public Object create(Jimple c) {
                return new Object();
            }
        });
        Assert.assertTrue(this.jimple.containsKey("test"));
    }

    @Test
    public void testRaw() throws Exception {
        Jimple.Item item = new Jimple.Item() {
            @Override
            public Object create(Jimple c) {
                return new Object();
            }
        };
        this.jimple.put("test", item);
        Assert.assertSame(item, this.jimple.raw("test"));
    }
}
