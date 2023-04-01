package be.tomcools.moderntestingpatterns.contract.base;

import be.tomcools.moderntestingpatterns.app.ModernTestingPatternsApplication;
import be.tomcools.moderntestingpatterns.app.velo.VeloDataSync;
import be.tomcools.moderntestingpatterns.app.velo.VeloResource;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ModernTestingPatternsApplication.class)
public abstract class ContractVerifierBase {

    @Autowired
    VeloResource veloResource;

    @Autowired
    VeloDataSync sync;

    @BeforeEach
    public void before() {
        // Force sync before test start
        sync.runSyncDirect();

        RestAssuredMockMvc.standaloneSetup(this.veloResource);
    }
}
