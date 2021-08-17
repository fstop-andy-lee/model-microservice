package tw.com.firstbank.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Exception class for business logic check.
 * Optional error message and business specific data can be attached to the exception object.
 * Attached data is a LinkedHashMap object and therefore will preserve it's input order for further usage.
 * 
 * <pre>
 * Best Practices for Exception Handling
 * 1. When deciding on checked exceptions vs. unchecked exceptions, ask yourself, 
 *    "What action can the client code take when the exception occurs?"
 *    Client code cannot do anything  Make it an unchecked exception
 *    Client code will take some useful recovery action based on information in exception Make it a checked exception
 * 2. Preserve encapsulation.
 *    Never let implementation-specific checked exceptions escalate to the higher layers.
 * 3. Try not to create new custom exceptions if they do not have useful information for client code.
 *    Do not forget that Java Exception classes are like other classes, wherein you can add methods that 
 *    you think the client code will invoke to get more information.
 * 4. Document exceptions.
 * 
 * Best Practices for Using Exceptions
 * 1. Always clean up after yourself
 * 2. Never use exceptions for flow control
 * 3. Do not suppress or ignore exceptions
 * 4. Do not catch top-level exceptions
 * 5. Log exceptions just once
 *
 * </pre>
 *
 * @since 1.0.1
 */
public class ParseException extends Exception
{

    private static final long serialVersionUID = 8648800222940411541L;

    Map<String, Object> attachData;
    
    public ParseException()
    {
        attachData = new LinkedHashMap<String, Object>();
    }

    public ParseException(String msg)
    {
        super(msg);
        attachData = new LinkedHashMap<String, Object>();
    }
    
    public Map<String, Object> getAttachData()
    {
        return attachData;
    }

    public void setAttachData(Map<String, Object> attachData)
    {
        this.attachData = attachData;
    }
    
    public Object get(String key)
    {
        return attachData.get(key);
    }
    
    public void set(String key, Object value)
    {
        attachData.put(key, value);
    }
    
}
