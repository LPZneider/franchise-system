package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.entrypoint.rest.router.FranchiseRouter;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;

@WebFluxTest(controllers = FranchiseHandler.class)
@Import({FranchiseRouter.class})
class FranchiseHandlerTest {

}

