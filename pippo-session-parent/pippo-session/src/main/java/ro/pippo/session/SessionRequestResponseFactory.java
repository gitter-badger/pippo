/*
 * Copyright (C) 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.pippo.session;

import ro.pippo.core.Application;
import ro.pippo.core.Request;
import ro.pippo.core.RequestResponseFactory;
import ro.pippo.core.Response;
import ro.pippo.core.ResponseFinalizeListener;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Decebal Suiu
 */
public class SessionRequestResponseFactory extends RequestResponseFactory {

    private final SessionManager sessionManager;

    public SessionRequestResponseFactory(Application application, SessionManager sessionManager) {
        super(application);

        this.sessionManager = sessionManager;
    }

    @Override
    public Request createRequest(HttpServletRequest httpServletRequest, Response response) {
        final SessionHttpServletRequest sessionHttpServletRequest = new SessionHttpServletRequest(httpServletRequest, sessionManager);
        response.getFinalizeListeners().add(new ResponseFinalizeListener() {

            @Override
            public void onFinalize(Response response) {
                sessionHttpServletRequest.commitSession(response.getHttpServletResponse());
            }

        });

        return super.createRequest(sessionHttpServletRequest, response);
    }

}
