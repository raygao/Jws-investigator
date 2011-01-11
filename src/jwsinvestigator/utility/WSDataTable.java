/*
 * WSDataTable.java
 *
 * Created on August 31, 2007, 9:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jwsinvestigator.utility;


///////////////////////////////////////////////////////////////////////////////////
/// Author:     Raymond Gao
/// Contact:    rgao@bea.com
/// Date:       May 21, 2007
/// This mini app lists all "web services" installed in an ALUI app.
/// This app is compiled with G6 MP1 server libraries.
/// This is an open-source project, no warranty or license available.
/// If you would like to participate in this project,
/// please contact Ray Gao at mailto:rgao@bea.com or mailto:raygao2000@yahoo.com
//////////////////////////////////////////////////////////////////////////////////


import com.plumtree.openfoundation.util.XPDateTime;
import java.util.Collections.*;
import com.plumtree.openfoundation.util.*;
import javax.swing.table.DefaultTableModel;

class WSDataTable extends DefaultTableModel {
    public WSDataTable() {
    }
    
    //table headings
    private String name, description, adminFolderPath, relativeURL, providerCLSID, propertybag_keys, fullURL;
    private Integer oid, ownerID, remoteServerID;
    //private int oid, ownerID, remoteServerID;
    //private boolean state;
    private Boolean state;
    private XPDateTime modifiedDate, createdDate;
    
    private String[] columnNames = {"Name", "Description", "State", "Object ID",
    "Owner's ID", "Admin Folder Path", "Remote Server ID",
    "Relative URL", "Full URL", "Provider CLSID",
    "Created Date", "Modified Date", "Property Bag Keys"
    };    
    
    public int getColumnCount() {
        return columnNames.length;
    }
        
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public void createDataSource() {
        // Define the columns of the table model.
        this.addColumn(name); //String
        this.addColumn(description); //String
        this.addColumn(state); //boolean
        this.addColumn(oid); //int
        this.addColumn(ownerID); //int
        this.addColumn(adminFolderPath); //String
        this.addColumn(remoteServerID); //int
        this.addColumn(relativeURL); //String
        this.addColumn(fullURL); //String
        this.addColumn(providerCLSID); //String
        this.addColumn(createdDate); //XPDateTime
        this.addColumn(modifiedDate); //XPDateTime
        this.addColumn(propertybag_keys); //String
    }
    
    /// <summary>
    /// ceating individual rows
    /// </summary>
    /// <returns>a data row object</returns>
    
    public Object []  createRow(String name, String description, Boolean state, Integer oid,
            Integer ownerID, String adminFolderPath, Integer remoteServerID,
            String relativeURL, String fullURL, String providerCLSID,
            XPDateTime createdDate, XPDateTime modifiedDate, String propertybag_keys) {
        
        Object []  aRow = {name, description, state, oid, ownerID, adminFolderPath,
        remoteServerID, relativeURL, fullURL, providerCLSID,
        createdDate, modifiedDate, propertybag_keys };
        return aRow;
    }
    
    public void addEntry(String name, String description, Boolean state, Integer oid, Integer ownerID,
            String adminFolderPath, Integer remoteServerID, String relativeURL, String fullURL,
            String providerCLSID, XPDateTime createdDate, XPDateTime modifiedDate, String propertybag_keys) {
        this.addRow(createRow(name, description, state, oid, ownerID,
                adminFolderPath, remoteServerID, relativeURL, fullURL,
                providerCLSID, createdDate, modifiedDate, propertybag_keys));
    }
    
}

