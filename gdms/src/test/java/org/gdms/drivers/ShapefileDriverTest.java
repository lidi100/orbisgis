package org.gdms.drivers;

import java.io.File;

import junit.framework.TestCase;

import org.gdms.SourceTest;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceFactory;
import org.gdms.driver.DriverException;
import org.gdms.spatial.SpatialDataSource;
import org.gdms.spatial.SpatialDataSourceDecorator;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.hardcode.driverManager.DriverLoadException;

public class ShapefileDriverTest extends TestCase {
	private DataSourceFactory dsf = new DataSourceFactory();

	private boolean crsConformity(final String fileName,
			final CoordinateReferenceSystem refCrs) throws DriverLoadException,
			DataSourceCreationException, DriverException {
		DataSource ds = dsf.getDataSource(new File(fileName));
		SpatialDataSource sds = new SpatialDataSourceDecorator(ds);
		sds.beginTrans();
		return sds.getCRS(null).toWKT().equals(refCrs.toWKT());
		// return CRS.equalsIgnoreMetadata(refCrs, sds.getCRS(null))
		// && sds.getCRS(null).toWKT().equals(refCrs.toWKT());
	}

	public void testPrj() throws NoSuchAuthorityCodeException,
			FactoryException, DriverLoadException, DataSourceCreationException,
			DriverException {
		final String withoutExistingPrj = SourceTest.externalData
				+ "shp/mediumshape2D/landcover2000.shp";
		assertTrue(crsConformity(withoutExistingPrj, DefaultGeographicCRS.WGS84));
		assertTrue(crsConformity(withoutExistingPrj, CRS.decode("EPSG:4326")));

		final String withExistingPrj = SourceTest.externalData
				+ "shp/mediumshape2D/bzh5_communes.shp";
		assertTrue(crsConformity(withExistingPrj, CRS.decode("EPSG:27572")));
		// assertTrue(crsConformity(withExistingPrj, CRS.decode("EPSG:27582")));
	}
}