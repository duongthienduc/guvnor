/*
 * Copyright 2012 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kie.guvnor.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.kie.guvnor.commons.ui.client.configurations.ApplicationPreferences;
import org.kie.guvnor.services.config.AppConfigService;
import org.uberfire.client.mvp.AbstractWorkbenchPerspectiveActivity;
import org.uberfire.client.mvp.ActivityManager;
import org.uberfire.client.mvp.Command;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.widgets.menu.MenuBar;
import org.uberfire.client.workbench.widgets.menu.MenuItemCommand;
import org.uberfire.client.workbench.widgets.menu.MenuItemSubMenu;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBarPresenter;
import org.uberfire.client.workbench.widgets.menu.impl.DefaultMenuBar;
import org.uberfire.client.workbench.widgets.menu.impl.DefaultMenuItemCommand;
import org.uberfire.client.workbench.widgets.menu.impl.DefaultMenuItemSubMenu;
import org.uberfire.shared.mvp.impl.DefaultPlaceRequest;

/**
 * GWT's Entry-point for Guvnor-Showcase
 */
@EntryPoint
public class ShowcaseEntryPoint {

    @Inject
    private Caller<AppConfigService> appConfigService;

    @Inject
    private WorkbenchMenuBarPresenter menubar;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private IOCBeanManager iocManager;

    @Inject
    private ActivityManager activityManager;

    @AfterInitialization
    public void startApp() {
        loadPreferences();
        setupMenu();
        hideLoadingPopup();
    }

    private void loadPreferences() {
        appConfigService.call( new RemoteCallback<Map<String, String>>() {
            @Override
            public void callback( final Map<String, String> response ) {
                ApplicationPreferences.setUp( response );
            }
        } ).loadPreferences();
    }

    private void setupMenu() {

        //Home
        final AbstractWorkbenchPerspectiveActivity defaultPerspective = getDefaultPerspectiveActivity();
        if ( defaultPerspective != null ) {
            menubar.addWorkbenchItem( new DefaultMenuItemCommand( "Home",
                                                                  new Command() {
                                                                      @Override
                                                                      public void execute() {
                                                                          placeManager.goTo( new DefaultPlaceRequest( defaultPerspective.getIdentifier() ) );
                                                                      }
                                                                  } ) );
        }

        //Perspectives
        final MenuBar perspectivesMenuBar = new DefaultMenuBar();
        final MenuItemSubMenu perspectivesMenu = new DefaultMenuItemSubMenu( "Perspectives",
                                                                             perspectivesMenuBar );
        final List<AbstractWorkbenchPerspectiveActivity> perspectives = getPerspectiveActivities();
        for ( final AbstractWorkbenchPerspectiveActivity perspective : perspectives ) {
            final String name = perspective.getPerspective().getName();
            final Command cmd = new Command() {

                @Override
                public void execute() {
                    placeManager.goTo( new DefaultPlaceRequest( perspective.getIdentifier() ) );
                }

            };
            final MenuItemCommand item = new DefaultMenuItemCommand( name,
                                                                     cmd );
            perspectivesMenuBar.addItem( item );
        }
        menubar.addWorkbenchItem( perspectivesMenu );

        //Static places
        final MenuItemCommand logout = new DefaultMenuItemCommand( "Logout",
                                                                   new Command() {
                                                                       @Override
                                                                       public void execute() {
                                                                           redirect( GWT.getModuleBaseURL() + "uf_logout" );
                                                                       }
                                                                   } );

        menubar.addWorkbenchItem( logout );
    }

    private AbstractWorkbenchPerspectiveActivity getDefaultPerspectiveActivity() {
        AbstractWorkbenchPerspectiveActivity defaultPerspective = null;
        final Collection<IOCBeanDef<AbstractWorkbenchPerspectiveActivity>> perspectives = iocManager.lookupBeans( AbstractWorkbenchPerspectiveActivity.class );
        final Iterator<IOCBeanDef<AbstractWorkbenchPerspectiveActivity>> perspectivesIterator = perspectives.iterator();
        outer_loop:
        while ( perspectivesIterator.hasNext() ) {
            final IOCBeanDef<AbstractWorkbenchPerspectiveActivity> perspective = perspectivesIterator.next();
            final AbstractWorkbenchPerspectiveActivity instance = perspective.getInstance();
            if ( instance.isDefault() ) {
                defaultPerspective = instance;
                break outer_loop;
            } else {
                iocManager.destroyBean( instance );
            }
        }
        return defaultPerspective;
    }

    private List<AbstractWorkbenchPerspectiveActivity> getPerspectiveActivities() {

        //Get Perspective Providers
        final Set<AbstractWorkbenchPerspectiveActivity> activities = activityManager.getActivities( AbstractWorkbenchPerspectiveActivity.class );

        //Sort Perspective Providers so they're always in the same sequence!
        List<AbstractWorkbenchPerspectiveActivity> sortedActivities = new ArrayList<AbstractWorkbenchPerspectiveActivity>( activities );
        Collections.sort( sortedActivities,
                          new Comparator<AbstractWorkbenchPerspectiveActivity>() {

                              @Override
                              public int compare( AbstractWorkbenchPerspectiveActivity o1,
                                                  AbstractWorkbenchPerspectiveActivity o2 ) {
                                  return o1.getPerspective().getName().compareTo( o2.getPerspective().getName() );
                              }

                          } );

        return sortedActivities;
    }

    //Fade out the "Loading application" pop-up
    private void hideLoadingPopup() {
        final Element e = RootPanel.get( "loading" ).getElement();

        new Animation() {

            @Override
            protected void onUpdate( double progress ) {
                e.getStyle().setOpacity( 1.0 - progress );
            }

            @Override
            protected void onComplete() {
                e.getStyle().setVisibility( Style.Visibility.HIDDEN );
            }
        }.run( 500 );
    }

    public static native void redirect( String url )/*-{
        $wnd.location = url;
    }-*/;

}