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
package ro.fortsoft.pippo.demo;

import ro.fortsoft.pippo.core.Application;
import ro.fortsoft.pippo.core.HttpConstants;
import ro.fortsoft.pippo.core.Request;
import ro.fortsoft.pippo.core.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James Moger
 */
public class CustomRequest extends Request {

    protected CustomRequest(HttpServletRequest httpServletRequest, Application application) {
        super(httpServletRequest, application);
    }

}
