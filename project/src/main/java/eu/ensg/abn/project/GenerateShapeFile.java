package eu.ensg.abn.project;
/**
 * Utiliser les classes geotools et opengis pour écrire les geometries crées dans un shapefile
 */

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.identity.FeatureId;

public class GenerateShapeFile {
	File outfile;
	public ShapefileDataStore shpDataStore;
     /**
      * Generer le fichier
      * @param f
      */
	public GenerateShapeFile(File f) {
		outfile = f;

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		try {
			params.put("url", outfile.toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("create spatial index", Boolean.TRUE);

		try {
			shpDataStore = (ShapefileDataStore) dataStoreFactory
					.createNewDataStore(params);
		} catch (IOException e) {
			
			e.printStackTrace();
		
		}
		
		}
	/**
	 * Ecrire les features dans un shapefile
	 * @param features
	 * @return {@link Boolean}
	 */
	public boolean writeFeatures(
			FeatureCollection<SimpleFeatureType, SimpleFeature> features) {

		if (shpDataStore == null) {
			throw new IllegalStateException("la datastore ne peut pas etre nulle");
		}
		SimpleFeatureType schema = features.getSchema();
		GeometryDescriptor geom = schema.getGeometryDescriptor();

		try {

			
			Transaction transaction = new DefaultTransaction("create");

			String typeName = shpDataStore.getTypeNames()[0];
			SimpleFeatureSource featureSource = shpDataStore
					.getFeatureSource(typeName);

			

			List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
			GeometryType geomType = null;
			List<AttributeDescriptor> attribs = new ArrayList<AttributeDescriptor>();
			for (AttributeDescriptor attrib : attributes) {
				AttributeType type = attrib.getType();
				if (type instanceof GeometryType) {
					geomType = (GeometryType) type;

				} else {
					attribs.add(attrib);
				}
			}

			GeometryTypeImpl gt = new GeometryTypeImpl(new NameImpl("the_geom"),
					geomType.getBinding(), geomType.getCoordinateReferenceSystem(),
					geomType.isIdentified(), geomType.isAbstract(),
					geomType.getRestrictions(), geomType.getSuper(),
					geomType.getDescription());

			GeometryDescriptor geomDesc = new GeometryDescriptorImpl(gt,
					new NameImpl("the_geom"), geom.getMinOccurs(), geom.getMaxOccurs(),
					geom.isNillable(), geom.getDefaultValue());

			attribs.add(0, geomDesc);

			SimpleFeatureType shpType = new SimpleFeatureTypeImpl(schema.getName(),
					attribs, geomDesc, schema.isAbstract(), schema.getRestrictions(),
					schema.getSuper(), schema.getDescription());

			shpDataStore.createSchema(shpType);

			if (featureSource instanceof SimpleFeatureStore) {
				SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

				List<SimpleFeature> feats = new ArrayList<SimpleFeature>();

				FeatureIterator<SimpleFeature> features2 = features.features();
				while (features2.hasNext()) {
					SimpleFeature f = features2.next();
					SimpleFeature reType = SimpleFeatureBuilder.build(shpType,
							f.getAttributes(), "");

					feats.add(reType);
				}
				features2.close();
				SimpleFeatureCollection collection = new ListFeatureCollection(shpType,
						feats);

				featureStore.setTransaction(transaction);
				try {
					List<FeatureId> ids = featureStore.addFeatures(collection);
					transaction.commit();
				} catch (Exception problem) {
					problem.printStackTrace();
					transaction.rollback();
				} finally {
					transaction.close();
				}
				shpDataStore.dispose();
				return true;
			} else {
				shpDataStore.dispose();
				System.err.println("Shapefile n'est pas écrit");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}}
