package ses.seq.logback.filters;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static ses.seq.logback.constants.Constants.*;

/**
 *  Filter that inserts various values retrieved from the incoming http
 *  request into the MDC. The values are removed after the request is processed.
 */
public class MDCWebReqInsertingFilter extends MDCInsertingServletFilter {

    private String appHostName;

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Get host name
        initHostName();
        super.init(arg0);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        addReqInfoToMDC(request);
        super.doFilter(request, response, chain);
        removeReqInfoFromMDC();
    }

    /**
     * Add the request info to the MDC
     * @param request
     */
    private void addReqInfoToMDC(ServletRequest request) {
        MDC.put(APP_HOST_NAME, appHostName);
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            MDC.put(REQUEST_REFERER, httpServletRequest.getHeader("Referer"));
            MDC.put(REQUEST_AUTH_TOKEN, httpServletRequest.getHeader("Authorization"));
        }
    }

    /**
     * Removes the added request info from the mdc
     */
    private void removeReqInfoFromMDC() {
        MDC.remove(REQUEST_REFERER);
        MDC.remove(REQUEST_AUTH_TOKEN);
    }

    /**
     * Initializes the hostname string once on init
     */
    private void initHostName() {
        try {
            this.appHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
