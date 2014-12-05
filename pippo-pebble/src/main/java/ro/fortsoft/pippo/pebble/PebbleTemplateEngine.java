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
package ro.fortsoft.pippo.pebble;

import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pippo.core.Languages;
import ro.fortsoft.pippo.core.Messages;
import ro.fortsoft.pippo.core.PippoConstants;
import ro.fortsoft.pippo.core.PippoRuntimeException;
import ro.fortsoft.pippo.core.PippoSettings;
import ro.fortsoft.pippo.core.TemplateEngine;
import ro.fortsoft.pippo.core.util.StringUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.debug.DebugExtension;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

/**
 * Pebble template engine for Pippo.
 *
 * @author James Moger
 */
public class PebbleTemplateEngine implements TemplateEngine {

    public static final String PEBBLE = "peb";

    public static final String FILE_SUFFIX = "." + PEBBLE;

    private final Logger log = LoggerFactory.getLogger(PebbleTemplateEngine.class);

    private Languages languages;

    private PebbleEngine engine;

    @Override
    public void init(PippoSettings pippoSettings, Languages languages, Messages messages) {
        this.languages = languages;

        String pathPrefix = pippoSettings.getString(PippoConstants.SETTING_TEMPLATE_PATH_PREFIX, defaultPathPrefix);

        List<Loader> loaders = Lists.newArrayList();

        PippoTemplateLoader templateLoader = new PippoTemplateLoader();
        templateLoader.setCharset(PippoConstants.UTF8);
        templateLoader.setPrefix(pathPrefix);
        templateLoader.setSuffix(FILE_SUFFIX);
        loaders.add(templateLoader);

        engine = new PebbleEngine(new DelegatingLoader(loaders));
        engine.setStrictVariables(false);
        engine.addExtension(new I18nExtension(messages));
        engine.addExtension(new FormatTimeExtension());
        engine.addExtension(new PrettyTimeExtension());
        engine.addExtension(new AngularJSExtension());

        if (pippoSettings.isDev()) {
            // do not cache templates in dev mode
            engine.setTemplateCache(null);
            engine.addExtension(new DebugExtension());
        }
    }

    @Override
    public void render(String templateName, Map<String, Object> model, Writer writer) {
        String language = (String) model.get(PippoConstants.REQUEST_PARAMETER_LANG);
        if (StringUtils.isNullOrEmpty(language)) {
            language = languages.getLanguageOrDefault(null);
        }
        Locale locale = (Locale) model.get(PippoConstants.REQUEST_PARAMETER_LOCALE);
        if (locale == null) {
            locale = languages.getLocaleOrDefault(language);
        }

        try {
            PebbleTemplate template = null;
            if (locale != null) {
                // try the complete Locale
                template = getTemplate(templateName, locale.toString());
                if (template == null) {
                    // try only the language
                    template = getTemplate(templateName, locale.getLanguage());
                }
            }

            if (template == null) {
                // fallback to the template without any language or locale
                template = engine.getTemplate(templateName);
            }

            template.evaluate(writer, model, locale);
            writer.flush();
        } catch (Exception e) {
            throw new PippoRuntimeException(e);
        }
    }

    private PebbleTemplate getTemplate(String templateName, String localePart) throws PebbleException {
        PebbleTemplate template = null;
        try {
            if (Strings.isNullOrEmpty(localePart)) {
                template = engine.getTemplate(templateName);
            } else {
                String localizedName = StringUtils.removeEnd(templateName, FILE_SUFFIX) + "_" + localePart;
                template = engine.getTemplate(localizedName);
            }
        } catch (LoaderException e) {
        }
        return template;
    }
}