package eu.ensg.abn.project;
/**
 * Créer le nuage de point à partir de la classe Coordinate de la librairie Java tools Suite
 * On utilise la classe coordinate pour pouvoir utiliser les fonctions utilitaires de Geotools
 * pour la création de géometrie
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
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class NuagePoint {
	public int n;
	// Définir les coordonnées
	public Coordinate[] coordinates = new Coordinate[n];
	//constructeur avec la liste des coordonnées
	public NuagePoint(Coordinate[] coordinates, int n)
	{
		this.coordinates=coordinates;
		this.n=n;
		
	}
	public Coordinate[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
	}
	 /**
     *  Creer le polygone de l'enveloppe  
     * @param schema
     * @return {@link SimpleFeature}
     */
	public  List<SimpleFeature> createSimpleFeature(SimpleFeatureType schema) {
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
		List<SimpleFeature> features = new ArrayList<>();
		String name = "nuage";
		int number = 1;

		GeometryFactory geometryFactory = new GeometryFactory();
		
		for (int i = 0; i < n; i++)
		{
			Point p = (Point) geometryFactory.createPoint(this.coordinates[i]);
		
		
		featureBuilder.add(p);
		featureBuilder.add(name);
		featureBuilder.add(number);
		SimpleFeature feature = featureBuilder.buildFeature(null);
		features.add(feature);
		
	}

		return features;
	}
	/**
     * exporter le résultat généré en shapefile
     */
	public  void exportShapefile()
	{
		File file;
		
		file = new File("nuagepoints.shp");
		
		
	
	List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
	SimpleFeatureType schema = null;
	try {
		schema = DataUtilities.createType("", "Points",
				"points:MultiPoint," + //  the geometry attribute
						"name:String," + // a String attribute
						"number:Integer" // a number attribute
				);
	} catch (SchemaException e) {
		
		e.printStackTrace();
		return;
	}

	
	feats = this.createSimpleFeature(schema);
	
	GenerateShapeFile writer = new GenerateShapeFile(file);

	FeatureCollection<SimpleFeatureType, SimpleFeature> features = new ListFeatureCollection(
			schema, feats);
	writer.writeFeatures(features);
	}
}
