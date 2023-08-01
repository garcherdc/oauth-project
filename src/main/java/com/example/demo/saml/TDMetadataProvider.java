package com.example.demo.saml;

import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;

import java.nio.charset.StandardCharsets;


public class TDMetadataProvider extends AbstractReloadingMetadataProvider {
    private String partnerCode;  // unique Id for DB lookups
    private String metadata ;
    /**
     * Constructor.
     * @param partnerCode the entity Id of the metadata.  Use as key to identify a database row.
     */
    public TDMetadataProvider(String partnerCode, String metadata) {
        this.partnerCode = partnerCode;
        this.metadata = metadata;
    }

    @Override
    protected String getMetadataIdentifier() {
        return partnerCode;
    }

    @Override
    protected byte[] fetchMetadata() {
        return metadata.getBytes(StandardCharsets.UTF_8);
    }
}
