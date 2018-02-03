package eu.ensg.abn.project;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class EnveloppeConvexeJarvisTest {
	Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
    NuagePoint nuage = new NuagePoint(pts,pts.length);
    EnveloppeConvexeJarvis jarv = new EnveloppeConvexeJarvis(nuage);
	EnveloppeConvexeJarvis j = new EnveloppeConvexeJarvis();
	
	@Test
	public void testConvexHull() {
		
		Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
	    Coordinate[] res = new Coordinate[] {new Coordinate(12.066667,13.116667 ),new Coordinate(46.066667,11.116667),new Coordinate(48.6441,23.0852),new Coordinate(44.9441,93.0852),new Coordinate(16.066667,66.116667),new Coordinate(12.066667,13.116667)};
	    Coordinate[] test = j.convexHull(pts, pts.length);
	    assertArrayEquals(test, res);
	}
	
	@Test 
	public void testSens()
	{
		Coordinate p = new Coordinate(0,2);
		Coordinate q = new Coordinate(1,3);
		Coordinate r = new Coordinate(3,5);
		double res = 0;
		double test = j.sens(p, q, r);
	    assertTrue(res == test);
      
	}

	@Test
	public void testExportshapefile() {
		jarv.exportShapefile();
	}
	
	
	
}  
