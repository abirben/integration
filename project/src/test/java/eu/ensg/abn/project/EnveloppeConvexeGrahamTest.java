package eu.ensg.abn.project;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class EnveloppeConvexeGrahamTest {
	int n =0;
	Coordinate[] coordinates = new Coordinate[n];
    NuagePoint nuagepts = new NuagePoint(coordinates,coordinates.length);
	EnveloppeConvexeGraham env = new EnveloppeConvexeGraham(nuagepts);

	@Test
	public void testConvexHull() {
		
		Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
	    Coordinate[] res = new Coordinate[] {new Coordinate(46.066667,11.116667 ),new Coordinate(48.6441,23.0852),new Coordinate(44.9441,93.0852),new Coordinate(16.066667,66.116667),new Coordinate(12.066667,13.116667),new Coordinate(46.066667,11.116667 )};
	    Coordinate[] test = env.convexHull(pts, n);
	    assertArrayEquals(test, res);
	}

	@Test
	public void testToCoordinateArray() {
		
		Stack<Coordinate> test = new Stack<Coordinate>();
		test.push(new Coordinate(1,5));
		test.push(new Coordinate(2,3));
		test.push(new Coordinate(4,8));
		Coordinate[] pts = new Coordinate[] {new Coordinate(1,5),new Coordinate(2,3),new Coordinate(4,8)};
		Coordinate[] res = env.toCoordinateArray(test);
		assertArrayEquals(pts,res);
		
	} 
	@Test
	public void testPlusPetiteOrd()
	
	{   
		Coordinate[] pts = new Coordinate[] {new Coordinate(1,5),new Coordinate(2,3),new Coordinate(4,8)};
		Coordinate c = new Coordinate(2,3);
		Coordinate res = env.getpetitOrd(pts);
		assertEquals(c,res);
	}
	
  @Test
  public void testExportshapefile()
  {   
	Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
    NuagePoint nuage = new NuagePoint(pts,pts.length);
    EnveloppeConvexeGraham env = new EnveloppeConvexeGraham(nuage);
    env.exportShapefile();
  }
}
