/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.core.renderer.se.transform;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.gdms.data.SpatialDataSourceDecorator;
import org.orbisgis.core.map.MapTransform;
import net.opengis.se._2_0.core.MatrixType;
import net.opengis.se._2_0.core.RotateType;
import net.opengis.se._2_0.core.ScaleType;
import net.opengis.se._2_0.core.TransformType;
import net.opengis.se._2_0.core.TranslateType;
import org.orbisgis.core.renderer.se.SeExceptions.InvalidStyle;
import org.orbisgis.core.renderer.se.SymbolizerNode;
import org.orbisgis.core.renderer.se.UomNode;
import org.orbisgis.core.renderer.se.common.Uom;
import org.orbisgis.core.renderer.se.parameter.ParameterException;

/**
 *
 * This class contains a collection of affine transformation
 *
 * @author maxence
 */
public class Transform implements SymbolizerNode, UomNode {
	private Uom uom;

    private SymbolizerNode parent;
    private AffineTransform consolidated;
    private ArrayList<Transformation> transformations;


	@Override
	public String toString(){
		String r = "";
		for (Transformation t : transformations){
			r += t.toString();
		}
		return r;
	}

    public Transform() {
        transformations = new ArrayList<Transformation>();
        consolidated = null;
    }

    public Transform(TransformType t) throws InvalidStyle {
        transformations = new ArrayList<Transformation>();
        consolidated = null;

		if (t.getUom() != null) {
			this.setUom(Uom.fromOgcURN(t.getUom()));
		}

        for (Object o : t.getTranslateOrRotateOrScale()){
            if (o instanceof TranslateType){
                this.transformations.add(new Translate((TranslateType)o));
            }
            else if (o instanceof RotateType){
                this.transformations.add(new Rotate((RotateType)o));
            }
            else if (o instanceof ScaleType){
                this.transformations.add(new Scale((ScaleType)o));
            }
            else if (o instanceof MatrixType){
                this.transformations.add(new Matrix((MatrixType)o));
            }
        }
    }

    public boolean moveDown(int i) {
        if (i >= 0 && i < transformations.size()-1){
            Transformation remove = transformations.remove(i);
            transformations.add(i+1, remove);
            return true;
        } else {
            return false;
        }
    }

    public boolean moveUp(int i) {
        if (i > 0 && i < transformations.size()){
            Transformation remove = transformations.remove(i);
            transformations.add(i-1, remove);
            return true;
        } else {
            return false;
        }
    }



    /**
     * Return an affine transformation for java Shape object.
     * The purpose is to transfom se.graphics
     *
     * @param ds 
     * @param fid 
     * @param isForSpatialFeatures
     * @return AffineTransofrm
     * @throws ParameterException
     */
    public AffineTransform getGraphicalAffineTransform(boolean isForSpatialFeatures, SpatialDataSourceDecorator sds, long fid, MapTransform mt, Double width, Double height) throws ParameterException, IOException {
        //return consolidateTrasformations(false).getGraphicalAffineTransform();
        this.consolidateTransformations(sds, fid, isForSpatialFeatures, mt, width, height);
        return consolidated;
    }


    /*
     * This method must be called after each modification of one of its transformations !
     *
     */
    public void consolidateTransformations(SpatialDataSourceDecorator sds, long fid, boolean forGeometries,
            MapTransform mt, Double width, Double height) throws ParameterException, IOException {
        int i;

        // Result is Identity
        consolidated = new AffineTransform();
        for (Transformation t : transformations) {
            if (!forGeometries || t.allowedForGeometries()) {
                AffineTransform at = t.getAffineTransform(sds, fid, this.getUom(), mt, width, height);
                consolidated.preConcatenate(at);
            }
        }
    }

    public void addTransformation(Transformation t) {
        transformations.add(t);
    }

    public boolean removeTransformation(int i) {
        try{
            transformations.remove(i);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public int getNumTransformation() {
        return transformations.size();
    }

    public Transformation getTransformation(int i) {
        return transformations.get(i);
    }

    @Override
    public SymbolizerNode getParent() {
        return parent;
    }

    @Override
    public void setParent(SymbolizerNode node) {
        parent = node;
    }


    public TransformType getJAXBType(){
        TransformType t = new TransformType();

		if (getOwnUom() != null) {
			t.setUom(getOwnUom().toURN());
		}

        List<Object> list = t.getTranslateOrRotateOrScale();

        for (Transformation tr : transformations){
            list.add(tr.getJAXBType());
        }

        return t;
    }

    public String dependsOnFeature() {
        String result = "";
        for (Transformation t : this.transformations){
            result += t.dependsOnFeature();
        }
        return result.trim();
    }

    @Override
    public Uom getUom() {
        if (uom != null) {
            return this.uom;
        } else {
            return parent.getUom();
        }
    }

	@Override
	public Uom getOwnUom(){
		return uom;
	}

	@Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }
}