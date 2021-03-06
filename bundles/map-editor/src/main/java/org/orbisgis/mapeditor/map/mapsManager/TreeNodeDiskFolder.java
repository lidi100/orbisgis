package org.orbisgis.mapeditor.map.mapsManager;

import java.io.File;
import javax.swing.ImageIcon;
import org.orbisgis.mapeditor.map.icons.MapEditorIcons;
import org.orbisgis.sif.components.fstree.TreeNodeCustomIcon;
import org.orbisgis.sif.components.fstree.TreeNodeFileFactoryManager;
import org.orbisgis.sif.components.fstree.TreeNodeFolder;

/**
 * TreeNodeFolder is overloaded in order to define custom folder icons.
 * @author ebocher
 */
public class TreeNodeDiskFolder extends TreeNodeFolder implements TreeNodeCustomIcon {

    public TreeNodeDiskFolder(File folderPath, TreeNodeFileFactoryManager factoryManager) {
        super(folderPath, factoryManager);
    }

    @Override
    protected TreeNodeFolder createInstance(File folderPath, TreeNodeFileFactoryManager factoryManager) {
        return new TreeNodeDiskFolder(folderPath, factoryManager);
    }    
    
    @Override
    public ImageIcon getLeafIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageIcon getClosedIcon() {
        return MapEditorIcons.getIcon("folder");
    }

    @Override
    public ImageIcon getOpenIcon() {
         return MapEditorIcons.getIcon("folder_open");
    }  
}
