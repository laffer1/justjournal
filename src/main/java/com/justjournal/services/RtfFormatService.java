package com.justjournal.services;

import com.justjournal.core.UserContext;
import com.justjournal.exception.ServiceException;
import com.lowagie.text.Document;
import com.lowagie.text.rtf.RtfWriter2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class RtfFormatService extends AbstractFormatService {
    public ByteArrayOutputStream generate(final UserContext userContext) throws ServiceException {
        final Document document = new Document();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RtfWriter2.getInstance(document, baos);
        format(userContext, document);
        document.close();

        return baos;
    }
}
