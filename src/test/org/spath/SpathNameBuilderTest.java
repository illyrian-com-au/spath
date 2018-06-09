package org.spath;

import java.math.BigDecimal;

import org.junit.Test;

import junit.framework.TestCase;

public class SpathNameBuilderTest extends TestCase {
    SpathNameBuilder builder = new SpathNameBuilder();

    @Test
    public void testSimpleRoot() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data", element.toString());
    }

    @Test
    public void testSimpleRelative() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.RELATIVE)
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("//data", element.toString());
    }

    @Test
    public void testRootStar() {
        SpathName element = builder
                .withStar()
                .withType(SpathType.ROOT)
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/*", element.toString());
    }

    @Test
    public void testRelativeStar() {
        SpathName element = builder
                .withStar()
                .withType(SpathType.RELATIVE)
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("//*", element.toString());
    }

    @Test
    public void testRootPath() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .next()
                .withName("address")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data/address", element.toString());
        assertEquals(SpathType.ROOT, element.getType());
        assertEquals(SpathType.ROOT, element.getParent().getType());
    }

    @Test
    public void testRootPathIndirect() {
        SpathName element = new SpathNameBuilder(
                    new SpathNameBuilder()
                            .withName("data")
                            .withType(SpathType.ROOT)
                            .build())
                .withName("address")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data/address", element.toString());
        assertEquals(SpathType.ROOT, element.getType());
        assertEquals(SpathType.ROOT, element.getParent().getType());
    }

    @Test
    public void testRelativePath() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.RELATIVE)
                .next()
                .withName("address")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("//data/address", element.toString());
        assertEquals(SpathType.ELEMENT, element.getType());
        assertEquals(SpathType.RELATIVE, element.getParent().getType());
    }

    @Test
    public void testRelativePath2() {
        SpathName element = new SpathNameBuilder(
                        new SpathNameBuilder()
                            .withName("data")
                            .withType(SpathType.RELATIVE)
                            .build())
                .withName("address")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("//data/address", element.toString());
        assertEquals(SpathType.ELEMENT, element.getType());
        assertEquals(SpathType.RELATIVE, element.getParent().getType());
    }

    @Test
    public void testAbsoluteRelativePath() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .next()
                .withName("address")
                .withType(SpathType.RELATIVE)
                .next()
                .withName("street")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data//address/street", element.toString());
        assertEquals(SpathType.ELEMENT, element.getType());
        assertEquals(SpathType.RELATIVE, element.getParent().getType());
        assertEquals(SpathType.ROOT, element.getParent().getParent().getType());
    }

    @Test
    public void testAbsoluteRelativePath2() {
        SpathName element = new SpathNameBuilder(
                    new SpathNameBuilder(
                         new SpathNameBuilder()
                         .withName("data")
                         .withType(SpathType.ROOT)
                         .build())
                    .withName("address")
                    .withType(SpathType.RELATIVE)
                    .build())
                .withName("street")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data//address/street", element.toString());
        assertEquals(SpathType.ELEMENT, element.getType());
        assertEquals(SpathType.RELATIVE, element.getParent().getType());
        assertEquals(SpathType.ROOT, element.getParent().getParent().getType());
    }

    @Test
    public void testPredicateName() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .withPredicate("currency")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data[@currency]", element.toString());
    }

    @Test
    public void testPredicateString() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .withPredicate("currency", SpathOperator.EQ, "AUD")
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data[@currency='AUD']", element.toString());
    }

    @Test
    public void testPredicateNumber() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .withPredicate("currency", SpathOperator.EQ, new BigDecimal("10.25"))
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data[@currency=10.25]", element.toString());
    }

    @Test
    public void testPredicateBoolean() {
        SpathName element = builder
                .withName("data")
                .withType(SpathType.ROOT)
                .withPredicate("currency", SpathOperator.EQ, Boolean.TRUE)
                .build();
        assertNotNull("SpathNameBuilder returned null", element.toString());
        assertEquals("/data[@currency=true]", element.toString());
    }
}
