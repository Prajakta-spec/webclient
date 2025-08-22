/*
 * Copyright (C) 2020, Liberty Mutual Group
 *
 * Created on 4/2/20, 11:15 AM
 */

package com.lmig.globalspecialty.surety.grsindividualdocmgmtapi.metadata.store;

import com.lmig.globalspecialty.surety.grsindividualdocmgmtapi.common.BaseLoaIntegrationTest;
import com.lmig.globalspecialty.surety.grsindividualdocmgmtapi.common.constants.Constants;
import com.lmig.globalspecialty.surety.grsindividualdocmgmtapi.loa.metadata.entity.error.LoaDocumentMgmtErrorResponse;
import com.lmig.globalspecialty.surety.grsindividualdocmgmtapi.loa.metadata.entity.store.LoaDocumentStoreResponse;
import java.util.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({Constants.INTEGRATION_TEST_ENVIRONMENT})
public class LoaStoreContentControllerIT extends BaseLoaIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${server.port}")
    private int definedServerPort;

    @Value("classpath:local-documents/file-example_PDF_1MB.pdf")
    private Resource loaDocumentPdfDocument;

    @Value("classpath:documents/GeoEye_GeoEye1_50cm_8bit_RGB_DRA_Mining_2009FEB14_8bits_sub_r_15.jpg")
    private Resource loaLargeImageDocument;

    @Value("classpath:local-documents/empty-file.txt")
    private Resource loaEmptyDocument;

    @Value("classpath:documents/no-extension-file")
    private Resource fileWithoutExtension;

    private static String baseUrl;

    @Before
    public void setUp() {
        baseUrl = "http://localhost:" + definedServerPort + Constants.LOA_METADATA_BASE_ENDPOINT;
    }

    @Test
    public void validateStorePdfDocument() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1",
                                false, null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateStoreLargeImageDocument() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaLargeImageDocument,
                                Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_2), Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_5,
                                Constants.LOCAL_USER_ID_1822, "2014-12-31", null, true,
                                null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateFileParameterMissing() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(null,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateCategoryIdMissing() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                null, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateSubCategoryIdMissing() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, null,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateWbUserIdMissing() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocumentsWithNoWbUserId(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                "2017-09-16", "Sample note -1",
                                false, null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateHeaderParameterWbUserIdNull() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                null, "2017-09-16", "Sample note -1",
                                false, null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateWbUserIdNullString() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocumentsWithWbUserIdAsString(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_3),
                                "\"null\"", "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateWbUserIdEmptyString() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocumentsWithWbUserIdAsString(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_3),
                                "\"  \"", "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateWbUserIdValueEmpty() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocumentsWithWbUserIdAsString(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                "", "2017-09-16", "Sample note -1",
                                false, null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateWbUserIdNotFoundInRedis() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_3),
                                Constants.LOCAL_USER_ID_11111_NOT_FOUND_IN_REDIS, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateReceivedDateMissing() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, null, null, null,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateEmptyFile() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaEmptyDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", null, null,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateCategoryIdNegative() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_NEGATIVE, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateCategoryIdZero() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_ZERO, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateCategoryIdMismatch() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_3), Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateSubCategoryIdNegative() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_NEGATIVE,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateSubCategoryIdZero() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_ZERO,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateSubCategoryIdMismatch() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_4),
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateWbUserIdZero() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_ZERO, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateWbUserIdNegative() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_NEGATIVE, "2017-09-16", "Sample note -1", false,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateReceivedDateInvalid() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "12/31/2008", null, null,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateFileWithoutExtension() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(fileWithoutExtension,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", null, null,
                                null, null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateStorePdfDocumentWithValidDocumentResidency() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                "CAN", null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateStorePdfDocumentWithNotSupportedDocumentResidency() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                "UAE", null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateStorePdfDocumentWithInvalidDocumentResidencyLongISOCode() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                "A123", null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }

    @Test
    public void validateStorePdfDocumentWithInvalidDocumentResidency() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                "ABC", null, null, null),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());
    }



    @Test
    public void validateCommissionStatementsSubCategoryWithRightAttributes() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_5, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_11,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1",
                                false, null, "2017-09-16", "123456", true),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);
    }

    @Test
    public void validateStoreDocumentWithNotSupportedAttributeAndSubCategoryCombination() {
        ResponseEntity<LoaDocumentMgmtErrorResponse> loaDocumentMgmtErrorResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1", false,
                                "USA", "2017-09-16", "123456", true),
                        LoaDocumentMgmtErrorResponse.class);

        Assert.assertNotNull(loaDocumentMgmtErrorResponse);
        Assert.assertNotNull(loaDocumentMgmtErrorResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, loaDocumentMgmtErrorResponse.getStatusCode());

    }

    @Test
    public void validateCommissionStatementDocumentsStoredInBillingRepository() {

        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_5, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_11,
                                Constants.LOCAL_USER_ID_2053, "2019-12-23", "Sample note - 4", false, null, "2019-12-23", "23467", true),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);

        String uniqueDocId = loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId();

        Base64.Decoder decoder = Base64.getDecoder();
        String decodedUniqueDocId = new String(decoder.decode(uniqueDocId));
        String[] decodedValues = decodedUniqueDocId.split("[/]");
        String repoName = decodedValues[1];
        Assert.assertEquals("surety-agency-bond-document", repoName);
    }

    @Test
    public void validateLoaInvoiceDocumentsStoredInBillingRepository() {

        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Integer.valueOf(Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_4), Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_10,
                                Constants.LOCAL_USER_ID_2053, "2019-12-23", "Sample note - 4", false, null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);

        String uniqueDocId = loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId();

        Base64.Decoder decoder = Base64.getDecoder();
        String decodedUniqueDocId = new String(decoder.decode(uniqueDocId));
        String[] decodedValues = decodedUniqueDocId.split("[/]");
        String repoName = decodedValues[1];
        Assert.assertEquals("surety-agency-bond-document", repoName);
    }

    @Test
    public void validateNonBillingDocumentsStoredInLoaRepository() {
        ResponseEntity<LoaDocumentStoreResponse> loaDocumentStoreResponse =
                testRestTemplate.exchange(baseUrl + "335414617/documents", HttpMethod.POST,
                        prepareMetadataForDocuments(loaDocumentPdfDocument,
                                Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1, Constants.LOCAL_CATEGORY_SUB_CATEGORY_ID_1,
                                Constants.LOCAL_USER_ID_2053, "2017-09-16", "Sample note -1",
                                false, null, null, null, null),
                        LoaDocumentStoreResponse.class);

        Assert.assertNotNull(loaDocumentStoreResponse);
        Assert.assertNotNull(loaDocumentStoreResponse.getBody());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore());
        Assert.assertNotNull(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId());
        Assert.assertTrue(loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId().length() > 1);

        String uniqueDocId = loaDocumentStoreResponse.getBody().getData().getDocumentStore().getUniqueDocId();

        Base64.Decoder decoder = Base64.getDecoder();
        String decodedUniqueDocId = new String(decoder.decode(uniqueDocId));
        String[] decodedValues = decodedUniqueDocId.split("[/]");
        String repoName = decodedValues[1];
        Assert.assertEquals("surety-agency-bond-document", repoName);
    }
}
