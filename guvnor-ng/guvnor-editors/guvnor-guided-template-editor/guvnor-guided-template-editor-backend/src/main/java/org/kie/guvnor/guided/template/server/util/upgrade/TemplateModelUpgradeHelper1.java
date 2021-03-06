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
package org.kie.guvnor.guided.template.server.util.upgrade;

import org.kie.guvnor.guided.rule.backend.server.util.upgrade.IUpgradeHelper;
import org.kie.guvnor.guided.template.model.InterpolationVariable;
import org.kie.guvnor.guided.template.model.TemplateModel;

/**
 * Utility class to support upgrades of the TemplateModel model
 */
public class TemplateModelUpgradeHelper1
        implements
        IUpgradeHelper<TemplateModel, TemplateModel> {

    public TemplateModel upgrade( TemplateModel model ) {
        updateInterpolationVariableFieldTypes( model );
        return model;
    }

    private void updateInterpolationVariableFieldTypes( TemplateModel model ) {
        //Fields' data-types may be qualified with Fact type. This can be removed
        for ( InterpolationVariable variable : model.getInterpolationVariablesList() ) {
            variable.setFactField( variable.getFactField() );
        }
    }

}
