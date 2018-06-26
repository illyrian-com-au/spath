package org.spath;

import java.math.BigDecimal;

import org.junit.Test;
import org.spath.query.SpathQueryBuilder;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathQueryType;

import junit.framework.TestCase;

public class SpathQueryBuilderTest extends TestCase {
    SpathQueryBuilder builder = new SpathQueryBuilder();

    @Test
    public void testSimpleRoot() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data", element.toString());
    }

    @Test
    public void testSimpleRelative() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.RELATIVE)
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("//data", element.toString());
    }

    @Test
    public void testRootStar() {
        SpathQuery element = builder
                .withStar()
                .withType(SpathQueryType.ROOT)
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/*", element.toString());
    }

    @Test
    public void testRelativeStar() {
        SpathQuery element = builder
                .withStar()
                .withType(SpathQueryType.RELATIVE)
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("//*", element.toString());
    }

    @Test
    public void testRootPath() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .next()
                .withName("address")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data/address", element.toString());
        assertEquals(SpathQueryType.ROOT, element.getType());
        assertEquals(SpathQueryType.ROOT, element.getParent().getType());
    }

    @Test
    public void testRootPathIndirect() {
        SpathQuery element = new SpathQueryBuilder(
                    new SpathQueryBuilder()
                            .withName("data")
                            .withType(SpathQueryType.ROOT)
                            .build())
                .withName("address")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data/address", element.toString());
        assertEquals(SpathQueryType.ROOT, element.getType());
        assertEquals(SpathQueryType.ROOT, element.getParent().getType());
    }

    @Test
    public void testRelativePath() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.RELATIVE)
                .next()
                .withName("address")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("//data/address", element.toString());
        assertEquals(SpathQueryType.ELEMENT, element.getType());
        assertEquals(SpathQueryType.RELATIVE, element.getParent().getType());
    }

    @Test
    public void testRelativePath2() {
        SpathQuery element = new SpathQueryBuilder(
                        new SpathQueryBuilder()
                            .withName("data")
                            .withType(SpathQueryType.RELATIVE)
                            .build())
                .withName("address")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("//data/address", element.toString());
        assertEquals(SpathQueryType.ELEMENT, element.getType());
        assertEquals(SpathQueryType.RELATIVE, element.getParent().getType());
    }

    @Test
    public void testAbsoluteRelativePath() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .next()
                .withName("address")
                .withType(SpathQueryType.RELATIVE)
                .next()
                .withName("street")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data//address/street", element.toString());
        assertEquals(SpathQueryType.ELEMENT, element.getType());
        assertEquals(SpathQueryType.RELATIVE, element.getParent().getType());
        assertEquals(SpathQueryType.ROOT, element.getParent().getParent().getType());
    }

    @Test
    public void testAbsoluteRelativePath2() {
        SpathQuery element = new SpathQueryBuilder(
                    new SpathQueryBuilder(
                         new SpathQueryBuilder()
                         .withName("data")
                         .withType(SpathQueryType.ROOT)
                         .build())
                    .withName("address")
                    .withType(SpathQueryType.RELATIVE)
                    .build())
                .withName("street")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data//address/street", element.toString());
        assertEquals(SpathQueryType.ELEMENT, element.getType());
        assertEquals(SpathQueryType.RELATIVE, element.getParent().getType());
        assertEquals(SpathQueryType.ROOT, element.getParent().getParent().getType());
    }

    @Test
    public void testPredicateName() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .withPredicate("currency")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data[@currency]", element.toString());
    }

    @Test
    public void testPredicateString() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .withPredicate("currency", SpathPredicateOperator.EQ, "AUD")
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data[@currency='AUD']", element.toString());
    }

    @Test
    public void testPredicateNumber() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .withPredicate("currency", SpathPredicateOperator.EQ, new BigDecimal("10.25"))
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data[@currency=10.25]", element.toString());
    }

    @Test
    public void testPredicateBoolean() {
        SpathQuery element = builder
                .withName("data")
                .withType(SpathQueryType.ROOT)
                .withPredicate("currency", SpathPredicateOperator.EQ, Boolean.TRUE)
                .build();
        assertNotNull("SpathQueryBuilder returned null", element.toString());
        assertEquals("/data[@currency=true]", element.toString());
    }
}
