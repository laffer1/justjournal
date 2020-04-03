package com.justjournal.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author Lucas Holt
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {
    private static final String[] IP_HEADER_NAMES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getRemoteIP() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();

            String ip = Arrays.asList(IP_HEADER_NAMES)
                    .stream()
                    .map(request::getHeader)
                    .filter(h -> h != null && h.length() != 0 && !"unknown".equalsIgnoreCase(h))
                    .map(h -> h.split(",")[0])
                    .reduce("", (h1, h2) -> h1 + ":" + h2);
            return ip + request.getRemoteAddr();
        } catch (final Exception e) {
            log.error("Could not obtain ip address", e);
            return "0.0.0.0";
        }
    }
}
