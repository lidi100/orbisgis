/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 * 
 *
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
 * A PARTICULAR PURPArrayList<ListenerContainer>OSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * info _at_ orbisgis.org
 */
package org.orbisgis.base.events;

import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.*;
import org.apache.log4j.Logger;
import org.orbisgis.utils.I18N;

/**
 * @brief Accessor to listeners
 * The listener container manager the Add,Remove and Call to listeners
 * corresponding to a single event type
 * The listener container is fully compatible with Java Beans specifications for event managments.
 * This is a refactorisation, leading to remove duplicate code.
 * @warning A listener must have only one method
 */

public class ListenerContainer<EventObjectType extends EventObject> {
    private static Logger logger = Logger.getLogger(ListenerContainer.class);
    private ListenerContainer upLevelContainer = null; /*!< This container will call the upLevelContainer on a new event */
    private Map<Object,ArrayList<WeakReference<Listener>>> targetToListeners = Collections.synchronizedMap(new HashMap<Object,ArrayList<WeakReference<Listener>>>()); /*!< Contain the link between target and listeners */
    private Set<Listener> listeners = Collections.synchronizedSet(new HashSet<Listener>()); /*!< Listerners of this container */

    /**
     * Declare the root or single event listener collection.
     */
    public ListenerContainer() {
    }
    
    /**
     * A leaf listener collection
     * This container will call the upLevelContainer on a new event
     * @param upLevelContainer The container to call on a callListeners event.
     */
    public ListenerContainer(ListenerContainer upLevelContainer) {
        this.upLevelContainer = upLevelContainer;
    }
    
    /**
     * Add this container in the release object
     * @param listenerRelease The instance of release manager
     * @return this object
     */
    public ListenerContainer addReleaseTool(ListenerRelease listenerRelease) {
        listenerRelease.addContainer(this);
        return this;
    }
    /**
     * Attach a listener between the event source and the event target
     * @param target The target called by the listener.
     * @param listener The listener object. Created by EventHandler
     */
    public void addListener(Object target,Listener listener) {
        if(listeners.contains(listener)) {
            throw(new InvalidParameterException(I18N.getString("org.orbisgis.base.events.ListenerContainer.eventListernerAlreadyPushed")));
        }
        ArrayList<WeakReference<Listener>> listenersOfTarget;
        if(!targetToListeners.containsKey(target)) {
            listenersOfTarget = new ArrayList<WeakReference<Listener>>();
            targetToListeners.put(target,listenersOfTarget);
        } else {
            listenersOfTarget = targetToListeners.get(target);
        }
        listenersOfTarget.add( new WeakReference<Listener>(listener));
        listeners.add(listener);
    }
    /**
     * Remove a provided listener
     * @param listener The listener instance
     * @return True if the listener has been found and removed
     */
    public boolean removeListener(Listener listener) {
        if(listeners.contains(listener)) {
            listeners.remove(listener);
            return true;
        }else{
            return false;
        }
    }
    /**
     * Remove the listeners linked with a specific target
     * @param target The instance of the target object
     */
    public void removeListeners(Object target) {
        if(targetToListeners.containsKey(target)) {
            ArrayList<WeakReference<Listener>> listenersOfTarget = targetToListeners.get(target);
            for(WeakReference<Listener> listenerRef : listenersOfTarget) {
                Listener listener = listenerRef.get();
                if(listener!=null) {
                    removeListener(listener);
                }
            }
            targetToListeners.remove(target);
        }
    }
    /**
     * Remove all listeners of this container
     */
    public void clearListeners() {
        targetToListeners.clear();
        listeners.clear();
    }
    /**
     * Call all listeners of this collection.
     * @param data Null or the event data specified in the declaration.
     * @throws EventException Throwed by a Listener
     * @throws InvalidParameterException if data is not null and is not EventObjectan instance of EventObject.
     */
    public void callListeners(EventObjectType data) throws EventException {
        Iterator<Listener> itListener = listeners.iterator();
        while(itListener.hasNext()) {
            Listener listener = itListener.next();
            try {
                if(data!=null) {
                    listener.onEvent((EventObject)data);
                }else{
                    listener.onEvent(null);                        
                }
            } catch (ListenerException ex) {
                //This listener stop the propagation of the event
                if(!ex.letContinueProcessing()) {
                    logger.error(I18N.getString("org.orbisgis.base.events.ListenerContainer.ListernerThrowStopEvent"), ex);
                    return;
                }
            } catch (RuntimeException e) { 
                //The listener throw unexpected error
                //To keep the thread and continue to propagate the event
                //the listener is removed and an error message is sent
                //@link http://www.ibm.com/developerworks/java/library/j-jtp07265/index.html#3.0
                logger.error(I18N.getString("org.orbisgis.base.events.ListenerContainer.ListernerUnexpectedThrow"), e);
                removeListener(listener);
                itListener.remove();
           }
        }
        if(this.upLevelContainer!=null) {
            this.upLevelContainer.callListeners(data);
        }
    }
}
