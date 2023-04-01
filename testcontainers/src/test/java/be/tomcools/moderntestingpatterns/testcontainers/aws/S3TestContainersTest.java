package be.tomcools.moderntestingpatterns.testcontainers.aws;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
public class S3TestContainersTest {

    @Container
    public static LocalStackContainer LOCALSTACK =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
                    .withServices(S3);

    private static S3ClientBuilder CLIENT_BUILDER;

    @BeforeAll
    public static void init() {
        CLIENT_BUILDER = S3Client
                .builder()
                .endpointOverride(LOCALSTACK.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        LOCALSTACK.getAccessKey(), LOCALSTACK.getSecretKey()
                )))
                .region(Region.of(LOCALSTACK.getRegion()));
    }

    @Test
    public void canSaveAndRetrieveStringFromS3Bucket() throws IOException {
        String bestConference = "JFokus";
        try (S3Client s3 = CLIENT_BUILDER.build()) {
            s3.createBucket(b -> b.bucket("confs"));

            // Insert
            s3.putObject(b -> b.bucket("confs").key("best"), RequestBody.fromString(bestConference));

            // Retrieve
            final ResponseInputStream<GetObjectResponse> response = s3.getObject(b -> b.bucket("confs").key("best"));
            final String retrieved = new String(response.readAllBytes());

            assertThat(retrieved).isEqualTo(bestConference);
        }
    }
}
