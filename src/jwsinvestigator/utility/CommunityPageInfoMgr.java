/*
 * WSInfoMgr.java
 *
 * Created on August 31, 2007, 9:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jwsinvestigator.utility;

import java.util.Collections;
import com.plumtree.server.*;
import com.plumtree.server.search.*;

import com.plumtree.openkernel.factory.*;
import com.plumtree.xpshared.config.*;
import com.plumtree.openkernel.*;
import com.plumtree.openkernel.util.*;
import com.plumtree.openkernel.config.*;
import com.plumtree.server.search.ptapps.*;
import com.plumtree.openfoundation.util.*;

/**
 *
 * @author Raymond Gao
 */
public class CommunityPageInfoMgr implements InfoMgr {
    private IPTSession ptSession;
    
    public CommunityPageInfoMgr(IPTSession _ptSession) {
        this.ptSession = _ptSession;
    }
    
    public CommunityPageDataTable getAllCommunityPagesInfo() {
        //return all webservices, regards of the enabled/disabled status
        return this.getAllCommunityPagesInfo(false);
    }
    
    public CommunityPageDataTable getAllCommunityPagesInfo(boolean status) {
        IPTObjectManager communityPageMgr;
        
        //get a communitys Object Manager.
        communityPageMgr = ptSession.GetPages();
        
        //build filter as required
        Object[][] objQueryFilter = new Object[3][];
        for (int i = 0; i < 3; i++) {
            objQueryFilter[i] = new Object[1];
        }
        
        //Build the query filters
        if (status) {
            //do nothing
            ////search for Status of the Web Services, enabled or disabled.
            //objQueryFilter[0][0] = PT_PROPIDS.PT_PROPID_GADGET_GADGETTYPE;
            //objQueryFilter[1][0] = PT_FILTEROPS.PT_FILTEROP_EQ;
            ////Enabled WS has value of 1, and Disabled WS has value 0;
            //objQueryFilter[2][0] = 1; //true
        } else {
            //search against a specific String in the WS name. In the following case, all ws.
            objQueryFilter[0][0] = new Integer (PT_PROPIDS.PT_PROPID_NAME);
            objQueryFilter[1][0] = new Integer (PT_FILTEROPS.PT_FILTEROP_CONTAINS);
            objQueryFilter[2][0] = "*";
        }
        //do query
        IPTQueryResult results =
                communityPageMgr.Query(PT_PROPIDS.PT_PROPID_OBJECTID | PT_PROPIDS.PT_PROPID_NAME, -1, PT_PROPIDS.PT_PROPID_OBJECTID, 0, -1, objQueryFilter);
        
        CommunityPageDataTable wstab = new CommunityPageDataTable();
        wstab.createDataSource();
        
        for (int i = 0; i < results.RowCount(); i++) {
            int objectId = results.ItemAsInt(i, PT_PROPIDS.PT_PROPID_OBJECTID);
            IPTPage communityPage = (IPTPage)communityPageMgr.Open(objectId, false);
            
            String name = communityPage.GetName() != null ? communityPage.GetName() : "";
            String description = communityPage.GetDescription() != "" ? communityPage.GetDescription() : "";
            //This solves the problem with return in the WS description box.
            // and carriage return (CR) -> \r
            // and line feed (LF)  -> \n
            if ((description != null) && !(description.equals("")))
                description = description.replaceAll("\r\n", " --linefeed-- ");
            
            Integer adminFolderID = new Integer(communityPage.GetAdminFolderID());
            String adminPath = (new MiscUtil().GetAdminFolderPath(ptSession, adminFolderID.intValue()));
            XPDateTime createdDate = communityPage.GetCreated();
            XPDateTime modifiedDate = communityPage.GetLastModified();
            Integer oid = new Integer (communityPage.GetObjectID());
            Integer ownerID = new Integer (communityPage.GetOwnerID());
            
            // Get potlets on this communityPage
            String portlets = "none";
            String plist = "";
            IPTQueryResult _portlets = communityPage.QueryPortlets();
            if (_portlets.RowCount() > 0) {
                for (int j = 0; j < _portlets.RowCount(); j++) {
                    plist += _portlets.ItemAsString(j, PT_PROPIDS.PT_PROPID_NAME) + ", ";
                }
                portlets = plist;
            }
            
            // Get inherited pagetemplate
            String pagetemplate = "";
            int _pagetemplate = communityPage.GetPageTemplateID();
            IPTQueryResult _result = ptSession.GetPageTemplates().QuerySingleObject(_pagetemplate);
            //some pages do not inherit any page template.
            if (_result.RowCount() > 0)
                pagetemplate = _result.ItemAsString(0, PT_PROPIDS.PT_PROPID_NAME);
            
            wstab.addEntry(name, description, oid, ownerID, adminPath, portlets, pagetemplate,
                    createdDate, modifiedDate);
        }
        return wstab;
    }
    
}

