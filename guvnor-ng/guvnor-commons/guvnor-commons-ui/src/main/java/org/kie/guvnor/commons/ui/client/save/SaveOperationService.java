/*
 * Copyright 2013 JBoss Inc
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

package org.kie.guvnor.commons.ui.client.save;

import org.uberfire.backend.vfs.Path;

import static org.kie.commons.validation.PortablePreconditions.checkNotNull;

public class SaveOperationService {

    public void save(final Path path,
                     final SaveCommand saveCommand) {
        checkNotNull("command", saveCommand);

        if (path.getFileSystem().supportedFileAttributeViews().contains("version")) {
            new CheckinPopup("", saveCommand).show();
        } else {
            saveCommand.execute("");
        }
    }
}
