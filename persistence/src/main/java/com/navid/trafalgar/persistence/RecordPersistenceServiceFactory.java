package com.navid.trafalgar.persistence;

import com.navid.trafalgar.persistence.localfile.FileRecordPersistenceService;

/**
 *
 * @author anf
 */
public class RecordPersistenceServiceFactory {
    
    public enum Type { LOCAL, ONLINE };
    
    static public RecordPersistenceService getFactory(Type type){
        switch (type){
        
            case ONLINE:
                throw new UnsupportedOperationException("Not supported yet.");
                
            case LOCAL:
            default:
                return new FileRecordPersistenceService();
        
        }
    }
    
}
