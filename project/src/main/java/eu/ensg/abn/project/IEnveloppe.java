package eu.ensg.abn.project;
/**
 * interface de méthodes pour les enveloppe convexe
 */

import java.util.Stack;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;

public interface IEnveloppe {
	public Coordinate[] convexHull(Coordinate[] points,int n);
	public Coordinate[] toCoordinateArray(Stack stack);
	

}
