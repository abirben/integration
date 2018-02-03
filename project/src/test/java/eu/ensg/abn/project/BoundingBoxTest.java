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

public class BoundingBoxTest {
	@Test
	public void testCoor()
	{   int n = 0;
	    Coordinate[] coordinates = new Coordinate[n];
	    NuagePoint nuagepts = new NuagePoint(coordinates,coordinates.length);
		Coordinate[] pts = new Coordinate[] {new Coordinate(11.0,15.0),new Coordinate(15.0,15.0),new Coordinate(10.0,12.0),new Coordinate(17.0,20.0),new Coordinate(10.0,25.0)};
	    Coordinate[] res = new Coordinate[] {new Coordinate(10.0,12.0),new Coordinate(17.0,12.0),new Coordinate(17.0,25.0),new Coordinate(10.0,25.0),new Coordinate(10.0,12.0)};
	    
	    BoundingBox b = new BoundingBox(nuagepts);
	    Coordinate[] test = b.boundingBoxCoor(pts);
	    assertArrayEquals(test, res);
	}

	
	
	@Test
	public void testExportshapefile()
	{
	
	Coordinate[] pts = new Coordinate[] {new Coordinate(46.066667,11.116667 ), new Coordinate(44.9441,93.0852),new Coordinate(42.9041,13.0852),new Coordinate(48.6441,23.0852),new Coordinate(30.9441,41.0852),new Coordinate(12.066667,13.116667),new Coordinate(16.066667,66.116667 )};
    NuagePoint nuage = new NuagePoint(pts,pts.length);
    BoundingBox box = new BoundingBox(nuage);
    box.exportShapefile();
	
	}

}


