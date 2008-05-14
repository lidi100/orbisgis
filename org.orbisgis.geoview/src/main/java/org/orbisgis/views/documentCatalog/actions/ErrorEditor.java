package org.orbisgis.views.documentCatalog.actions;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.orbisgis.editor.IEditor;
import org.orbisgis.views.documentCatalog.DocumentException;
import org.orbisgis.views.documentCatalog.IDocument;

public class ErrorEditor implements IEditor {

	private String message;
	private String documentName;

	public ErrorEditor(String documentName, String message) {
		this.message = message;
		this.documentName = documentName;
	}

	public boolean acceptDocument(IDocument doc) {
		return false;
	}

	public IDocument getDocument() {
		return new DummyDocument();
	}

	public String getTitle() {
		return documentName;
	}

	public void setDocument(IDocument doc) {
	}

	public void delete() {
	}

	public Component getComponent() {
		return new JLabel(message);
	}

	public void initialize() {

	}

	public void loadStatus() {
	}

	public void saveStatus() {
	}

	private class DummyDocument implements IDocument {

		public void addDocument(IDocument document) {

		}

		public boolean allowsChildren() {
			return false;
		}

		public void closeDocument() throws DocumentException {

		}

		public IDocument getDocument(int index) {
			return null;
		}

		public int getDocumentCount() {
			return 0;
		}

		public Icon getIcon() {
			return null;
		}

		public String getName() {
			return message;
		}

		public HashMap<String, String> getPersistenceProperties()
				throws DocumentException {
			return null;
		}

		public void openDocument() throws DocumentException {
		}

		public void saveDocument() throws DocumentException {
		}

		public void setName(String name) {
		}

		public void setPersistenceProperties(HashMap<String, String> properties)
				throws DocumentException {
		}

	}

}
