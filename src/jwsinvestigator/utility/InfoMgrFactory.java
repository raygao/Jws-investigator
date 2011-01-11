/*
 * InfoMgrFactory.java
 *
 * Created on August 31, 2007, 6:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jwsinvestigator.utility;

import com.plumtree.server.*;
import jwsinvestigator.*;

/**
 *
 * @author Raymond Gao
 */
public class InfoMgrFactory {
    
    //private constructor, Factory pattern
    private InfoMgrFactory() {
    }
    
    public static InfoMgr getMgr(String type, IPTSession iptsession) {
        if (type.equals(Constants.WEBSERVICES))  {
            return (InfoMgr) (new WSInfoMgr(iptsession));
        } else if (type.equals(Constants.PORTLET))  {
            return (InfoMgr) (new PortletInfoMgr(iptsession));
        } else if (type.equals(Constants.COMMUNITY)) {
            return (InfoMgr) (new CommunityInfoMgr(iptsession));
        } else if (type.equals(Constants.COMMUNITYPAGE)) {
            return (InfoMgr) (new CommunityPageInfoMgr(iptsession));
        }
        
        //default to at least return a webservices manager.
        else {
            return (InfoMgr)(new WSInfoMgr(iptsession));
        }
        
        
    }
    
}
