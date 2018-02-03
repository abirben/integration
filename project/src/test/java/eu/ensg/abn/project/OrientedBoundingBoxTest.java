package eu.ensg.abn.project;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;

public class OrientedBoundingBoxTest {
	
	@Test
	public void testExportshapefile()
	{
	
	Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
    NuagePoint nuage = new NuagePoint(pts,pts.length);
    OrientedBoundingBox box = new OrientedBoundingBox(nuage);
    box.exportShapefile();
	
	} 
	@Test
	public void testRotateCoord()
	{
	
    OrientedBoundingBox box = new OrientedBoundingBox();	
	
	Coordinate[] coors = new Coordinate[] { new Coordinate(1,1),  new Coordinate(1,3)};
	Coordinate[] test = new Coordinate[] { new Coordinate(1,1),  new Coordinate(1,3)};
	Coordinate c = new Coordinate(1,1);
    Coordinate[] res = box.rotateCoord(coors, c, 0.0);
    assertArrayEquals(test, res);
	}

	
		
	
}
