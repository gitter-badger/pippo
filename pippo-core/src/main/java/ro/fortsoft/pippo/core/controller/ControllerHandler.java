/*
 * Copyright (C) 2014 the original author or authors.
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
package ro.fortsoft.pippo.core.controller;

import ro.fortsoft.pippo.core.route.RouteHandler;

import java.lang.reflect.Method;

/**
 * Interface that declares a handler for controller methods.
 *
 * @author James Moger
 */
public interface ControllerHandler extends RouteHandler {

    public Class<? extends Controller> getControllerClass();

    public String getMethodName();

    public Method getMethod();

}
