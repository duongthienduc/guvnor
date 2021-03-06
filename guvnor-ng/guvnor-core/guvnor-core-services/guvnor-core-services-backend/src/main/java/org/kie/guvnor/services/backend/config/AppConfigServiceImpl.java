/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.guvnor.services.backend.config;

import java.util.Date;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.kie.guvnor.services.config.AppConfigService;

@Service
@ApplicationScoped
public class AppConfigServiceImpl implements AppConfigService {

    @Override
    public Map<String, String> loadPreferences() {
        Map<String, String> preferences = ApplicationPreferencesLoader.load();
        ApplicationPreferencesInitializer.setSystemProperties( preferences );
        return preferences;

    }

    @Override
    public long getTimestamp() {
        return new Date().getTime();
    }
}
