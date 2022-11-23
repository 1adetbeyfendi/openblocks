package com.openblocks.api.query;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.openblocks.api.home.SessionUserService;
import com.openblocks.api.query.view.QueryExecutionRequest;
import com.openblocks.api.query.view.QueryResultView;
import com.openblocks.api.util.BusinessEventPublisher;
import com.openblocks.infra.constant.NewUrl;
import com.openblocks.infra.constant.Url;
import com.openblocks.sdk.exception.BizError;
import com.openblocks.sdk.exception.BizException;
import com.openblocks.sdk.models.QueryExecutionResult;
import com.openblocks.sdk.util.CookieHelper;
import com.openblocks.sdk.util.LocaleUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = {Url.QUERY_URL, NewUrl.QUERY_URL})
public class QueryController {

    @Autowired
    private ApplicationQueryApiService applicationQueryApiService;

    @Autowired
    private LibraryQueryApiService libraryQueryApiService;

    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private SessionUserService sessionUserService;
    @Autowired
    private BusinessEventPublisher businessEventPublisher;

    @PostMapping("/execute")
    public Mono<QueryResultView> executeAction(ServerWebExchange exchange,
            @RequestBody QueryExecutionRequest queryExecutionRequest) {
        return Mono.deferContextual(contextView -> {
            Locale locale = LocaleUtils.getLocale(contextView);
            return getQueryResult(exchange, queryExecutionRequest)
                    .map(result -> new QueryResultView(result, locale))
                    .onErrorResume(throwable -> {
                        if (throwable instanceof BizException bizException && bizException.getError() == BizError.LOGIN_EXPIRED) {
                            String cookieToken = cookieHelper.getCookieToken(exchange);
                            return sessionUserService.removeUserSession(cookieToken)
                                    .then(businessEventPublisher.publishUserLogoutEvent())
                                    .then(Mono.error(throwable));
                        }
                        return Mono.error(throwable);
                    });
        });
    }

    private Mono<QueryExecutionResult> getQueryResult(ServerWebExchange exchange, QueryExecutionRequest queryExecutionRequest) {
        if (queryExecutionRequest.isApplicationQueryRequest()) {
            return applicationQueryApiService.executeApplicationQuery(exchange, queryExecutionRequest);
        }
        return libraryQueryApiService.executeLibraryQuery(exchange, queryExecutionRequest);
    }


}
