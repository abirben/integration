package eu.ensg.abn.project;
/**
 * Créer une enveloppe convexe en utilisant la marche de jarvis
 */
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

//import com.vividsolutions.jts.awt.PointShapeFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


public class EnveloppeConvexeJarvis implements IEnveloppe, Ifeature{
	int longeur;
	Coordinate[] coordinates = new Coordinate[longeur];
	NuagePoint nuagepts = new NuagePoint(coordinates,coordinates.length);
	//constructeur ave cle nuage de points
	public EnveloppeConvexeJarvis(NuagePoint nuagepts)
	{
		this.nuagepts= nuagepts;
	}
	//constructeur vide
	public EnveloppeConvexeJarvis()
	{
		
	}
	
	/**
	 * Vérifier si les points sont colineaires ou non
	 * @param p
	 * @param q
	 * @param r
	 * @return val
	 */
	public double sens(Coordinate p, Coordinate q, Coordinate r)
    {
        double val = (q.y - p.y) * (r.x - q.x) -
                  (q.x - p.x) * (r.y - q.y);
      
        if (val == 0) return 0; 
        return (val > 0)? 1: 2; 
    }
	/**
	 * On commence par chercher le point p qui a le plus petit x, en partant de ce point
	 * on cherche le point q tel que tous les autres points soient à gauche
	 * jusqu'à atteindre le point de départ p.
	 * @param points
	 * @param n
	 * @return {@link Coordinate}
	 */
	
    public Coordinate[] convexHull(Coordinate points[],int n)
    {
    	
       Stack<Coordinate> pts = new Stack<Coordinate>();
      
        
        int l = 0;
        for (int i = 0; i < n; i++)
         if (points[i].x < points[l].x)
          l = i;
     
        int p = l, q;
        do
        {
            pts.add(points[p]);
            q = (p + 1) % n;
             
            for (int i = 0; i < n; i++)
            {
           
             if (sens(points[p], points[i], points[q])  == 2)
                   q = i;
            }
            p = q;
      
        } while (p != l); 
      
       // Ajouter le premier élement à la fin de la liste
        pts.add(pts.firstElement());
    
       for (Coordinate i : pts)
        	
        {  System.out.println("(" + i.x + ", " +i.y + ")");
          
        }
       
        return toCoordinateArray(pts);
    }
       public Coordinate[] toCoordinateArray(Stack stack) {
            Coordinate[] coordinates = new Coordinate[stack.size()];
            for (int i = 0; i < stack.size(); i++) {
              Coordinate coordinate = (Coordinate) stack.get(i);
              coordinates[i] = coordinate;
            }
            return coordinates;
          
       
        }
       /**
	     *  Créer le polygone de l'enveloppe  
	     * @param schema
	     * @return {@link SimpleFeature}
	     */
        public  SimpleFeature createSimpleFeature(SimpleFeatureType schema) {
    		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
    		
    		String name = "jarvis";
    		int number = 5;

    		GeometryFactory geometryFactory = new GeometryFactory();
  
    		Polygon jarv = (Polygon) geometryFactory.createPolygon(this.convexHull(this.nuagepts.coordinates,this.nuagepts.n));
    		featureBuilder.add(jarv);
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
			
			file = new File("jarvis.shp");
			
		
		List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
		SimpleFeatureType schema = null;
		try {
			schema = DataUtilities.createType("", "jarvis",
					"jarvis:MultiPolygon," + //  the geometry attribute:
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

		FeatureCollection<SimpleFeatureType, SimpleFeature> features = new ListFeatureCollection(
				schema, feats);
		writer.writeFeatures(features);
	}
    
    
}
