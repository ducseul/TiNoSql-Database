/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.constant;

/**
 *
 * @author Ducnm62
 */
public class CONSTANT {
    public interface MESSAGE{
        String COMMON_OK = "Ok";

        String PING_RESPONSE="Half cycle take %s ms. Full cycle take %s ms";

        String INVALIDARG_KEYWORD = "Invalid sentence, collection\'s name can\'t be keywords";
        String INVALIDARG_NO= "Invalid sentence, not specify collection";
        
        String ADD_INVALID = "Key %s in collection %s already exist.";
        
        String NO_DATA = "No data";
        String NO_COLLECTION = "No collection name %s";
        String QUERY_FAIL = "Query fail";
        
        String SUCCESS = "Query success";
        String CREATE_SUCCESS = "Collection %s created";
        String INSERT_DONE = "Insert (%s,%s) into %s success";
    }
}
