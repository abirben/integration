package eu.ensg.abn.project;
/**
 * Créer un minimal bounding box à partir d'un nuage de points
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
import com.vividsolutions.jts.geom.Polygon;

public class BoundingBox implements Ifeature {
	
	int longeur;
	Coordinate[] coordinates = new Coordinate[longeur];
	NuagePoint nuagepts = new NuagePoint(coordinates,coordinates.length);
	//constructeur
	public BoundingBox(NuagePoint nuagepts)
	{
		this.nuagepts= nuagepts;
	}
	/**
	 * On cherche  la valeur maximale d’abcisse x Xmax la valeur minimale Xmin , 
	 * de même la valeur maximale de de l’ordonnée y Ymax et la valeur minimale Ymin
     *  Les coordonnées de   quatre sommets  de rectangle sont  
     * (Xmin,Ymax) ,(Xmax,Ymin) ,(Xmax,Ymax) ,(Xmin,Ymax).
	 * @param points
	 * @return {@link Coordinate}
	 */
	public Coordinate[] boundingBoxCoor(Coordinate points[]){
		
		 double max_x = points[0].x;
		 double max_y = points[0].y;
		 double min_x = points[0].x;
		 double min_y = points[0].y;
		for(int i =1;i < points.length; i++)
		
		{
			if (points[i].x  > max_x)
			 max_x = points[i].x;
			if (points[i].y  >max_y)
				max_y = points[i].y;
		   if (points[i].x  <min_x)
				 min_x = points[i].x;
			if (points[i].y  < min_y)
			 min_y = points[i].y;
			
		}
		
		Coordinate[] coordinates = new Coordinate[] {new Coordinate(min_x, min_y),new Coordinate(max_x,min_y),new Coordinate(max_x,max_y),new Coordinate(min_x,max_y),new Coordinate(min_x, min_y)};
		 for (Coordinate temp : coordinates)
	        	
	        {  System.out.println("(" + temp.x + ", " +
	                               temp.y + ")");
	       
	         
	          
	        }
		
		
		return coordinates;
    
		
	}
	 /**
     *  Creer le polygone de l'enveloppe  
     * @param schema
     * @return {@link SimpleFeature}
     */
	public  SimpleFeature createSimpleFeature(SimpleFeatureType schema) {
   		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
   		
   		String name = "bounding";
   		int number = 5;

   		GeometryFactory geometryFactory = new GeometryFactory();
   		Polygon box = (Polygon) geometryFactory.createPolygon(this.boundingBoxCoor(this.nuagepts.coordinates));
   		featureBuilder.add(box);
   		featureBuilder.add(name);
   		featureBuilder.add(number);
   		SimpleFeature feature = featureBuilder.buildFeature(null);
   		return feature;
   	}
	/**
     * exporter le résultat généré en shapefile
     */
	public void exportShapefile()
	{
		File file;
		
		file = new File("boundingbox.shp");
		
	
	List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
	SimpleFeatureType schema = null;
	try {
		schema = DataUtilities.createType("", "box",
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