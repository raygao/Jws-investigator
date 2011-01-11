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
public class PortletInfoMgr implements InfoMgr {
    private IPTSession ptSession;
    
    public PortletInfoMgr(IPTSession _ptSession) {
        this.ptSession = _ptSession;
    }
    
    public PortletDataTable getAllportletsInfo() {
        //return all portlets, regards of the enabled/disabled status
        return this.getAllportletsInfo(false);
    }
    
    public PortletDataTable getAllportletsInfo(boolean status) {
        IPTObjectManager portletMgr;
        
        //get a portlets Object Manager.
        portletMgr = ptSession.GetGadgets();
        
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
            objQueryFilter[0][0] = new Integer(PT_PROPIDS.PT_PROPID_NAME);
            objQueryFilter[1][0] = new Integer(PT_FILTEROPS.PT_FILTEROP_CONTAINS);
            objQueryFilter[2][0] = "*";
        }
        //do query
        IPTQueryResult results =
                portletMgr.Query(PT_PROPIDS.PT_PROPID_OBJECTID | PT_PROPIDS.PT_PROPID_NAME, -1, PT_PROPIDS.PT_PROPID_OBJECTID, 0, -1, objQueryFilter);
        
        PortletDataTable portletDataTable = new PortletDataTable();
        portletDataTable.createDataSource();
        
        for (int i = 0; i < results.RowCount(); i++) {
            int objectId = results.ItemAsInt(i, PT_PROPIDS.PT_PROPID_OBJECTID);
            IPTGadget portlet = (IPTGadget) portletMgr.Open(objectId, false);
            
            String name = portlet.GetName() != null ? portlet.GetName() : "";
            int template = portlet.GetGadgetTemplateID();
            
            String description = portlet.GetDescription() != "" ? portlet.GetDescription() : "" ;
            //This solves the problem with return in the WS description box.
            // and carriage return (CR) -> \r
            // and line feed (LF)  -> \n
            if ((description != null) && !(description.equals("")))
                description = description.replaceAll("\r\n", " --linefeed-- ");
            
            Integer adminFolderID =  new Integer(portlet.GetAdminFolderID());
            String adminPath = (new MiscUtil().GetAdminFolderPath(ptSession, adminFolderID.intValue()));
            XPDateTime createdDate = portlet.GetCreated();
            XPDateTime modifiedDate = portlet.GetLastModified();
            Integer oid = new Integer(portlet.GetObjectID());
            Integer ownerID = new Integer (portlet.GetOwnerID());
            Integer webserviceID = new Integer (portlet.GetWebServiceID());
            
            ///portlet alignment means the type of portlet, e.g. narrow, wide, header, footer, canvas, ...
            int _alignment = portlet.GetAlignment();
            String alignment = "";
            switch (_alignment) {
                case 0:
                    alignment = "null";
                    break;
                case 1:
                    alignment = "narrow";
                    break;
                case 2:
                    alignment = "wide";
                    break;
                case 3:
                    alignment = "header";
                    break;
                case 4:
                    alignment = "footer";
                    break;
                case 5:
                    alignment = "canvas";
                    break;
            }
            
            IXPPropertyBag ixp = portlet.GetProviderInfo();
            // get all the available property bag objects for this class. used for test code.
            IXPEnumerator iee = ixp.GetEnumerator();
            String propertybag_keys = "";
            while (iee.MoveNext() != false)
                propertybag_keys += iee.GetCurrent() + ", ";
            
            portletDataTable.addEntry(name, description, alignment, oid, ownerID, adminPath,
                    webserviceID, createdDate, modifiedDate, propertybag_keys);
        }
        return portletDataTable;
    }
    
}

