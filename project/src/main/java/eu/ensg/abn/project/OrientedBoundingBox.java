package eu.ensg.abn.project;
/**
 * Créer l'oriented bounding box à partir de sommets de l'enveloppe convexe 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class OrientedBoundingBox implements Ifeature {
	int longeur;
	Coordinate[] coordinates = new Coordinate[longeur];
	NuagePoint nuagepts = new NuagePoint(coordinates,coordinates.length);
	EnveloppeConvexeJarvis j = new EnveloppeConvexeJarvis(nuagepts);
	
	
	public OrientedBoundingBox(NuagePoint nuagepts)
	{
		this.nuagepts= nuagepts;
	}
	public OrientedBoundingBox()
	{
		
	}
	/**
	 * Rotation d'une liste de coordonnées selon un centre et un angle données
	 * @param coord
	 * @param centre
	 * @param angle
	 * @return {@link Coordinate}
	 */
	public static Coordinate[] rotateCoord(Coordinate[] coord, Coordinate centre, double angle){
		Coordinate[] nv_coord= new Coordinate[coord.length];
		Coordinate c;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double xc = centre.x;
		double yc = centre.y;
		double x, y;
		for(int i=0; i<coord.length; i++) {
			c = coord[i];
			x = c.x;
			y = c.y;
			nv_coord[i] = new Coordinate(xc+cos*(x-xc)-sin*(y-yc), yc+sin*(x-xc)+cos*(y-yc));
		}
		return nv_coord;
	}
	/**
	 * Rotation d'un linear ring et la création d'un nouveau linear ring
	 * @param ligne
	 * @param centre
	 * @param angle
	 * @param geomf
	 * @return {@link LinearRing}
	 */
	public  LinearRing rotateLinear(LinearRing ligne, Coordinate centre, double angle, GeometryFactory geomf) {
		return geomf.createLinearRing(rotateCoord(ligne.getCoordinates(), centre, angle));
	}
	public  Polygon rotatePoly(Polygon p, Coordinate c, double angle, GeometryFactory geomf) {
		
	    LinearRing ligne =  rotateLinear((LinearRing)p.getExteriorRing(), c, angle, geomf);
		
	    LinearRing[] ligne_r = new LinearRing[p.getNumInteriorRing()];
		
	    for(int j=0; j<p.getNumInteriorRing(); j++) 
	    ligne_r[j] =  rotateLinear((LinearRing)p.getInteriorRingN(j), c, angle, geomf);
		return geomf.createPolygon(ligne, ligne_r);
	}
	/**
	 * on identifie le centre c de l'enveloppe convexe , on calcule l'angle 
	 * on fait la rotation jusqu'a obtenir le rectange de surface minimale 
	 * qui englobe tt les points
	 *
	 * @param pts
	 * @param geomf
	 * @return {@link Polygon}
	 */
	public  Polygon orientedBox(Coordinate[] pts, GeometryFactory geomf){
		
		NuagePoint nuage = new NuagePoint(pts,pts.length);
		
		EnveloppeConvexeJarvis j = new EnveloppeConvexeJarvis(nuage);
		
		Coordinate[] coords = j.convexHull(pts, pts.length);
		
		Polygon box = (Polygon) geomf.createPolygon(coords);
		
		Coordinate c = box.getCentroid().getCoordinate();
		
        for (Coordinate temp : coords)
        	
        {  System.out.println("(" + temp.x + ", " +
                               temp.y + ")");
        
        }
       
        System.out.println("(" + c.x + ", " +c.y + ")");
       
        double minAngle = 0.0;
		double minArea = Double.MAX_VALUE ;
		Polygon res = null;
		Coordinate ci = coords[0], cii;
		for(int i=0; i<coords.length-1; i++){
			cii = coords[i+1];
			double angle = Math.atan2(cii.y-ci.y, cii.x-ci.x);
			Polygon test = (Polygon) rotatePoly(box, c, -1.0*angle, geomf).getEnvelope();
			double area = test.getArea();
			if (area < minArea) {
				minArea = area;
				res = test;
				minAngle = angle;
			}
			ci = cii;
		}
		System.out.println("(" + minAngle+ ")");
		System.out.println("(" + minArea+ ")");
		return rotatePoly(res, c, minAngle, geomf);
	}
	/**
	 * @param schema
	 * @return {@link SimpleFeature}
	 * 
	 */
	
	public  SimpleFeature createSimpleFeature(SimpleFeatureType schema) {
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
		
		String name = "oriented";
		int number = 3;

		GeometryFactory gf = new GeometryFactory();
	
		Polygon p = (Polygon) this.orientedBox(this.nuagepts.coordinates, gf);
		featureBuilder.add(p);
		featureBuilder.add(name);
		featureBuilder.add(number);
		SimpleFeature feature = featureBuilder.buildFeature(null);
		return feature;
	}
	/**
	 * exporter l'oriented box en shapefile
	 */
	public void exportShapefile()
	{
		File file;
		
		file = new File("orientedboundingbox.shp");
		
	
	List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
	SimpleFeatureType schema = null;
	try {
		schema = DataUtilities.createType("", "orientedbox",
				"Box:Polygon," + //  the geometry attribute:
						// Polygon type
						"name:String," + //  a String attribute
						"number:Integer" // a number attribute
				);
	} catch (SchemaException e) {
		e.printStackTrace();
		return;
	}

	SimpleFeature f = this.createSimpleFeature(schema);
	
	feats.add(f);

	GenerateShapeFile writer = new GenerateShapeFile(file);

	FeatureCollection<SimpleFeatureType, SimpleFeature> features = new ListFeatureCollection(schema, feats);
	writer.writeFeatures(features);
	}
	
	

}
