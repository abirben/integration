package eu.ensg.abn.project;
/**
 * Créer une enveloppe convexe en utilisant l'algorithme de Graham Scan
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

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


public class EnveloppeConvexeGraham implements IEnveloppe , Ifeature {
	int longeur;
	Coordinate[] coordinates = new Coordinate[longeur];
	NuagePoint nuagepts = new NuagePoint(coordinates,coordinates.length);
	
	 public static enum Sens { DROITE, GAUCHE, COLINEAIRE }

	  
	 public EnveloppeConvexeGraham(NuagePoint nuagepts) 
	 {
		 this.nuagepts= nuagepts;
	 }  
	
	/**
	 * les coordonnées de sommets de l'enveloppe convexe
	 * @param points
	 * @param n
	 * @return {@link Coordinate}
	 */
	    public Coordinate[] convexHull(Coordinate points[],int  n) throws IllegalArgumentException {

	        List<Coordinate> pts = new ArrayList<Coordinate>(triPoints(points));

	        if(pts.size() < 3) {
	            throw new IllegalArgumentException(" on ne peut pas créer une enveloppe convexe de deux points ");
	        }


	        Stack<Coordinate> envGraham = new Stack<Coordinate>();
	        envGraham.push(pts.get(0));
	        envGraham.push(pts.get(1));

	        for (int i = 2; i < pts.size(); i++) {

	            Coordinate p = pts.get(i);
	            Coordinate m = envGraham.pop();
	            Coordinate d =envGraham.peek();

	            Sens sens = orientation(d, m, p);

	            switch(sens) {
	                case GAUCHE:
	                	envGraham.push(m);
	                	envGraham.push(p);
	                    break;
	                case DROITE:
	                    i--;
	                    break;
	                case COLINEAIRE:
	                	envGraham.push(p);
	                    break;
	            }
	        }

	        // Fermer la liste en ajoutnat le premier élement pour q'on puisse construire le polygone
	        envGraham.push(pts.get(0));
	        for (Coordinate temp : envGraham)
	        	
	        {  System.out.println("(" + temp.x + ", " +
	                               temp.y + ")");
	       
	         
	          
	        }

	        return toCoordinateArray(envGraham);
	    }
	    /**
	     *  
	     * Convertir une liste de type Stack à une liste de type coordinate[]
	     *
	     *@param stack
	     *@return {@link Coordinate}
	     *               
	     *               .
	     */
	    public Coordinate[] toCoordinateArray(Stack stack) {
            Coordinate[] coordinates = new Coordinate[stack.size()];
            for (int i = 0; i < stack.size(); i++) {
              Coordinate coordinate = (Coordinate) stack.get(i);
              coordinates[i] = coordinate;
            }
            return coordinates;
          
       
        }

	    /**
	     * Retourne le point avec le plus petit y, s'ils existent deux points avec le meme y  , on choisit le point avec le petit x 
	     * 
	     *
	     * @param points
	     * @return   {@link Coordinate}
	     *               
	     *               .
	     */
	    public  Coordinate getpetitOrd(Coordinate[] points) {

	        Coordinate ord = points[0];

	        for(int i = 1; i < points.length;i++) {

	            Coordinate temp = points[i];

	            if(temp.y < ord.y || (temp.y == ord.y && temp.x < ord.x)) {
	                ord = temp;
	            }
	        }

	        return ord;
	    }

	    /**
	     * 
	     * Tirer les points  en ordre croissant d'angle par rapport
	     * à l’axe des abscisses. S'il y a plusieurs points avec le même angle,
	     * on choisit  celui qui est le plus près du point qui a le plus petit y.
	     * 
	     * @param  points
	     * @return {@link Coordinate}
	     * 
	     */
	    public  Set<Coordinate> triPoints(Coordinate[] points) {

	      final Coordinate ord = getpetitOrd(points);

	        TreeSet<Coordinate> set = new TreeSet<Coordinate>(new Comparator<Coordinate>() {
	            public int compare(Coordinate a, Coordinate b) {

	                if(a == b || a.equals(b)) {
	                    return 0;
	                }

	               
	                double angleA = Math.atan2((long)a.y - ord.y, (long)a.x - ord.x);
	                double angleB = Math.atan2((long)b.y - ord.y, (long)b.x - ord.x);

	                if(angleA < angleB) {
	                    return -1;
	                }
	                else if(angleA > angleB) {
	                    return 1;
	                }
	                else {
	  
	                    double distanceA = Math.sqrt((((long)ord.x - a.x) * ((long)ord.x - a.x)) +
	                                                (((long)ord.y - a.y) * ((long)ord.y - a.y)));
	                    double distanceB = Math.sqrt((((long)ord.x - b.x) * ((long)ord.x - b.x)) +
	                                                (((long)ord.y - b.y) * ((long)ord.y - b.y)));

	                    if(distanceA < distanceB) {
	                        return -1;
	                    }
	                    else {
	                        return 1;
	                    }
	                }
	            }

				
				
	        });
            for(int i =0;i < points.length;i++)
	        {set.add(points[i]);
	        }
	        return set;
	    }

	    /**
	     * Vérifier le sens de l'orientation d'une séquence de trois points,
	     * s'ils sont colineaires , ou s'ils tournent à gauche ou à droite.
	     *
	     * @param a
	     * @param b
	     * @param c
	     * @return {@link Sens}
	     */
	    public static Sens orientation(Coordinate a, Coordinate b, Coordinate c) {

	       
	        long val = (long) ((((long)b.x - a.x) * ((long)c.y - a.y)) -
	                            (((long)b.y - a.y) * ((long)c.x - a.x)));

	        if(val > 0) {
	            return Sens.GAUCHE;
	        }
	        else if(val < 0) {
	            return Sens.DROITE;
	        }
	        else {
	            return Sens.COLINEAIRE;
	        }
	    }
	    /**
	     *  Créer le polygone de l'enveloppe  
	     * @param schema
	     * @return {@link SimpleFeature}
	     */
        	    
	    public  SimpleFeature createSimpleFeature(SimpleFeatureType schema) {
    		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
    		int n=0;
    		String name = "graham";
    		int number = 4;
    		GeometryFactory geometryFactory = new GeometryFactory();
    		
    		Polygon box = (Polygon) geometryFactory.createPolygon(this.convexHull(this.nuagepts.coordinates,n));
    		
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
			
			file = new File("graham.shp");
			
		
		List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
		SimpleFeatureType schema = null;
		try {
			schema = DataUtilities.createType("", "Graham",
					"Graham:MultiPolygon," + //  the geometry attribute:
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
