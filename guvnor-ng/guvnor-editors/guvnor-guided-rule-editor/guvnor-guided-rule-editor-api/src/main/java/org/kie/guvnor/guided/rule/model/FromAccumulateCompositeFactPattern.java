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

package org.kie.guvnor.guided.rule.model;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.kie.guvnor.datamodel.model.IPattern;

@Portable
public class FromAccumulateCompositeFactPattern extends FromCompositeFactPattern {

    public static final String USE_FUNCTION = "use_function";
    public static final String USE_CODE     = "use_code";

    private IPattern sourcePattern;
    private String   initCode;
    private String   actionCode;
    private String   reverseCode;
    private String   resultCode;

    private String function;

    public FromAccumulateCompositeFactPattern() {
    }

    public String useFunctionOrCode() {
        if ( this.initCode != null && !this.initCode.trim().equals( "" ) ) {
            //if the initCode is set, we must use it.
            return FromAccumulateCompositeFactPattern.USE_CODE;
        }

        //otherwise use Function. (this is the default)
        return FromAccumulateCompositeFactPattern.USE_FUNCTION;
    }

    public void clearCodeFields() {
        this.initCode = null;
        this.actionCode = null;
        this.reverseCode = null;
        this.resultCode = null;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode( String actionCode ) {
        this.actionCode = actionCode;
    }

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode( String initCode ) {
        this.initCode = initCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode( String resultCode ) {
        this.resultCode = resultCode;
    }

    public String getReverseCode() {
        return reverseCode;
    }

    public void setReverseCode( String reverseCode ) {
        this.reverseCode = reverseCode;
    }

    public IPattern getSourcePattern() {
        return sourcePattern;
    }

    public void setSourcePattern( IPattern sourcePattern ) {
        this.sourcePattern = sourcePattern;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction( String function ) {
        this.function = function;
    }

}
