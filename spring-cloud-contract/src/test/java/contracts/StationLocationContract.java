package contracts;

import org.springframework.cloud.contract.spec.Contract;

import java.util.function.Supplier;

import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.map;

public class StationLocationContract implements Supplier<Contract> {

    @Override
    public Contract get() {
        return Contract.make(c -> {
            c.priority(3);
            c.name("Get VeloStation details for Station Locations");
            c.request(r -> {
                r.method("GET");
                r.url(r.$(r.consumer(r.regex("\\/velo\\/[0-9]{3}")), r.producer("/velo/001")));
            });
            c.response(r -> {
                r.status(r.OK());
                r.body(map()
                        .entry("lat", r.$(r.consumer("51.21782"), r.producer(r.regex("[0-9]*.[0-9]*"))))
                        .entry("lon",  r.$(r.consumer(r.value(4.42065)), r.producer(r.regex("[0-9]*.[0-9]*")))));
                r.headers(h -> {
                    h.contentType(h.applicationJson());
                });
            });
        });
    }

}
