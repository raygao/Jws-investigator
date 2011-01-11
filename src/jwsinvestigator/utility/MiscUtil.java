/*
 * MiscUtil.java
 *
 * Created on August 31, 2007, 4:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jwsinvestigator.utility;

/**
 *
 * @author Raymond Gao
 */

import com.plumtree.server.search.*;
import com.plumtree.server.*;

public class MiscUtil {
    public String GetAdminFolderPath(IPTSession ptSession, int adminFolderID) {
        return  ptSession.GetAdminCatalog().OpenAdminFolder(adminFolderID, false).GetPath();
    }
}

