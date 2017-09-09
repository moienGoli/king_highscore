//package com.king;
//
//import com.king.model.*;
//import com.king.exception.AppException;
//import com.king.factory.PolygonFactory;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import static org.junit.Assert.assertTrue;
//
//
///**
// * Unit test for PolygonFactory.
// */
//public class PolygonFactoryTest {
//
//
//    @Rule
//    public ExpectedException expectedEx = ExpectedException.none();
//
//    @Test
//    public void testCreateEquilateral() {
//
//        Polygon equilateralTriangle = PolygonFactory.create(5, 5, 5);
//        assertTrue(equilateralTriangle instanceof Equilateral);
//        assertTrue(TriangleType.EQUILATERAL.equals(((Triangle) equilateralTriangle).getType()));
//    }
//
//    @Test
//    public void testCreateIsosceles() {
//
//        Polygon isoscelesTriangle = PolygonFactory.create(5, 5, 2);
//        assertTrue(isoscelesTriangle instanceof Isosceles);
//        assertTrue(TriangleType.ISOSCELES.equals(((Triangle) isoscelesTriangle).getType()));
//    }
//
//    @Test
//    public void testCreateScalene() {
//
//        Polygon scaleneTriangle = PolygonFactory.create(5, 3, 4);
//        assertTrue(scaleneTriangle instanceof Scalene);
//        assertTrue(TriangleType.SCALENE.equals(((Triangle) scaleneTriangle).getType()));
//    }
//
//    @Test
//    public void testCreateInvalidTriangle() {
//
//        expectedEx.expect(AppException.class);
//        expectedEx.expectMessage("Triangle inequality");
//        PolygonFactory.create(2, 1, 3);
//    }
//
//    @Test
//    public void testInvalidPolygonWithNegativeSide() {
//
//        expectedEx.expect(AppException.class);
//        expectedEx.expectMessage("Invalid polygon");
//        PolygonFactory.create(2, -1, 3);
//    }
//
//    @Test
//    public void testInvalidPolygonWithZeroSizedSide() {
//
//        expectedEx.expect(AppException.class);
//        expectedEx.expectMessage("Invalid polygon");
//        PolygonFactory.create(2, 0, 3);
//    }
//
//    @Test
//    public void testInvalidPolygonWithLessThanThreeSides() {
//
//        expectedEx.expect(AppException.class);
//        expectedEx.expectMessage("Invalid polygon");
//        PolygonFactory.create(2, 4);
//    }
//
//    @Test
//    public void testNotImplementedPolygon() {
//
//        expectedEx.expect(AppException.class);
//        expectedEx.expectMessage("Not Implemented");
//        PolygonFactory.create(2, 4, 5, 4);
//    }
//
//}
