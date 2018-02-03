package eu.ensg.abn.project;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class NuagePointTest {


	@Test
	public void testGenerationShapeFile() {
		Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
		System.out.println(pts.length);
		NuagePoint nuage = new NuagePoint(pts,pts.length);
		nuage.exportShapefile();
	}

}
