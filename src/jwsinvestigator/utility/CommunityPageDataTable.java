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

class CommunityPageDataTable extends DefaultTableModel {
    public CommunityPageDataTable() {
    }
    
    //table headings
    private String name, description, adminFolderPath, portlets, pagetemplate;
    private Integer oid, ownerID;
    //private int oid, ownerID;
    private XPDateTime modifiedDate, createdDate;
    
    private String[] columnNames = {"Name", "Description", "Object ID",
    "Owner's ID", "Admin Folder Path", "Portlets", "Page Template", 
    "Created Date", "Modified Date"
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
        this.addColumn(oid); //int
        this.addColumn(ownerID); //int
        this.addColumn(adminFolderPath); //String
        this.addColumn(portlets); //String
        this.addColumn(pagetemplate); //String
        this.addColumn(createdDate); //XPDateTime
        this.addColumn(modifiedDate); //XPDateTime
    }
    
    /// <summary>
    /// ceating individual rows
    /// </summary>
    /// <returns>a data row object</returns>
    public Object []  createRow(String name, String description, Integer oid,
            Integer ownerID, String adminFolderPath, String portlets, String pagetemplate,
            XPDateTime createdDate, XPDateTime modifiedDate) {
        
        Object []  aRow = {name, description, oid, ownerID, adminFolderPath,
        portlets, pagetemplate, createdDate, modifiedDate};
        return aRow;
    }
    
    public void addEntry(String name, String description, Integer oid,
            Integer ownerID, String adminFolderPath, String portlets, String pagetemplate,
            XPDateTime createdDate, XPDateTime modifiedDate) {
        this.addRow(createRow(name, description, oid, ownerID, adminFolderPath,
                portlets, pagetemplate, createdDate, modifiedDate));
    }
    
}

