package contracts;

import org.springframework.cloud.contract.spec.Contract;

import java.util.function.Supplier;

import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.map;

public class BikeCounterContract implements Supplier<Contract> {

    @Override
    public Contract get() {
        return Contract.make(c -> {
            c.priority(5);
            c.name("Get VeloStation details for Bike Counter");
            c.request(r -> {
                r.method("GET");
                r.url(r.$(r.consumer(r.regex("\\/velo\\/[0-9]{3}")), r.producer("/velo/001")));
            });
            c.response(r -> {
                r.status(r.OK());
                r.body(map()
                        .entry("slots", r.$(r.consumer("13"), r.producer(r.regex("[0-9]*"))))
                        .entry("bikes",  r.$(r.consumer("5"), r.producer(r.regex("[0-9]*")))));
                r.headers(h -> {
                    h.contentType(h.applicationJson());
                });
            });
        });
    }

}
