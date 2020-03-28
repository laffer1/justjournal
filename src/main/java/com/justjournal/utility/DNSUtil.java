package com.justjournal.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Lucas Holt
 */
public class DNSUtil {

    private DNSUtil() {
        super();
    }

    public static String getDomainFromEmail(final String address) {
        final int at = address.lastIndexOf('@');
        if (address.length() == at)
            return null;
        
        return address.substring(at + 1);
    }

    public static boolean isEmailDomainValid(final String address) {
        if (address == null)
            return false;

        return isDomainValid(getDomainFromEmail(address));
    }

    public static boolean isDomainValid(final String domainName) {
        if (domainName == null)
            return false;

        try {
            final InetAddress inetAddress = InetAddress.getByName(domainName);
            return inetAddress != null;
        } catch (UnknownHostException ignored) {
            return false;
        }
    }
}
