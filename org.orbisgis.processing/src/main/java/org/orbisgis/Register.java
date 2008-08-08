/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.orbisgis;

import org.gdms.sql.customQuery.QueryManager;
import org.gdms.sql.customQuery.spatial.geometry.CheckSpatialEquivalence;
import org.gdms.sql.customQuery.spatial.geometry.convert.PointsToXYZ;
import org.gdms.sql.customQuery.spatial.geometry.jgrapht.ShortestPath;
import org.gdms.sql.customQuery.spatial.geometry.others.RandomGeometry;
import org.gdms.sql.customQuery.spatial.geometry.tin.BuildTIN;
import org.gdms.sql.customQuery.spatial.geometry.tin.Cdt;
import org.gdms.sql.customQuery.spatial.geometry.tin.CheckDelaunayProperty;
import org.gdms.sql.customQuery.spatial.geometry.topology.ToLineNoder;
import org.gdms.sql.customQuery.spatial.raster.convert.RasterToPoints;
import org.gdms.sql.customQuery.spatial.raster.convert.RasterToPolygons;
import org.gdms.sql.customQuery.spatial.raster.convert.RasterToXYZ;
import org.gdms.sql.customQuery.spatial.raster.convert.RasterizeLine;
import org.gdms.sql.customQuery.spatial.raster.convert.VectorizeLine;
import org.gdms.sql.function.FunctionManager;
import org.gdms.sql.function.alphanumeric.SubString;
import org.gdms.sql.function.spatial.geometry.extract.ConvexHull;
import org.gdms.sql.function.spatial.geometry.extract.ToMultiSegments;
import org.gdms.sql.function.spatial.geometry.generalize.Generalize;
import org.gdms.sql.function.spatial.raster.hydrology.D8Accumulation;
import org.gdms.sql.function.spatial.raster.hydrology.D8AllOutlets;
import org.gdms.sql.function.spatial.raster.hydrology.D8ConstrainedAccumulation;
import org.gdms.sql.function.spatial.raster.hydrology.D8Direction;
import org.gdms.sql.function.spatial.raster.hydrology.D8DistanceToTheOutlet;
import org.gdms.sql.function.spatial.raster.hydrology.D8RiverDistance;
import org.gdms.sql.function.spatial.raster.hydrology.D8Slope;
import org.gdms.sql.function.spatial.raster.hydrology.D8StrahlerStreamOrder;
import org.gdms.sql.function.spatial.raster.hydrology.D8Watershed;
import org.gdms.sql.function.spatial.raster.hydrology.FillSinks;
import org.gdms.sql.function.spatial.raster.utilities.CropRaster;
import org.gdms.sql.function.spatial.raster.utilities.ToEnvelope;
import org.gdms.triangulation.michaelm.TinMM;
import org.orbisgis.pluginManager.PluginActivator;
import org.orbisgis.processing.tin.Generate2DMesh;

public class Register implements PluginActivator {
	public void start() throws Exception {

		// Raster processing

		QueryManager.registerQuery(new RasterToPoints());
		QueryManager.registerQuery(new RasterToPolygons());
		QueryManager.registerQuery(new RasterToXYZ());

		QueryManager.registerQuery(new RasterizeLine());

		FunctionManager.addFunction(D8Slope.class);
		FunctionManager.addFunction(D8Direction.class);
		FunctionManager.addFunction(D8Accumulation.class);
		FunctionManager.addFunction(D8AllOutlets.class);
		FunctionManager.addFunction(D8Watershed.class);
		FunctionManager.addFunction(D8StrahlerStreamOrder.class);
		FunctionManager.addFunction(D8ConstrainedAccumulation.class);
		FunctionManager.addFunction(CropRaster.class);
		FunctionManager.addFunction(ToEnvelope.class);
		FunctionManager.addFunction(FillSinks.class);
		FunctionManager.addFunction(D8DistanceToTheOutlet.class);
		FunctionManager.addFunction(D8RiverDistance.class);

		QueryManager.registerQuery(new VectorizeLine());

		// Vector processing

		QueryManager.registerQuery(new Generate2DMesh());
		QueryManager.registerQuery(new PointsToXYZ());
		QueryManager.registerQuery(new ToLineNoder());
		QueryManager.registerQuery(new ShortestPath());
		QueryManager.registerQuery(new BuildTIN());
		QueryManager.registerQuery(new RandomGeometry());

		QueryManager.registerQuery(new Cdt());
		QueryManager.registerQuery(new CheckDelaunayProperty());

		FunctionManager.addFunction(ToMultiSegments.class);
		FunctionManager.addFunction(Generalize.class);

		FunctionManager.addFunction(ConvexHull.class);

		FunctionManager.addFunction(SubString.class);
		QueryManager.registerQuery(new TinMM());

		QueryManager.registerQuery(new CheckSpatialEquivalence());
	}

	public void stop() throws Exception {
	}

	public boolean allowStop() {
		return true;
	}
}