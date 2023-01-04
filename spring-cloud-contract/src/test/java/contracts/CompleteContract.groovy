package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    priority 1
    name "Get VeloStation details Complete"
    request {
        method GET();
        url $(consumer(~/\/velo\/[0-9]{3}/), producer('/velo/001'))
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
                id: $(
                        consumer(fromRequest().path(1)),
                        producer(~/[0-9]{3}/)
                ),
                slots: $(
                        consumer(13),
                        producer(~/[0-9]*/)
                ),
                bikes: $(
                        consumer("5"),
                        producer(~/[0-9]*/)
                ),
                status: "OPN",
                lat: $(
                        consumer("51.217820000000000000"),
                        producer(~/[0-9]*.[0-9]*/)
                ),
                lon: 4.42065
        )
    }
}
