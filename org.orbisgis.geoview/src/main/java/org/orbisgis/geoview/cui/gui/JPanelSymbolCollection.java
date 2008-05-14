/*
 * JPanelLegend.java
 *
 * Created on 22 de febrero de 2008, 15:36
 */

package org.orbisgis.geoview.cui.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;

import org.orbisgis.geoview.cui.gui.widgets.JPanelComboSymbolPicker;
import org.orbisgis.renderer.legend.Legend;
import org.sif.UIFactory;
import org.sif.UIPanel;


/**
 *
 * @author  david
 */
public class JPanelSymbolCollection extends javax.swing.JPanel implements UIPanel {

	String infoText="Symbol Collection";
	String title="Symbol Collection";
	int constraint=0;



    public JPanelSymbolCollection( int constraint ) {
    	this.constraint = constraint;
        initComponents();
        fillPanels();
        fillList();
    }

    
	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonMenuUp = new javax.swing.JButton();
        jButtonMenuDown = new javax.swing.JButton();
        jButtonMenuAdd = new javax.swing.JButton();
        jButtonMenuDel = new javax.swing.JButton();
        jButtonMenuRename = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonLoad = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 160));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );

        jToolBar1.setFloatable(false);
        jButtonMenuUp.setText("up");
        jButtonMenuUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuUpActionPerformed(evt);
            }
        });

        jToolBar1.add(jButtonMenuUp);

        jButtonMenuDown.setText("down");
        jButtonMenuDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuDownActionPerformed(evt);
            }
        });

        jToolBar1.add(jButtonMenuDown);

        jButtonMenuAdd.setText("add");
        jButtonMenuAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuAddActionPerformed(evt);
            }
        });

        jToolBar1.add(jButtonMenuAdd);

        jButtonMenuDel.setText("del");
        jButtonMenuDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuDelActionPerformed(evt);
            }
        });

        jToolBar1.add(jButtonMenuDel);

        jButtonMenuRename.setText("rename");
        jButtonMenuRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuRenameActionPerformed(evt);
            }
        });

        jToolBar1.add(jButtonMenuRename);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonLoad.setText("Load");
        jButtonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonLoad)
                            .addComponent(jButtonSave))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(this);
        File file = chooser.getSelectedFile();
        if (file!=null)
        	System.out.println(file.getAbsolutePath());
    }//GEN-LAST:event_jButtonLoadActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(this);
        File file = chooser.getSelectedFile();
        if (file!=null)
        	System.out.println(file.getAbsolutePath());
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonMenuRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuRenameActionPerformed
        DefaultListModel mod = (DefaultListModel)jList1.getModel();
    	String nombre=(String)jList1.getSelectedValue();
    	int idx=jList1.getSelectedIndex();
    	mod.remove(idx);

        //TODO show a "select name" pane"
        //TODO Change the name in the pane

        mod.add(idx, "New Name");
    }//GEN-LAST:event_jButtonMenuRenameActionPerformed

    private void jButtonMenuDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuDelActionPerformed
    	DefaultListModel mod = (DefaultListModel)jList1.getModel();
    	String nombre=(String)jList1.getSelectedValue();
    	int idx=jList1.getSelectedIndex();
    	mod.remove(idx);

    	int count=jPanel2.getComponentCount();
    	for (int i=0; i<count; i++){
    		Component com = jPanel2.getComponent(i);
    		ILegendPanelUI pan = (ILegendPanelUI)com;
    		if (pan.toString().equals(nombre)){
    			jPanel2.remove(com);
    			break;
    		}
    	}
    	
    	//refill composite panels
    	ArrayList <JPanelUniqueSymbolLegend> paneles = getUniqueSymbolPanels();
        
        Component[] com=jPanel2.getComponents();
        
        for (int i=0; i<com.length; i++){
        	if (com[i] instanceof JPanelCompositeSymbol) {
				JPanelCompositeSymbol compo = (JPanelCompositeSymbol) com[i];
				compo.setPanels(paneles);
			}
        }
        
        if (mod.getSize()>0){
            jList1.setSelectedIndex(idx);
            if (idx==0){
            	jButtonMenuUp.setEnabled(false);
            	jButtonMenuDel.setEnabled(true);
            	jButtonMenuDown.setEnabled(true);
            	jButtonMenuRename.setEnabled(true);
            }else{
            	if (idx==mod.getSize()-1){
            		jButtonMenuUp.setEnabled(true);
                	jButtonMenuDel.setEnabled(false);
                	jButtonMenuDown.setEnabled(true);
                	jButtonMenuRename.setEnabled(true);
            	}
            }
            
            if (mod.getSize()==1){
            	jButtonMenuUp.setEnabled(false);
            	jButtonMenuDown.setEnabled(false);
            }
        }else{
        	jButtonMenuDel.setEnabled(false);
        	jButtonMenuDown.setEnabled(false);
        	jButtonMenuRename.setEnabled(false);
        	jButtonMenuUp.setEnabled(false);
        }
    }//GEN-LAST:event_jButtonMenuDelActionPerformed

    private void jButtonMenuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuAddActionPerformed
    	//get de names of the available panels
    	ArrayList<String> paneNames = new ArrayList<String>();
    	paneNames.add("Simple symbol");
    	paneNames.add("Composite symbol");
    	//show the combo legend picker
    	JPanelComboSymbolPicker legendPicker = new JPanelComboSymbolPicker(paneNames.toArray());

    	if (!UIFactory.showDialog(legendPicker)){
    		return;
    	}

    	String value=legendPicker.getSelected();
    	String nombre=legendPicker.getSelectedName();

    	if (nombre.equals("")){
    		return;
    	}

    	//set the selected panel into the jlist
    	ListModel mod = jList1.getModel();
    	int tam=mod.getSize();
    	for (int i=0; i<tam; i++){
    		if (((String)mod.getElementAt(i)).equals(nombre)){
    			JOptionPane.showMessageDialog(this, "Oops!! the name already exists");
    			return;
    		}
    	}

    	((DefaultListModel)mod).addElement(nombre);

    	//create the panel and insert in the cardlayout
    	JPanel pane=null;
    	if (value.equals("Simple symbol")){
    		pane=new JPanelUniqueSymbolLegend(legendPicker.getGeometryType());
    		((JPanelUniqueSymbolLegend)pane).setIdentity(nombre);
    	}
    	if (value.equals("Composite symbol")){
    		ArrayList <JPanelUniqueSymbolLegend> paneles = getUniqueSymbolPanels();
    		pane=new JPanelCompositeSymbol(paneles);
    		((JPanelCompositeSymbol)pane).setIdentity(nombre);
    	}
    	
    	jPanel2.add(nombre, pane);

    	( (CardLayout)jPanel2.getLayout() ).show( jPanel2, nombre );

    	jPanel2.validate();
    	jPanel2.repaint();

    	jList1.setSelectedValue(nombre, true);
    	
    	jButtonMenuDel.setEnabled(true);
    	
    	jButtonMenuRename.setEnabled(true);
    	
    	
    	if (mod.getSize()==1){
    		jButtonMenuUp.setEnabled(false);
    		jButtonMenuDown.setEnabled(false);
    	}else{
    		jButtonMenuUp.setEnabled(true);
    		jButtonMenuDown.setEnabled(false);
    	}
    	
    }//GEN-LAST:event_jButtonMenuAddActionPerformed

    private ArrayList<JPanelUniqueSymbolLegend> getUniqueSymbolPanels() {
		Component[] coms = jPanel2.getComponents();
		ArrayList<JPanelUniqueSymbolLegend> paneles = new ArrayList<JPanelUniqueSymbolLegend>();
		
		for (int i=0; i <coms.length; i++){
			if (coms[i] instanceof JPanelUniqueSymbolLegend) {
				JPanelUniqueSymbolLegend unique = (JPanelUniqueSymbolLegend) coms[i];
				paneles.add(unique);
			}
		}
		
		return paneles;
		
	}


	private void jButtonMenuDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuDownActionPerformed
    	DefaultListModel mod = (DefaultListModel)jList1.getModel();
    	int idx=0;
    	idx=jList1.getSelectedIndex();
    	System.out.println(idx);
    	if (idx<mod.size()-1){
    		String element=(String)mod.get(idx);
    		mod.remove(idx);
    		mod.add(idx+1, element);
    	}
    	jList1.setSelectedIndex(idx+1);

    	jButtonMenuUp.setEnabled(true);

    	if (idx+1==mod.getSize()-1){
    		jButtonMenuDown.setEnabled(false);
    	}
    }//GEN-LAST:event_jButtonMenuDownActionPerformed

    private void jButtonMenuUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuUpActionPerformed
    	DefaultListModel mod = (DefaultListModel)jList1.getModel();
    	int idx=0;
    	idx=jList1.getSelectedIndex();
    	System.out.println(idx);
    	if (idx>0){
    		String element=(String)mod.get(idx);
    		mod.remove(idx);
    		mod.add(idx-1, element);
    	}
    	jList1.setSelectedIndex(idx-1);
    	if (idx-1==0){
    		jButtonMenuUp.setEnabled(false);
    	}

    	jButtonMenuDown.setEnabled(true);

    }//GEN-LAST:event_jButtonMenuUpActionPerformed



	private void fillList( ) {
        jList1 = new JList();
        DefaultListModel mod = new DefaultListModel();
//        for (int i=0; i<paneles.size(); i++)
//        	mod.addElement(((ILegendPanelUI)paneles.get(i)).getIdentity());
        jList1.setModel(mod);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);
        jScrollPane1.validate();
        jScrollPane1.repaint();
        if (jList1.getModel().getSize()>0){
        	if (jList1.getModel().getSize()==1){
        		jButtonMenuUp.setEnabled(false);
        		jButtonMenuDown.setEnabled(false);
        	}
            jList1.setSelectedIndex(0);
        }
        else{
        	jButtonMenuDel.setEnabled(false);
        	jButtonMenuDown.setEnabled(false);
        	jButtonMenuRename.setEnabled(false);
        	jButtonMenuUp.setEnabled(false);
        	
        }
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt){
        String whatPanel = (String) jList1.getSelectedValue();
        
        ArrayList <JPanelUniqueSymbolLegend> paneles = getUniqueSymbolPanels();
        
        Component[] com=jPanel2.getComponents();
        
        for (int i=0; i<com.length; i++){
        	if (com[i] instanceof JPanelCompositeSymbol) {
				JPanelCompositeSymbol compo = (JPanelCompositeSymbol) com[i];
				compo.setPanels(paneles);
			}
        }
        
        ( (CardLayout)jPanel2.getLayout() ).show( jPanel2,whatPanel );
        
        ListModel mod = jList1.getModel();
        int idx = jList1.getSelectedIndex();
        
        if (mod.getSize()>0)      
        	jButtonMenuDel.setEnabled(true);
        
        if (idx==0){
        	jButtonMenuUp.setEnabled(false);
        	jButtonMenuDown.setEnabled(true);
        }
        
        if (idx==mod.getSize()-1){
        	jButtonMenuUp.setEnabled(true);
        	jButtonMenuDown.setEnabled(false);
        }
        
        if (mod.getSize()==1){
        	jButtonMenuUp.setEnabled(false);
        	jButtonMenuDown.setEnabled(false);
        }
        
    }


    private void fillPanels (){
    	CardLayout jPanel2Layout = new CardLayout();
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.maximumLayoutSize(jPanel2);
        jPanel2Layout.minimumLayoutSize(jPanel2);
    	
//        JPanelVoid panelVoid=new JPanelVoid();
//        jPanel2.add("noPanel", panelVoid);
//        ( (CardLayout)jPanel2.getLayout() ).show( jPanel2, "noPanel" );
    	jPanel2.validate();
    	jPanel2.repaint();
    	
//        for (int i=0; i<paneles.size(); i++){
//            jPanel2.add(paneles.get(i).toString(), (paneles.get(i)).getComponent());
//        }
    }

    private javax.swing.JList jList1;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLoad;
    private javax.swing.JButton jButtonMenuAdd;
    private javax.swing.JButton jButtonMenuDel;
    private javax.swing.JButton jButtonMenuDown;
    private javax.swing.JButton jButtonMenuRename;
    private javax.swing.JButton jButtonMenuUp;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
	public Component getComponent() {
		// TODO Auto-generated method stub
		return this;
	}

	public URL getIconURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInfoText() {
		// TODO Auto-generated method stub
		return infoText;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String initialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public String postProcess() {
		// TODO Auto-generated method stub
		return null;
	}

	public String validateInput() {
		// TODO Auto-generated method stub
		return null;
	}

	public Legend[] getLegend(){

		ArrayList<Legend>legends = new ArrayList<Legend>();
		Component[] com=jPanel2.getComponents();

		for (int i=0; i<com.length; i++){
			Component comp = com[i];
			if ((comp instanceof ILegendPanelUI) && !(comp instanceof JPanelVoid)) {
				ILegendPanelUI jp = (ILegendPanelUI) comp;
				Legend leg=jp.getLegend();
				legends.add(leg);
			}


		}
		Legend[] legendsL = new Legend[legends.size()];
		for (int j=0; j<legendsL.length; j++){
			legendsL[j]=legends.get(j);
		}

		return legendsL;
	}
}
