/**
 * OrbisToolBox is an OrbisGIS plugin dedicated to create and manage processing.
 * <p/>
 * OrbisToolBox is distributed under GPL 3 license. It is produced by CNRS <http://www.cnrs.fr/> as part of the
 * MApUCE project, funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
 * <p/>
 * OrbisToolBox is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p/>
 * OrbisToolBox is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with OrbisToolBox. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * For more information, please consult: <http://www.orbisgis.org/> or contact directly: info_at_orbisgis.org
 */

package org.orbisgis.wpsclient.view.ui;

import org.orbisgis.sif.components.actions.ActionCommands;
import org.orbisgis.sif.components.actions.DefaultAction;
import org.orbisgis.sif.components.filter.DefaultActiveFilter;
import org.orbisgis.sif.components.filter.FilterFactoryManager;
import org.orbisgis.sif.components.fstree.CustomTreeCellRenderer;
import org.orbisgis.sif.components.fstree.FileTree;
import org.orbisgis.sif.components.fstree.FileTreeModel;
import org.orbisgis.wpsclient.WpsClientImpl;
import org.orbisgis.wpsclient.view.utils.Filter.IFilter;
import org.orbisgis.wpsclient.view.utils.Filter.SearchFilter;
import org.orbisgis.wpsclient.view.utils.ToolBoxIcon;
import org.orbisgis.wpsclient.view.utils.TreeNodeWps;
import org.orbisgis.wpsservice.controller.process.ProcessIdentifier;
import org.orbisgis.wpsservice.model.Process;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.EventHandler;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main panel of the ToolBox.
 * This panel contains the JTree of all the loaded scripts.
 *
 * @author Sylvain PALOMINOS
 **/

public class ToolBoxPanel extends JPanel {

    private static final String ADD_SOURCE = "ADD_SOURCE";
    private static final String ADD_SCRIPT = "ADD_SCRIPT";
    private static final String RUN_SCRIPT = "RUN_SCRIPT";
    private static final String REFRESH_SOURCE = "REFRESH_SOURCE";
    private static final String REMOVE = "REMOVE";

    public final static String TAG_MODEL = "Advanced interface";
    public final static String FILE_MODEL = "Simple interface";
    public final static String FILTERED_MODEL = "Filtered";

    private static final String ORBISGIS_STRING = "OrbisGIS";

    private static final String LOCALHOST_STRING = "localhost";
    private static final URI LOCALHOST_URI = URI.create(LOCALHOST_STRING);

    /** ComboBox with the different model of the tree */
    private JComboBox<String> treeNodeBox;

    /** Reference to the toolbox.*/
    private WpsClientImpl wpsClient;

    /** JTree */
    private JTree tree;
    /** Model of the JTree */
    private FileTreeModel fileModel;
    /** Model of the JTree */
    private FileTreeModel tagModel;
    /** Model of the JTree*/
    private FileTreeModel filteredModel;
    /** Model of the JTree*/
    private FileTreeModel selectedModel;

    /** Action available in the right click popup on selecting the panel */
    private ActionCommands popupGlobalActions;
    /** Action available in the right click popup on selecting a node */
    private ActionCommands popupNodeActions;
    /** Action available in the right click popup on selecting a process (leaf) */
    private ActionCommands popupLeafActions;
    /** Action available in the right click popup on selecting a default OrbisGIS process (leaf) */
    private ActionCommands popupOrbisGISLeafActions;
    /** Action available in the right click popup on selecting a default OrbisGIS node (folder) */
    private ActionCommands popupOrbisGISNodeActions;

    /** Map containing all the host (localhost ...) and the associated node. */
    private Map<URI, TreeNodeWps> mapHostNode;
    /** List of existing tree model. */
    private List<FileTreeModel> modelList;

    private static final String DEFAULT_FILTER_FACTORY = "name_contains";
    private FilterFactoryManager<IFilter,DefaultActiveFilter> filterFactoryManager;

    public ToolBoxPanel(WpsClientImpl wpsClient){
        super(new BorderLayout());

        this.wpsClient = wpsClient;

        //By default add the localhost
        mapHostNode = new HashMap<>();
        TreeNodeWps localhostNode = new TreeNodeWps();
        localhostNode.setNodeType(TreeNodeWps.NodeType.HOST_LOCAL);
        localhostNode.setUserObject(LOCALHOST_STRING);
        mapHostNode.put(LOCALHOST_URI, localhostNode);

        TreeNodeWps fileRoot = new TreeNodeWps();
        fileRoot.setUserObject(FILE_MODEL);
        fileModel = new FileTreeModel(localhostNode);
        //fileModel.insertNodeInto(localhostNode, fileRoot, 0);

        TreeNodeWps tagRoot = new TreeNodeWps();
        tagRoot.setUserObject(TAG_MODEL);
        tagModel = new FileTreeModel(tagRoot);

        TreeNodeWps filteredRoot = new TreeNodeWps();
        filteredRoot.setUserObject(FILTERED_MODEL);
        filteredModel = new FileTreeModel(filteredRoot);

        treeNodeBox = new JComboBox<>();
        treeNodeBox.addItem(FILE_MODEL);
        treeNodeBox.addItem(TAG_MODEL);
        treeNodeBox.setSelectedItem(FILE_MODEL);
        treeNodeBox.addActionListener(EventHandler.create(ActionListener.class, this, "onModelSelected"));

        tree = new FileTree();
        tree.setRootVisible(false);
        tree.setScrollsOnExpand(true);
        tree.setToggleClickCount(1);
        tree.setCellRenderer(new CustomTreeCellRenderer(tree));
        tree.addMouseListener(EventHandler.create(MouseListener.class, this, "onMouseClicked", "", "mouseReleased"));

        JScrollPane treeScrollPane = new JScrollPane(tree);
        this.add(treeScrollPane, BorderLayout.CENTER);
        this.add(treeNodeBox, BorderLayout.PAGE_END);

        popupGlobalActions = new ActionCommands();
        popupGlobalActions.setAccelerators(this, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        createPopupActions(wpsClient);

        //Sets the filter
        filterFactoryManager = new FilterFactoryManager<>();
        filterFactoryManager.setDefaultFilterFactory(DEFAULT_FILTER_FACTORY);
        FilterFactoryManager.FilterChangeListener refreshFilterListener = EventHandler.create(
                FilterFactoryManager.FilterChangeListener.class,
                this,
                "setFilters",
                "source.getFilters");
        filterFactoryManager.getEventFilterChange().addListener(this, refreshFilterListener);
        filterFactoryManager.getEventFilterFactoryChange().addListener(this, refreshFilterListener);
        this.add(filterFactoryManager.makeFilterPanel(false), BorderLayout.NORTH);
        SearchFilter searchFilter = new SearchFilter();
        filterFactoryManager.registerFilterFactory(searchFilter);
        filterFactoryManager.setUserCanRemoveFilter(false);
        filterFactoryManager.addFilter(new SearchFilter().getDefaultFilterValue());

        modelList = new ArrayList<>();
        modelList.add(tagModel);
        modelList.add(fileModel);
        modelList.add(filteredModel);
        tree.setModel(tagModel);
        onModelSelected();
    }

    /**
     * Returns the selected node.
     * @return The selected node.
     */
    public TreeNodeWps getSelectedNode(){
        return (TreeNodeWps)tree.getLastSelectedPathComponent();
    }

    /**
     * Action done when the mouse is clicked.
     * @param event Mouse event.
     */
    public void onMouseClicked(MouseEvent event){
        //Test if it is a right click
        if(event.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu popupMenu = new JPopupMenu();
            //find what was clicked to give to the popup the good action
            if(event.getSource().equals(tree)){
                if(tree.getLastSelectedPathComponent() == null ||
                        tree.getLastSelectedPathComponent().equals(fileModel.getRoot()) ||
                        tree.getLastSelectedPathComponent().equals(tagModel.getRoot())){
                    popupGlobalActions.copyEnabledActions(popupMenu);
                }
                else {
                    TreeNodeWps node = (TreeNodeWps) tree.getLastSelectedPathComponent();
                    if(node.isDefaultOrbisGIS()){
                        if (node.isLeaf() && !node.getNodeType().equals(TreeNodeWps.NodeType.FOLDER)) {
                            popupOrbisGISLeafActions.copyEnabledActions(popupMenu);
                        } else {
                            popupOrbisGISNodeActions.copyEnabledActions(popupMenu);
                        }
                    }
                    else {
                        if (node.isLeaf() && !node.getNodeType().equals(TreeNodeWps.NodeType.FOLDER)) {
                            popupLeafActions.copyEnabledActions(popupMenu);
                        } else {
                            popupNodeActions.copyEnabledActions(popupMenu);
                        }
                    }
                }
            }
            if (popupMenu.getComponentCount()>0) {
                popupMenu.show(event.getComponent(), event.getX(), event.getY());
            }
        }
        else {
            TreeNodeWps selectedNode = (TreeNodeWps) ((FileTree)event.getSource()).getLastSelectedPathComponent();
            if(selectedNode != null) {
                //if a simple click is done
                if (event.getClickCount() == 1) {
                    switch(selectedNode.getNodeType()){
                        case HOST_DISTANT:
                            //TODO : check if the host is reachable an if it contains a WPS service.
                            break;
                        case HOST_LOCAL:
                            //TODO : check if the OrbisGIS WPS script folder is available or not
                            break;
                        case FOLDER:
                            if(selectedNode.getChildCount() != 0) {
                                //Check if the folder exists and it it contains some scripts
                                if (selectedModel == fileModel) {
                                    refresh(selectedNode);
                                }
                            }
                            else{
                                wpsClient.addLocalSource(selectedNode.getUri());
                            }
                            break;
                        case PROCESS:
                            refresh(selectedNode);
                            break;
                    }
                }
                //If a double click is done
                if (event.getClickCount() == 2) {
                    if (selectedNode.isValidNode()) {
                        //if the selected node is a PROCESS node, open a new instance.
                        if(selectedNode.getNodeType().equals(TreeNodeWps.NodeType.PROCESS)) {
                            wpsClient.openProcess(selectedNode.getUri());
                        }
                    }
                }
            }
        }
    }

    /**
     * Action done when a model is selected in the comboBox.
     */
    public void onModelSelected(){
        if(treeNodeBox.getSelectedItem().equals(FILE_MODEL)){
            selectedModel = fileModel;
        }
        else if(treeNodeBox.getSelectedItem().equals(TAG_MODEL)){
            selectedModel = tagModel;
        }
        tree.setModel(selectedModel);
    }

    /**
     * Adds a process in the tag model.
     * @param p Process to add.
     * @param uri Process URI.
     */
    public void addScriptInTagModel(Process p, URI uri, String iconName, boolean isDefault){
        TreeNodeWps root = (TreeNodeWps) tagModel.getRoot();
        TreeNodeWps script = new TreeNodeWps();
        script.setUri(uri);
        script.setNodeType(TreeNodeWps.NodeType.PROCESS);
        script.setDefaultOrbisGIS(isDefault);

        script.setValidNode(p!=null);
        if(iconName != null){
            script.setCustomIcon(iconName);
        }
        if(p!=null){
            script.setUserObject(p.getTitle());
            if(p.getKeywords() != null) {
                for (String tag : p.getKeywords()) {
                    TreeNodeWps tagNode = getChildWithUserObject(tag, root);
                    if (tagNode == null) {
                        tagNode = new TreeNodeWps();
                        tagNode.setNodeType(TreeNodeWps.NodeType.FOLDER);
                        tagNode.setUserObject(tag);
                        tagNode.setValidNode(true);
                        tagModel.insertNodeInto(tagNode, root, 0);
                    }
                    if (getChildrenWithUri(uri, tagNode).isEmpty()) {
                        tagModel.insertNodeInto(script.deepCopy(), tagNode, 0);
                    }
                }
            }
            else{
                TreeNodeWps tagNode = getChildWithUserObject("no_tag", root);
                if(tagNode == null){
                    tagNode = new TreeNodeWps();
                    tagNode.setNodeType(TreeNodeWps.NodeType.FOLDER);
                    tagNode.setUserObject("no_tag");
                    tagNode.setValidNode(true);
                    tagModel.insertNodeInto(tagNode, root, 0);
                }
                if(getChildrenWithUri(uri, tagNode).isEmpty()){
                    tagModel.insertNodeInto(script.deepCopy(), tagNode, 0);
                }
            }
        }
        else{
            script.setUserObject(new File(uri).getName().replace(".groovy", ""));
            TreeNodeWps tagNode = getChildWithUserObject("invalid", root);
            if(tagNode == null){
                tagNode = new TreeNodeWps();
                tagNode.setNodeType(TreeNodeWps.NodeType.FOLDER);
                tagNode.setUserObject("invalid");
                tagNode.setValidNode(true);
                tagModel.insertNodeInto(tagNode, root, 0);
            }
            if(getChildrenWithUri(uri, tagNode).isEmpty()){
                tagModel.insertNodeInto(script.deepCopy(), tagNode, 0);
            }
        }
    }

    /**
     * Tests if the parent node contain a child representing the given file.
     * @param uri URI to test.
     * @param parent Parent to test.
     * @return True if the parent contain the file.
     */
    private boolean isNodeExisting(URI uri, TreeNodeWps parent){
        boolean exist = false;
        for(int l=0; l<parent.getChildCount(); l++){
            if(((TreeNodeWps)parent.getChildAt(l)).getUri().equals(uri)){
                exist = true;
            }
        }
        return exist;
    }

    /**
     * Gets the the child node of the parent node which has the given userObject.
     * @param nodeURI URI of the node.
     * @param parent Parent to analyse.
     * @return The child which has the given userObject. Null if not found.
     */
    private TreeNodeWps getSubNode(URI nodeURI, TreeNodeWps parent){
        TreeNodeWps child = null;
        for(int i = 0; i < parent.getChildCount(); i++){
            if(((TreeNodeWps)parent.getChildAt(i)).getUri().equals(nodeURI)){
                child = (TreeNodeWps)parent.getChildAt(i);
            }
        }
        return child;
    }

    /**
     * Adds a local source of default scripts. Open the given directory and find all the groovy script contained.
     */
    public void addLocalSource(ProcessIdentifier pi) {
        //Add the process in the File model
        addLocalSourceInFileModel(pi.getParent(), mapHostNode.get(LOCALHOST_URI), pi.getCategory(), pi.getURI(),
                pi.getNodePath(), pi.isDefault());
        //Get the last icon to use it for the Category model
        String categoryIconName = null;
        if(pi.getCategory() != null){
            categoryIconName = pi.getCategory()[pi.getCategory().length-1];
        }
        //Add the process to the Category model
        addScriptInTagModel(pi.getProcess(), pi.getURI(), categoryIconName, pi.isDefault());
        TreeNodeWps scriptFileModel = getChildrenWithUri(pi.getParent(), (TreeNodeWps)fileModel.getRoot()).get(0);
        scriptFileModel.setDefaultOrbisGIS(pi.isDefault());
        for(TreeNodeWps node : getAllChild(scriptFileModel)){
            node.setDefaultOrbisGIS(pi.isDefault());
            List<TreeNodeWps> tagNodeList = getChildrenWithUri(node.getUri(), (TreeNodeWps)tagModel.getRoot());
            for(TreeNodeWps tagNode : tagNodeList){
                tagNode.setDefaultOrbisGIS(pi.isDefault());
            }
        }
        refresh();
    }

    public void addFolder(URI folderUri, URI parentUri){
        TreeNodeWps hostNode = mapHostNode.get(LOCALHOST_URI);
        List<TreeNodeWps> sourceList = getChildrenWithUri(parentUri, hostNode);
        TreeNodeWps parentNode;
        if(sourceList.isEmpty()){
            parentNode = null;
        }
        else{
            parentNode = sourceList.get(0);
            if(getSubNode(folderUri, parentNode) != null){
                return;
            }
        }
        for(TreeNodeWps node : getChildrenWithUri(folderUri, hostNode)){
            remove(node);
        }
        String folderName = new File(folderUri).getName();
        TreeNodeWps folderNode = new TreeNodeWps();
        folderNode.setValidNode(true);
        folderNode.setUserObject(folderName);
        folderNode.setUri(folderUri);
        folderNode.setNodeType(TreeNodeWps.NodeType.FOLDER);

        if (parentNode == null) {
            fileModel.insertNodeInto(folderNode, hostNode, 0);
        } else {
            fileModel.insertNodeInto(folderNode, parentNode, 0);
            tree.expandPath(new TreePath(parentNode.getPath()));
        }
    }

    /**
     * Adds a source in the file model.
     */
    private void addLocalSourceInFileModel(URI parentUri, TreeNodeWps hostNode, String[] iconName, URI processUri,
                                           String nodePath, boolean isDefault){
        //Ensure that the tree contains all the node of the given nodePath of the process
        String[] split = nodePath.split("/");
        TreeNodeWps parent = hostNode;
        TreeNodeWps node = null;
        int index = 0;
        //For each node of the path
        for(String str : split){
            //Test if the node exist
            node = getChildWithUserObject(str, parent);
            if (node == null) {
                //If it doesn't, create and set it
                node = new TreeNodeWps();
                node.setValidNode(true);
                node.setUserObject(str);
                node.setUri(URI.create(parentUri.toString()+"/"+str));
                node.setNodeType(TreeNodeWps.NodeType.FOLDER);
                node.setDefaultOrbisGIS(isDefault);
                //If icon names were defined, use them.
                if(iconName != null) {
                    if (iconName.length > index) {
                        node.setCustomIcon(iconName[index]);
                    } else {
                        node.setCustomIcon(iconName[iconName.length - 1]);
                    }
                }
                fileModel.insertNodeInto(node, parent, 0);
            }
            parent = node;
            index++;
        }
        parent.setUri(parentUri);

        if(getChildrenWithUri(processUri, node).isEmpty()) {
            Process process = wpsClient.getWpsService().describeProcess(processUri);
            TreeNodeWps script = new TreeNodeWps();
            script.setUri(processUri);
            script.setValidNode(process != null);
            script.setNodeType(TreeNodeWps.NodeType.PROCESS);
            script.setDefaultOrbisGIS(isDefault);
            if(process != null){
                script.setUserObject(process.getTitle());
            }
            else{
                script.setUserObject(new File(processUri).getName().replace(".groovy", ""));
            }
            fileModel.insertNodeInto(script, node, 0);
        }
        tree.expandPath(new TreePath(parent.getPath()));
    }

    /**
     * Returns all the WPS script file contained by the directory.
     * @param directory URI to analyse.
     * @return The list of URI.
     */
    private List<URI> getAllWpsScript(URI directory) {
        List<URI> scriptList = new ArrayList<>();
        File f = new File(directory);
        if (f.exists() && f.isDirectory()) {
            for (File file : f.listFiles()) {
                if (file != null) {
                    if (file.isFile() && file.getName().endsWith(".groovy")) {
                        scriptList.add(file.toURI());
                    }
                }
            }
        }
        return scriptList;
    }

    /**
     * Remove the selected node.
     */
    public void removeSelected(){
        TreeNodeWps selected = (TreeNodeWps)tree.getLastSelectedPathComponent();
        remove(selected);
    }

    /**
     * Remove from the toolBox a node and the associated process.
     * @param node
     */
    public void remove(TreeNodeWps node){
        if(!node.equals(fileModel.getRoot()) && !node.equals(tagModel.getRoot())){
            List<TreeNodeWps> leafList = new ArrayList<>();
            leafList.addAll(getAllChild(node));
            for(TreeNodeWps leaf : leafList){
                switch(leaf.getNodeType()){
                    case FOLDER:
                        for (TreeNodeWps child : getChildrenWithUri(leaf.getUri(), (TreeNodeWps) selectedModel.getRoot())) {
                            if (!child.isDefaultOrbisGIS()) {
                                cleanParentNode(child, selectedModel);
                            }
                        }
                        break;
                    case PROCESS:
                        for (FileTreeModel model : modelList) {
                            for (TreeNodeWps child : getChildrenWithUri(leaf.getUri(), (TreeNodeWps) model.getRoot())) {
                                if (child != null) {
                                    cleanParentNode(child, model);
                                }
                            }
                        }
                        wpsClient.removeProcess(leaf.getUri());
                        break;
                }
            }
        }
    }

    /**
     * Get the child node of a parent which represent the given file.
     * @param uri URI represented by the node.
     * @param parent Parent of the node.
     * @return The child node.
     */
    private List<TreeNodeWps> getChildrenWithUri(URI uri, TreeNodeWps parent){
        List<TreeNodeWps> nodeList = new ArrayList<>();
        for(int i=0; i<parent.getChildCount(); i++){
            TreeNodeWps child = (TreeNodeWps)parent.getChildAt(i);
            if(child.getUri() != null && child.getUri().equals(uri)){
                nodeList.add(child);
            }
            else{
                nodeList.addAll(getChildrenWithUri(uri, child));
            }
        }
        return nodeList;
    }

    /**
     * Get the first encountered child node of a parent which represent the same user object.
     * @param userObject Object represented by the node.
     * @param parent Parent of the node.
     * @return The child node.
     */
    private TreeNodeWps getChildWithUserObject(Object userObject, TreeNodeWps parent){
        for(int i=0; i<parent.getChildCount(); i++){
            TreeNodeWps child = (TreeNodeWps)parent.getChildAt(i);
            if(child.getUserObject() != null && child.getUserObject().equals(userObject)){
                return child;
            }
            else{
                TreeNodeWps result = getChildWithUserObject(userObject, child);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * If the given parent node is empty, remove it except if it is the root of the model.
     * Then do the same for its parent.
     * @param node Node to check.
     * @param model Model containing the node.
     */
    private void cleanParentNode(TreeNodeWps node, FileTreeModel model){
        //If the node is the last one from its parent, call 'cleanParentNode()' on it
        if(node.getParent().getChildCount() == 1){
            cleanParentNode((TreeNodeWps)node.getParent(), model);
        }
        else {
            model.removeNodeFromParent(node);
        }
    }

    /**
     * Refresh the selected node.
     * If the node is a process (a leaf), check if it is valid or not,
     * If the node is a category check the contained process,
     * If the node is a folder, check the folder to re-add all the contained processes.
     */
    public void refresh(){
        refresh((TreeNodeWps) tree.getLastSelectedPathComponent());
    }

    public void refreshAll(){
        List<TreeNodeWps> leafList = getAllLeaf((TreeNodeWps)selectedModel.getRoot());
        for(TreeNodeWps node : leafList){
            refresh(node);
        }
    }
    /**
     * Refresh the given node.
     * If the node is a process (a leaf), check if it is valid or not,
     * If the node is a category check the contained process,
     * If the node is a folder, check the folder to re-add all the contained processes.
     */
    public void refresh(TreeNodeWps node){
        if(node != null) {
            if (node.getNodeType().equals(TreeNodeWps.NodeType.PROCESS)) {
                if(!wpsClient.checkProcess(node.getUri())){
                    remove(node);
                }
            } else {
                //For each node, test if it is valid, and set the state of the corresponding node in the trees.
                for (TreeNodeWps child : getAllLeaf(node)) {
                    refresh(child);
                }
            }
        }
    }

    /**
     * Returns all the leaf child of a node.
     * @param node Node to explore.
     * @return List of child leaf.
     */
    private List<TreeNodeWps> getAllLeaf(TreeNodeWps node){
        List<TreeNodeWps> nodeList = new ArrayList<>();
        for(int i=0; i<node.getChildCount(); i++){
            TreeNodeWps child = (TreeNodeWps) node.getChildAt(i);
            if(child.isLeaf()){
                nodeList.add(child);
            }
            else{
                nodeList.addAll(getAllLeaf(child));
            }
        }
        return nodeList;
    }

    /**
     * Returns all the child of a node.
     * @param node Node to explore.
     * @return List of child.
     */
    private List<TreeNodeWps> getAllChild(TreeNodeWps node){
        List<TreeNodeWps> nodeList = new ArrayList<>();
        for(int i=0; i<node.getChildCount(); i++){
            TreeNodeWps child = (TreeNodeWps) node.getChildAt(i);
            if(child.isLeaf()){
                nodeList.add(child);
            }
            else{
                nodeList.addAll(getAllChild(child));
            }
        }
        nodeList.add(node);
        return nodeList;
    }

    /**
     * Returns all the node child of a node with the specified type.
     * @param node Node to explore.
     * @param nodeType Type of the nodes.
     * @return List of child nodes.
     */
    private List<TreeNodeWps> getAllChildWithType(TreeNodeWps node, TreeNodeWps.NodeType nodeType){
        List<TreeNodeWps> nodeList = new ArrayList<>();
        for(int i=0; i<node.getChildCount(); i++){
            TreeNodeWps child = (TreeNodeWps) node.getChildAt(i);
            if(child.getNodeType().equals(nodeType)){
                nodeList.add(child);
            }
            else if(!child.isLeaf()){
                nodeList.addAll(getAllChildWithType(child, nodeType));
            }
        }
        return nodeList;
    }

    /**
     * Creates the action for the popup.
     * @param wpsClient ToolBox.
     */
    private void createPopupActions(WpsClientImpl wpsClient) {
        DefaultAction addSource = new DefaultAction(
                ADD_SOURCE,
                "Add folder",
                "Add a local folder",
                ToolBoxIcon.getIcon("folder_add"),
                EventHandler.create(ActionListener.class, wpsClient, "addNewLocalSource"),
                null
        );
        DefaultAction addFile = new DefaultAction(
                ADD_SCRIPT,
                "Add file",
                "Add a local file",
                ToolBoxIcon.getIcon("script_add"),
                EventHandler.create(ActionListener.class, wpsClient, "addNewLocalScript"),
                null
        );
        DefaultAction runScript = new DefaultAction(
                RUN_SCRIPT,
                "Run",
                "Run a script",
                ToolBoxIcon.getIcon("execute"),
                EventHandler.create(ActionListener.class, wpsClient, "openProcess"),
                null
        );
        DefaultAction refresh_source = new DefaultAction(
                REFRESH_SOURCE,
                "Refresh",
                "Refresh a source",
                ToolBoxIcon.getIcon("refresh"),
                EventHandler.create(ActionListener.class, this, "refresh"),
                null
        );
        DefaultAction remove = new DefaultAction(
                REMOVE,
                "Remove",
                "Remove a source or a script",
                ToolBoxIcon.getIcon("remove"),
                EventHandler.create(ActionListener.class, this, "removeSelected"),
                null
        );

        popupGlobalActions = new ActionCommands();
        popupGlobalActions.addAction(addSource);
        popupGlobalActions.addAction(addFile);

        popupOrbisGISLeafActions = new ActionCommands();
        popupOrbisGISLeafActions.addAction(runScript);


        popupLeafActions = new ActionCommands();
        popupLeafActions.addAction(runScript);
        popupLeafActions.addAction(remove);

        popupNodeActions = new ActionCommands();
        popupNodeActions.addAction(addSource);
        popupNodeActions.addAction(addFile);
        popupNodeActions.addAction(refresh_source);
        popupNodeActions.addAction(remove);

        popupOrbisGISNodeActions = new ActionCommands();
        popupOrbisGISNodeActions.addAction(addSource);
        popupOrbisGISNodeActions.addAction(addFile);
    }

    /**
     * Sets and applies the filters to the list of WPS scripts and display only the compatible one.
     * @param filters List of IFilter to apply.
     */
    public void setFilters(List<IFilter> filters){
        if(filters.size() == 1){
            IFilter filter = filters.get(0);
            //If the filter is empty, use the previously selected model and open the tree.
            if(filter.acceptsAll()){
                tree.setModel(selectedModel);
                if(selectedModel != null) {
                    TreeNodeWps root = (TreeNodeWps) selectedModel.getRoot();
                    tree.expandPath(new TreePath(((TreeNodeWps)root.getChildAt(0)).getPath()));
                }
            }
            //Else, use the filteredModel
            else {
                tree.setModel(filteredModel);
                for (TreeNodeWps node : getAllChildWithType((TreeNodeWps) fileModel.getRoot(), TreeNodeWps.NodeType.PROCESS)) {
                    //For all the leaf, tests if they are accepted by the filter or not.
                    TreeNodeWps filteredRoot = (TreeNodeWps) filteredModel.getRoot();
                    List<TreeNodeWps> filteredNode = getChildrenWithUri(node.getUri(), filteredRoot);
                    if (filteredNode.isEmpty()) {
                        if (filter.accepts(node)) {
                            TreeNodeWps newNode = node.deepCopy();
                            filteredModel.insertNodeInto(newNode, filteredRoot, 0);
                            filteredModel.nodeStructureChanged(filteredRoot);
                            tree.expandPath(new TreePath(newNode.getPath()));
                        }
                    }
                    else {
                        if (!filter.accepts(filteredNode.get(0))) {
                            filteredModel.removeNodeFromParent(filteredNode.get(0));
                        }
                        else{
                            tree.expandPath(new TreePath(filteredNode.get(0).getPath()));
                        }
                    }
                }
            }
        }
    }

    public void dispose(){
        filterFactoryManager.getEventFilterChange().clearListeners();
        filterFactoryManager.getEventFilterFactoryChange().clearListeners();
    }

    /**
     * Open in the tree the node corresponding to the give name in the given model
     * @param nodeName Name of the node to open.
     * @param modelName Name of the model where the node is.
     */
    public void openNode(String nodeName, String modelName){
        FileTreeModel model = null;
        switch(modelName){
            case FILE_MODEL:
                model = fileModel;
                break;
            case TAG_MODEL:
                model = tagModel;
                break;
        }
        TreePath treePath = null;
        if(model != null){
            TreeNodeWps node = getChildWithUserObject(nodeName, (TreeNodeWps)model.getRoot());
            if(node != null) {
                treePath = new TreePath(node.getPath());
            }
        }
        else{
            TreeNodeWps node =getChildWithUserObject(ORBISGIS_STRING, (TreeNodeWps)tagModel.getRoot());
            if(node != null) {
                treePath = new TreePath(node.getPath());
            }
        }
        if(treePath != null){
            tree.expandPath(treePath);
        }
        tree.setModel(model);
        selectedModel = model;
    }
}
