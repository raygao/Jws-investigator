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
public class WSInfoMgr implements InfoMgr {
    
    /** Creates a new instance of WSInfoMgr */
    public WSInfoMgr() {
    }
    
    private IPTSession ptSession;
    
    public WSInfoMgr(IPTSession _ptSession) {
        this.ptSession = _ptSession;
    }
    
    public WSDataTable getAllWebServicesInfo() {
        //return all webservices, regards of the enabled/disabled status
        return this.getAllWebServicesInfo(false);
    }
    
    public WSDataTable getAllWebServicesInfo(boolean status) {
        IPTObjectManager webserviceMgr;
        
        //get a webservices Object Manager.
        webserviceMgr = ptSession.GetWebServices();
        
        //build filter as required
        Object[][] objQueryFilter = new Object[3][];
        for (int i = 0; i < 3; i++) {
            objQueryFilter[i] = new Object[1];
        }
        
        //Build the query filters
        if (status) {
            //search for Status of the Web Services, enabled or disabled.
            objQueryFilter[0][0] = new Integer(PT_PROPIDS.PT_PROPID_WEBSERVICE_ENABLED);
            objQueryFilter[1][0] = new Integer(PT_FILTEROPS.PT_FILTEROP_EQ);
            //Enabled WS has value of 1, and Disabled WS has value 0;
            objQueryFilter[2][0] = new Integer(1); //true
        } else {
            //search against a specific String in the WS name. In the following case, all ws.
            objQueryFilter[0][0] = new Integer(PT_PROPIDS.PT_PROPID_NAME);
            objQueryFilter[1][0] = new Integer(PT_FILTEROPS.PT_FILTEROP_CONTAINS);
            objQueryFilter[2][0] = "*";
        }
        //do query
        IPTQueryResult results =
                webserviceMgr.Query(PT_PROPIDS.PT_PROPID_OBJECTID | PT_PROPIDS.PT_PROPID_NAME, -1, PT_PROPIDS.PT_PROPID_OBJECTID, 0, -1, objQueryFilter);
        
        WSDataTable wstab = new WSDataTable();
        wstab.createDataSource();
        
        for (int i = 0; i < results.RowCount(); i++) {
            int objectId = results.ItemAsInt(i, PT_PROPIDS.PT_PROPID_OBJECTID);
            IPTWebService webservice = (IPTWebService)webserviceMgr.Open(objectId, false);
            
            String name = webservice.GetName() != null ? webservice.GetName() : "";
            Integer remoteServerID = new Integer(webservice.GetRemoteServerID());
            
            String description = webservice.GetDescription() != "" ? webservice.GetDescription() : "" ;
            //This solves the problem with return in the WS description box.
            // and carriage return (CR) -> \r
            // and line feed (LF)  -> \n
            if ((description != null) && !(description.equals("")))
                description = description.replaceAll("\r\n", " --linefeed-- ");
            
            Boolean state = new Boolean(webservice.GetEnabled());
            Integer adminFolderID = new Integer(webservice.GetAdminFolderID());
            String adminPath = (new MiscUtil().GetAdminFolderPath(ptSession, adminFolderID.intValue()));
            XPDateTime createdDate = webservice.GetCreated();
            XPDateTime modifiedDate = webservice.GetLastModified();
            Integer oid = new Integer(webservice.GetObjectID());
            Integer ownerID = new Integer(webservice.GetOwnerID());
            String providerCLSID = webservice.GetProviderCLSID() != null ? webservice.GetProviderCLSID() : "none";
            
            IXPPropertyBag ixp = webservice.GetProviderInfo();
            
            // get all the available property bag objects for this class. used for test code.
            IXPEnumerator iee = ixp.GetEnumerator();
            String propertybag_keys = "";
            while (iee.MoveNext() != false)
                propertybag_keys += iee.GetCurrent() + ", ";
            // com.plumtree.server.PlumtreeExtensibility.PT_PROPBAG_HTTPGADGET_URL = "PTC_HTTPGADGET_URL"
            // see DotNet version documentation.
            
            //get relative url
            String relativeURL = ixp.ReadAsString(PlumtreeExtensibility.PT_PROPBAG_HTTPGADGET_URL);
            //create full url
            String fullURL = getRemoteServerURL(remoteServerID.intValue()) + relativeURL;
            
            wstab.addEntry(name, description, state, oid, ownerID, adminPath, remoteServerID,
                    relativeURL, fullURL, providerCLSID, createdDate, modifiedDate, propertybag_keys);
        }
        return wstab;
    }
    
    public String getRemoteServerURL(int _serverOID) {
        String baseurl = "";
        if (_serverOID != 0) {
            //IPTGadgetContentServer is now called as the portlet Remote server.
            IPTObjectManager iserverMgr = ptSession.GetGadgetContentServers();
            IPTGadgetContentServer iserver = (IPTGadgetContentServer)iserverMgr.Open(_serverOID, false);
            baseurl = iserver.GetURL();
        } else
            baseurl = "Intrinsic Web Service";
        return baseurl;
    }
    
}

