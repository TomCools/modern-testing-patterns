package be.tomcools.advancedtestingpatterns.testcontainers.azure;

import org.testcontainers.containers.GenericContainer;

public class AzureStorageContainer extends GenericContainer<AzureStorageContainer> {

    public static final int DEFAULT_BLOB_PORT = 10000;
    public static final int DEFAULT_QUEUE_PORT = 10001;
    public static final int DEFAULT_TABLE_PORT = 10002;
    private final static String DEFAULT_ACCOUNT_NAME = "devstoreaccount1";
    private final static String DEFAULT_ACCOUNT_KEY = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";
    private String accountName;
    private String accountKey;

    public AzureStorageContainer() {
        super("mcr.microsoft.com/azure-storage/azurite");

        this.accountName = DEFAULT_ACCOUNT_NAME;
        this.accountKey = DEFAULT_ACCOUNT_KEY;

        // make sure all functionality is available from outside the container.
        withCommand("azurite --blobHost 0.0.0.0 --queueHost 0.0.0.0 --tableHost 0.0.0.0");
        //withEnv("AZURITE_ACCOUNTS", "%s:%s".formatted(accountName, accountKey));

        withExposedPorts(DEFAULT_BLOB_PORT, DEFAULT_QUEUE_PORT, DEFAULT_TABLE_PORT);
    }

    public String getConnectionString() {
        // This is part of the Azurite Image.
        return new StringBuilder()
                .append("DefaultEndpointsProtocol=http;AccountName=")
                .append(accountName)
                .append(";AccountKey=")
                .append(accountKey)
                .append(";BlobEndpoint=http://127.0.0.1:")
                .append(getMappedPort(DEFAULT_BLOB_PORT))
                .append("/")
                .append(accountName)
                .append(";QueueEndpoint=http://127.0.0.1:")
                .append(getMappedPort(DEFAULT_QUEUE_PORT))
                .append("/")
                .append(accountName)
                .append(";TableEndpoint=http://127.0.0.1:")
                .append(getMappedPort(DEFAULT_TABLE_PORT))
                .append("/")
                .append(accountName)
                .append(";")
                .toString();
    }

    public AzureStorageContainer withAccountName(String accountName) {
        this.accountName = accountName;
        return self();
    }

    public AzureStorageContainer withAccountKey(String accountKey) {
        this.accountKey = accountKey;
        return self();
    }
}

