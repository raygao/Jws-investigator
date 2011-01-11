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

class CommunityDataTable extends DefaultTableModel {
    public CommunityDataTable() {
    }
    
    //table headings
    private String name, description, footer, header, adminFolderPath;
    //private int oid, ownerID, users, pages, groups, subcommunities;
    private Integer oid, ownerID, users, pages, groups, subcommunities;
    private XPDateTime modifiedDate, createdDate;
    
    private String[] columnNames = {"Name", "Description", "Object ID",
    "Footer", "Header", "Owner's ID", "Admin Folder Path", "Users",
    "Pages", "Groups", "Subcommunities", "Created Date", "Modified Date"
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
        this.addColumn(footer); //String
        this.addColumn(header); //String
        this.addColumn(ownerID); //int
        this.addColumn(adminFolderPath); //String
        this.addColumn(users); //String
        this.addColumn(pages); //String
        this.addColumn(groups); //String
        this.addColumn(subcommunities); //String
        this.addColumn(createdDate); //XPDateTime
        this.addColumn(modifiedDate); //XPDateTime
    }
    
    /// <summary>
    /// ceating individual rows
    /// </summary>
    /// <returns>a data row object</returns>
    public Object []  createRow(String name, String description, Integer oid,
            String footer, String header, Integer ownerID, String adminFolderPath,
            String users, String pages, String groups, String subcommunities,
            XPDateTime createdDate, XPDateTime modifiedDate) {
        
        Object []  aRow = {name, description, oid, footer, header, ownerID,
        adminFolderPath, users, pages, groups, subcommunities, createdDate,
        modifiedDate};
        return aRow;
    }
    
    public void addEntry(String name, String description, Integer oid,
            String footer, String header, Integer ownerID, String adminFolderPath,
            String users, String pages, String groups, String subcommunities,
            XPDateTime createdDate, XPDateTime modifiedDate) {
        this.addRow(createRow(name, description, oid, footer, header, ownerID,
                adminFolderPath, users, pages, groups, subcommunities, createdDate,
                modifiedDate));
    }
    
}

