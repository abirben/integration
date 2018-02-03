package eu.ensg.abn.project;
/**
 * interface pour les méthodes des fichiers :ecriture des features et export de fichier
 */

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public interface Ifeature {
	SimpleFeature createSimpleFeature(SimpleFeatureType schema);
	public  void exportShapefile();

}
