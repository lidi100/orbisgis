/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE 
 * team located in University of South Brittany, Vannes.
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.sif.components;

import java.io.File;
import java.net.URL;
import javax.swing.filechooser.FileFilter;
import org.orbisgis.sif.UIFactory;

/**
 * @class OpenFilePanel this class is used to import files in the geocatalog. To
 * manage folder, use directly OpenFolderPanel.
 */
public class OpenFilePanel extends AbstractOpenPanel {

        public static final String FIELD_NAME = "file";
        public static final String FILTER_NAME = "filter";

        public OpenFilePanel(String id, String title) {
                super(id, title);
        }

        /**
         * Check that the input given by the user can be used.
         *
         * @return
         */
        @Override
        public String validateInput() {
                File file = getSelectedFile();
                if (file == null) {
                        return UIFactory.getI18n().tr("A file must be selected");
                } else if (!file.exists()) {
                        return UIFactory.getI18n().tr("The file must exists");
                } else {
                        return null;
                }
        }

        /**
         * Return the names of the fields in the FileChooser.
         *
         * @return
         */
        public String[] getFieldNames() {
                return new String[]{FIELD_NAME, FILTER_NAME};
        }

        /**
         * we don't want to show only folders, as we are about to import files.
         *
         * @return
         */
        @Override
        public boolean showFoldersOnly() {
                return false;
        }       

        public void setValue(String fieldName, String fieldValue) {
                if (fieldName.equals(FIELD_NAME)) {
                        String[] files = fieldValue.split("\\Q||\\E");
                        File[] selectedFiles = new File[files.length];
                        for (int i = 0; i < selectedFiles.length; i++) {
                                selectedFiles[i] = new File(files[i]);
                        }
                        getFileChooser().setSelectedFiles(selectedFiles);
                } else {
                        FileFilter[] filters = getFileChooser().getChoosableFileFilters();
                        for (FileFilter fileFilter : filters) {
                                if (fieldValue.equals(fileFilter.getDescription())) {
                                        getFileChooser().setFileFilter(fileFilter);
                                }
                        }
                }
        }

        @Override
        public URL getIconURL() {
                return UIFactory.getDefaultIcon();
        }
}
