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
public class CommunityInfoMgr implements InfoMgr {
    private IPTSession ptSession;
    
    public CommunityInfoMgr(IPTSession _ptSession) {
        this.ptSession = _ptSession;
    }
    
    
    public CommunityDataTable getAllCommunityInfo() {
        //return all communitys, regards of the enabled/disabled status
        return this.getAllCommunityInfo(false);
    }
    
    public CommunityDataTable getAllCommunityInfo(boolean status) {
        IPTObjectManager communityMgr;
        
        //get a communitys Object Manager.
        communityMgr = ptSession.GetCommunities();
        
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
                communityMgr.Query(PT_PROPIDS.PT_PROPID_OBJECTID | PT_PROPIDS.PT_PROPID_NAME, -1, PT_PROPIDS.PT_PROPID_OBJECTID, 0, -1, objQueryFilter);
        
        CommunityDataTable wstab = new CommunityDataTable();
        wstab.createDataSource();
        
        for (int i = 0; i < results.RowCount(); i++) {
            int objectId = results.ItemAsInt(i, PT_PROPIDS.PT_PROPID_OBJECTID);
            IPTCommunity community = (IPTCommunity)communityMgr.Open(objectId, false);
            
            String name = community.GetName() != null ? i + ", " + community.GetName() : ""; //with row number
            //String name = community.GetName() != null ? community.GetName() : "";  //without row number
            String description = community.GetDescription() != "" ? community.GetDescription() : "";
            //This solves the problem with return in the WS description box.
            // and carriage return (CR) -> \r
            // and line feed (LF)  -> \n
            if ((description != null) && !(description.equals("")))
                description = description.replaceAll("\r\n", " --linefeed-- ");
            
            Integer adminFolderID = new Integer(community.GetAdminFolderID());
            String adminPath = (new MiscUtil().GetAdminFolderPath(ptSession, adminFolderID.intValue()));
            XPDateTime createdDate = community.GetCreated();
            XPDateTime modifiedDate = community.GetLastModified();
            Integer oid = new Integer(community.GetObjectID());
            Integer ownerID = new Integer(community.GetOwnerID());
            String footer = this.getPortletName(community.GetFooterID());
            String header = this.getPortletName(community.GetHeaderID());
            
            // Get users in a communityPage
            String users = "none";
            String ulist = "";
            IPTQueryResult _users = community.QueryMembers(PT_CLASSIDS.PT_USER_ID);
            if (_users.RowCount() > 0) {
                for (int j = 0; j < _users.RowCount(); j++) {
                    ulist += _users.ItemAsString(j, PT_PROPIDS.PT_PROPID_NAME) + ", ";
                }
                users = ulist;
            }
            
            // Get groups in a communityPage
            String groups = "";
            String glist = "";
            IPTQueryResult _groups = community.QueryMembers(PT_CLASSIDS.PT_USERGROUP_ID);
            if (_groups.RowCount() > 0) {
                for (int j = 0; j < _groups.RowCount(); j++) {
                    glist += _groups.ItemAsString(j, PT_PROPIDS.PT_PROPID_NAME) + ", ";
                }
                groups = glist;
            }
            
            // Get pages in a communityPage
            String pages = "";
            String plist = "";
            IPTQueryResult _pages = community.QueryPages(0);
            if (_pages.RowCount() > 0) {
                for (int j = 0; j < _pages.RowCount(); j++) {
                    plist += _pages.ItemAsString(j, PT_PROPIDS.PT_PROPID_NAME) + ", ";
                }
                pages = plist;
            }
            
            // Get subcommunities
            String subcommunities = "";
            String subcommunitiesList = "";
            IPTQueryResult _subcommunities = community.QuerySubcommunities();
            if (_subcommunities.RowCount() > 0) {
                for (int j = 0; j < _subcommunities.RowCount(); j++) {
                    subcommunitiesList += _subcommunities.ItemAsString(j, PT_PROPIDS.PT_PROPID_NAME) + ", ";
                }
                subcommunities = subcommunitiesList;
            }
            
            wstab.addEntry(name, description, oid, footer, header, ownerID, adminPath, users,
                    pages, groups, subcommunities, createdDate, modifiedDate);
        }
        return wstab;
    }
    
    public String getPortletName(int oid) {
        String name = "";
        if (oid != 0) {
            //IPTGadgetContentServer is now called as the portlet Remote server.
            IPTObjectManager iMgr = ptSession.GetGadgets();
            IPTGadget iobj = (IPTGadget)iMgr.Open(oid, false);
            name = iobj.GetName();
        } else
            name = "none";
        return name;
    }
    
    public String getUsers() {
        String users = "";
        
        return users;
    }
    
}

