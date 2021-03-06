package org.kie.guvnor.datamodel.backend.server;

import org.kie.guvnor.datamodel.model.ModelField;

/**
 * Simple builder for Fact Types
 */
public class SimpleFactBuilder extends BaseFactBuilder {

    public SimpleFactBuilder( final DataModelBuilder builder,
                              final String factType ) {
        this( builder,
              factType,
              false );
    }

    public SimpleFactBuilder( final DataModelBuilder builder,
                              final String factType,
                              final boolean isEvent ) {
        super( builder,
               factType,
               isEvent );
    }

    public SimpleFactBuilder addField( final ModelField field ) {
        super.addField( field );
        return this;
    }

}
