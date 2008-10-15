package org.contrib.algorithm.triangulation.tin2;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.vividsolutions.jts.geom.Coordinate;

public class SetOfVertices {
	private List<Vertex> vertices;
	private VertexPtr[] verticesPtr;

	public SetOfVertices() {
		vertices = new ArrayList<Vertex>();
	}

	public void addAll(final Coordinate[] coordinates) {
		for (Coordinate coordinate : coordinates) {
			this.vertices.add(new Vertex(coordinate));
		}
	}

	public void addAll(final Coordinate[] coordinates, final int gid) {
		for (Coordinate coordinate : coordinates) {
			this.vertices.add(new Vertex(coordinate, gid));
		}
	}

	private VertexPtr[] getVerticesPtr() {
		if (null == verticesPtr) {
			// final SortedSet<VertexPtr> verticesSet = new TreeSet<VertexPtr>(
			// new VertexPtrSimpleComparator());
			final SortedSet<VertexPtr> verticesSet = new TreeSet<VertexPtr>(
					new VertexPtrRadiusComparator(getBarycentre()));
			for (int i = 0; i < vertices.size(); i++) {
				verticesSet.add(new VertexPtr(vertices, i));
			}
			verticesPtr = verticesSet.toArray(new VertexPtr[0]);
		}
		return verticesPtr;
	}
	
	private VertexPtr getVertexPtr(final int index) {
		return getVerticesPtr()[index];
	}

	public Coordinate getCoordinate(final int index) {
		return getVertexPtr(index).getCoordinate();
	}

	public int size() {
		return getVerticesPtr().length;
	}

	private Coordinate getBarycentre() {
		final Coordinate barycentre = new Coordinate();
		for (Vertex coordinate : vertices) {
			barycentre.x += coordinate.getCoordinate().x;
			barycentre.y += coordinate.getCoordinate().y;
		}
		barycentre.x /= vertices.size();
		barycentre.y /= vertices.size();
		return barycentre;
	}
}