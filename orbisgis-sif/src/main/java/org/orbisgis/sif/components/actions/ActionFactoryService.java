/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE 
 * team located in University of South Brittany, Vannes.
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.sif.components.actions;

import javax.swing.Action;
import java.util.List;

/**
 * Root extension point to all menu related services.
 * You can define additional properties using ActionTools.
 * Or use an instance of DefaultAction.
 * @author Nicolas Fortin
 */
public interface ActionFactoryService<TargetComponent> {
        /**
         * Each instance of TargetComponent call createAction once.
         * Do not make a reference to created actions in the ActionFactoryService instance.
         * @param target Holder of new actions.
         * @return Action list instance, linked with target.
         * {@link ActionsHolder#addActionFactory(ActionFactoryService, Object)}
         */
        List<Action> createActions(TargetComponent target);

        /**
         * Before the target component unload theses resources,
         * it call this method with all action obtained though createActions.
         * If you register some listeners on target component, this is the time to remove the listeners.
         * @param target The target being unloaded.
         * @param actions The action created by this factory.
         */
        void disposeActions(TargetComponent target, List<Action> actions);
}
